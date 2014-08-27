package com.example.headdiary.data;

import java.util.ArrayList;
import java.util.Calendar;

import android.R.integer;
import android.util.Log;

import com.example.headdiary.util.TimeManager;

/**
 * POJO
 * @author Jane Huang
 *
 */
public class HeadacheAnalysis implements Cloneable{
	Calendar StartAnalysisTime,EndAnalysisTime;
	int TotalDiary,TotalMonth;
	
	long SumIntervalMinutes,MinIntervalMinutes,MaxIntervalMinutes;
	int MinFrequencyPerMonth,MaxFrequencyPerMonth,MinDegree,MaxDegree,SumDegree,curMonthInterval,curFrequencyPerMonth,IfAroundEyeCount,IfActivityAggravateCount;
	int[] IntervalCount,PositionCount,TypeCount,DegreeCount,PrecipiatingCount,MitigatingCount;
	int[][] ProdromeCount,CompanionCount;
	ArrayList<String> DrugNameList;
	ArrayList<Integer> DrugCountList;
	
	public HeadacheAnalysis(){
		clearAnalysis();
	}

	private void clearAnalysis() {
		// TODO Auto-generated method stub
		TotalDiary=0;
		SumIntervalMinutes=MaxIntervalMinutes=0;
		MinIntervalMinutes=-1;
		curMonthInterval=curFrequencyPerMonth=MinFrequencyPerMonth=MinDegree=-1;
		MaxFrequencyPerMonth=IfAroundEyeCount=MaxDegree=SumDegree=IfActivityAggravateCount=0;
		IntervalCount=new int[StrConfig.HDIntervalCategory.length];
		PositionCount=new int[StrConfig.HDPosition.length];
		TypeCount=new int[StrConfig.HDAcheType.length];
		DegreeCount=new int[StrConfig.HDAcheDegree.length];
		ProdromeCount=new int[StrConfig.HDProdromeValue.length][StrConfig.HDProdromeValue[0].length];
		CompanionCount=new int[StrConfig.HDCompanionValue.length][StrConfig.HDCompanionValue[0].length];
		PrecipiatingCount=new int[StrConfig.HDPrecipiating.length];
		MitigatingCount=new int[StrConfig.HDMitigating.length];
		DrugNameList=new ArrayList<String>();
		DrugCountList=new ArrayList<Integer>();
	}

