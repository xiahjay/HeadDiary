package com.example.headdiary;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.DocumentAdapter;

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

public class MainDocumentActivity extends ListActivity {
	private static final String TAG="MainDocumentActivity";
	private DocumentAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> HDList=new ArrayList<HashMap<String, Object>>();
	private TextView tvTime,tvRecord;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_document);
		HeadacheDiaryDAO.getInstance().setHeadacheDiarySelected(null);
		HeadacheDiaryDAO.getInstance().setDocumentHDChoice(-1);
		initView();
		refreshDispArray();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tvTime=(TextView)findViewById(R.id.document_tv_time);
		tvRecord=(TextView)findViewById(R.id.document_tv_record);		
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if (HeadacheDiaryDAO.getInstance().getIfSelectedDiaryChanged()){
			refreshDispArray();
		}
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    // Get the item that was clicked
	    HeadacheDiaryDAO.getInstance().setDocumentHDChoice(position);
	    HeadacheDiary headacheDiary=(HeadacheDiary) HeadacheDiaryDAO.getInstance().getDocumentHDiaryList().get(position).clone();
	    HeadacheDiaryDAO.getInstance().setHeadacheDiarySelected(headacheDiary);
	    Intent intent=new Intent(this,HeadDiaryFormActivity.class);
        startActivity(intent);
	  }
	
	//--------------Click响应事件-------------------//
	
	public void onClickBack(View v){
    	this.finish();
	}

	public void onClickPeriod(View v){
		new AlertDialog.Builder(this)
			.setTitle("时间跨度")
			.setSingleChoiceItems(StrConfig.TimePeriod, UserDAO.getInstance().getTimePeriod(),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (UserDAO.getInstance().getTimePeriod()!=which){
								UserDAO.getInstance().setTimePeriod(which);
								refreshDispArray();
							}
							dialog.dismiss();
						}
			}).show();
	}
	
	/*public void onClickStyle(View v){
		new AlertDialog.Builder(this)
		.setTitle("显示方式")
		.setSingleChoiceItems(StrConfig.DocumentStyle, UserDAO.getInstance().getDocumentStyle(),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (UserDAO.getInstance().getDocumentStyle()!=which){
							UserDAO.getInstance().setDocumentStyle(which);
						}
						dialog.dismiss();
					}
		}).show();
	}*/
	
	//--------------方法-------------------//
	private void refreshDispArray(){
		int size=0;
		HDList=HeadacheDiaryDAO.getInstance().getHDiaryListForDisplay(HeadacheDiaryDAO.getInstance().getDocumentHDiaryList(),UserDAO.getInstance().getTimePeriod());
		if (HDList!=null){
			mAdapter = new DocumentAdapter(this,HDList);
			setListAdapter(mAdapter);
			size=HDList.size();
		}
		tvTime.setText(StrConfig.TimePeriod[UserDAO.getInstance().getTimePeriod()]);
		tvRecord.setText(Integer.toString(size)+"条记录");
	}

}
