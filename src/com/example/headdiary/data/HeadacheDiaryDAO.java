package com.example.headdiary.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.R.anim;
import android.R.integer;
import android.util.Log;

import com.example.headdiary.HeadDiaryFormActivity;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.DocumentAdapter;
import com.example.headdiary.util.TimeManager;

public class HeadacheDiaryDAO {
	public static HeadacheDiaryDAO mInstance;
	/*
	 * for HeadacheDiaryFormActivity:Deep Copy. user can choose to save or abandon the changes.
	 */
	private HeadacheDiary headacheDiarySelected;
	private Boolean ifSelectedDiaryChanged;
	/*
	 * Record lastHeadacheDiary: always be consistent with database(this is incomplete and recId=0)
	 */
	private HeadacheDiary lastHeadacheDiary;
	/*
	 * for MainDocumentActivity & MainAnalysisActivity: always be consistent with database
	 */
	private ArrayList<HeadacheDiary> documentHDiaryList;
	private int documentHDChoice;
	private HeadacheAnalysis[] headacheAnalysisList;

	public static HeadacheDiaryDAO getInstance() {
		if (mInstance==null)
			mInstance=new HeadacheDiaryDAO();
		return mInstance;
	}
	
	public static HeadacheDiaryDAO newInstance() {
		mInstance=new HeadacheDiaryDAO();
		return mInstance;
	}
	
	public HeadacheDiaryDAO(){
		ifSelectedDiaryChanged=false;
		documentHDChoice=-1;
	}

	public HeadacheDiary getHeadacheDiarySelected() {
		if (headacheDiarySelected==null)
			headacheDiarySelected=new HeadacheDiary();
		return headacheDiarySelected;
	}

	public void setHeadacheDiarySelected(HeadacheDiary headacheDiarySelected) {
		this.headacheDiarySelected = headacheDiarySelected; 								//reset the record
		setIfSelectedDiaryChanged(false);
	}

	public HeadacheDiary getLastHeadacheDiary() {
		return lastHeadacheDiary;
	}

	public void setLastHeadacheDiary(HeadacheDiary lastHeadacheDiary) {
		this.lastHeadacheDiary = lastHeadacheDiary;
	}

	public Boolean getIfSelectedDiaryChanged() {
		return ifSelectedDiaryChanged;
	}

	public void setIfSelectedDiaryChanged(Boolean ifSelectedDiaryChanged) {
		this.ifSelectedDiaryChanged = ifSelectedDiaryChanged;
	}

	public ArrayList<HeadacheDiary> getDocumentHDiaryList() {
		if (documentHDiaryList==null)
			DBManager.getDocumentHDiaryFromDBToDAO();
		return documentHDiaryList;
	}

	public void setDocumentHDiaryList(ArrayList<HeadacheDiary> documentHDiaryList) {
		this.documentHDiaryList = documentHDiaryList;
	}
	
	public void removeDocumentHDiayList(int posi) {
		if (posi>=0 && posi<documentHDiaryList.size())
			documentHDiaryList.remove(posi);
	}
	
	public int getDocumentHDChoice() {
		return documentHDChoice;
	}
	
	public void setDocumentHDChoice(int documentHDChoice) {
		this.documentHDChoice = documentHDChoice;
	}
	
	public HeadacheAnalysis[] getHeadacheAnalysisList() {
		if (headacheAnalysisList==null)
			doHeadacheAnalysis();
		return headacheAnalysisList;
	}
	
	public void setHeadacheAnalysisList(HeadacheAnalysis[] headacheAnalysisList) {
		this.headacheAnalysisList = headacheAnalysisList;
	}
	
	
	//----------------方法----------------------//

	public Boolean getIfLastDiaryComplete() {
		if (lastHeadacheDiary==null)
			return true;
		return false;
	}
	