	@Override  
    public Object clone() {  
        HeadacheAnalysis newHeadacheanaAnalysis = null;  
        try{  
            newHeadacheanaAnalysis = (HeadacheAnalysis)super.clone();   //浅复制 
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        
        //深度复制 
        int length,length2,i,j;
		length=StrConfig.HDIntervalCategory.length;
		newHeadacheanaAnalysis.IntervalCount=new int[length];
		for (i=0;i<length;i++)
			newHeadacheanaAnalysis.IntervalCount[i]=IntervalCount[i];
		 
		length=StrConfig.HDPosition.length;
		newHeadacheanaAnalysis.PositionCount=new int[length];
		for (i=0;i<length;i++)
			newHeadacheanaAnalysis.PositionCount[i]=PositionCount[i];
		
		length=StrConfig.HDAcheType.length;
		newHeadacheanaAnalysis.TypeCount=new int[length];
		for (i=0;i<length;i++)
			newHeadacheanaAnalysis.TypeCount[i]=TypeCount[i];
		
		length=StrConfig.HDAcheDegree.length;
		newHeadacheanaAnalysis.DegreeCount=new int[length];
		for (i=0;i<length;i++)
			newHeadacheanaAnalysis.DegreeCount[i]=DegreeCount[i];
		
		length=StrConfig.HDProdromeValue.length;
		length2=StrConfig.HDProdromeValue[0].length;
		newHeadacheanaAnalysis.ProdromeCount=new int[length][length2];
		for (i=0;i<length;i++)
			for (j=0;j<length2;j++)
				newHeadacheanaAnalysis.ProdromeCount[i][j]=ProdromeCount[i][j];
		
		length=StrConfig.HDCompanionValue.length;
		length2=StrConfig.HDCompanionValue[0].length;
		newHeadacheanaAnalysis.CompanionCount=new int[length][length2];
		for (i=0;i<length;i++)
			for (j=0;j<length2;j++)
				newHeadacheanaAnalysis.CompanionCount[i][j]=CompanionCount[i][j];
		
		length=StrConfig.HDPrecipiating.length;
		newHeadacheanaAnalysis.PrecipiatingCount=new int[length];
		for (i=0;i<length;i++)
			newHeadacheanaAnalysis.PrecipiatingCount[i]=PrecipiatingCount[i];
		
		length=StrConfig.HDMitigating.length;
		newHeadacheanaAnalysis.MitigatingCount=new int[length];
		for (i=0;i<length;i++)
			newHeadacheanaAnalysis.MitigatingCount[i]=MitigatingCount[i];
		
		newHeadacheanaAnalysis.DrugNameList=new ArrayList<String>();
		length=DrugNameList.size();
		for (i=0;i<length;i++){
			newHeadacheanaAnalysis.DrugNameList.add(DrugNameList.get(i));
		}
		
		newHeadacheanaAnalysis.DrugCountList=new ArrayList<Integer>();
		length=DrugCountList.size();
		for (i=0;i<length;i++){
			newHeadacheanaAnalysis.DrugCountList.add(DrugCountList.get(i));
		}
        return newHeadacheanaAnalysis;  
    } 

	public Calendar getStartAnalysisTime() {
		return StartAnalysisTime;
	}

	public void setStartAnalysisTime(Calendar startAnalysisTime) {
		StartAnalysisTime = startAnalysisTime;
	}

	public Calendar getEndAnalysisTime() {
		return EndAnalysisTime;
	}

	public void setEndAnalysisTime(Calendar endAnalysisTime) {
		EndAnalysisTime = endAnalysisTime;
	}

	public int getTotalDiary() {
		return TotalDiary;
	}

	public void increaseTotalDiary() {
		TotalDiary++;
	}

	public int getTotalMonth() {
		return TotalMonth;
	}

	public void setTotalMonth(int totalMonth) {
		TotalMonth = totalMonth;
	}

	public int getMinFrequencyPerMonth() {
		return MinFrequencyPerMonth;
	}
	public int getMaxFrequencyPerMonth() {
		return MaxFrequencyPerMonth;
	}

	public long getSumIntervalMinutes() {
		return SumIntervalMinutes;
	}

	public long getMinIntervalMinutes() {
		return MinIntervalMinutes;
	}

	public long getMaxIntervalMinutes() {
		return MaxIntervalMinutes;
	}

	
	public int[] getIntervalCount() {
		return IntervalCount;
	}

	public int getIfAroundEyeCount() {
		return IfAroundEyeCount;
	}

	public int[] getPositionCount() {
		return PositionCount;
	}

	
	public int[] getTypeCount() {
		return TypeCount;
	}

	
	public int getMinDegree() {
		return MinDegree;
	}

	public int getMaxDegree() {
		return MaxDegree;
	}

	public int getSumDegree() {
		return SumDegree;
	}

	
	public int[] getDegreeCount() {
		return DegreeCount;
	}

	public int getIfActivityAggravateCount() {
		return IfActivityAggravateCount;
	}

	public int[][] getProdromeCount() {
		return ProdromeCount;
	}
	
	public int[][] getCompanionCount() {
		return CompanionCount;
	}

	public int[] getPrecipiatingCount() {
		return PrecipiatingCount;
	}

	public int[] getMitigatingCount() {
		return MitigatingCount;
	}

	public ArrayList<String> getDrugNameList() {
		return DrugNameList;
	}

	public ArrayList<Integer> getDrugCountList() {
		return DrugCountList;
	}

	//-----------------方法---------------------//
	/**
	 * 要求传入的头痛日志按照时间顺序排好了进来分析，不然头痛频率那个每月最少 最多头痛次数会出错！ （当前是时间倒序）
	 * @param headacheDiary
	 */
	public void addSingleHeadacheDiaryToAnalysis(HeadacheDiary headacheDiary) { 
		// TODO Auto-generated method stub
		if (headacheDiary==null)
			return;
		increaseTotalDiary();
		String startTime=headacheDiary.getStartTime();
		String endTime=headacheDiary.getEndTime();
		Calendar start=TimeManager.parseStrDateTime(startTime);
		Calendar end=TimeManager.parseStrDateTime(endTime);
		
		//Frequency
		if(StartAnalysisTime==null || start.before(StartAnalysisTime))
			StartAnalysisTime=start;
		if(EndAnalysisTime==null || start.after(EndAnalysisTime))
			EndAnalysisTime=start;
		
		int nowMonthInterval=TimeManager.getMonthInterval(start, Calendar.getInstance());
		if(curMonthInterval==-1){
			curFrequencyPerMonth=1;
			curMonthInterval=nowMonthInterval;
		}
		else{
			if (Math.abs(curMonthInterval-nowMonthInterval)>1){
				MinFrequencyPerMonth=0;
				curFrequencyPerMonth=1;
				curMonthInterval=nowMonthInterval;
			}
			else{
				curFrequencyPerMonth++;
			}
				
		}
		
		if (MinFrequencyPerMonth ==-1 || curFrequencyPerMonth<MinFrequencyPerMonth)
			MinFrequencyPerMonth=curFrequencyPerMonth;
		if (curFrequencyPerMonth>MaxFrequencyPerMonth)
			MaxFrequencyPerMonth=curFrequencyPerMonth;
		
		//Duration
        long interval=end.getTimeInMillis()-start.getTimeInMillis();
        long minute=interval/60000;
        SumIntervalMinutes+=minute;
        if (MinIntervalMinutes==-1 || minute<MinIntervalMinutes)
        	MinIntervalMinutes=minute;
        if (minute>MaxIntervalMinutes)
        	MaxIntervalMinutes=minute;
        
        float hour=minute/(float)60;
        if (hour<0.5f){
        	IntervalCount[0]++;
        }
        else if (hour<=3)
        	IntervalCount[1]++;
        else if (hour<4)
        	IntervalCount[2]++;
        else if (hour<=72)
        	IntervalCount[3]++;
        else
        	IntervalCount[4]++;
        
      //Position
        if (headacheDiary.getIfAroundEye()>0)
        	IfAroundEyeCount++;
        if (headacheDiary.getPosition()>=0)
        	PositionCount[headacheDiary.getPosition()]++;
        
       //Type
        if(headacheDiary.getType()>=0){
        	TypeCount[headacheDiary.getType()]++;
        }
        
       //Degree
        int degree=headacheDiary.getDegree();
        SumDegree+=degree;
        if (degree<MinDegree||MinDegree==-1)
        	MinDegree=degree;
        if (degree>MaxDegree)
        	MaxDegree=degree;
        if (degree<=3)
        	DegreeCount[0]++;
        else if (degree<=6)
        	DegreeCount[1]++;
        else if (degree<=9)
        	DegreeCount[2]++;
        else 
        	DegreeCount[3]++;
        
        //Activity
        if (headacheDiary.getIfActivityAggravate()>0)
        	IfActivityAggravateCount++;
        
        //Prodrome
        for (int i=0;i<StrConfig.HDProdromeValue.length;i++){
        	int value=headacheDiary.getProdrome(i);
        	if (value>0){
        		ProdromeCount[i][value]++;
        		ProdromeCount[i][0]++;  //累计count
        	}
        }
        
        //Companion
        for (int i=0;i<StrConfig.HDCompanionValue.length;i++){
        	int value=headacheDiary.getCompanion(i);
        	if (value>0){
        		CompanionCount[i][value]++;
        		CompanionCount[i][0]++;  //累计count
        	}
        }
        
        //Precipiating
        for(int i=0; i<StrConfig.HDPrecipiating.length;i++){
        	if (headacheDiary.getPrecipiating(i)>0)
        		PrecipiatingCount[i]++;
        }
        
        //Mitigating
        for(int i=0; i<StrConfig.HDMitigating.length;i++){
        	if (headacheDiary.getMitigating(i)>0)
        		MitigatingCount[i]++;
        }
        
        //Drug
        for (Drug drug:headacheDiary.getDrugList()){
        	int posi=DrugNameList.indexOf(drug.getName());
        	if (posi>=0)
        		DrugCountList.set(posi, DrugCountList.get(posi)+1);
        	else{
        		DrugNameList.add(drug.getName());
        		DrugCountList.add(1);
        	}
        }
	}
	
}
