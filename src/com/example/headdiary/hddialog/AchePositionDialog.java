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
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AchePositionDialog extends Activity {
	private static final int RADIO_NUM=3;
	private  RadioGroup radioGroup;  
	private  RadioButton[] radioBtn=new RadioButton[RADIO_NUM];
	private static final int[] radioID=new int[]{R.id.position_radio0,R.id.position_radio1,R.id.position_radio2};
	
	private static final int RADIO_NUM2=2;
	private  RadioGroup radioGroup2;  
	private  RadioButton[] radioBtn2=new RadioButton[RADIO_NUM2];
	private static final int[] radioID2=new int[]{R.id.positionAroundEye_radio0,R.id.positionAroundEye_radio1};
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ache_position_dialog);
		initView();
		
	}
	
	private void initView(){
		radioGroup=(RadioGroup)findViewById(R.id.position_radioGroup);
		for (int i=0;i<RADIO_NUM;i++){
			radioBtn[i]=(RadioButton)findViewById(radioID[i]);
			radioBtn[i].setText(StrConfig.HDPosition[i]);
		}
		
		int posi=headacheDiary.getPosition();
		if (posi!=-1)
			radioGroup.check(radioID[posi]);
		else
			radioGroup.check(-1);
		
		radioGroup2=(RadioGroup)findViewById(R.id.positionAroundEye_radioGroup);
		for (int i=0;i<RADIO_NUM2;i++){
			radioBtn2[i]=(RadioButton)findViewById(radioID2[i]);
			radioBtn2[i].setText(StrConfig.HDPositionAroundEye[i]);
		}
		
		posi=headacheDiary.getIfAroundEye();
		if (posi!=-1)
			radioGroup2.check(radioID2[posi]);
		else
			radioGroup2.check(-1);
		
	}
	
	public void onClickCancel(View v){
		finish();
	}
	
	public void onClickConfirm(View v){
		int answer=radioGroup.getCheckedRadioButtonId ();
		int answer2=radioGroup2.getCheckedRadioButtonId ();
		if (answer==-1 || answer2==-1){
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_incomplete_input), Toast.LENGTH_SHORT).show();
		}
		else{
			headacheDiary.setPosition(getAnsbyId(answer));
			headacheDiary.setIfAroundEye(getAnsbyId2(answer2));
			finish();
		}
	}
	
	private static int getAnsbyId(int answer){
		for (int i=0;i<radioID.length;i++)
			if (radioID[i]==answer)
				return i;
		return -1;
	}
	
	private static int getAnsbyId2(int answer){
		for (int i=0;i<radioID2.length;i++)
			if (radioID2[i]==answer)
				return i;
		return -1;
	}

}
