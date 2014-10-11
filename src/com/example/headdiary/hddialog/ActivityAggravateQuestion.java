package com.example.headdiary.hddialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.headdiary.HomeActivity;
import com.example.headdiary.R;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;

public class ActivityAggravateQuestion extends Activity {
	
	private static final int RADIO_NUM=2;
	private  RadioGroup radioGroup;  
	private  RadioButton[] radioBtn=new RadioButton[RADIO_NUM];
	private static final int[] radioID=new int[]{R.id.activity_radio0,R.id.activity_radio1};
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	private Button btnCancel;
	private Button btnConfirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_aggravate_dialog);
		btnCancel=(Button)findViewById(R.id.activity_btn_cancel);
		btnCancel.setText("上一步");
		btnConfirm=(Button)findViewById(R.id.activity_btn_confirm);
		btnConfirm.setText("下一步");
		initView();
	}

	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){  
	        //捕捉返回键
	    	finish();
	    	Intent intent = new Intent();  
			intent.setClass(ActivityAggravateQuestion.this, HomeActivity.class);  
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面  
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity  
			startActivity(intent);  
			}
            return true;
	    
	    } 
	
	private void initView(){
		radioGroup=(RadioGroup)findViewById(R.id.activity_radioGroup);
		for (int i=0;i<RADIO_NUM;i++){
			radioBtn[i]=(RadioButton)findViewById(radioID[i]);
			radioBtn[i].setText(StrConfig.HDActivity[i]);
		}
		
		int acti=headacheDiary.getIfActivityAggravate();
		if (acti!=-1)
			radioGroup.check(radioID[acti]);
		else
			radioGroup.check(-1);
		
	}

	public void onClickCancel(View v){
		finish();
		Intent intent = new Intent (ActivityAggravateQuestion.this,AcheDegreeQuestion.class);	
		startActivity(intent); 
	}
	
	public void onClickConfirm(View v){
		int answer=radioGroup.getCheckedRadioButtonId ();
		if (answer==-1){
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_no_input), Toast.LENGTH_SHORT).show();
		}
		else{
			headacheDiary.setIfActivityAggravate(getAnsbyId(answer));
			finish();
			Intent intent = new Intent (ActivityAggravateQuestion.this,CompanionQuestion.class);	
			startActivity(intent);
		}
		

	}
	
	private static int getAnsbyId(int answer){
		for (int i=0;i<radioID.length;i++)
			if (radioID[i]==answer)
				return i;
		return -1;
	}
}

