package com.example.headdiary.util;

import com.example.headdiary.data.HeadacheDiary;

public class Diagnosis {

	public Diagnosis() {
		// TODO Auto-generated constructor stub
	}

	public static int DiagnoseForSecondaryClassification(HeadacheDiary headacheDiary){
		Boolean flag[]={true,true,true}; //0偏头痛 1紧张型头痛 2 丛集性头痛
		int count[]={0,0};
		
		//PART 1
		long duration=headacheDiary.getDurationMin();
		if (duration<240 || duration>4320)
			flag[0]=false;
		if (duration<15 || duration>180)
			flag[2]=false;
		
		//PART 2
		if (headacheDiary.getPosition()==2){ //双侧头痛
			flag[2]=false;
			count[1]++;
		}
		else //左侧OR右侧
			count[0]++;
		
		if (headacheDiary.getType()==0)//搏动痛
			count[0]++;
		else if (headacheDiary.getType()==1)//闷胀痛
			count[1]++;
		
		int degree=headacheDiary.getDegree(); //1~10
		if (degree>=4 && degree<=9)
			count[0]++;
		if (degree<=6)
			count[1]++;
		
		if (headacheDiary.getIfActivityAggravate()==1)//日常活动加重头痛
			count[0]++;
		else 
			count[1]++;
		
		if (count[0]<2)
			flag[0]=false;
		if (count[1]<2)
			flag[1]=false;
		
		if (flag[2])
			if (headacheDiary.getIfAroundEye()==0 ||degree<7)//不在眼眶、太阳穴附近 ||程度未达到重度
				flag[2]=false;
		
		//PART 3
		int[] companion=headacheDiary.getCompanion();
		if (!(companion[0]==0 && companion[1]==0 && !(companion[2]!=0 && companion[3]!=0)))
			flag[1]=false;
		else 
			flag[0]=false;
		
		int i;
		if (flag[2]){
			for (i=4;i<10;i++){
				if (companion[i]==1)
					break;
			}
			if (i==10)
				flag[2]=false;
		}
		
		int res=-1;
		if (flag[0])
			res=0;
		else if (flag[1] && flag[2])
			res=3;
		else if (flag[1])
			res=1;
		else if (flag[2])
			res=2;
		else 
			res=4;
		
		return res;
		
	}

}
