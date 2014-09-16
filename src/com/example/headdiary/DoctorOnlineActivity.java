package com.example.headdiary;

import java.util.ArrayList;
import java.util.HashMap;



import com.example.headdiary.data.Suggestion;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.util.MessageAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.View;
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

	        // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
	        float density = getResources().getDisplayMetrics().density;
	        listView.setFirstTopOffset((int) (10 * density));

	        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
	        SimpleHeader header = new SimpleHeader(this);
	        header.setTextColor(0xff0066aa);
	        header.setCircleColor(0xff33bbee);
	        listView.setHeadable(header);

	        // 设置加载更多的样式（可选）
	        SimpleFooter footer = new SimpleFooter(this);
	        footer.setCircleColor(0xff33bbee);
	        listView.setFootable(footer);

	        // 设置列表项出现动画（可选）
	        listView.setItemAnimForTopIn(R.anim.topitem_in);
	        listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

	        // 下拉刷新事件回调（可选）
	        listView.setOnRefreshStartListener(new OnStartListener() {
	            @Override
	            public void onStart() {
	                refresh();
	            }
	        });

	        // 加载更多事件回调（可选）
	        listView.setOnLoadMoreStartListener(new OnStartListener() {
	            @Override
	            public void onStart() {
	                loadMore();
	            }
	        });
	        
	        listView.refresh(); // 主动下拉刷新
	    }
	
	private void refresh(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               
            listView.setRefreshSuccess("刷新成功"); // 通知加载成功
            listView.startLoadMore(); // 开启LoadingMore功能
                
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

		
	//--------------Click响应事件-------------------//
	
	public void onClickBack(View v){
		
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
		  // setListAdapter(mAdapter);	
		   listView.setAdapter(mAdapter);
		   }
		
		for(Suggestion suggestion:suggestionList){			
	       UserDAO.getInstance().setSuggestionRead(suggestion.getRecId());
		   }		
	}

}
