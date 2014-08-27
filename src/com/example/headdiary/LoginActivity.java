package com.example.headdiary;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.headdiary.data.Config.MsgConfig;
import com.example.headdiary.data.User;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.util.AllExit;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.NetWorkManager;
import com.example.headdiary.util.ToastManager;
import com.example.headdiary.util.WebServiceManager;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends Activity {
	ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	private static ProgressBar progressBar;
	private static EditText etAccount,etPswd;
	private static CheckBox cbRemPswd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		AllExit.getInstance().addActivity(this);

		initView();
	}

	
	
	private void initView() {
		// TODO Auto-generated method stub
		progressBar=(ProgressBar)findViewById(R.id.login_progressBar);
		etAccount=(EditText)findViewById(R.id.login_et_account);
		etAccount.setText(UserDAO.getInstance().getLoginUser().getPhone());
		
		etPswd=(EditText)findViewById(R.id.login_et_password);
		etPswd.setText("");
		
		cbRemPswd=(CheckBox)findViewById(R.id.login_cb_rempswd);
		cbRemPswd.setChecked(UserDAO.getInstance().getLoginUser().getRemPswd()==1);
	}


	public void onClickLogin(View v){
		String account=etAccount.getText().toString().trim();
		String pswd=etPswd.getText().toString().trim();
		if (account.equals("")){
			etAccount.setError("请输入账号");
			return;
		}
		
			
		if (pswd.equals("")){
			etPswd.setError("请输入密码");
			return;
		}
		
		int remPswd=0;
		if (cbRemPswd.isChecked())
			remPswd=1;
		
		User loginUser=new User(remPswd, pswd, account);
		UserDAO.getInstance().setLoginUser(loginUser);

		//先尝试用本地密码匹配然后登陆,并将userId存入Userdata,若返回true则代表登录成功
		if (DBManager.tryLocalLogin(loginUser)){
			Intent intent=new Intent(this,HomeActivity.class);
	        startActivity(intent);
	        this.finish();
		}
		else{
			//本地登陆不成功，则用web service获得通过UserId获得该User的所有信息 OR 是否登陆成功的String。
			if (NetWorkManager.isNetworkConnected(this)){
				progressBar.setVisibility(View.VISIBLE);
				singleThreadExecutor.execute(loginRunnable); 
			}
			else{
				ToastManager.showShortToast("本地登录失败，请打开网络尝试登录");
			}	
		}
	}
	
	public void onClickRegister(View v){
		Intent intent=new Intent(this,RegisterActivity.class);
		startActivity(intent);
	}
	
	Runnable loginRunnable = new Runnable(){
        public void run() {
         // TODO Auto-generated method stub
        	
        	WebServiceManager.clearProperties();
        	WebServiceManager.addProperties("phone", UserDAO.getInstance().getLoginUser().getPhone());
        	WebServiceManager.addProperties("password", UserDAO.getInstance().getLoginUser().getPassword());
        	String res=WebServiceManager.callWebServiceForString("attemptLogin");  
        	
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
 	        else if (res.equals(MsgConfig.NO_USER)){
 	        	if (UserDAO.getInstance().getLoginUser().getUserId()!=-1){
 	        		ToastManager.showShortToast("密码错误");  //该用户尚未进行云端认证同步
 	        	}
 	        	else{
 	        		ToastManager.showShortToast("该用户尚未注册");
 	        	}
 	        }
 	        else if (res.equals(MsgConfig.PASSWORD_ERROR))
 	        	ToastManager.showShortToast("密码错误");
 	        else{
 	        	//Login Success
 	        	try {
 	        		User user=new User();
 	        		Gson gson=new Gson();
 	        		user=gson.fromJson(res, User.class);
 	        		user.setUserId(UserDAO.getInstance().getLoginUser().getUserId());
 	        		user.setRemPswd(UserDAO.getInstance().getLoginUser().getRemPswd());
 	        		user.setHDVersion(0);		//本地尚未同步，所以version为0
					UserDAO.getInstance().setUser(user);
					
					DBManager.putUsertoDB(user);
					DBManager.loginSuccess();
					UserDAO.getInstance().setLoginFromWeb(true);
	 	        	Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
	 	        	startActivity(intent);
	 	        	LoginActivity.this.finish();
				} catch (Exception e) {
					// TODO: handle exception
					ToastManager.showShortToast("无法解析数据");
				}
 	        	
 	        }	
 	        
 	        progressBar.setVisibility(View.GONE);
 	    }
 	       
 	};
 	

}
