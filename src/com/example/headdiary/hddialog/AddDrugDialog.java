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
import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddDrugDialog extends Activity {
	private static final int RADIO_NUM=4;
	private  RadioGroup radioGroup;  
	private  RadioButton[] radioBtn=new RadioButton[RADIO_NUM];
	private static final int[] radioID=new int[]{R.id.drug_radio0,R.id.drug_radio1,R.id.drug_radio2,R.id.drug_radio3};
	private int choice;//choice=0~4 means modify the drugs already exists choice=5 means add new drug
	private EditText etName,etQuantity;
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	private PopupWindow popupwindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_drug_dialog);				
		initView();
		//button = (Button) findViewById(R.id.button1);
		//button.setOnClickListener(this);
	}
	
	

	
	public void initmPopupWindowView() {

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item,
				null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 500, 480);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		popupwindow.setAnimationStyle(R.style.AnimationFade);
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}

				return false;
			}
		});

		/** 在这里可以实现自定义视图的功能 */
		ListView drugList = (ListView) customView.findViewById(R.id.drug_ListView);
		//TextView tx =(TextView)findViewById(android.R.id.text1);
		//tx.setTextColor(Color.BLACK);
		//Button btton3 = (Button) customView.findViewById(R.id.button3);
		//Button btton4 = (Button) customView.findViewById(R.id.button4);
		//btton2.setOnClickListener(this);
		//btton3.setOnClickListener(this);
		//btton4.setOnClickListener(this);
		String[] data={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		ArrayAdapter<String> aaData = new ArrayAdapter<String>(this,
				R.layout.simple_drug_items, data);
		
		drugList.setAdapter(aaData);	
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
		etName .setOnClickListener(new View.OnClickListener() {
			
			 @Override         
			 public void onClick(View v) {
			  //do something


					switch (v.getId()) {
					case R.id.drug_et_name:
						if (popupwindow != null&&popupwindow.isShowing()) {
							popupwindow.dismiss();
							return;
						} else {
							initmPopupWindowView();
							popupwindow.showAsDropDown(v, 0, 5);
						}
						break;
					default:
						break;
					}

				
			          }
			      });
		
		
		
		
		
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
		if (choice<5 && headacheDiary.getDrugInList(choice)!=null) //修改原有的记录
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
