/**
 * 
 */
package com.example.headdiary.data;

import java.util.Locale;

/**
 * @author Jane Huang
 *	���ڹ�������е�String, ����ѡ�����Ե�ʱ���Զ��޸�
 */
public class StrConfig {
	/**
	 * һ���޸�������ѡ���Ҫ����config�е�string
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
		if (lan==1){//����
			HDIntervalCategory=new String[]{"<30����","[30����~3Сʱ]","(3Сʱ~4Сʱ)","[4Сʱ~72Сʱ]",">72Сʱ"};
			HDPosition=new String[]{"���Ϊ��","�Ҳ�Ϊ��","˫��ͷʹ"};
			HDPositionAroundEye=new String[]{"δ����","����"};
			HDPositionAroundEyeTrue="�ۿ���̫��Ѩ����";
			HDAcheType=new String[]{"������ʹ����ʹ��","����ʹ","���ʹ","����ʹ","����"};
			HDAcheDegreeGrade=new String[]{"���","���","���","���","�ж�","�ж�","�ж�","�ض�","�ض�","�ض�","���ض�"};
			HDAcheDegree=new String[]{"���","�ж�","�ض�","���ض�"};
			HDActivity=new String[]{"��","��"};
			HDProdromeCategory=new String[]{"��״��ɫ�ߡ���������þ�","�Ӿ�ȱ��","��̸�","��ľ��","�����ϰ�"};
			HDProdromeValue=new String[][]{
					{"��","����","˫��"},
					{"��","����","˫��"},
					{"��","����","˫��"},
					{"��","����","˫��"},
					{"��","��",""}};
			HDProdromeValueBrief=HDProdromeValue;
			HDCompanionCategory=new String[]{"����","Ż��","�¹�","�³�",
					"ͬ���Ĥ��Ѫ������","ͬ��ǳ�Ѫ������","ͬ������ˮ��","ǰ����沿����","ͫ����С������Ƥ�޷�̧��","�о��궯�򲻰�"};
			HDCompanionValue=new String[][]{
					{"��","��","��","��"},
					{"��","��","��","��"},
					{"��","��","��","��"},
					{"��","��","��","��"},
					{"��","��","",""},
					{"��","��","",""},
					{"��","��","",""},
					{"��","��","",""},
					{"��","��","",""},
					{"��","��","",""}
				};
			HDCompanionValueBrief=HDCompanionValue;
			HDPrecipiating=new String[]{
					"˯��","����","��������","����","������ζ","ʳ��","�ƾ�","�¾�","�Ի","����"};
			HDMitigating=new String[]{
					"�ڰ�/��������","����","վ��","�첽��","����","��Ħ","�ȷ�","���","����"};
			HDDrugEffect=new String[]{"δ֪","�ޱ仯","����","��ʧ"};
			
			HDSecondaryClassification=new String[]{"ƫͷʹ","������ͷʹ","�Լ���ͷʹ","��������Լ���ͷʹ","����ͷʹ"};
			
			Language=new String[]{"ϵͳĬ��","����","English"};
			AutoUpload=new String[]{"�ֶ�","�Զ�"};
			TimePeriod=new String[]{"һ����","������","������","ȫ��"};
			DocumentStyle=new String[]{"�б�","����"};
			AnalysisStyle=new String[]{"ͳ�Ʊ�","����ͼ"};
			
			
			//temp
			analysisAnsArray=new String[]{
					"ƽ����6Сʱ42�֣����٣�4Сʱ13�֣����8Сʱ42��\n4~72Сʱ��100%",
					"��ࣺ67%���Ҳࣺ33%",
					"����ʹ��100%",
					"ƽ����6.2����С��4�����7\n�жȣ�87%���ضȣ�13%",
					"���أ�100%",	
					"��",
					"���ģ�80%��Ż�£�20%��η�⣺100%��η����30%",
					"˯�ߣ�80%��������45%",
					"���£�100%����Ħ87%",
					"��˾ƥ�֣�3�Σ�50%��",
				};
			msgArray1=new String[]{"��ո������һ��ͷʹ��¼���Ѿ��ϴ�����������ҽ�����ȣ�", "�õ�", "����ֹʹҩ��ʹ�ù���Ƶ������������ҩ�������ͷʹ������ҽ����ѯ"};
			dateArray1=new String[]{"2013-11-09 18:00", "2013-11-09 18:15", 
			"2013-11-11 15:30"};
			msgArray2=new String[]{"���Ѿ����������ͷʹ�ռ�", "�����鷳ҽ���ˣ�", "��Ҫ����ʹ��ֹʹҩ�������һ��ͷʹ���ǳ�����ô�õĻ����ҽ�����ʹ��Ԥ����ҩ�ƽʱע����Ϣ����Ҫ���Ǵ��ھ�����ŵ�״̬��","�õ�ҽ������һ����ע��ģ���"};

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
			
			Language=new String[]{"System Default","����","English"};
			AutoUpload=new String[]{"Manual","Auto"};
			TimePeriod=new String[]{"����","������","������","ȫ��"};
			DocumentStyle=new String[]{"�б�","����"};
			AnalysisStyle=new String[]{"ͳ�Ʊ�","����ͼ"};
			
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
