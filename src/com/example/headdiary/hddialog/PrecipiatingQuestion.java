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
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PrecipiatingQuestion extends Activity {
	private static final int CATEGORY_NUM=10;
	private  CheckBox[] checkBox=new CheckBox[CATEGORY_NUM];
	private static final int[] checkBoxID=new int[]{
		R.id.precipiating_check0,R.id.precipiating_check1,R.id.precipiating_check2,R.id.precipiating_check3,R.id.precipiating_check4,
		R.id.precipiating_check5,R.id.precipiating_check6,R.id.precipiating_check7,R.id.precipiating_check8,R.id.precipiating_check9
		};
	
	private RelativeLayout reLayout;
	private EditText etElse;
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_precipiating_dialog);
		initView();

	}

	private void initView(){
		//checkbox and editbox
		etElse=(EditText)findViewById(R.id.precipiating_et_else);
		for (int i=0;i<CATEGORY_NUM;i++){
			checkBox[i]=(CheckBox)findViewById(checkBoxID[i]);
			checkBox[i].setText(StrConfig.HDPrecipiating[i]);
			if (headacheDiary.getPrecipiating(i)==1)
				checkBox[i].setChecked(true);
			else
				checkBox[i].setChecked(false);
		}
		
		if (headacheDiary.getPrecipiating(CATEGORY_NUM-1)==1){ //选择了其他选项
			etElse.setText(headacheDiary.getPrecipiatingComment());
			etElse.setVisibility(View.VISIBLE);
		}
		else
			etElse.setVisibility(View.GONE);
		
		checkBox[CATEGORY_NUM-1].setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean flag) {
				// TODO Auto-generated method stub
				if (flag){
					etElse.setText(headacheDiary.getPrecipiatingComment());
					etElse.setVisibility(View.VISIBLE);
					etElse.requestFocus();
				}
				else
					etElse.setVisibility(View.GONE);
				
			}
		});

	}
	
	public void onClickCancel(View v){
		finish();
	}
	
	public void onClickConfirm(View v){
		int i;

		for (i=0;i<CATEGORY_NUM;i++){
			if(checkBox[i].isChecked())
				headacheDiary.setPrecipiating(i,1);
			else {
				headacheDiary.setPrecipiating(i,0);
			}
		}

		if (headacheDiary.getPrecipiating(CATEGORY_NUM-1)==1)
			headacheDiary.setPrecipiatingComment(etElse.getText().toString().trim());
		else
			headacheDiary.setPrecipiatingComment("");
		Intent intent = new Intent (PrecipiatingQuestion.this,MitigatingQuestion.class);	
		startActivity(intent);
		
		finish();
	}
	
	
	
}
