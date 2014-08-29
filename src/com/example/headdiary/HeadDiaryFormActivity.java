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
import com.example.headdiary.hddialog.MitigatingDialog;
import com.example.headdiary.hddialog.PrecipiatingDialog;
import com.example.headdiary.hddialog.ProdromeDialog;
import com.example.headdiary.hddialog.StartTimeDialog;
import com.example.headdiary.util.AllExit;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.TimeManager;
import com.example.headdiary.util.ToastManager;

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
public class HeadDiaryFormActivity extends Activity {

	private HeadacheDiary headacheDiary;
	private final static int DRUG_MAX_NUM=5;
	private RelativeLayout[] reLayoutDrug=new RelativeLayout[DRUG_MAX_NUM];
	private int[] reLayoutDrugID={
		R.id.newdiary_rlayout_drug0,R.id.newdiary_rlayout_drug1,R.id.newdiary_rlayout_drug2,R.id.newdiary_rlayout_drug3,R.id.newdiary_rlayout_drug4	
	};
	private TextView[] tvDrug=new TextView[DRUG_MAX_NUM];
	private int[] tvDrugID={
		R.id.newdiary_tv_drug0,R.id.newdiary_tv_drug1,R.id.newdiary_tv_drug2,R.id.newdiary_tv_drug3,R.id.newdiary_tv_drug4	
	};
	
