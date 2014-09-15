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
			etName.setError("请输入姓名");
			return;
		}
		
			
		if (phone.equals("")){
			etPhone.setError("请输入手机号");
			return;
		}
		
		if (password.equals("")){
			etPassword.setError("请输入密码");
			return;
		}
		
		if (!password.equals(passwordAgain)){
			etPasswordAgain.setError("两次密码不一致");
			return;
		}

		User registerUser=new User(name,phone,password);
		UserDAO.getInstance().setRegisterUser(registerUser);

		//先尝试查找本地账户，如果没有改phone，则联网注册
		if (DBManager.ifLocalAccountExists(registerUser)){
			ToastManager.showShortToast("该手机号已经注册");
		}
		else{
			//本地登陆不成功，则用web service获得通过UserId获得该User的所有信息 OR 是否登陆成功的String。
			if (NetWorkManager.isNetworkConnected(this)){
				progressBar.setVisibility(View.VISIBLE);
				singleThreadExecutor.execute(registerRunnable); 
			}
			else{
				ToastManager.showShortToast("抱歉，连接网络后才能注册。");;
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
 	        	ToastManager.showShortToast("该手机号已经注册");
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
						ToastManager.showShortToast("注册成功，并已关联您的医生："+user.getDoctorName());
					registerSuccess();
					
				} catch (Exception e) {
					// TODO: handle exception
					ToastManager.showShortToast("无法解析数据");
				}
 	        	
 	        }	
 	        
 	        progressBar.setVisibility(View.GONE);
 	    }

		private void showHintDialog() {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(RegisterActivity.this)
			.setTitle("提示")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setMessage("在就诊记录中找到该手机号，但是姓名不匹配，若仍以该姓名注册，则无法关联头痛医生。")
			.setPositiveButton("仍然注册", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					UserDAO.getInstance().setUser(UserDAO.getInstance().getRegisterUser());
					registerSuccess();
				}
			})
			.setNegativeButton("修改姓名", new OnClickListener() {
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
