package com.example.headdiary.hddialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headdiary.HomeActivity;
import com.example.headdiary.R;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;

public class CompanionQuestion extends Activity {
	private static final int RADIO_NUM=4;
	private static final int CATEGORY_NUM=10;
	private  RadioGroup[] radioGroup=new RadioGroup[CATEGORY_NUM];  
	private  RadioButton[][] radioBtn=new RadioButton[CATEGORY_NUM][RADIO_NUM];
	private static final int[] radioGroupID={
		R.id.companion_radioGroup0,R.id.companion_radioGroup1,R.id.companion_radioGroup2,R.id.companion_radioGroup3,
		R.id.companion_radioGroup4,R.id.companion_radioGroup5,R.id.companion_radioGroup6,R.id.companion_radioGroup7,
		R.id.companion_radioGroup8,R.id.companion_radioGroup9
		};
	private static final int[][] radioID={
		{R.id.companion_radio0_0,R.id.companion_radio0_1,R.id.companion_radio0_2,R.id.companion_radio0_3},
		{R.id.companion_radio1_0,R.id.companion_radio1_1,R.id.companion_radio1_2,R.id.companion_radio1_3},
		{R.id.companion_radio2_0,R.id.companion_radio2_1,R.id.companion_radio2_2,R.id.companion_radio2_3},
		{R.id.companion_radio3_0,R.id.companion_radio3_1,R.id.companion_radio3_2,R.id.companion_radio3_3},
		{R.id.companion_radio4_0,R.id.companion_radio4_1,R.id.companion_radio4_2,R.id.companion_radio4_3},
		{R.id.companion_radio5_0,R.id.companion_radio5_1,R.id.companion_radio5_2,R.id.companion_radio5_3},
		{R.id.companion_radio6_0,R.id.companion_radio6_1,R.id.companion_radio6_2,R.id.companion_radio6_3},
		{R.id.companion_radio7_0,R.id.companion_radio7_1,R.id.companion_radio7_2,R.id.companion_radio7_3},
		{R.id.companion_radio8_0,R.id.companion_radio8_1,R.id.companion_radio8_2,R.id.companion_radio8_3},
		{R.id.companion_radio9_0,R.id.companion_radio9_1,R.id.companion_radio9_2,R.id.companion_radio9_3}
		};
	
	private TextView[] tv=new TextView[CATEGORY_NUM];
	private static final int[] tvID={
		R.id.companion_tv0,R.id.companion_tv1,R.id.companion_tv2,R.id.companion_tv3,
		R.id.companion_tv4,R.id.companion_tv5,R.id.companion_tv6,R.id.companion_tv7,
		R.id.companion_tv8,R.id.companion_tv9
		};
	private Button btnConfirm;
	private Button btnBefore;
	
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_companion_dialog);
		btnConfirm=(Button)findViewById(R.id.companion_btn_confirm);
		btnConfirm.setText("完成");
		btnBefore=(Button)findViewById(R.id.companion_btn_before);
		btnBefore.setText("上一步");
		initView();
	}
	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){  
	        //捕捉返回键
	    	finish();
	    	Intent intent = new Intent();  
			intent.setClass(CompanionQuestion.this, HomeActivity.class);  
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面  
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity  
			startActivity(intent);  
			}
            return true;
	    
	    } 
	
	private void initView(){
		//RadioGroup & TextView
		int i,j,value;
		String mText;
		
		for (i=0;i<CATEGORY_NUM;i++){
			tv[i]=(TextView)findViewById(tvID[i]);
			tv[i].setText(StrConfig.HDCompanionCategory[i]);
			
			radioGroup[i]=(RadioGroup)findViewById(radioGroupID[i]);
			for (j=0;j<RADIO_NUM;j++){
				radioBtn[i][j]=(RadioButton)findViewById(radioID[i][j]);
				mText=StrConfig.HDCompanionValueBrief[i][j];
				radioBtn[i][j].setText(mText);
				value=headacheDiary.getCompanion(i);
				if (value!=-1)
					radioGroup[i].check(radioID[i][value]);
				else
					radioGroup[i].check(-1);
			}
		}
	}
	
	public void onClickCancel(View v){
		int i,answer,value;
		for (i=0;i<CATEGORY_NUM;i++){
			answer=radioGroup[i].getCheckedRadioButtonId ();
			value=getAnsbyId(i,answer);
			
			if (value==-1){
				radioGroup[i].check(radioID[i][0]);
				headacheDiary.setCompanion(i, 0);
			}
		}
	}
	
public void onClickBefore(View v){
	Intent intent = new Intent (CompanionQuestion.this,ActivityAggravateQuestion.class);	
	startActivity(intent);
		finish();
	}
	
	public void onClickConfirm(View v){
		int i,answer,value;
		Boolean flag=true;//flag=false, the input is incomplete.
		for (i=0;i<CATEGORY_NUM;i++){
			answer=radioGroup[i].getCheckedRadioButtonId ();
			value=getAnsbyId(i,answer);
			headacheDiary.setCompanion(i, value);
			if (value==-1)
				flag=false;
		}
		if (!flag)
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_incomplete_input), Toast.LENGTH_SHORT).show();
		else
			{
			
			finish();			
			}
	}
	
	private static int getAnsbyId(int cate,int answer){
		if (answer==-1)
			return -1;
		
		for (int j=0;j<RADIO_NUM;j++)
			if (radioID[cate][j]==answer)
				return j;
		
		return -1;
	}

}

