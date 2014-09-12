package com.example.headdiary;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.Suggestion;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.DocumentAdapter;
import com.example.headdiary.util.MessageAdapter;
import com.igexin.sdk.PushManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DoctorOnlineActivity extends ListActivity {
	private static final String TAG="MainDocumentActivity";
	private MessageAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> HDList=new ArrayList<HashMap<String, Object>>();	
	public static ArrayList<Suggestion> suggestionList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_online);				
		refreshDispArray();
		
		//PushManager.getInstance().turnOffPush(getApplicationContext());
		//DBManager.setSelectedSuggestionRead(1);
	}

	
	
	@Override
	public void onResume(){
		super.onResume();
		//mAdapter.notifyDataSetChanged();
		//if (HeadacheDiaryDAO.getInstance().getIfSelectedDiaryChanged()){
			//refreshDispArray();
		//}
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		//PushManager.getInstance().turnOnPush(getApplicationContext());
	}

		
	//--------------Click响应事件-------------------//
	
	public void onClickBack(View v){
		//if(UserDAO.getInstance().getPayload()!=null){
		//Intent intent=new Intent(DoctorOnlineActivity.this,HomeActivity.class);
		//startActivity(intent);}
    	this.finish();
	}
						
	//--------------方法-------------------//
	private void refreshDispArray(){	
		
	  suggestionList= UserDAO.getInstance().getSuggestionList();
	  HDList=UserDAO.getInstance().getSuggestionListForDisplay(suggestionList);
	  Log.i("JYM","refreshDispArray has ran");
	  Log.i("JYM","suggestionList="+suggestionList);
		if (HDList!=null){
		   mAdapter = new MessageAdapter(this,HDList);
		   setListAdapter(mAdapter);											
		   }
		
		for(Suggestion suggestion:suggestionList){			
	       UserDAO.getInstance().setSuggestionRead(suggestion.getRecId());
		   }		
	}

}
