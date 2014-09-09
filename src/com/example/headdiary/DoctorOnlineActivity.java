package com.example.headdiary;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.DocumentAdapter;
import com.example.headdiary.util.MessageAdapter;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_online);				
		refreshDispArray();
	}

	
	
	@Override
	public void onResume(){
		super.onResume();
		//if (HeadacheDiaryDAO.getInstance().getIfSelectedDiaryChanged()){
			//refreshDispArray();
		//}
		
	}

	/*@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    // Get the item that was clicked
	    HeadacheDiaryDAO.getInstance().setDocumentHDChoice(position);
	    HeadacheDiary headacheDiary=(HeadacheDiary) HeadacheDiaryDAO.getInstance().getDocumentHDiaryList().get(position).clone();
	    HeadacheDiaryDAO.getInstance().setHeadacheDiarySelected(headacheDiary);
	    Intent intent=new Intent(this,HeadDiaryFormActivity.class);
        startActivity(intent);
	  }*/
	
	//--------------Click响应事件-------------------//
	
	public void onClickBack(View v){
    	this.finish();
	}

	
		
	
	
	
	//--------------方法-------------------//
	private void refreshDispArray(){
		//int size=0;
		for(int i=0; i<2; i++){
		HashMap<String, Object> map = new HashMap<String, Object>();		
		map.put(MessageAdapter.ArrayKey_FirstLine,"少吃止痛药"+i );
		map.put(MessageAdapter.ArrayKey_SecondLine, "持续时间： "+i);
		HDList.add(map);
		}
		if (HDList!=null){
			mAdapter = new MessageAdapter(this,HDList);
			setListAdapter(mAdapter);
			//size=HDList.size();
		}
		
	}

}
