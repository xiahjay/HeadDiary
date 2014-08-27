package com.example.headdiary.data;

import java.util.ArrayList;

import com.example.headdiary.HeadDiaryFormActivity;
import com.example.headdiary.util.Diagnosis;
import com.example.headdiary.util.TimeManager;

/**
 * POJO
 * @author Jane Huang
 *
 */
public class HeadacheDiary implements Cloneable{
	int RecId;//RecId=0 表示这是尚不完整的头痛日志，
	String RecordTime,StartTime,EndTime;
	int Position,IfAroundEye,Type,Degree,IfActivityAggravate,AidDiagnosis;
	String PositionComment,TypeComment,PrecipiatingComment,MitigatingComment;
	int[] Prodrome,Companion,Precipiating,Mitigating;
	ArrayList<Drug> DrugList;
	
	public HeadacheDiary(){
		clearDiary();
	}
	
	@Override  
    public Object clone() {  
        HeadacheDiary newHeadacheDiary = null;  
        try{  
            newHeadacheDiary = (HeadacheDiary)super.clone();   //浅复制  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        
        //深度复制 
        int length;
		length=StrConfig.HDProdromeCategory.length;
		newHeadacheDiary.Prodrome=new int[length];
		for (int i=0;i<length;i++)
			newHeadacheDiary.Prodrome[i]=Prodrome[i];
		
		length=StrConfig.HDCompanionCategory.length;
		newHeadacheDiary.Companion=new int[length];
		for (int i=0;i<length;i++)
			newHeadacheDiary.Companion[i]=Companion[i];
		
		length=StrConfig.HDPrecipiating.length;
		newHeadacheDiary.Precipiating=new int[length];
		for (int i=0;i<length;i++)
			newHeadacheDiary.Precipiating[i]=Precipiating[i];
		
		length=StrConfig.HDMitigating.length;
		newHeadacheDiary.Mitigating=new int[length];
		for (int i=0;i<length;i++)
			newHeadacheDiary.Mitigating[i]=Mitigating[i];
		
		newHeadacheDiary.DrugList=new ArrayList<Drug>();
        Drug newDrug;
        for (Drug drug:DrugList){
        	newDrug=new Drug();
        	newDrug= (Drug) drug.clone(); 
        	newHeadacheDiary.DrugList.add(newDrug);
        }
       
        return newHeadacheDiary;  
    }  
	
	
	public HeadacheDiary(int recId, String recordTime, String startTime,
			String endTime, int position, int ifAroundEye, int type,
			int degree, int ifActivityAggravate, int aidDiagnosis,
			String positionComment, String typeComment,
			String precipiatingComment, String mitigatingComment,
			int[] prodrome, int[] companion, int[] precipiating,
			int[] mitigating, ArrayList<Drug> drugList) {
		super();
		RecId = recId;
		RecordTime = recordTime;
		StartTime = startTime;
		EndTime = endTime;
		Position = position;
		IfAroundEye = ifAroundEye;
		Type = type;
		Degree = degree;
		IfActivityAggravate = ifActivityAggravate;
		AidDiagnosis = aidDiagnosis;
		PositionComment = positionComment;
		TypeComment = typeComment;
		PrecipiatingComment = precipiatingComment;
		MitigatingComment = mitigatingComment;
		Prodrome = prodrome;
		Companion = companion;
		Precipiating = precipiating;
		Mitigating = mitigating;
		DrugList = drugList;
	}

	private void clearDiary() {
		// TODO Auto-generated method stub
		RecId=0;
		Position=IfAroundEye=Type=Degree=IfActivityAggravate=AidDiagnosis=-1;
		
		int length;
		length=StrConfig.HDProdromeCategory.length;
		Prodrome=new int[length];
		for (int i=0;i<length;i++)
			Prodrome[i]=-1;
		
		length=StrConfig.HDCompanionCategory.length;
		Companion=new int[length];
		for (int i=0;i<length;i++)
			Companion[i]=-1;
		
		length=StrConfig.HDPrecipiating.length;
		Precipiating=new int[length];
		for (int i=0;i<length;i++)
			Precipiating[i]=0;
		
		length=StrConfig.HDMitigating.length;
		Mitigating=new int[length];
		for (int i=0;i<length;i++)
			Mitigating[i]=0;
		
		DrugList=new ArrayList<Drug>();
	}

	public int getRecId() {
		return RecId;
	}

	public void setRecId(int recId) {
		RecId = recId;
	}

	public String getRecordTime() {
		return RecordTime;
	}

	public void setRecordTime(String recordTime) {
		if (RecordTime != recordTime){
			RecordTime = recordTime;
			diaryChanged();
		}
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		if (StartTime!=startTime){
			StartTime = startTime;
			diaryChanged();
		}
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		if (EndTime!=endTime){
			EndTime = endTime;
			diaryChanged();
		}
		
	}

	public int getPosition() {
		return Position;
	}

	public void setPosition(int position) {
		if (Position!=position){
			Position = position;
			diaryChanged();
		}
		
	}

	public int getIfAroundEye() {
		return IfAroundEye;
	}

	public void setIfAroundEye(int ifAroundEye) {
		if (IfAroundEye!=ifAroundEye){
			IfAroundEye = ifAroundEye;
			diaryChanged();
		}
		
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		if (Type!=type){
			Type = type;
			diaryChanged();
		}
		
	}

	public int getDegree() {
		return Degree;
	}

	public void setDegree(int degree) {
		if (Degree!=degree){
			Degree = degree;
			diaryChanged();
		}
		
	}

	public int getIfActivityAggravate() {
		return IfActivityAggravate;
	}

	public void setIfActivityAggravate(int ifActivityAggravate) {
		if (IfActivityAggravate!=ifActivityAggravate){
			IfActivityAggravate = ifActivityAggravate;
			diaryChanged();
		}
		
	}

	public String getPositionComment() {
		return PositionComment;
	}

	public void setPositionComment(String positionComment) {
		if (PositionComment!=positionComment){
			PositionComment = positionComment;
			diaryChanged();
		}
		
	}

	public String getTypeComment() {
		return TypeComment;
	}

	public void setTypeComment(String typeComment) {
		if (TypeComment!=typeComment){
			TypeComment = typeComment;
			diaryChanged();
		}
		
	}

	public String getPrecipiatingComment() {
		return PrecipiatingComment;
	}

	public void setPrecipiatingComment(String precipiatingComment) {
		if (PrecipiatingComment!=precipiatingComment){
			PrecipiatingComment = precipiatingComment;
			diaryChanged();
		}
		
	}

	public String getMitigatingComment() {
		return MitigatingComment;
	}

	public void setMitigatingComment(String mitigatingComment) {
		if (MitigatingComment!=mitigatingComment){
			MitigatingComment = mitigatingComment;
			diaryChanged();
		}
		
	}

	
	public int getProdrome(int posi) {
		return Prodrome[posi];
		
	}

	public void setProdrome(int posi,int prodrome) {
		if (Prodrome[posi] != prodrome){
			Prodrome[posi] = prodrome;
			diaryChanged();
		}
		
	}
	
	public int getCompanion(int posi) {
		return Companion[posi];
	}
	
	public void setCompanion(int posi,int companion) {
		if (Companion[posi]!=companion){
			Companion[posi] = companion;
			diaryChanged();
		}
		
	}

	public int getPrecipiating(int posi) {
		return Precipiating[posi];
	}

	public void setPrecipiating(int posi,int precipiating) {
		if (Precipiating[posi] != precipiating){
			Precipiating[posi] = precipiating;
			diaryChanged();
		}
		
	}

	public int getMitigating(int posi) {
		return Mitigating[posi];
	}

	public void setMitigating(int posi,int mitigating) {
		if (Mitigating[posi]!=mitigating){
			Mitigating[posi] = mitigating;
			diaryChanged();
		}
		
	}

	public Drug getDrugInList(int posi) {
		if (posi>=0 && posi<DrugList.size())
			return DrugList.get(posi);
		return null;
	}

	public void setDrugInList(int posi,Drug drug) {
		if (posi>=0 && posi<DrugList.size() && DrugList.get(posi)!=drug){
			DrugList.set(posi, drug);
			diaryChanged();
		}
	}
	
	public void removeDrugInList(int posi) {
		if (posi>=0 && posi<DrugList.size())
			DrugList.remove(posi);
		diaryChanged();
	}
	
	public void addDrugToList(Drug drug) {
		DrugList.add(drug);
		diaryChanged();
	}
	
	public int getDrugListSize() {
		return DrugList.size();
	}
	
	public int[] getProdrome() {
		return Prodrome;
	}

	public int[] getCompanion() {
		return Companion;
	}

	public int[] getPrecipiating() {
		return Precipiating;
	}

	public int[] getMitigating() {
		return Mitigating;
	}

	public ArrayList<Drug> getDrugList() {
		return DrugList;
	}

	public int getAidDiagnosis() {
		return AidDiagnosis;
		
	}
	
	public String getStrAidDiagnosis() {
		String result="";
		if(AidDiagnosis>=0)
			result="头痛分类为："+StrConfig.HDSecondaryClassification[AidDiagnosis];
		return result;
		
	}
	
	//-----------------方法-----------------------------//
	private void diaryChanged() {
		// TODO Auto-generated method stub
		HeadacheDiaryDAO.getInstance().setIfSelectedDiaryChanged(true);
	}

	public long getDurationMin() {
		long mDurationMin=(TimeManager.parseStrDateTime(EndTime).getTimeInMillis()-TimeManager.parseStrDateTime(StartTime).getTimeInMillis())/60000;
		return mDurationMin;
	}
	
	public Boolean getIfComplete() {
		// TODO Auto-generated method stub
		Boolean ifComplete;
		ifComplete=StartTime!=null && EndTime!=null && Position!=-1 && IfAroundEye!=-1 && Type!=-1 && Degree!=-1 && IfActivityAggravate!=-1;
		
		if (ifComplete){
			for (int each:Prodrome){
				if(each==-1){
					ifComplete=false;
					break;
				}
			}
		}
		
		if (ifComplete){
			for (int each:Companion){
				if(each==-1){
					ifComplete=false;
					break;
				}
			}
		}
		
		return ifComplete;
	}

	public void makeAidDiagnosis() {
		// TODO Auto-generated method stub
		AidDiagnosis=Diagnosis.DiagnoseForSecondaryClassification(this);
	}
	
}
