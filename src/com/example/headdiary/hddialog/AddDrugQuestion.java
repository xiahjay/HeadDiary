




package com.example.headdiary.hddialog;

import java.util.ArrayList;

import com.example.headdiary.HeadDiaryFormActivity;
import com.example.headdiary.R;
import com.example.headdiary.R.id;
import com.example.headdiary.R.layout;
import com.example.headdiary.R.menu;
import com.example.headdiary.R.string;
import com.example.headdiary.data.Drug;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.TimeManager;

import android.os.Bundle;
import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

public class AddDrugQuestion extends Activity{
	private static final int RADIO_NUM=4;
	private  RadioGroup radioGroup;  
	private  RadioButton[] radioBtn=new RadioButton[RADIO_NUM];
	private static final int[] radioID=new int[]{R.id.drug_radio0,R.id.drug_radio1,R.id.drug_radio2,R.id.drug_radio3};
	private int choice;//choice=0~4 means modify the drugs already exists choice=5 means add new drug
	private EditText etName,etQuantity;
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	private PopupWindow popupwindow;	
	private Button btn_before, btn_finish, btn_nodrug;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_drug_dialog);				
		initView();		
		btn_before= (Button)findViewById(R.id.drug_btn_cancel);
		btn_before.setText("上一步");
		btn_finish= (Button)findViewById(R.id.drug_btn_confirm);
		btn_finish.setText("完成");
		
		
	}
	
	

	
	public void initmPopupWindowView(final EditText eText) {

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item,
				null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 500, 400);
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

		//设置输入框添加新药物
		ListView drugList = (ListView) customView.findViewById(R.id.drug_ListView);
		final EditText enterDrug = (EditText) customView.findViewById(R.id.enter_drug);
		Button addDrug = (Button) customView.findViewById(R.id.add_drug);
		addDrug.setOnClickListener(new View.OnClickListener() {
			
			 @Override         
			 public void onClick(View v) {
			  //do something
                 eText.setText(enterDrug.getText());
                 popupwindow.dismiss();
     			 popupwindow = null;									
			          }
			      });
		
		//设置 Listview
		ArrayList<String> drugName = DBManager.getDruglistFromDB();		
		final String[] data= new String[drugName.size()];
		Log.i("drugsize", "drugsize="+drugName.size());
		for(int i=0; i<data.length; i++){
			data[i]=drugName.get(i);
			Log.i("data", "data="+i);
		}
		
		ArrayAdapter<String> aaData = new ArrayAdapter<String>(this,
				R.layout.simple_drug_items, data);
		
		drugList.setAdapter(aaData);		
		drugList.setOnItemClickListener(new AdapterView.OnItemClickListener(){   
          @Override   
          public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,   
                  long arg3) {   
               //do something
          	 Log.i("arg2", "arg2="+arg2);
          	eText.setText(data[arg2]);
          	popupwindow.dismiss();
			popupwindow = null;
          	
          }   
             
      });  
		
		popupwindow.setFocusable(true);
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
							//popupwindow.setFocusable(false);
							popupwindow.dismiss();
							return;
						} else {
							//popupwindow.setFocusable(true);
							initmPopupWindowView(etName);
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
		Intent intent = new Intent (AddDrugQuestion.this,MitigatingQuestion.class);	
		startActivity(intent);
		finish();
	}
	
	public void onClickConfirm(View v){
		Drug newDrug=new Drug();
		String name="",quantity="";
		
		name=etName.getText().toString().trim();
		quantity=etQuantity.getText().toString().trim();
		
		if (!name.equals("") && quantity.equals("")){
			Toast.makeText(this, this.getResources().getString(R.string.error_no_drug_quantity), Toast.LENGTH_SHORT).show();
			return;
		}
		
		int answer=radioGroup.getCheckedRadioButtonId ();
		if (!name.equals("") && answer==-1){
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_no_drug_effect), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(name.equals("") && quantity.equals("") && answer==-1){
			finish();
			Intent intent = new Intent (AddDrugQuestion.this,HeadDiaryFormActivity.class);	
			startActivity(intent);
		}else{
		
		newDrug.setName(name);
		newDrug.setQuantity(quantity);
		newDrug.setEffect(getAnsbyId(answer));
		newDrug.setRecordTime(TimeManager.getStrDateTime());
		 Log.i("time", "data="+newDrug.getRecordTime());
		if (choice<5 && headacheDiary.getDrugInList(choice)!=null) //修改原有的记录
			headacheDiary.setDrugInList(choice, newDrug);
		else
			headacheDiary.addDrugToList(newDrug);
		finish();	
		Intent intent = new Intent (AddDrugQuestion.this,HeadDiaryFormActivity.class);	
		startActivity(intent);
		}
	}
	
	private static int getAnsbyId(int answer){
		for (int i=0;i<radioID.length;i++)
			if (radioID[i]==answer)
				return i;
		return -1;
	}

   public void onClickNodrug(View v){	  
	   finish();
	   Intent intent = new Intent (AddDrugQuestion.this,HeadDiaryFormActivity.class);	
	   startActivity(intent);
   }

}
