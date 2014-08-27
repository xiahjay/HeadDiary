/**
 * 
 */
package com.example.headdiary.data;

import java.util.Locale;

/**
 * @author Jane Huang
 *	用于管理代码中的String, 便于选择语言的时候自动修改
 */
public class StrConfig {
	/**
	 * 一旦修改了语言选项，就要重载config中的string
	 */
	public StrConfig() {
		// TODO Auto-generated constructor stub
		initStrConfig();
	}
	
	public static String HDIntervalCategory[];
	public static String HDPosition[];
	public static String HDPositionAroundEye[];
	public static String HDPositionAroundEyeTrue;
	public static String HDAcheType[];
	public static String HDAcheDegree[];
	public static String HDAcheDegreeGrade[];
	public static String HDActivity[];
	public static String HDProdromeCategory[];
	public static String HDProdromeValue[][];
	public static String HDProdromeValueBrief[][];
	public static String HDCompanionCategory[];
	public static String HDCompanionValue[][];
	public static String HDCompanionValueBrief[][];
	
	public static String HDPrecipiating[];
	public static String HDMitigating[];
	public static String HDDrugEffect[];
	
	public static String HDSecondaryClassification[];
	
	public static String Language[];
	public static String AutoUpload[];
	public static String TimePeriod[];
	public static String DocumentStyle[];
	public static String AnalysisStyle[];
	//temp
	public static String analysisAnsArray[];
	public static String msgArray1[];
	public static String msgArray2[];
	public static String dateArray1[];
	public static String dateArray2[];
	
