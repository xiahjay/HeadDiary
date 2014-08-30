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

public class AchePositionQuestion extends Activity {
	private static final int RADIO_NUM=3;
	private  RadioGroup radioGroup;  
	private  RadioButton[] radioBtn=new RadioButton[RADIO_NUM];
	private static final int[] radioID=new int[]{R.id.position_radio0,R.id.position_radio1,R.id.position_radio2};
	
	private static final int RADIO_NUM2=2;
	private  RadioGroup radioGroup2;  
	private  RadioButton[] radioBtn2=new RadioButton[RADIO_NUM2];
	private static final int[] radioID2=new int[]{R.id.positionAroundEye_radio0,R.id.positionAroundEye_radio1};
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	private Button btnCancel;
	private Button btnConfirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ache_position_dialog);
		initView();
		btnCancel=(Button)findViewById(R.id.position_btn_cancel);
		btnCancel.setText("��һ��");
		btnConfirm=(Button)findViewById(R.id.position_btn_confirm);
		btnConfirm.setText("��һ��");
		
	}
	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){  
	        //��׽���ؼ�
	    	finish();
	    	Intent intent = new Intent();  
			intent.setClass(AchePositionQuestion.this, HomeActivity.class);  
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//���ò�Ҫˢ�½�Ҫ�����Ľ���  
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//�����Թص���Ҫ���Ľ����м��activity  
			startActivity(intent);  
			}
            return true;
	    
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
		Intent intent = new Intent (AchePositionQuestion.this,StartTimeQuestion.class);	
		startActivity(intent);
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
			Intent intent = new Intent (AchePositionQuestion.this,AcheTypeQuestion.class);	
			startActivity(intent);
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
