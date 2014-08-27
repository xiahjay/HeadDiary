package com.example.headdiary.hddialog;

import com.example.headdiary.R;
import com.example.headdiary.R.id;
import com.example.headdiary.R.layout;
import com.example.headdiary.R.menu;
import com.example.headdiary.R.string;
import com.example.headdiary.data.Drug;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddDrugDialog extends Activity {
	private static final int RADIO_NUM=4;
	private  RadioGroup radioGroup;  
	private  RadioButton[] radioBtn=new RadioButton[RADIO_NUM];
	private static final int[] radioID=new int[]{R.id.drug_radio0,R.id.drug_radio1,R.id.drug_radio2,R.id.drug_radio3};
	private int choice;//choice=0~4 means modify the drugs already exists choice=5 means add new drug
	private EditText etName,etQuantity;
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_drug_dialog);
		initView();
	}
	
	private void initView(){
		Drug tempDrug;
		
		Intent intent=getIntent();
		choice=intent.getIntExtra("choice", 5); 
		if (choice<5 && headacheDiary.getDrugInList(choice)!=null){
			Button btn=(Button)findViewById(R.id.drug_btn_cancel);
			btn.setText(this.getResources().getString(R.string.action_delete));  //else display cancel
		}
			
		radioGroup=(RadioGroup)findViewById(R.id.drug_radioGroup);
		for (int i=0;i<RADIO_NUM;i++){
			radioBtn[i]=(RadioButton)findViewById(radioID[i]);
			radioBtn[i].setText(StrConfig.HDDrugEffect[i]);
		}
		
		etName=(EditText)findViewById(R.id.drug_et_name);
		etQuantity=(EditText)findViewById(R.id.drug_et_quantity);
		
		if (choice<5 && headacheDiary.getDrugInList(choice)!=null){
			tempDrug=headacheDiary.getDrugInList(choice);
			radioGroup.check(radioID[tempDrug.getEffect()]);
			etName.setText(tempDrug.getName());
			etQuantity.setText(tempDrug.getQuantity());
		}
		else
			radioGroup.check(-1);
		
	}
	
	
	public void onClickCancel(View v){
		if (choice<5 && headacheDiary.getDrugInList(choice)!=null)
			headacheDiary.removeDrugInList(choice);
		finish();
	}
	
	public void onClickConfirm(View v){
		Drug newDrug=new Drug();
		String name="",quantity="";
		
		name=etName.getText().toString().trim();
		quantity=etQuantity.getText().toString().trim();
		if (name.equals("")){
			Toast.makeText(this, this.getResources().getString(R.string.error_no_drug_name), Toast.LENGTH_SHORT).show();
			return;
		}
		if (quantity.equals("")){
			Toast.makeText(this, this.getResources().getString(R.string.error_no_drug_quantity), Toast.LENGTH_SHORT).show();
			return;
		}
		
		int answer=radioGroup.getCheckedRadioButtonId ();
		if (answer==-1){
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_no_drug_effect), Toast.LENGTH_SHORT).show();
			return;
		}
		
		newDrug.setName(name);
		newDrug.setQuantity(quantity);
		newDrug.setEffect(getAnsbyId(answer));
		if (choice<5 && headacheDiary.getDrugInList(choice)!=null) //�޸�ԭ�еļ�¼
			headacheDiary.setDrugInList(choice, newDrug);
		else
			headacheDiary.addDrugToList(newDrug);
		finish();		

	}
	
	private static int getAnsbyId(int answer){
		for (int i=0;i<radioID.length;i++)
			if (radioID[i]==answer)
				return i;
		return -1;
	}


}
