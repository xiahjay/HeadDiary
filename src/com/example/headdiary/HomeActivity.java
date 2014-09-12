package com.example.headdiary;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.Suggestion;
import com.example.headdiary.data.User;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.data.Config.MsgConfig;
import com.example.headdiary.hddialog.StartTimeDialog;
import com.example.headdiary.hddialog.StartTimeQuestion;
import com.example.headdiary.util.AllExit;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.NetWorkManager;
import com.example.headdiary.util.ToastManager;
import com.example.headdiary.util.WebServiceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushManager;

import android.R.integer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeActivity extends Activity {
	static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	static ExecutorService singleThreadExecutorForSynchronize = Executors.newSingleThreadExecutor();
	private TextView tvNewsContent;
	private Button btnDiary,btnSynchronize;
	private ProgressBar progressBar;
	private static Boolean isSynchronizing=false;
	private static Boolean synchonizingSuggAvaliable=true;
	private static Boolean synchonizeingAvaliable=true;	
	public static  String getPayload=null;
	public static TextView tvUnread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AllExit.getInstance().addActivity(this);
		setContentView(R.layout.activity_home);
		init();	
		//if(UserDAO.getInstance().getPayload()!=null){
		//singleThreadExecutorForSynchronize.execute(synchronizeSuggestionRunnable);
		//Log.i("JYM","singleThreadExecutorForSynchronize");
		//UserDAO.getInstance().setPayload(null);
		//}
	}

	private void init(){
		tvNewsContent = (TextView)findViewById(R.id.home_tv_news_content);   
		tvNewsContent.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		btnDiary=(Button)findViewById(R.id.home_btn_new_headache);
		btnSynchronize=(Button)findViewById(R.id.home_btn_synchronize);
		progressBar=(ProgressBar)findViewById(R.id.home_progressBar);	
		tvUnread =  (TextView)findViewById(R.id.main_tab_unread_tv); 
	}

	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    setBtnDiary(); 
	    
	   	    
	    int unread = UserDAO.getInstance().getUnreadSuggestion();
	    
		   if(unread==0){
		     tvUnread.setVisibility(View.GONE);
		     }
		   else{
		     tvUnread.setVisibility(View.VISIBLE);	
		     tvUnread.setText(Integer.toString(unread));
		     }
		 Log.i("UnreadHome", "UnreadSuggestion="+unread);
		 
		// if(getPayload!=null){
			 //Intent intent=new Intent(HomeActivity.this,DoctorOnlineActivity.class);
			// startActivity(intent);
			 //getPayload=null;
		// }
		 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.home_exit:
	    		AllExit.getInstance().exit();
	    		return true;
	    	default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){  
	        //捕捉返回键
	    	AllExit.getInstance().exit();
            return true;
	    }  
	   
	   return super.onKeyDown(keyCode, event);  
	}  
	
	public void onClickNewHeadDiary(View v) {   
		if (HeadacheDiaryDAO.getInstance().getIfLastDiaryComplete()){			
			HeadacheDiaryDAO.getInstance().setHeadacheDiarySelected(new HeadacheDiary());
			Intent intent = new Intent (HomeActivity.this,UnfinishedDiaryActivity.class);	
			startActivity(intent);
		}
		else{
			HeadacheDiaryDAO.getInstance().setHeadacheDiarySelected((HeadacheDiary) HeadacheDiaryDAO.getInstance().getLastHeadacheDiary().clone());
			Intent intent = new Intent (HomeActivity.this,UnfinishedDiaryActivity.class);	
			startActivity(intent);	
		}	
	}
	
	public void onClickSynchronize(View v){
		User user=UserDAO.getInstance().getUser();
		if (user.ifHasUUID()){
			synchronize(); 
			//singleThreadExecutorForSynchronize.execute(synchronizeSuggestionRunnable);
		}
		else{
			if (progressBar.getVisibility()==View.VISIBLE){
				return;
			}
			
			new AlertDialog.Builder(this)
			.setTitle("同步功能")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage("关联医生成功之后才能开启同步功能，是否根据姓名和手机号搜索就诊记录并关联头痛医生？")
			.setPositiveButton("是", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					findDoctor();
				}

				
			})
			.setNegativeButton("否", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			}).show();
		}
	}
	
	public void onClickDocument(View v) {     
		if (isLoadingData())
			return;
		Intent intent = new Intent (HomeActivity.this,MainDocumentActivity.class);			
		startActivity(intent);	
	}
	
	public void onClickAnalysis(View v) {   
		if (isLoadingData())
			return;
		if(UserDAO.getInstance().getListStyle()==0)
		{Intent intent = new Intent (HomeActivity.this,MainAnalysisActivity.class);			
		startActivity(intent);}
		if(UserDAO.getInstance().getListStyle()==1)
		{Intent intent = new Intent (HomeActivity.this,GraphicActivity.class);			
		startActivity(intent);}
			
	}
	
	public void onClickOnlineDoctor(View v) {  
		Intent intent = new Intent (HomeActivity.this,DoctorOnlineActivity.class);			
		startActivity(intent);	
	}
	
	public void onClickSetting(View v) {     
		if (isLoadingData())
			return;
		Intent intent = new Intent (HomeActivity.this,MainSettingActivity.class);			
		startActivity(intent);	
	}
	
	
	//----------------方法---------------------//
	private Boolean isLoadingData(){
		return isSynchronizing || progressBar.getVisibility()==View.VISIBLE;
	}
	private void setBtnDiary(){
		if (HeadacheDiaryDAO.getInstance().getIfLastDiaryComplete()){
			btnDiary.setBackground(this.getResources().getDrawable(R.drawable.btn_style_blue));
			btnDiary.setText(this.getResources().getString(R.string.action_new_headache));	
		}
		else{
			btnDiary.setBackground(this.getResources().getDrawable(R.drawable.btn_style_red));
			btnDiary.setText(this.getResources().getString(R.string.action_continue_headache));
		}
		
		//--------------------synchronize------------------------------//
		synchonizeingAvaliable=synchonizeingAvaliable && ifNetWorkOpen();
		
		if (!UserDAO.getInstance().getUser().ifHasUUID()){
			btnSynchronize.setText("尚未开通同步功能");
			btnSynchronize.setTextColor(this.getResources().getColor(R.color.green));
		}
		else if (isSynchronizing){
			btnSynchronize.setText("正在同步...");
			btnSynchronize.setTextColor(this.getResources().getColor(R.color.dodgerblue));
		}
		else if (UserDAO.getInstance().getLoginFromWeb()){
			if (synchonizeingAvaliable){
				synchronize();
				UserDAO.getInstance().setLoginFromWeb(false);	
			}
			else{
				btnSynchronize.setText("本地数据待同步");
				btnSynchronize.setTextColor(this.getResources().getColor(R.color.red));
			}
			
		}
		else if (UserDAO.getInstance().getUser().ifNeedSynchronize()){
			if (UserDAO.getInstance().getUser().getAutoUpload()==1 && synchonizeingAvaliable){
				synchronize();
			}
			else{
				btnSynchronize.setText("本地数据待同步");
				btnSynchronize.setTextColor(this.getResources().getColor(R.color.red));
			}
		}
		else{
			btnSynchronize.setText("云端同步头痛日志");
			btnSynchronize.setTextColor(this.getResources().getColor(R.color.green));
			
		}
	}

    private void synchronize() {
		// TODO Auto-generated method stub
    	if (isSynchronizing){
			return;
		}

		if (NetWorkManager.isNetworkConnected(this)){
			btnSynchronize.setText("正在同步...");
			btnSynchronize.setTextColor(this.getResources().getColor(R.color.blue));
			isSynchronizing=true;			 
			//singleThreadExecutorForSynchronize.execute(synchronizeSuggestionRunnable);
			singleThreadExecutor.execute(synchronizeRunnable);	
			//singleThreadExecutor.execute(synchronizeSuggestionRunnable);
		}
		else{
			ToastManager.showNoWeb();
		}	
		
	}
                 
    
    private Boolean ifNetWorkOpen(){
    	return NetWorkManager.isNetworkConnected(this);
    }
    
    //---------------找医生,与MainSetting相同的函数----------------------//
    private void findDoctor() {
		// TODO Auto-generated method stub
		if (NetWorkManager.isNetworkConnected(this)){
			progressBar.setVisibility(View.VISIBLE);
			singleThreadExecutor.execute(findDoctorRunnable); 
		}
		else{
			ToastManager.showNoWeb();
		}	
	}
    
	Runnable findDoctorRunnable = new Runnable(){
        public void run() {
         // TODO Auto-generated method stub
        	User user=UserDAO.getInstance().getUser();
        	WebServiceManager.clearProperties();
        	WebServiceManager.addProperties("userName", user.getUserName());
        	WebServiceManager.addProperties("phone", user.getPhone());
        	WebServiceManager.addProperties("password", user.getPassword());
        	WebServiceManager.addProperties("autoUpload", user.getAutoUpload());
        	String res=WebServiceManager.callWebServiceForString("findDoctor");  
        	
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
 	        else if (res.equals(MsgConfig.NO_DOCTOR_MATCH)){
 	        	ToastManager.showShortToast("没有找到关联的头痛医生。");
 	        }
 	        else if (res.equals(MsgConfig.PASSWORD_ERROR)){
 	        	ToastManager.showShortToast("已找到相关联的头痛医生，但您的登录密码不正确，请注销后重新登录。");
 	        }
 	        else{
 	        	//find doctor successfully
 	        	try {
 	        		User user=new User();
 	        		Gson gson=new Gson();
 	        		user=gson.fromJson(res, User.class);
 	        		user.setUserId(UserDAO.getInstance().getUser().getUserId());
 	        		user.setHDVersion(UserDAO.getInstance().getUser().getHDVersion());
 	        		user.setHDNeedUpload(UserDAO.getInstance().getUser().getHDNeedUpload());
 	        		user.setHDNeedDelete(UserDAO.getInstance().getUser().getHDNeedDelete());
 	        		user.setRemPswd(UserDAO.getInstance().getUser().getRemPswd());
					UserDAO.getInstance().setUser(user);
					ToastManager.showShortToast("成功关联您的医生："+user.getDoctorName());
					DBManager.putUsertoDB(user);
					setBtnDiary();
					
				} catch (Exception e) {
					// TODO: handle exception
					ToastManager.showShortToast("无法解析数据");
				}
 	        	
 	        }	
 	        
 	        progressBar.setVisibility(View.GONE);
 	    }	
	};
	
	//--------------同步功能--------------------//
	Runnable synchronizeRunnable = new Runnable(){
        public void run() {
         // TODO Auto-generated method stub
        	Gson gson=new Gson();
        	
        	User user=UserDAO.getInstance().getUser();
        	String uuid=user.getUserUUID();
        	int hdVersion=user.getHDVersion();
        	ArrayList<HeadacheDiary> hDiaryList=DBManager.getDiaryList(user.getHDNeedUpload());
        	String jsonUpload=gson.toJson(hDiaryList);
        	String jsonDelete=gson.toJson(user.getHDNeedDelete());
        	WebServiceManager.clearProperties();
        	WebServiceManager.addProperties("userUUID", uuid);
        	WebServiceManager.addProperties("hdVersion", hdVersion);
        	WebServiceManager.addProperties("jsonUploadDiaryList", jsonUpload);
        	WebServiceManager.addProperties("jsonDeleteRecIdList", jsonDelete);
        	Log.i("HJVersion", Integer.toString(hdVersion));
        	Log.i("HJUpload", jsonUpload);
        	String res=WebServiceManager.callWebServiceForString("synchronizeHeadacheDiary");  
        	
        	Message message = new Message();
	        Bundle data = new Bundle();
	        data.putString(MsgConfig.KEY_RESULT,res);
	        message.setData(data);
			synHandler.sendMessage(message);
        }

	};
	
	Handler synHandler = new Handler(){
 	    @Override
 	    public void handleMessage(Message msg) {
 	        super.handleMessage(msg);
 	        
 	        Bundle data = msg.getData();
 	        String res = data.getString(MsgConfig.KEY_RESULT);
 	        if(res==null){
 	        	ToastManager.showCallWebServiceError();
 	        	synchonizeingAvaliable=false;
 	        }
 	        else{
 	        	//syn Success
 	        	try {
 	        		JSONObject jsonObject;
 	        		jsonObject = new JSONObject(res);
		   			int webVersion = jsonObject.getInt("webVersion");  
		   			String jsonAllDiaryList = jsonObject.getString("jsonAllDiaryList"); 
					String jsonUploadNewRecIdList = jsonObject.getString("jsonUploadNewRecIdList"); 
 	        		
					Gson gson=new Gson();
					User user=UserDAO.getInstance().getUser();
					if (!jsonAllDiaryList.equals(MsgConfig.NO_DATA)){
						ArrayList<HeadacheDiary> hDiaryList=gson.fromJson(jsonAllDiaryList, new TypeToken<ArrayList<HeadacheDiary>>(){}.getType());
						DBManager.updateAllHeadDiary(hDiaryList);
					}
					if (!jsonUploadNewRecIdList.equals(MsgConfig.NO_DATA)){
						ArrayList<Integer> newRecIdList=gson.fromJson(jsonUploadNewRecIdList, new TypeToken<ArrayList<Integer>>(){}.getType());
						DBManager.updateUploadHeadDiaryRecId(user.getHDNeedUpload(),newRecIdList);
					}
					
 	        		user.setHDVersion(webVersion);
 	        		user.setHDNeedUpload(null);
 	        		user.setHDNeedDelete(null);
 	        		DBManager.putUsertoDB(user);
 	        		
 	        		synchonizeingAvaliable=true;
 	        		ToastManager.showShortToast("同步成功");	
				} catch (Exception e) {
					// TODO: handle exception
					ToastManager.showShortToast("无法解析数据");
					synchonizeingAvaliable=false;
				}
 	        	
 	        }	
 	        
 	        isSynchronizing=false;
 	        setBtnDiary();
 	        
 	    }
 	       
 	};
 	
 	//--------------同步医生建议功能--------------------//
 	Runnable synchronizeSuggestionRunnable = new Runnable(){
        public void run() {
         // TODO Auto-generated method stub
        	Gson gson=new Gson();
        	
        	User user=UserDAO.getInstance().getUser();
        	String uuid=user.getUserUUID();
        	String cid = UserDAO.getInstance().getPushClientId();
            String lastSuggestionTime=null;
            if(user.getLastSuggestionTime()!=null){
        	 lastSuggestionTime = user.getLastSuggestionTime();}
            else{
             lastSuggestionTime=null;
            }
           
            
            
        	WebServiceManager.clearProperties();
        	WebServiceManager.addProperties("userUUID", uuid);
        	WebServiceManager.addProperties("pushClientId", cid);
        	WebServiceManager.addProperties("lastSuggestionTime", "2014-04-25 12:20:00");        	
        	
        	String res=WebServiceManager.callWebServiceForString("synchronizeSuggestion");  
        	
        	Message message = new Message();
	        Bundle data = new Bundle();
	        data.putString(MsgConfig.KEY_RESULT,res);
	        message.setData(data);
			synSuggHandler.sendMessage(message);
        }

	};
	
	Handler synSuggHandler = new Handler(){
 	    @Override
 	    public void handleMessage(Message msg) {
 	        super.handleMessage(msg); 	        
 	        Bundle data = msg.getData();
 	        String res = data.getString(MsgConfig.KEY_RESULT);
 	        if(res==null){
 	        	//ToastManager.showCallWebServiceError();
 	        	synchonizingSuggAvaliable=false;
 	        }
 	        else{
 	        	//syn Success
 	        	try {
 	        		JSONObject jsonObject;
 	        		jsonObject = new JSONObject(res);
 	        		String lastTime = jsonObject.getString("webLastSuggestionTime");
 	        		String jsonSuggestionList = jsonObject.getString("jsonNewSuggestionList");
 	        		Log.i("JYMwebLastSuggestionTime", lastTime);
 	        		Log.i("JYMjsonNewSuggestionList", jsonSuggestionList);
					Gson gson=new Gson();
					User user=UserDAO.getInstance().getUser();
					
					if (!jsonSuggestionList.equals(MsgConfig.NO_DATA)){
						ArrayList<Suggestion> nSuggestionList =gson.fromJson(jsonSuggestionList, new TypeToken<ArrayList<Suggestion>>(){}.getType());
						DBManager.updateSuggestionList(nSuggestionList);
					}
					
				   // user.setLastSuggestionTime(lastTime); 	        		
 	        		synchonizingSuggAvaliable=true;
 	        		//ToastManager.showShortToast("同步成功");	
				} catch (Exception e) {
					// TODO: handle exception
					//ToastManager.showShortToast("无法解析数据");
					synchonizingSuggAvaliable=false;
				}
 	        	
 	        }	
 	        
 	        	        
 	    }
 	       
 	};
}

