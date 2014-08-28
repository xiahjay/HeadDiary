package com.example.headdiary.hddialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.headdiary.R;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;

public class AcheTypeQuestion extends Activity {
	private static final int RADIO_NUM=5;
	private  RadioGroup radioGroup;  
	private  RadioButton[] radioBtn=new RadioButton[RADIO_NUM];
	private static final int[] radioID=new int[]{R.id.type_radio0,R.id.type_radio1,R.id.type_radio2,R.id.type_radio3,R.id.type_radio4};
	private EditText etElse;
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ache_type_dialog);
		initView();
		
	}
	
	private void initView(){
		//init radiogroup
		radioGroup=(RadioGroup)findViewById(R.id.type_radioGroup);
		for (int i=0;i<RADIO_NUM;i++){
			radioBtn[i]=(RadioButton)findViewById(radioID[i]);
			radioBtn[i].setText(StrConfig.HDAcheType[i]);
		}
			
		int type=headacheDiary.getType();
		if (type!=-1)
			radioGroup.check(radioID[type]);
		else
			radioGroup.check(-1);
		
		//init etElse
		etElse=(EditText)findViewById(R.id.type_et_else);
		if (type==RADIO_NUM-1){
			etElse.setText(headacheDiary.getTypeComment());
			etElse.setVisibility(View.VISIBLE);
		}
		else
			etElse.setVisibility(View.GONE);
		
		//set radio listener
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { 
            @Override 
            public void onCheckedChanged(RadioGroup group, int checkedID) { 
                // TODO Auto-generated method stub 
                if (checkedID==radioID[RADIO_NUM-1])
                	etElse.setVisibility(View.VISIBLE);
                else
                	etElse.setVisibility(View.GONE);
            } 
        });
		
	}
	
	
	public void onClickCancel(View v){
		finish();
	}
	
	public void onClickConfirm(View v){
		int checkedID=radioGroup.getCheckedRadioButtonId ();
		if (checkedID==-1){
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_no_input), Toast.LENGTH_SHORT).show();
		}
		else{
			int ans=getAnsbyId(checkedID);
			headacheDiary.setType(ans);
			if (ans==RADIO_NUM-1)
				headacheDiary.setTypeComment(etElse.getText().toString().trim());
			else
				headacheDiary.setTypeComment("");
			finish();	
			Intent intent = new Intent (AcheTypeQuestion.this, AcheDegreeQuestion.class);	
			startActivity(intent);
		}
		
	}
	
	private static int getAnsbyId(int checkedID){
		for (int i=0;i<radioID.length;i++)
			if (radioID[i]==checkedID)
				return i;
		return -1;
	}

}

