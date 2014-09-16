package com.example.headdiary;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.headdiary.data.Suggestion;
import com.example.headdiary.data.User;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.data.Config.MsgConfig;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.WebServiceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

public class PushDemoReceiver extends BroadcastReceiver {
	private static Boolean synchonizingSuggAvaliable=true;
	static ExecutorService singleThreadExecutorForSynchronize = Executors.newSingleThreadExecutor();
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			
			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");

			// smartPush第三方回执调用接口，actionid范围�?90000-90999，可根据业务场景执行
			boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
			System.out.println("第三方回执接口调�?" + (result ? "成功" : "失败"));
			
			if (payload != null) {
				String data = new String(payload);
                UserDAO.getInstance().setPayload(data);
               DoctorOnlineActivity.getPayload = data;
               HomeActivity.getPayload = data;
               singleThreadExecutorForSynchronize.execute(synchronizeSuggestionRunnable);
               //������ҳ����Ϣ����
               setReminder();
               //DoctorOnlineActivity.suggestionList= UserDAO.getInstance().getSuggestionList();
              // Log.i("JYM","suggestionList="+UserDAO.getInstance().getSuggestionList());
                Log.d("GetuiSdkDemo", "Got Payload:" + data);
				//if (GetuiSdkDemoActivity.tLogView != null)
					//GetuiSdkDemoActivity.tLogView.append(data + "\n");
			}
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后�?�过用户帐号查找CID进行消息推�??
			String cid = bundle.getString("clientid");
			UserDAO.getInstance().setPushClientId(cid);
			Log.i("PushClientId", "PushClientId="+UserDAO.getInstance().getPushClientId());
			//if (GetuiSdkDemoActivity.tView != null)
				//GetuiSdkDemoActivity.tView.setText(cid);
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*String appid = bundle.getString("appid");
			String taskid = bundle.getString("taskid");
			String actionid = bundle.getString("actionid");
			String result = bundle.getString("result");
			long timestamp = bundle.getLong("timestamp");

			Log.d("GetuiSdkDemo", "appid = " + appid);
			Log.d("GetuiSdkDemo", "taskid = " + taskid);
			Log.d("GetuiSdkDemo", "actionid = " + actionid);
			Log.d("GetuiSdkDemo", "result = " + result);
			Log.d("GetuiSdkDemo", "timestamp = " + timestamp);*/
			break;
		default:
			break;
		}
	}
	
	private void setReminder() {
		// TODO Auto-generated method stub
		HomeActivity.tvUnread.setVisibility(View.VISIBLE);	
		HomeActivity.tvUnread.setText(Integer.toString(UserDAO.getInstance().getUnreadSuggestion()));
	}

		//--------------ͬ��ҽ�����鹦��--------------------//
	 	Runnable synchronizeSuggestionRunnable = new Runnable(){
	        public void run() {
	         // TODO Auto-generated method stub
	        	Gson gson=new Gson();
	        	
	        	User user=UserDAO.getInstance().getUser();
	        	String uuid=user.getUserUUID();
	        	//String cid = UserDAO.getInstance().getPushClientId();
	            String lastSuggestionTime=null;
	            if(user.getLastSuggestionTime()!=null){
	        	 lastSuggestionTime = user.getLastSuggestionTime();}
	            else{
	             lastSuggestionTime="1993-07-08 18:00:00";
	            }
	           
	            
	            
	        	WebServiceManager.clearProperties();
	        	WebServiceManager.addProperties("userUUID", uuid);
	        	//WebServiceManager.addProperties("pushClientId", cid);
	        	WebServiceManager.addProperties("lastSuggestionTime", "1993-07-08 18:00:00");        	
	        	
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
						
					    user.setLastSuggestionTime(lastTime); 
					    Log.i("PushDemoReceiver","lastSuggestionTime="+user.getLastSuggestionTime());
	 	        		synchonizingSuggAvaliable=true;
	 	        		//ToastManager.showShortToast("ͬ���ɹ�");	
					} catch (Exception e) {
						// TODO: handle exception
						//ToastManager.showShortToast("�޷���������");
						synchonizingSuggAvaliable=false;
					}
	 	        	
	 	        }	
	 	        
	 	        	        
	 	    }
	 	       
	 	};
	
	
}
