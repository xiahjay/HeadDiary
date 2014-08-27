package com.example.headdiary;

import java.util.Locale;
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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainSettingActivity extends Activity {
	ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	private TextView tvLanguage,tvName,tvCellphone,tvDoctor,tvAutoUpload;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_setting);
		init();
		AllExit.getInstance().addActivity(this);
	}

	private void init(){
		tvName=(TextView)findViewById(R.id.setting_tv_name);
		tvCellphone=(TextView)findViewById(R.id.setting_tv_cellphone);
		tvDoctor=(TextView)findViewById(R.id.setting_tv_doctor);
		tvAutoUpload=(TextView)findViewById(R.id.setting_tv_autoupload);
		tvLanguage=(TextView)findViewById(R.id.setting_tv_language);
		progressBar=(ProgressBar)findViewById(R.id.setting_progressBar);
		
		refreshDisplay();
	}


	public void onClickBack(View v){
		this.finish();
	}
	
	public void onClickDoctor(View v){
		//����Ѱ�ҹ�����ҽ��
		new AlertDialog.Builder(this)
		.setTitle("����ҽ��")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage("ϵͳ�����������������ֻ������������¼�����Ҳ�����ͷʹҽ��������ҽ�����Լ�ʱ�鿴���ϴ���ͷʹ��־�������轡�����顣")
		.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				findDoctor();
			}

			
		})
		.setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		}).show();
	}
	
	public void onClickAutoUpload(View v){
		User user=UserDAO.getInstance().getUser();
		if (user.ifHasUUID()){
			new AlertDialog.Builder(this)
			.setTitle("ͬ������")
			.setSingleChoiceItems(StrConfig.AutoUpload, UserDAO.getInstance().getUser().getAutoUpload(),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							User user=UserDAO.getInstance().getUser();
							if (user.getAutoUpload()!=which){
								user.setAutoUpload(which);
								DBManager.putUsertoDB(user);
								tvAutoUpload.setText(StrConfig.AutoUpload[user.getAutoUpload()]);
							}
							dialog.dismiss();
						}

						
			}).show();
		}
		else{
			new AlertDialog.Builder(this)
			.setTitle("ͬ������")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage("����ҽ���ɹ�֮����ܿ���ͬ�����ܣ��Ƿ�����������ֻ������������¼������ͷʹҽ����")
			.setPositiveButton("��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					findDoctor();
				}

				
			})
			.setNegativeButton("��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			}).show();
		}
		
	}
	
	public void onClickLanguage(View v){
		new AlertDialog.Builder(this)
		.setTitle("ѡ������")
		.setSingleChoiceItems(StrConfig.Language, UserDAO.getInstance().getLanguage(),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						User user=UserDAO.getInstance().getUser();
						if (UserDAO.getInstance().getLanguage()!=which){
							UserDAO.getInstance().setLanguage(which);
							changeLanguage();
						}
						dialog.dismiss();
					}

					
		}).show();
	}
	
	public void onClickExitAndLogout(View v){
		User user=UserDAO.getInstance().getUser();
		if (user.getRemPswd()==1){
			user.setRemPswd(0);
			DBManager.updateUserInfo_RemPswdByUserId(user);
		}
		AllExit.getInstance().exit();
	}

	//---------------------����-----------------------//
	private void changeLanguage() {
		// TODO Auto-generated method stub
		Resources resources =getResources();//���res��Դ����  
		Configuration config = resources.getConfiguration();//������ö���
		DisplayMetrics dm = resources.getDisplayMetrics();//�����Ļ��������Ҫ�Ƿֱ��ʣ����صȡ�
		switch(UserDAO.getInstance().getLanguage()){
		case 0:
			config.locale=Locale.getDefault();
			break;
		case 1:
			config.locale=Locale.SIMPLIFIED_CHINESE;
			break;
		case 2:
			config.locale=Locale.ENGLISH;
			break;
		}
		resources.updateConfiguration(config, dm);
		StrConfig.initStrConfig();
		AllExit.getInstance().exit();
		Intent intent=new Intent(this,HomeActivity.class);
		startActivity(intent);
		
		finish();
	}
	
	private void refreshDisplay() {
		// TODO Auto-generated method stub
		User user=UserDAO.getInstance().getUser();
		
		String name=user.getUserName();
		if (name==null)
			name="";
		tvName.setText(name);
		
		String phone=user.getPhone();
		if (phone==null)
			phone="";
		tvCellphone.setText(phone);
		
		if(user.getDoctorId()!=-1)
			tvDoctor.setText(user.getDoctorName());
		else {
			tvDoctor.setText(this.getResources().getString(R.string.prompt_none));
		}
		
		if (user.ifHasUUID())
			tvAutoUpload.setText(StrConfig.AutoUpload[user.getAutoUpload()]);
		else {
			tvAutoUpload.setText("δ����");
		}
		tvLanguage.setText(StrConfig.Language[UserDAO.getInstance().getLanguage()]);
	}
	
	
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
 	        	ToastManager.showShortToast("û���ҵ�������ͷʹҽ����");
 	        }
 	        else if (res.equals(MsgConfig.PASSWORD_ERROR)){
 	        	ToastManager.showShortToast("���ҵ��������ͷʹҽ���������ĵ�¼���벻��ȷ����ע�������µ�¼��");
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
					ToastManager.showShortToast("�ɹ���������ҽ����"+user.getDoctorName());
					DBManager.putUsertoDB(user);
					refreshDisplay();
					
				} catch (Exception e) {
					// TODO: handle exception
					ToastManager.showShortToast("�޷���������");
				}
 	        	
 	        }	
 	        
 	        progressBar.setVisibility(View.GONE);
 	    }	
	};
}
