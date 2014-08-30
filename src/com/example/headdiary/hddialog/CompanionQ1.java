package com.example.headdiary.hddialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headdiary.R;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;

public class CompanionQ1 extends Activity {
	private static final int RADIO_NUM=4;
	private static final int CATEGORY_NUM=2;
	private  RadioGroup[] radioGroup=new RadioGroup[CATEGORY_NUM];  
	private  RadioButton[][] radioBtn=new RadioButton[CATEGORY_NUM][RADIO_NUM];
	private static final int[] radioGroupID={
		R.id.companion_radioGroup0_q1,R.id.companion_radioGroup1_q1
		};
	private static final int[][] radioID={
		{R.id.companion_radio0_0_q1,R.id.companion_radio0_1_q1,R.id.companion_radio0_2_q1,R.id.companion_radio0_3_q1},
		{R.id.companion_radio1_0_q1,R.id.companion_radio1_1_q1,R.id.companion_radio1_2_q1,R.id.companion_radio1_3_q1},
		};
	
	private TextView[] tv=new TextView[CATEGORY_NUM];
	private static final int[] tvID={
		R.id.companion_tv0_q1,R.id.companion_tv1_q1
		};
	
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_companion_q1);
		
		initView();
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