	private TextView tvStartTime,tvEndTime,tvPosition,tvType,tvDegree,tvActivity,tvCompanion,tvPrecipiating,tvMitigating,tvDiagnoseResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head_diary_form);
		headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		tvStartTime=(TextView)findViewById(R.id.newdiary_tv_start_time);
		tvEndTime=(TextView)findViewById(R.id.newdiary_tv_end_time);
		tvPosition=(TextView)findViewById(R.id.newdiary_tv_position);
		tvType=(TextView)findViewById(R.id.newdiary_tv_ache_type);
		tvDegree=(TextView)findViewById(R.id.newdiary_tv_ache_degree);
		tvActivity=(TextView)findViewById(R.id.newdiary_tv_activity);		
		tvCompanion=(TextView)findViewById(R.id.newdiary_tv_companion);
		tvPrecipiating=(TextView)findViewById(R.id.newdiary_tv_precipiating);
		tvMitigating=(TextView)findViewById(R.id.newdiary_tv_mitigating);
		tvDiagnoseResult=(TextView)findViewById(R.id.newdiary_diagnose_result);
		
		for (int i=0;i<DRUG_MAX_NUM;i++){
			tvDrug[i]=(TextView)findViewById(tvDrugID[i]);
			reLayoutDrug[i]=(RelativeLayout)findViewById(reLayoutDrugID[i]);
		}
	}

	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    getMyDiaryInfo();        
	    
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
		if (HeadacheDiaryDAO.getInstance().getIfSelectedDiaryChanged())
			saveAndBack();
	}
	
	public void onClickStartTime(View v){
		Intent intent = new Intent (this, StartTimeDialog.class);		
    	startActivity(intent);
	}
	
	public void onClickEndTime(View v){
		Intent intent = new Intent (this, EndTimeDialog.class);		
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
	
	public void onClickProdrome(View v){
		Intent intent = new Intent (this, ProdromeDialog.class);		
    	startActivity(intent);
	}
	
	public void onClickCompanion(View v){
		Intent intent = new Intent (this, CompanionDialog.class);		
    	startActivity(intent);
	}
	
	public void onClickPrecipiating(View v){
		Intent intent = new Intent (this, PrecipiatingDialog.class);		
    	startActivity(intent);
	}
	
	public void onClickMitigating(View v){
		Intent intent = new Intent (this, MitigatingDialog.class);		
    	startActivity(intent);
	}
	
	public void onClickDrug0(View v){	
		Intent intent = new Intent();
		intent.setClass(this, AddDrugDialog.class);
		intent.putExtra("choice",0);
    	startActivity(intent);
	}
	
	public void onClickDrug1(View v){	
		Intent intent = new Intent();
		intent.setClass(this, AddDrugDialog.class);
		intent.putExtra("choice",1);
    	startActivity(intent);
	}
	
	public void onClickDrug2(View v){	
		Intent intent = new Intent();
		intent.setClass(this, AddDrugDialog.class);
		intent.putExtra("choice",2);
    	startActivity(intent);
	}
	
	public void onClickDrug3(View v){	
		Intent intent = new Intent();
		intent.setClass(this, AddDrugDialog.class);
		intent.putExtra("choice",3);
    	startActivity(intent);
	}
	
	public void onClickDrug4(View v){	
		Intent intent = new Intent();
		intent.setClass(this, AddDrugDialog.class);
		intent.putExtra("choice",4);
    	startActivity(intent);
	}
	
	public void onClickDrugNew(View v){	
		if(headacheDiary.getDrugListSize()>=5){
			Toast.makeText(this, this.getResources().getString(R.string.error_drug_maxnum), Toast.LENGTH_SHORT).show();;
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, AddDrugDialog.class);
		intent.putExtra("choice",5);
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
		AlertDialog.Builder builder = new Builder(HeadDiaryFormActivity.this);
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
				HeadDiaryFormActivity.this.finish();
			}
		});
		
		builder.create().show();
	}

	protected void showDeleteAlertDialog() {
		AlertDialog.Builder builder = new Builder(HeadDiaryFormActivity.this);
		builder.setMessage("是否删除该头痛日志？");
		builder.setPositiveButton("删除", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				deleteHDiary();
				HeadDiaryFormActivity.this.finish();
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
				headacheDiary.makeAidDiagnosis();
				String diagnoseResult=headacheDiary.getStrAidDiagnosis();
				tvDiagnoseResult.setText(diagnoseResult);
		
		//StartTime
		String startTime=headacheDiary.getStartTime();
		if (startTime!=null)
			startTime=startTime.substring(0,16);
    	
    	tvStartTime.setText(startTime);
    	
    	//EndTime
    	String endTime=headacheDiary.getEndTime();
		if (endTime!=null)
			endTime=endTime.substring(0,16);
    	
    	tvEndTime.setText(endTime);
    	
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
    		
    	//Prodrome
    	/*String prodrome="";
    	Boolean completeProdromeFlag=true;
    	for (i=0;i<StrConfig.HDProdromeCategory.length;i++){
    		ans=headacheDiary.getProdrome(i);
    		if (ans!=-1){
    			if (ans!=0){		//ans==0 means no this type of prodrome
    				prodrome+=StrConfig.HDProdromeCategory[i];
    				tempstr=StrConfig.HDProdromeValue[i][ans];
    				if (!tempstr.equals(this.getResources().getString(R.string.prompt_therebe)))	//if the answer is "therebe",there is no need to show.
    					prodrome+=" ( "+tempstr+" )";
    				prodrome+=this.getResources().getString(R.string.punctuation_semicolon);
    			}
    		}
    		else{
    			completeProdromeFlag=false;
    			break;
    		}
    	}
    	
    	if (completeProdromeFlag){
    		if (!prodrome.equals("")){
    			prodrome=prodrome.substring(0,prodrome.length()-1);
    		}
    		else{
    			prodrome=this.getResources().getString(R.string.prompt_none);
    		}
    		tvProdrome.setVisibility(View.VISIBLE);
    		tvProdrome.setText(prodrome);
    	}
    	else{
    		tvProdrome.setVisibility(View.GONE);
    	}*/
    	
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
    	
    	//-----------------------------------------//
    	//			additional information         //
    	//-----------------------------------------//
    	
    	//Precipiating factor
    	String precipiating="";
    	length=StrConfig.HDPrecipiating.length;
    	for (i=0;i<length-1;i++)
    		if (headacheDiary.getPrecipiating(i)==1){
    			tempstr=StrConfig.HDPrecipiating[i];
				precipiating+=tempstr+this.getResources().getString(R.string.punctuation_semicolon);
			}	
    	
    	if (headacheDiary.getPrecipiating(length-1)==1){ //选择了其他
			tempstr=headacheDiary.getPrecipiatingComment();
			if (tempstr.equals(""))
				tempstr=StrConfig.HDPrecipiating[length-1];
			precipiating+=tempstr+this.getResources().getString(R.string.punctuation_semicolon);
		}	
    	
    	if (!precipiating.equals("")){
    		precipiating = precipiating.substring(0,precipiating.length()-1);
    		tvPrecipiating.setVisibility(View.VISIBLE);
    		tvPrecipiating.setText(precipiating);
    	}
    	else{
    		tvPrecipiating.setVisibility(View.GONE);
    	}
    	
    	//Mitigating factor
    	String mitigating="";
    	length=StrConfig.HDMitigating.length;
    	
    	for (i=0;i<length-1;i++)
    		if (headacheDiary.getMitigating(i)==1){
    			tempstr=StrConfig.HDMitigating[i];
				mitigating+=tempstr+this.getResources().getString(R.string.punctuation_semicolon);
			}	
    	
    	if (headacheDiary.getMitigating(length-1)==1){ //选择了其他
			tempstr=headacheDiary.getMitigatingComment();
			if (tempstr.equals(""))
				tempstr=StrConfig.HDMitigating[length-1];
			mitigating+=tempstr+this.getResources().getString(R.string.punctuation_semicolon);
		}	
    	
    	if (!mitigating.equals("")){
    		mitigating = mitigating.substring(0,mitigating.length()-1);
    		tvMitigating.setVisibility(View.VISIBLE);
    		tvMitigating.setText(mitigating);
    	}
    	
    	else{
    		tvMitigating.setVisibility(View.GONE);
    	}
    	
    	//-----------------------------------------//
    	//			   drug information            //
    	//-----------------------------------------//
    	length=headacheDiary.getDrugListSize();
    	Drug tempDrug;
    	ImageView drugArrow=(ImageView)findViewById(R.id.newdiary_arrow_drug0);
    	
    	if (length==0){
    		drugArrow.setVisibility(View.GONE);
    		
    		mText= "<font color=\"#000000\">"+this.getResources().getString(R.string.prompt_no_drug)+"</font>";
    		tvDrug[0].setText(Html.fromHtml(mText));
    		reLayoutDrug[0].setVisibility(View.VISIBLE);
    		
    		for (i=1;i<DRUG_MAX_NUM;i++)
    			reLayoutDrug[i].setVisibility(View.GONE);
    	}
    	else{
    		drugArrow.setVisibility(View.VISIBLE);
    		
    		for (i=0;i<length;i++){
    			tempDrug=headacheDiary.getDrugInList(i);
    			mText= "<font color=\"#000000\">"+this.getResources().getString(R.string.title_drug_name)+"&nbsp;&nbsp;"+tempDrug.getName()+"</font><br>";
    			mText+="<font color=\"#000000\">"+this.getResources().getString(R.string.title_drug_quantity)+"&nbsp;&nbsp;"+tempDrug.getQuantity()+"</font><br>";
    			mText+= "<font color=\"#0066FF\">"+this.getResources().getString(R.string.prompt_drug_effect)+" "+StrConfig.HDDrugEffect[tempDrug.getEffect()]+"</font>";
    			tvDrug[i].setText(Html.fromHtml(mText));
    			reLayoutDrug[i].setVisibility(View.VISIBLE);
    		}
    		for (i=length;i<DRUG_MAX_NUM;i++)
    			reLayoutDrug[i].setVisibility(View.GONE);
    	}
    		
	}
}
