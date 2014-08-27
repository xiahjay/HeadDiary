package com.example.headdiary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.headdiary.data.ChatMsgEntity;
import com.example.headdiary.data.ChatMsgViewAdapter;
import com.example.headdiary.data.StrConfig;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DoctorChatActivity extends Activity{

	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_chat);
        //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        initView();
        
        initData();
    }
    
    public void initView()
    {
    	mListView = (ListView) findViewById(R.id.chat_listview); 	
    	mEditTextContent = (EditText) findViewById(R.id.chat_et_sendmessage);
    }
    
    private String[]msgArray,dateArray;
    
    public void initData()
    {
    	int headImg;
    	String name;
    	TextView tv;
    	
    	tv=(TextView)findViewById(R.id.chat_tv_name);
    	
    	Intent intent=getIntent();
    	int type=intent.getIntExtra("type", 0); 
    	if (type==0){
    		name=this.getResources().getString(R.string.title_headache_assist);
    		tv.setText(name);
    		headImg=R.drawable.ic_launcher;
    		msgArray = StrConfig.msgArray1;

    		dateArray = StrConfig.dateArray1;
    	}
    	else{
    		name=this.getResources().getString(R.string.title_doc_name);
    		tv.setText(name);
    		headImg=R.drawable.img_doctor;
    		msgArray = StrConfig.msgArray2;

    		dateArray = StrConfig.dateArray2;
    	}
    	
    	for(int i = 0; i < msgArray.length; i++)
    	{
    		ChatMsgEntity entity = new ChatMsgEntity();
    		entity.setDate(dateArray[i]);
    		if (i % 2 == 0)
    		{
    			entity.setName(name);
    			entity.setHeadImg(this.getResources().getDrawable(headImg));
    			entity.setMsgType(true);
    		}else{
    			entity.setName(this.getResources().getString(R.string.title_patient_name));
    			entity.setHeadImg(this.getResources().getDrawable(R.drawable.img_patient));
    			entity.setMsgType(false);
    		}
    		
    		entity.setText(msgArray[i]);
    		mDataArrays.add(entity);
    	}

    	mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
		
    }


    public void onClickBack(View v){
    	finish();
    }
    
    public void onClickSend(View v){
    	
    	String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0)
		{
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName(this.getResources().getString(R.string.title_patient_name));
			entity.setHeadImg(this.getResources().getDrawable(R.drawable.img_patient));
			entity.setMsgType(false);
			entity.setText(contString);
			
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			
			mEditTextContent.setText("");
			
			mListView.setSelection(mListView.getCount() - 1);
		}
		
    }
    
	
    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        
        
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins); 
        						
        						
        return sbBuffer.toString();
    }
    
	

}
