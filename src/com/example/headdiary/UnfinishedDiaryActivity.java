package com.example.headdiary;

import java.util.Calendar;

import com.example.headdiary.R;
import com.example.headdiary.R.id;
import com.example.headdiary.R.layout;
import com.example.headdiary.R.menu;
import com.example.headdiary.R.string;
import com.example.headdiary.data.Drug;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.hddialog.AcheDegreeDialog;
import com.example.headdiary.hddialog.AchePositionDialog;
import com.example.headdiary.hddialog.AcheTypeDialog;
import com.example.headdiary.hddialog.ActivityAggravateDialog;
import com.example.headdiary.hddialog.AddDrugDialog;
import com.example.headdiary.hddialog.CompanionDialog;
import com.example.headdiary.hddialog.EndTimeDialog;
import com.example.headdiary.hddialog.EndTimeQuestion;
import com.example.headdiary.hddialog.MitigatingDialog;
import com.example.headdiary.hddialog.PrecipiatingDialog;
import com.example.headdiary.hddialog.ProdromeDialog;
import com.example.headdiary.hddialog.StartTimeDialog;
import com.example.headdiary.hddialog.StartTimeQuestion;
import com.example.headdiary.util.AllExit;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.TimeManager;
import com.example.headdiary.util.ToastManager;
import com.example.headdiary.ActivityManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//display headacheDiarySelected in HeadacheDiaryDAO
public class UnfinishedDiaryActivity extends Activity {
	private HeadacheDiary headacheDiary;	
	private TextView tvStartTime,tvPosition,tvType,tvDegree,tvActivity,tvCompanion,tvDiagnoseResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unfinished_diary);
		//Boolean ifComplete=headacheDiary.getIfComplete();
		if (HeadacheDiaryDAO.getInstance().getIfLastDiaryComplete())
		{Intent intent = new Intent (UnfinishedDiaryActivity.this,StartTimeQuestion.class);	
		startActivity(intent);}
		else {
			Intent intent = new Intent (UnfinishedDiaryActivity.this,EndTimeQuestion.class);	
			startActivity(intent);
		}
		
		headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
		findView();		
	}
	private void findView() {
		// TODO Auto-generated method stub
		tvStartTime=(TextView)findViewById(R.id.unfinished_tv_start_time);		
		tvPosition=(TextView)findViewById(R.id.unfinished_tv_position);
		tvType=(TextView)findViewById(R.id.unfinished_tv_ache_type);
		tvDegree=(TextView)findViewById(R.id.unfinished_tv_ache_degree);
		tvActivity=(TextView)findViewById(R.id.unfinished_tv_activity);		
		tvCompanion=(TextView)findViewById(R.id.unfinished_tv_companion);		
		tvDiagnoseResult=(TextView)findViewById(R.id.un_diagnose_result);	
	}

	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    if(headacheDiary.getEndTime()!=null)
	    {finish();}
	    getMyDiaryInfo();
	    //ActivityManager.getInstance().addActivity(this);
	    
	    
	}
	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){  
	        //捕捉返回键
	    	clickBack();
            return true;
	    }  
	   
	   return super.onKeyDown(keyCode, event);  
	}  
	
	
	public void onClickBack(View v){
		clickBack();
	}
	
	public void onClickDelete(View v){
		
		showDeleteAlertDialog();
	}
	
	public void onClickSave(View v){
		//if (HeadacheDiaryDAO.getInstance().getIfSelectedDiaryChanged())
			saveAndBack();
		//else {finish();}
	}
	
	public void onClickStartTime(View v){
		Intent intent = new Intent (this, StartTimeDialog.class);		
    	startActivity(intent);
	}
		
	
	public void onClickAchePosition(View v){
		Intent intent = new Intent (this, AchePositionDialog.class);		
    	startActivity(intent);
	}
	
	public void onClickAcheType(View v){
		Intent intent = new Intent (this, AcheTypeDialog.class);		
    	startActivity(intent);
	}
	
	public void onClickAcheDegree(View v){
		Intent intent = new Intent (this, AcheDegreeDialog.class);		
    	startActivity(intent);
	}
	
	public void onClickActivity(View v){
		Intent intent = new Intent (this, ActivityAggravateDialog.class);		
    	startActivity(intent);
	}
			
	public void onClickCompanion(View v){
		Intent intent = new Intent (this, CompanionDialog.class);		
    	startActivity(intent);
	}
	

	
	//-------------------------------//
	//            方法                                                         //
	//-------------------------------//
	private void clickBack(){
    	if (HeadacheDiaryDAO.getInstance().getIfSelectedDiaryChanged())
    		showExitAlertDialog();
    	else {
			finish();
		}
	}
	
	protected void showExitAlertDialog() {
		AlertDialog.Builder builder = new Builder(UnfinishedDiaryActivity.this);
		builder.setMessage("是否保存修改？");
		builder.setPositiveButton("保存", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				saveAndBack();
			}
		 
		});
		builder.setNegativeButton("放弃", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				UnfinishedDiaryActivity.this.finish();
			}
		});
		
		builder.create().show();
	}

	protected void showDeleteAlertDialog() {
		AlertDialog.Builder builder = new Builder(UnfinishedDiaryActivity.this);
		builder.setMessage("是否删除该头痛日志？");
		builder.setPositiveButton("删除", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				deleteHDiary();
				UnfinishedDiaryActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method 
			}
		});
		
		builder.create().show();
	}
	
	private void saveAndBack(){
		headacheDiary.setRecordTime(TimeManager.getStrDateTime());
		String hint=DBManager.saveHDiaryToDB(headacheDiary);
		ToastManager.showShortToast(hint);
    	this.finish();
	}
	
	private void deleteHDiary() {
		// TODO Auto-generated method stub
		DBManager.deleteHDiaryByRecId(HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected());
		if (headacheDiary.getRecId()==0){
			HeadacheDiaryDAO.getInstance().setLastHeadacheDiary(null);
		}
		else{
			HeadacheDiaryDAO.getInstance().removeDocumentHDiayList(HeadacheDiaryDAO.getInstance().getDocumentHDChoice());
			HeadacheDiaryDAO.getInstance().setDocumentHDChoice(-1);
		}
		HeadacheDiaryDAO.getInstance().setHeadacheDiarySelected(null);
		HeadacheDiaryDAO.getInstance().setIfSelectedDiaryChanged(true);		
	}

	private void getMyDiaryInfo(){
		String mText,tempstr;
		int ans,i,length;
		//diagnose result
		//Boolean ifComplete=headacheDiary.getIfComplete();
		//if(ifComplete){			
		headacheDiary.makeAidDiagnosis();
		String diagnoseResult=headacheDiary.getStrAidDiagnosis();
		tvDiagnoseResult.setText(diagnoseResult);
		//StartTime
		String startTime=headacheDiary.getStartTime();
		if (startTime!=null)
			startTime=startTime.substring(0,16);
    	
    	tvStartTime.setText(startTime);
    	    	
    	
    	//AchePosition
    	ans=headacheDiary.getPosition();
    	String position="";
    	if (ans!=-1){
    		position=StrConfig.HDPosition[ans];
    		ans=headacheDiary.getIfAroundEye();
    		if (ans==1)
    			position+=" - "+StrConfig.HDPositionAroundEyeTrue;
    	}

    	tvPosition.setText(position);
    	
    	//AcheType
    	ans=headacheDiary.getType();
    	String type=headacheDiary.getTypeComment();
    	if (ans!=-1){
    		if (type.equals("")){
    			type=StrConfig.HDAcheType[ans];
    		}
    	}
			
    	tvType.setText(type);
    	
    	//AcheDegree
    	String degree="";
    	ans=headacheDiary.getDegree();
    	if (ans!=-1)
    		degree=String.format("%d - %s",ans,StrConfig.HDAcheDegreeGrade[ans]);
    	
    	tvDegree.setText(degree);
    	
    	//Activity
    	String activity="";
    	ans=headacheDiary.getIfActivityAggravate();
    	if (ans!=-1)
    		activity=StrConfig.HDActivity[ans];
    	tvActivity.setText(activity);
    		    	    	
    	//Companion
    	String companion="";
    	Boolean completeCompanionFlag=true;
    	for (i=0;i<StrConfig.HDCompanionCategory.length;i++){
    		ans=headacheDiary.getCompanion(i);
    		if (ans!=-1){
    			if (ans!=0){
    				companion+=StrConfig.HDCompanionCategory[i];
    				tempstr=StrConfig.HDCompanionValue[i][ans];
    				if (!tempstr.equals(this.getResources().getString(R.string.prompt_therebe)))	//if the answer is "therebe",there is no need to show.
    					companion+=" - "+tempstr;
    				companion+=this.getResources().getString(R.string.punctuation_semicolon);
    			}
    		}
    		else{
    			completeCompanionFlag=false; //仍有不完整的数据
    			break;
    		}
    	}

    	if (completeCompanionFlag){
    		if (!companion.equals("")){
    			companion=companion.substring(0,companion.length()-1);
    		}
    		else{
    			companion=this.getResources().getString(R.string.prompt_none);
    		}
    		tvCompanion.setVisibility(View.VISIBLE);
    		tvCompanion.setText(companion);
    	}
    	else{
    		tvCompanion.setVisibility(View.GONE);
    	}
    	
	}

	
}
