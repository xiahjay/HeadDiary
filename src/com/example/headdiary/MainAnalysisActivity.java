package com.example.headdiary;

import java.text.DecimalFormat;

import com.example.headdiary.data.HeadacheAnalysis;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.hddialog.AchePositionQuestion;
import com.example.headdiary.hddialog.StartTimeQuestion;
import com.example.headdiary.util.DocumentAdapter;
import com.example.headdiary.util.TimeManager;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainAnalysisActivity extends Activity {
	private TextView tvTime,tvRecord,tvStyle;
	private TextView tvFrequency,tvDuration,tvPosition,tvType,tvDegree,tvActivity,tvProdrome,tvCompanion,tvPrecipiating,tvMitigating,tvDrug;
	private int[] textViewIdList={R.id.analysis_tv_frequency,R.id.analysis_tv_interval,R.id.analysis_tv_position,R.id.analysis_tv_ache_type,R.id.analysis_tv_ache_degree
			,R.id.analysis_tv_activity,R.id.analysis_tv_prodrome,R.id.analysis_tv_companion,R.id.analysis_tv_precipiating,R.id.analysis_tv_mitigating,R.id.analysis_tv_drug};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_analysis);
		initView();
		refreshAnalysis();
	}

	
	
	private void initView(){
		tvTime=(TextView)findViewById(R.id.analysis_tv_time);
		tvRecord=(TextView)findViewById(R.id.analysis_tv_record);
		tvStyle=(TextView)findViewById(R.id.analysis_tv_style);
		tvStyle.setText(StrConfig.AnalysisStyle[0]);
		
		tvFrequency=(TextView)findViewById(R.id.analysis_tv_frequency);
		tvDuration=(TextView)findViewById(R.id.analysis_tv_interval);
		tvPosition=(TextView)findViewById(R.id.analysis_tv_position);
		tvType=(TextView)findViewById(R.id.analysis_tv_ache_type);
		tvDegree=(TextView)findViewById(R.id.analysis_tv_ache_degree);
		tvActivity=(TextView)findViewById(R.id.analysis_tv_activity);
		tvProdrome=(TextView)findViewById(R.id.analysis_tv_prodrome);
		tvCompanion=(TextView)findViewById(R.id.analysis_tv_companion);
		tvPrecipiating=(TextView)findViewById(R.id.analysis_tv_precipiating);
		tvMitigating=(TextView)findViewById(R.id.analysis_tv_mitigating);
		tvDrug=(TextView)findViewById(R.id.analysis_tv_drug);
		
	}
	
	//--------------Click响应事件-------------------//
	
	public void onClickBack(View v){
    	this.finish();
	}

	public void onClickPeriod(View v){
		new AlertDialog.Builder(this)
			.setTitle("时间跨度")
			.setSingleChoiceItems(StrConfig.TimePeriod, UserDAO.getInstance().getTimePeriod(),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (UserDAO.getInstance().getTimePeriod()!=which){
								UserDAO.getInstance().setTimePeriod(which);
								refreshAnalysis();
							}
							dialog.dismiss();
						}

			}).show();
	}
	
	public void onClickStyle(View v){
		new AlertDialog.Builder(this)
		.setTitle("显示方式")
		.setSingleChoiceItems(StrConfig.AnalysisStyle, UserDAO.getInstance().getDocumentStyle(),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (UserDAO.getInstance().getAnalysisStyle()!=which){
							UserDAO.getInstance().setListStyle(which);
							Intent intent = new Intent (MainAnalysisActivity.this,GraphicActivity.class);	
							startActivity(intent);
							finish();
						}
						dialog.dismiss();
					}
		}).show();
	}	
	
	//--------------方法---------------------//
	private void refreshAnalysis() {
		// TODO Auto-generated method stub
		int timePeriod=UserDAO.getInstance().getTimePeriod();
		tvTime.setText(StrConfig.TimePeriod[timePeriod]);
		HeadacheAnalysis[] headacheAnalysisList=HeadacheDiaryDAO.getInstance().getHeadacheAnalysisList();
		if (headacheAnalysisList!=null)
			displayHeadacheAnalysis(headacheAnalysisList[timePeriod]);
		else {
			displayHeadacheAnalysis(null);
		}
		
	}



	private void displayHeadacheAnalysis(HeadacheAnalysis headacheAnalysis) {
		// TODO Auto-generated method stub
		TextView tv;
		String strTemp="";
		int intTemp;
		if (headacheAnalysis==null){
			for (int id:textViewIdList){
				tv=(TextView)findViewById(id);
				tv.setText("");
			}
			tvRecord.setText("0条记录");
			return;
		}
		
		tvRecord.setText(Integer.toString(headacheAnalysis.getTotalDiary())+"条记录");
		int totalDiary=headacheAnalysis.getTotalDiary();
		
		//Frequency
		String frequency="统计范围："+TimeManager.getStrDate(headacheAnalysis.getStartAnalysisTime())+" ~ "+TimeManager.getStrDate(headacheAnalysis.getEndAnalysisTime())+"\n";
		float averageFrequencyPerMonth=totalDiary/(float)headacheAnalysis.getTotalMonth();
		String strAverageFrequency=new DecimalFormat("###,###,###.#").format(averageFrequencyPerMonth);
		frequency+="平均："+ strAverageFrequency+"次/月；最少："+Integer.toString(headacheAnalysis.getMinFrequencyPerMonth())+"次/月；最多："+Integer.toString(headacheAnalysis.getMaxFrequencyPerMonth())+"次/月";
		tvFrequency.setText(frequency);
		
		//Duration
		String duration;
		long averageDuration=headacheAnalysis.getSumIntervalMinutes()/totalDiary;
		duration="平均："+TimeManager.getStrDayHourMin(averageDuration)+"；最短："+TimeManager.getStrDayHourMin(headacheAnalysis.getMinIntervalMinutes())+"；最长："+TimeManager.getStrDayHourMin(headacheAnalysis.getMaxIntervalMinutes());

		strTemp=getStrPercentByCategoryCountList(headacheAnalysis.getIntervalCount(), StrConfig.HDIntervalCategory, totalDiary);
		
		if (!strTemp.equals(""))
			duration+="\n"+strTemp.substring(0, strTemp.length()-1);
		tvDuration.setText(duration);
		
		//Position
		String position=getStrPercentByCategoryCountList(headacheAnalysis.getPositionCount(), StrConfig.HDPosition, totalDiary);

		intTemp=headacheAnalysis.getIfAroundEyeCount()*100/totalDiary;
		if (intTemp>0)
			position+="\n"+StrConfig.HDPositionAroundEyeTrue+"："+Integer.toString(intTemp)+"%；";
		tvPosition.setText(position);
		
		//Type
		String type=getStrPercentByCategoryCountList(headacheAnalysis.getTypeCount(), StrConfig.HDAcheType, totalDiary);
		tvType.setText(type);
		
		//Degree
		float averageDegree=headacheAnalysis.getSumDegree()/(float)totalDiary;
		strTemp=new DecimalFormat("###,###,###.#").format(averageDegree);
		String degree="平均："+strTemp+"；最轻："+Integer.toString(headacheAnalysis.getMinDegree())+"；最重："+Integer.toString(headacheAnalysis.getMaxDegree());
		degree+="\n"+getStrPercentByCategoryCountList(headacheAnalysis.getDegreeCount(), StrConfig.HDAcheDegree, totalDiary);
		tvDegree.setText(degree);
		
		//Activity
		String activity=StrConfig.HDActivity[1]+"："+Integer.toString(headacheAnalysis.getIfActivityAggravateCount()*100/totalDiary)+"%";
		tvActivity.setText(activity);
		
		//Prodrome
		String prodrome=getStrPercentByMultipleCategoryCountList(headacheAnalysis.getProdromeCount(), StrConfig.HDProdromeCategory, StrConfig.HDProdromeValue,totalDiary);
		if(prodrome.equals(""))
			prodrome=this.getResources().getString(R.string.prompt_none);
		tvProdrome.setText(prodrome);
		
		//Companion
		String companion=getStrPercentByMultipleCategoryCountList(headacheAnalysis.getCompanionCount(), StrConfig.HDCompanionCategory, StrConfig.HDCompanionValue, totalDiary);
		if(companion.equals(""))
			companion=this.getResources().getString(R.string.prompt_none);
		tvCompanion.setText(companion);
		
		//Precipiating
		String precipiating=getStrPercentByCategoryCountList(headacheAnalysis.getPrecipiatingCount(), StrConfig.HDPrecipiating, totalDiary);
		if(precipiating.equals(""))
			precipiating=this.getResources().getString(R.string.prompt_none);
		tvPrecipiating.setText(precipiating);
		
		//Mitigating
		String mitigating=getStrPercentByCategoryCountList(headacheAnalysis.getMitigatingCount(), StrConfig.HDMitigating, totalDiary);
		if(mitigating.equals(""))
			mitigating=this.getResources().getString(R.string.prompt_none);
		tvMitigating.setText(mitigating);
		
		//Drug
		String drug="";
		for (int i=0;i<headacheAnalysis.getDrugNameList().size();i++){
			int count=headacheAnalysis.getDrugCountList().get(i);
			drug+=headacheAnalysis.getDrugNameList().get(i)+"："+Integer.toString(count)+"次 ( "+count*100/totalDiary+"% )\n";
		}
		if(!drug.equals("")){
			drug=drug.substring(0,drug.length()-1);
		}
		else {
			drug=this.getResources().getString(R.string.prompt_none);
		}
		tvDrug.setText(drug);
	}
	
	private String getStrPercentByCategoryCountList(int[] countList,String[] categoryNameList,int totalNum){
		int countValue;
		String result="";
		for(int i=0;i<countList.length;i++){
			countValue=countList[i];
			if (countValue>0){
				result+=categoryNameList[i]+"："+Integer.toString(countValue*100/totalNum)+"%；";
			}
		}
		return result;
	}
	
	private String getStrPercentByMultipleCategoryCountList(int[][] countList,String[] categoryNameList,String[][] categoryValueList,int totalNum){
		int countValue,countValue2=0;
		String result="";
		for(int i=0;i<countList.length;i++){
			countValue=countList[i][0];
			if (countValue>0){
				result+=categoryNameList[i]+"："+Integer.toString(countValue*100/totalNum)+"% ";
				if (categoryValueList[i][1].equals(this.getResources().getString(R.string.prompt_therebe))){
					result+="\n";
					continue;
				}
				result+=" ( ";
				for (int j=1;j<countList[0].length;j++){
					countValue2=countList[i][j];
					if (countValue2>0){
						result+=categoryValueList[i][j]+" "+Integer.toString(countValue2*100/totalNum)+"%；";
					}
				}
				result=result.substring(0, result.length()-1);
				result+=" )\n";
			}
		}
		
		if (!result.equals("")){
			result=result.substring(0, result.length()-1);
		}

		return result;
	}

}
