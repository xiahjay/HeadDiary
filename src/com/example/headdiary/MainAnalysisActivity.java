package com.example.headdiary;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.w.song.widget.navigation.RollNavigationBar;
import cn.w.song.widget.navigation.adapter.RollNavigationBarAdapter;
import cn.w.song.widget.scroll.SlidePageView;
import cn.w.song.widget.scroll.SlidePageView.OnPageViewChangedListener;


import com.example.headdiary.data.HeadacheAnalysis;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.util.DocumentAdapter;
import com.example.headdiary.util.TimeManager;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAnalysisActivity extends Activity {
	private TextView tvTime,tvRecord,tvStyle;
	private TextView tvFrequency,tvDuration,tvPosition,tvType,tvDegree,tvActivity,tvProdrome,tvCompanion,tvPrecipiating,tvMitigating,tvDrug;
	private int[] textViewIdList={R.id.analysis_tv_frequency,R.id.analysis_tv_interval,R.id.analysis_tv_position,R.id.analysis_tv_ache_type,R.id.analysis_tv_ache_degree
			,R.id.analysis_tv_activity,R.id.analysis_tv_prodrome,R.id.analysis_tv_companion,R.id.analysis_tv_precipiating,R.id.analysis_tv_mitigating,R.id.analysis_tv_drug};
	//private String tag = "UIADemoActivity";
	private String[] title = { "�鿴��־ͳ��", "�鿴��չ����" };
	private int[] photo = { R.drawable.nav_menu_home, R.drawable.nav_menu_hot};
	private int[] photoSelected = { R.drawable.nav_menu_home_selected,
			R.drawable.nav_menu_hot_selected,};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uiademo_ui);
		/* ��ȡ��� */
		final RollNavigationBar rnb = (RollNavigationBar) findViewById(R.id.uiademo_ui_RollNavigationBar);
		final SlidePageView spv = (SlidePageView) findViewById(R.id.uiademo_ui_SlidePageView);
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		final MyNavigationBarAdapter adapter = new MyNavigationBarAdapter(this,list);
		/* ���ƶ�̬���� */
		
		for (int i = 0; i < title.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title[i]);
			map.put("photo", photo[i]);
			map.put("photoSelected", photoSelected[i]);
			list.add(map);
		}
		/* ���û������Ļ���ʱ�䣬ʱ�䷶Χ��0.1~1s�����ڷ�Χ��Ĭ��0.1s */
		rnb.setSelecterMoveContinueTime(0.1f);// ���Բ����ã�Ĭ��0.1s
		/* ���û�������ʽ��ͼƬ�� */
		rnb.setSelecterDrawableSource(R.drawable.nav_menu_bg);// ����
		/* ���õ������ı�ѡλ�� */
		rnb.setSelectedChildPosition(0);// ���Բ�����

		/* ��������չ */
		rnb.setAdapter(adapter);// ����
		rnb.setNavigationBarListener(new RollNavigationBar.NavigationBarListener() {
			/**
			 * position ��ѡλ�� view Ϊ������ event �ƶ��¼�
			 */
			@Override
			public void onNavigationBarClick(int position, View view,
					MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// ����ȥʱ
					spv.setCurrPagePosition(position);
					spv.CurrPageScrollToScreenCenter();
					break;
				case MotionEvent.ACTION_MOVE:// �ƶ���

					break;
				case MotionEvent.ACTION_UP:// ̧��ʱ

					break;
				}

			}

		});
		
		/* ����ҳ�棨���ģ����� */
		spv.setOnPageViewChangedListener(new OnPageViewChangedListener() {

			@Override
			public void OnPageViewChanged(int currPagePosition,
					View currPageView) {
				rnb.setSelectedChildPosition(currPagePosition);
				rnb.refreshView(adapter);
			}
		});
	
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
	
	//--------------Click��Ӧ�¼�-------------------//
	
	public void onClickBack(View v){
    	this.finish();
	}

	public void onClickPeriod(View v){
		new AlertDialog.Builder(this)
			.setTitle("ʱ����")
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
		.setTitle("��ʾ��ʽ")
		.setSingleChoiceItems(StrConfig.AnalysisStyle, UserDAO.getInstance().getDocumentStyle(),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (UserDAO.getInstance().getAnalysisStyle()!=which){
							UserDAO.getInstance().setAnalysisStyle(which);
						}
						dialog.dismiss();
					}
		}).show();
	}	
	
	//--------------����---------------------//
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
			tvRecord.setText("0����¼");
			return;
		}
		
		tvRecord.setText(Integer.toString(headacheAnalysis.getTotalDiary())+"����¼");
		int totalDiary=headacheAnalysis.getTotalDiary();
		
		//Frequency
		String frequency="ͳ�Ʒ�Χ��"+TimeManager.getStrDate(headacheAnalysis.getStartAnalysisTime())+" ~ "+TimeManager.getStrDate(headacheAnalysis.getEndAnalysisTime())+"\n";
		float averageFrequencyPerMonth=totalDiary/(float)headacheAnalysis.getTotalMonth();
		String strAverageFrequency=new DecimalFormat("###,###,###.#").format(averageFrequencyPerMonth);
		frequency+="ƽ����"+ strAverageFrequency+"��/�£����٣�"+Integer.toString(headacheAnalysis.getMinFrequencyPerMonth())+"��/�£���ࣺ"+Integer.toString(headacheAnalysis.getMaxFrequencyPerMonth())+"��/��";
		tvFrequency.setText(frequency);
		
		//Duration
		String duration;
		long averageDuration=headacheAnalysis.getSumIntervalMinutes()/totalDiary;
		duration="ƽ����"+TimeManager.getStrDayHourMin(averageDuration)+"����̣�"+TimeManager.getStrDayHourMin(headacheAnalysis.getMinIntervalMinutes())+"�����"+TimeManager.getStrDayHourMin(headacheAnalysis.getMaxIntervalMinutes());

		strTemp=getStrPercentByCategoryCountList(headacheAnalysis.getIntervalCount(), StrConfig.HDIntervalCategory, totalDiary);
		
		if (!strTemp.equals(""))
			duration+="\n"+strTemp.substring(0, strTemp.length()-1);
		tvDuration.setText(duration);
		
		//Position
		String position=getStrPercentByCategoryCountList(headacheAnalysis.getPositionCount(), StrConfig.HDPosition, totalDiary);

		intTemp=headacheAnalysis.getIfAroundEyeCount()*100/totalDiary;
		if (intTemp>0)
			position+="\n"+StrConfig.HDPositionAroundEyeTrue+"��"+Integer.toString(intTemp)+"%��";
		tvPosition.setText(position);
		
		//Type
		String type=getStrPercentByCategoryCountList(headacheAnalysis.getTypeCount(), StrConfig.HDAcheType, totalDiary);
		tvType.setText(type);
		
		//Degree
		float averageDegree=headacheAnalysis.getSumDegree()/(float)totalDiary;
		strTemp=new DecimalFormat("###,###,###.#").format(averageDegree);
		String degree="ƽ����"+strTemp+"�����᣺"+Integer.toString(headacheAnalysis.getMinDegree())+"�����أ�"+Integer.toString(headacheAnalysis.getMaxDegree());
		degree+="\n"+getStrPercentByCategoryCountList(headacheAnalysis.getDegreeCount(), StrConfig.HDAcheDegree, totalDiary);
		tvDegree.setText(degree);
		
		//Activity
		String activity=StrConfig.HDActivity[1]+"��"+Integer.toString(headacheAnalysis.getIfActivityAggravateCount()*100/totalDiary)+"%";
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
			drug+=headacheAnalysis.getDrugNameList().get(i)+"��"+Integer.toString(count)+"�� ( "+count*100/totalDiary+"% )\n";
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
				result+=categoryNameList[i]+"��"+Integer.toString(countValue*100/totalNum)+"%��";
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
				result+=categoryNameList[i]+"��"+Integer.toString(countValue*100/totalNum)+"% ";
				if (categoryValueList[i][1].equals(this.getResources().getString(R.string.prompt_therebe))){
					result+="\n";
					continue;
				}
				result+=" ( ";
				for (int j=1;j<countList[0].length;j++){
					countValue2=countList[i][j];
					if (countValue2>0){
						result+=categoryValueList[i][j]+" "+Integer.toString(countValue2*100/totalNum)+"%��";
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

	/**
	 * ��������չ
	 * 
	 * @author w.song
	 * @version 1.0.1
	 * @date 2012-4-22
	 */
	class MyNavigationBarAdapter extends RollNavigationBarAdapter {
		private List<Map<String, Object>> list;
		private LayoutInflater mInflater;

		public MyNavigationBarAdapter(Activity activity,
				List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(activity);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * ��ȡÿ�����
		 * 
		 * @param position
		 *            �����λ��
		 * @param contextView
		 *            ���
		 * @param parent
		 *            �ϲ����
		 */
		@Override
		public View getView(int position, View contextView, ViewGroup parent) {
			mInflater.inflate(R.layout.item, (ViewGroup) contextView);
			RollNavigationBar rollNavigationBar = (RollNavigationBar) parent;
			/* ��ȡ��� */
			ImageView imageView = (ImageView) contextView
					.findViewById(R.id.image_view);
			TextView titleView = (TextView) contextView
					.findViewById(R.id.title_view);

			/* ��ȡ���� */
			String title = "" + list.get(position).get("title");
			int photo = (Integer) list.get(position).get("photo");
			int photoSelected = (Integer) list.get(position).get(
					"photoSelected");

			/* ������ò��� */
			// ����ѡ���뱻ѡ��ͼƬ
			if (position == rollNavigationBar.getSelectedChildPosition()) {// ��ѡ��
				imageView.setBackgroundResource(photoSelected);
			} else {// û��ѡ��
				imageView.setBackgroundResource(photo);
				titleView.setTextColor(Color.argb(0, 0xff, 0, 0));
			}						
			titleView.setText(title);

			return contextView;
		}

	}
}
