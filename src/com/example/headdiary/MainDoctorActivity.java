package com.example.headdiary;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

public class MainDoctorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_doctor);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  
	}

	
	public void onClickBack(View v){
		this.finish();
	}
	
	public void onClickHeadAssist(View v){	
		Intent intent = new Intent (this, DoctorChatActivity.class);	
		intent.putExtra("type",0);
    	startActivity(intent);
	}
	
	public void onClickDoctor(View v){
		Intent intent = new Intent (this, DoctorChatActivity.class);	
		intent.putExtra("type",1);
    	startActivity(intent);
	}


}
