package com.example.headdiary;

import java.net.PasswordAuthentication;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.User;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.data.Config.MsgConfig;
import com.example.headdiary.util.AllExit;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.NetWorkManager;
import com.example.headdiary.util.ToastManager;
import com.example.headdiary.util.WebServiceManager;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Build;

public class RegisterActivity extends Activity{
	ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	EditText etName,etPhone,etPassword,etPasswordAgain;
	ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		AllExit.getInstance().addActivity(this);
		init();
	}

	private void init(){
		etName=(EditText)findViewById(R.id.register_et_name);
		etPhone=(EditText)findViewById(R.id.register_et_phone);
		etPassword=(EditText)findViewById(R.id.register_et_password);
		etPasswordAgain=(EditText)findViewById(R.id.register_et_password_again);
		progressBar=(ProgressBar)findViewById(R.id.register_progressBar);
	}
	
	public void onClickRegister(View v){
		String name=etName.getText().toString().trim();
		String phone=etPhone.getText().toString().trim();
		String password=etPassword.getText().toString().trim();
		String passwordAgain=etPasswordAgain.getText().toString().trim();
		if (name.equals("")){
			etName.setError("����������");
			return;
		}
		
			
		if (phone.equals("")){
			etPhone.setError("�������ֻ���");
			return;
		}
		
		if (password.equals("")){
			etPassword.setError("����������");
			return;
		}
		
		if (!password.equals(passwordAgain)){
			etPasswordAgain.setError("�������벻һ��");
			return;
		}

		User registerUser=new User(name,phone,password);
		UserDAO.getInstance().setRegisterUser(registerUser);

		//�ȳ��Բ��ұ����˻������û�и�phone��������ע��
		if (DBManager.ifLocalAccountExists(registerUser)){
			ToastManager.showShortToast("���ֻ����Ѿ�ע��");
		}
		else{
			//���ص�½���ɹ�������web service���ͨ��UserId��ø�User��������Ϣ OR �Ƿ��½�ɹ���String��
			if (NetWorkManager.isNetworkConnected(this)){
				progressBar.setVisibility(View.VISIBLE);
				singleThreadExecutor.execute(registerRunnable); 
			}
			else{
				ToastManager.showShortToast("��Ǹ��������������ע�ᡣ");;
			}	
		}
	}
	
	Runnable registerRunnable = new Runnable(){
        public void run() {
         // TODO Auto-generated method stub
        	User user=UserDAO.getInstance().getRegisterUser();
        	String cid = UserDAO.getInstance().getPushClientId();
        	WebServiceManager.clearProperties();
            WebServiceManager.addProperties("pushClientId", cid);
        	WebServiceManager.addProperties("userName", user.getUserName());
        	WebServiceManager.addProperties("phone", user.getPhone());
        	WebServiceManager.addProperties("password", user.getPassword());
        	String res=WebServiceManager.callWebServiceForString("register");  
        	
        	Message message = new Message();
	        Bundle data = new Bundle();
	        data.putString(MsgConfig.KEY_RESULT,res);
	        message.setData(data);
			handler.sendMessage(message);
        }

	};
	
	Handler handler = new Handler(){
 	    @Override
 	    public void handleMessage(Message msg) {
 	        super.handleMessage(msg);
 	        
 	        Bundle data = msg.getData();
 	        String res = data.getString(MsgConfig.KEY_RESULT);
 	        if(res==null){
 	        	ToastManager.showCallWebServiceError();
 	        }
 	        else if (res.equals(MsgConfig.USER_EXISTS)){
 	        	ToastManager.showShortToast("���ֻ����Ѿ�ע��");
 	        }
 	        else if (res.equals(MsgConfig.USERNAME_ERROR)){
 	        	showHintDialog();
 	        }
 	        else if (res.equals(MsgConfig.NO_PHONE_MATCH)){
 		        UserDAO.getInstance().setUser(UserDAO.getInstance().getRegisterUser());
 	        	registerSuccess();
 	        }
 	        else{
 	        	//register on web successfully
 	        	try {
 	        		User user=new User();
 	        		Gson gson=new Gson();
 	        		user=gson.fromJson(res, User.class);
					UserDAO.getInstance().setUser(user);
					if (user.getDoctorName()!=null && !user.getDoctorName().equals(""))
						ToastManager.showShortToast("ע��ɹ������ѹ�������ҽ����"+user.getDoctorName());
					registerSuccess();
					
				} catch (Exception e) {
					// TODO: handle exception
					ToastManager.showShortToast("�޷���������");
				}
 	        	
 	        }	
 	        
 	        progressBar.setVisibility(View.GONE);
 	    }

		private void showHintDialog() {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(RegisterActivity.this)
			.setTitle("��ʾ")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setMessage("�ھ����¼���ҵ����ֻ��ţ�����������ƥ�䣬�����Ը�����ע�ᣬ���޷�����ͷʹҽ����")
			.setPositiveButton("��Ȼע��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					UserDAO.getInstance().setUser(UserDAO.getInstance().getRegisterUser());
					registerSuccess();
				}
			})
			.setNegativeButton("�޸�����", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					etName.requestFocus();
				}
			}).show();
		}
		
		private void registerSuccess() {
			// TODO Auto-generated method stub
	        DBManager.putUsertoDB(UserDAO.getInstance().getUser());
			DBManager.loginSuccess();
			
			AllExit.getInstance().exit();
	        Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
	        startActivity(intent);
	        RegisterActivity.this.finish();
		}
 	       
 	};


}
