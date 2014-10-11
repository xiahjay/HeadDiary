package com.example.headdiary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;



import com.example.headdiary.data.Suggestion;
import com.example.headdiary.data.User;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.data.Config.MsgConfig;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.MessageAdapter;
import com.example.headdiary.util.WebServiceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnStartListener;


public class DoctorOnlineActivity extends Activity {
	private static final String TAG="MainDocumentActivity";
	private MessageAdapter mAdapter;
	private Handler handler;
	private ArrayList<HashMap<String, Object>> HDList=new ArrayList<HashMap<String, Object>>();	
	public static ArrayList<Suggestion> suggestionList;
	private ZrcListView listView;
	private static Boolean synchonizingSuggAvaliable=true;
	static ExecutorService singleThreadExecutorForSynchronize = Executors.newSingleThreadExecutor();
	public static String getPayload=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_online);	
		listView = (ZrcListView) findViewById(R.id.zListView);
		refreshDispArray();
		loadListview();
		
	} 

	
	
	private void loadListview() {
		// TODO Auto-generated method stub
		 handler = new Handler();

	        // ����Ĭ��ƫ��������Ҫ����ʵ��͸�����������ܡ�����ѡ��
	        float density = getResources().getDisplayMetrics().density;
	        listView.setFirstTopOffset((int) (10 * density));

	        // ��������ˢ�µ���ʽ����ѡ�������û��Header���޷�����ˢ�£�
	        SimpleHeader header = new SimpleHeader(this);
	        header.setTextColor(0xff0066aa);
	        header.setCircleColor(0xff33bbee);
	        listView.setHeadable(header);

	        // ���ü��ظ������ʽ����ѡ��
	      //  SimpleFooter footer = new SimpleFooter(this);
	       // footer.setCircleColor(0xff33bbee);
	        //listView.setFootable(footer);

	        // �����б�����ֶ�������ѡ��
	        listView.setItemAnimForTopIn(R.anim.topitem_in);
	        listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

	        // ����ˢ���¼��ص�����ѡ��
	        listView.setOnRefreshStartListener(new OnStartListener() {
	            @Override
	            public void onStart() {
	                refresh();	
	               // refreshDispArray();

	            }
	        });

	        // ���ظ����¼��ص�����ѡ��
	      //  listView.setOnLoadMoreStartListener(new OnStartListener() {
	           // @Override
	            //public void onStart() {
	             //   loadMore();
	           // }
	       // });
	        
	        //if(getPayload!=null){
	       // listView.refresh(); 
	        // ��������ˢ��	   
	        //}
	       
	    }
	
	private void refresh(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            if(getPayload!=null){
             singleThreadExecutorForSynchronize.execute(synchronizeSuggestionRunnable);  
             refreshDispArray();
             getPayload=null;
             }
            if(synchonizingSuggAvaliable){
               listView.setRefreshSuccess("ˢ�³ɹ�"); // ֪ͨ���سɹ�
               listView.startLoadMore(); // ����LoadingMore����               
               }
           else{
            	listView.setRefreshFail("ˢ��ʧ��");
               }
            
            
            }
        }, 2 * 1000);
    }

    private void loadMore(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                
             listView.setLoadMoreSuccess();
               
            }
        }, 2 * 1000);
    }


	@Override
	public void onResume(){
		super.onResume();
		
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		
	}

		
	//--------------Click��Ӧ�¼�-------------------//
	
	public void onClickBack(View v){
		
    	this.finish();
	}
						
	//--------------����-------------------//
	private void refreshDispArray(){	
	  //int count=DBManager.getAllCountFromDB();
	  suggestionList= UserDAO.getInstance().getSuggestionList();
	  HDList=UserDAO.getInstance().getSuggestionListForDisplay(suggestionList);
	  Log.i("JYM","refreshDispArray has ran");
	  
		if (HDList!=null){
		   mAdapter = new MessageAdapter(this,HDList);
		   Log.i("JYM","suggestionList="+suggestionList);
		   listView.setAdapter(mAdapter);
		   }
		
		
		for(Suggestion suggestion:suggestionList){			
	       UserDAO.getInstance().setSuggestionRead(suggestion.getRecId());
		   }		
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
