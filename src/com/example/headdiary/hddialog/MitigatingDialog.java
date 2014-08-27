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
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MitigatingDialog extends Activity {
	private static final int CATEGORY_NUM=9;
	private  CheckBox[] checkBox=new CheckBox[CATEGORY_NUM];
	private static final int[] checkBoxID=new int[]{
		R.id.mitigating_check0,R.id.mitigating_check1,R.id.mitigating_check2,R.id.mitigating_check3,R.id.mitigating_check4,
		R.id.mitigating_check5,R.id.mitigating_check6,R.id.mitigating_check7,R.id.mitigating_check8
		};
	
	private EditText etElse;
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitigating_dialog);
		initView();

	}

	private void initView(){
		//checkbox and editbox
		etElse=(EditText)findViewById(R.id.mitigating_et_else);
		for (int i=0;i<CATEGORY_NUM;i++){
			checkBox[i]=(CheckBox)findViewById(checkBoxID[i]);
			checkBox[i].setText(StrConfig.HDMitigating[i]);
			if (headacheDiary.getMitigating(i)==1)
				checkBox[i].setChecked(true);
			else
				checkBox[i].setChecked(false);
		}
		
		if (headacheDiary.getMitigating(CATEGORY_NUM-1)==1){ //选择了其他选项
			etElse.setText(headacheDiary.getMitigatingComment());
			etElse.setVisibility(View.VISIBLE);
		}
		else
			etElse.setVisibility(View.GONE);
		
		checkBox[CATEGORY_NUM-1].setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean flag) {
				// TODO Auto-generated method stub
				if (flag){
					etElse.setText(headacheDiary.getMitigatingComment());
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
				headacheDiary.setMitigating(i,1);
			else {
				headacheDiary.setMitigating(i,0);
			}
		}

		if (headacheDiary.getMitigating(CATEGORY_NUM-1)==1)
			headacheDiary.setMitigatingComment(etElse.getText().toString().trim());
		else
			headacheDiary.setMitigatingComment("");
		
		finish();
	}
}