	public ArrayList<HashMap<String, Object>> getHDiaryListForDisplay(ArrayList<HeadacheDiary> hDiaryList, int timePeriod) {
		ArrayList<HashMap<String, Object>> resultList=new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map;
		Calendar start,end;
		long interval=0,tillNowInterval=0;
		String strLastTime="";
		String startTime,endTime;
		int degree,aidDiagnosis;
		int maxMonthInterval=0;
		switch (timePeriod) {
		case 0:
			maxMonthInterval=1;
			break;
		case 1:
			maxMonthInterval=3;
			break;
		case 2:
			maxMonthInterval=6;
		default:
			break;
		}
		for (HeadacheDiary headacheDiary:hDiaryList){
			startTime=headacheDiary.getStartTime();
			endTime=headacheDiary.getEndTime();
			degree=headacheDiary.getDegree();
			aidDiagnosis=headacheDiary.getAidDiagnosis();
			
			start=TimeManager.parseStrDateTime(startTime);
			tillNowInterval=Calendar.getInstance().getTimeInMillis()-start.getTimeInMillis();
			tillNowInterval=tillNowInterval/30/24/3600/1000;

			if (tillNowInterval>maxMonthInterval && maxMonthInterval>0){
				break;
			}
			
	        end=TimeManager.parseStrDateTime(endTime);
	        interval=end.getTimeInMillis()-start.getTimeInMillis();
	        long min=interval/60000;
	        strLastTime=TimeManager.getStrDayHourMin(min);
	        
	        map=new HashMap<String, Object>();
			map.put(DocumentAdapter.ArrayKey_Degree, degree);
			map.put(DocumentAdapter.ArrayKey_FirstLine, startTime.substring(0, 16)+"  "+StrConfig.HDSecondaryClassification[aidDiagnosis]);
			map.put(DocumentAdapter.ArrayKey_SecondLine, "持续时间： "+strLastTime);
			resultList.add(map);
		}
		return resultList;
	}
	
	public void doHeadacheAnalysis(){
		if (documentHDiaryList==null)
			DBManager.getDocumentHDiaryFromDBToDAO();
		if (documentHDiaryList==null || documentHDiaryList.size()==0){
			headacheAnalysisList=null;
			return;
		}
		
		headacheAnalysisList=new HeadacheAnalysis[StrConfig.TimePeriod.length];
		//0:1个月内；1：3个月内；2:6个月内；3全部
		String startTime;
		int tillNowMonthInterval=0;
		int id=-1;
		int[] totalMonth={1,3,6,0};
		Calendar start,end;
		HeadacheAnalysis headacheAnalysis=null;
		
		for (HeadacheDiary headacheDiary:documentHDiaryList){		//按照时间倒序排列，离现在越近的在前面
			startTime=headacheDiary.getStartTime();			
			start=TimeManager.parseStrDateTime(startTime);
			tillNowMonthInterval=TimeManager.getMonthInterval(start, Calendar.getInstance());
			
			if (tillNowMonthInterval<=1){
				id=0;
			}
			else if (tillNowMonthInterval<=3){
				id=1;
			}
			else if (tillNowMonthInterval<=6){
				id=2;
			}
			else{
				id=3;
			}
			
			//进入一个新的时间类别分析前，先处理好之前的时间分类
			if (headacheAnalysisList[id]==null)
				clonePreviousAnalysis(id);
			
			headacheAnalysis=headacheAnalysisList[id];
			headacheAnalysis.setTotalMonth(totalMonth[id]);
			headacheAnalysis.addSingleHeadacheDiaryToAnalysis(headacheDiary);
			
		}
		//最后一条记录是在半年前，也就是说进入“全部”的统计,需要计算totalMonth
		if (id==3){
			start=headacheAnalysis.getStartAnalysisTime();
			end=headacheAnalysis.getEndAnalysisTime();
			headacheAnalysis.setTotalMonth(TimeManager.getMonthInterval(start, end));
			return;
		}
		
		//分析结束前处理好后面的时间分类
		for (int i=id+1;i<headacheAnalysisList.length;i++){
			clonePreviousAnalysis(i);
		}	
		
	}


	private void clonePreviousAnalysis(int id) {
		// TODO Auto-generated method stub
		for (int i=1;i<=id;i++){
			if (headacheAnalysisList[i]==null && headacheAnalysisList[i-1]!=null){
					headacheAnalysisList[i]=(HeadacheAnalysis) headacheAnalysisList[i-1].clone();
			}
		}
		
		if(headacheAnalysisList[id]==null)
			headacheAnalysisList[id]=new HeadacheAnalysis();
	}

	
	
	
}
