package com.example.headdiary.hddialog;

import com.example.headdiary.R;
import com.example.headdiary.R.id;
import com.example.headdiary.R.layout;
import com.example.headdiary.R.menu;
import com.example.headdiary.R.string;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ActivityAggravateDialog extends Activity {
	
	private static final int RADIO_NUM=2;
	private  RadioGroup radioGroup;  
	private  RadioButton[] radioBtn=new RadioButton[RADIO_NUM];
	private static final int[] radioID=new int[]{R.id.activity_radio0,R.id.activity_radio1};
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_aggravate_dialog);
		
		initView();
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
	}
	
	public void onClickConfirm(View v){
		int answer=radioGroup.getCheckedRadioButtonId ();
		if (answer==-1){
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_no_input), Toast.LENGTH_SHORT).show();
		}
		else{
			headacheDiary.setIfActivityAggravate(getAnsbyId(answer));
			finish();			
		}
		

	}
	
	private static int getAnsbyId(int answer){
		for (int i=0;i<radioID.length;i++)
			if (radioID[i]==answer)
				return i;
		return -1;
	}
}