	public static void initStrConfig(){
		int lan=UserDAO.getInstance().getLanguage();
		
		if (lan==0){
			String tempLan=Locale.getDefault().getLanguage();
			if (tempLan.equals("en"))
				lan=2;
			else
				lan=1;
		}
		if (lan==1){//中文
			HDIntervalCategory=new String[]{"<30分钟","[30分钟~3小时]","(3小时~4小时)","[4小时~72小时]",">72小时"};
			HDPosition=new String[]{"左侧为主","右侧为主","双侧头痛"};
			HDPositionAroundEye=new String[]{"未出现","出现"};
			HDPositionAroundEyeTrue="眼眶或太阳穴附近";
			HDAcheType=new String[]{"搏动样痛（跳痛）","闷胀痛","针刺痛","过电痛","其他"};
			HDAcheDegreeGrade=new String[]{"轻度","轻度","轻度","轻度","中度","中度","中度","重度","重度","重度","极重度"};
			HDAcheDegree=new String[]{"轻度","中度","重度","极重度"};
			HDActivity=new String[]{"否","是"};
			HDProdromeCategory=new String[]{"点状、色斑、线型闪光幻觉","视觉缺损","针刺感","麻木感","语言障碍"};
			HDProdromeValue=new String[][]{
					{"无","单侧","双侧"},
					{"无","单侧","双侧"},
					{"无","单侧","双侧"},
					{"无","单侧","双侧"},
					{"无","有",""}};
			HDProdromeValueBrief=HDProdromeValue;
			HDCompanionCategory=new String[]{"恶心","呕吐","怕光","怕吵",
					"同侧结膜充血或流泪","同侧鼻充血或流涕","同侧眼睑水肿","前额或面部出汗","瞳孔缩小或上眼皮无法抬起","感觉躁动或不安"};
			HDCompanionValue=new String[][]{
					{"无","轻","中","重"},
					{"无","轻","中","重"},
					{"无","轻","中","重"},
					{"无","轻","中","重"},
					{"无","有","",""},
					{"无","有","",""},
					{"无","有","",""},
					{"无","有","",""},
					{"无","有","",""},
					{"无","有","",""}
				};
			HDCompanionValueBrief=HDCompanionValue;
			HDPrecipiating=new String[]{
					"睡眠","劳累","情绪波动","气候","特殊气味","食物","酒精","月经","性活动","其他"};
			HDMitigating=new String[]{
					"黑暗/安静环境","躺下","站立","快步走","锻炼","按摩","热敷","冷敷","其他"};
			HDDrugEffect=new String[]{"未知","无变化","缓解","消失"};
			
			HDSecondaryClassification=new String[]{"偏头痛","紧张型头痛","丛集性头痛","紧张型与丛集性头痛","其他头痛"};
			
			Language=new String[]{"系统默认","中文","English"};
			AutoUpload=new String[]{"手动","自动"};
			TimePeriod=new String[]{"一月内","三月内","近半年","全部"};
			DocumentStyle=new String[]{"列表","日历"};
			AnalysisStyle=new String[]{"统计表","趋势图"};
			
			
			//temp
			analysisAnsArray=new String[]{
					"平均：6小时42分；最少：4小时13分；最长：8小时42分\n4~72小时：100%",
					"左侧：67%；右侧：33%",
					"搏动痛：100%",
					"平均：6.2；最小：4；最大：7\n中度：87%；重度：13%",
					"加重：100%",	
					"无",
					"恶心：80%；呕吐：20%；畏光：100%；畏声：30%",
					"睡眠：80%；情绪：45%",
					"躺下：100%；按摩87%",
					"阿司匹林：3次（50%）",
				};
			msgArray1=new String[]{"你刚刚添加了一条头痛记录，已经上传给您的责任医生董钊！", "好的", "您的止痛药物使用过于频繁，容易引发药物过量型头痛，请向医生咨询"};
			dateArray1=new String[]{"2013-11-09 18:00", "2013-11-09 18:15", 
			"2013-11-11 15:30"};
			msgArray2=new String[]{"我已经看到了你的头痛日记", "恩，麻烦医生了！", "不要过度使用止痛药，如果下一次头痛还是持续这么久的话，我建议你使用预防性药物！平时注意休息，不要总是处于精神紧张的状态！","好的医生，我一定会注意的！！"};

    		dateArray2=new String[]{"2013-11-12 11:00", "2013-11-12 12:15", 
					"2013-11-12 15:30","2013-11-12 15:45"};
		}
		else if (lan==2){//English
			HDIntervalCategory=new String[]{"<30min","[30min,3h]","(3h,4h)","[4h,72h]",">72h"};
			HDPosition=new String[]{"Mainly Left","Mainly Right","Both Sides"};
			HDPositionAroundEye=new String[]{"No","Yes"};
			HDPositionAroundEyeTrue="Around Eyes or Temples";
			HDAcheType=new String[]{"Pulsating quality","Pressing or Tightening","Acupuncture Pain","Overcurrent Pain","Others"};
			HDAcheDegreeGrade=new String[]{"Gental","Gental","Gental","Gental","Moderate","Moderate","Moderate","Severe","Severe","Severe","Very Severe"};
			HDAcheDegree=new String[]{"Gental","Moderate","Severe","Very Severe"};
			HDActivity=new String[]{"Not aggravate","Agrravate"};
			HDProdromeCategory=new String[]{"Flikering lights, spots or lines","Loss of vision","Pins and needles","Numbness","Speech disturbance"};
			HDProdromeValue=new String[][]{
					{"No","Unilateral","Bilateral"},
					{"No","Unilateral","Bilateral"},
					{"No","Unilateral","Bilateral"},
					{"No","Unilateral","Bilateral"},
					{"No","Yes",""}};
			HDProdromeValueBrief=new String[][]{
					{"No","Uni","Bi"},
					{"No","Uni","Bi"},
					{"No","Uni","Bi"},
					{"No","Uni","Bi"},
					{"No","Yes",""}};
			HDCompanionCategory=new String[]{"Nausea","Vomit","Photophobia","Phonophobia",
					"Ipsilateral conjunctival injection and/or lacrimation","Ipsilateral nasal congestion and/or rhinorrhoea","Ipsilateral eyelid oedema","Ipsilateral eyelid oedema","Ipsilateral miosis and/or ptosis","A sense of restlessness or agitaion"};
			HDCompanionValue=new String[][]{
					{"No","Gental","Moderate","Severe"},
					{"No","Gental","Moderate","Severe"},
					{"No","Gental","Moderate","Severe"},
					{"No","Gental","Moderate","Severe"},
					{"No","Yes","",""},
					{"No","Yes","",""},
					{"No","Yes","",""},
					{"No","Yes","",""},
					{"No","Yes","",""},
					{"No","Yes","",""}
				};
			HDCompanionValueBrief=new String[][]{
					{"No","G","M","S"},
					{"No","G","M","S"},
					{"No","G","M","S"},
					{"No","G","M","S"},
					{"No","Yes","",""},
					{"No","Yes","",""},
					{"No","Yes","",""},
					{"No","Yes","",""},
					{"No","Yes","",""},
					{"No","Yes","",""}
				};
			HDPrecipiating=new String[]{
					"Sleeping","Fatigue","Emotion","Climate","Special odor","Food","Alcohol","Menstruation","Sexual activity","Others"};
			HDMitigating=new String[]{
					"Dark or Silent Atmosphere","Lying down","Standing up","Trot","Doing Exercise","Massage","Fomentation","Cold","Others"};
			HDDrugEffect=new String[]{"Unknow","No Change","Lighten","Disappear"};
			
			HDSecondaryClassification=new String[]{"Migraine","Tension-type headache","Cluster Headache","Tension-type and Cluster Headache","Other Headache"};
			
			Language=new String[]{"System Default","中文","English"};
			AutoUpload=new String[]{"Manual","Auto"};
			TimePeriod=new String[]{"本月","近三月","近半年","全部"};
			DocumentStyle=new String[]{"列表","日历"};
			AnalysisStyle=new String[]{"统计表","趋势图"};
			
			//temp
			analysisAnsArray=new String[]{
					"Average: 6h42m; Min: 4h13m; Max: 8h42m\n4~72h: 100%",
					"Mainly Left: 67%; Mainly Right: 33%",
					"Pulsating 100%",
					"Average: 6.2; Min: 4; Max: 7\nModerate: 87%; Severe: 13%",
					"Aggrate: 100%",	
					"None",
					"Nausea: 80%; Vomit: 20%; Photophobia: 100%; phonophobia: 30%",
					"Sleeping: 80%; Emotion: 45%",
					"Lying down: 100%; Massage: 87%",
					"Asprine: 3 (50%)",
				};
			msgArray1=new String[]{"You just complete a headache diary, we have uploaded it to your doctor Dr.Dong.", "Fine.", "You use too much Anodyne, it would aggravate your headache. Please consult your doctor Dr.Dong later."};
			dateArray1=new String[]{"2013-11-09 18:00", "2013-11-09 18:15", "2013-11-11 15:30"};
			msgArray2=new String[]{"I have read your diary.", "Thank you, Dr.Dong!", "Don't use too much Anodyne. If your headache lasts so much long next time, you need to use some preventative drugs. Keep away from nervous situations. Wish you all the best!","No problem! I would keep an eye on it. Thank you very much!"};
    		dateArray2=new String[]{"2013-11-12 11:00", "2013-11-12 12:15", "2013-11-12 15:30","2013-11-12 15:45"};
		}
	}
}
