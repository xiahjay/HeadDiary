package com.example.headdiary;



import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


import com.example.headdiary.data.Graphic;
import com.example.headdiary.data.HeadacheAnalysis;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.hddialog.AchePositionQuestion;
import com.example.headdiary.hddialog.SelectMonthDialog;
import com.example.headdiary.hddialog.StartTimeQuestion;
import com.example.headdiary.util.TimeManager;



import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class GraphicActivity extends Activity {
	 private Timer timer = new Timer();  
	    private TimerTask task;  
	    private Handler handler;  
	    private String title = "Signal Strength";  
	    private XYSeries series1;  
	    private XYMultipleSeriesDataset mDataset1;  
	    private GraphicalView chart1; 
	    private XYSeries series2;  
	    private XYMultipleSeriesDataset mDataset2;  
	    private GraphicalView chart2; 
	    private XYSeries series3;  
	    private XYMultipleSeriesDataset mDataset3;  
	    private GraphicalView chart3; 
	    private XYSeries series4;  
	    private XYMultipleSeriesDataset mDataset4;  
	    private GraphicalView chart4; 
	    private XYMultipleSeriesRenderer renderer1;  
	    private XYMultipleSeriesRenderer renderer2; 
	    private XYMultipleSeriesRenderer renderer3; 
	    private XYMultipleSeriesRenderer renderer4; 
	    private Context context1; 
	    private Context context2; 
	    private Context context3;
	    private Context context4;
	    private TextView tvStyle;
	    private TextView textTitle;
	    private TextView text2;
	    private TextView text3;
	    private TextView text4;
	    private Dialog dialog;
		private RadioGroup radiogroup;
		private TextView tvRecord;
		private String month;
			
		
		//ArrayList<String> dayList= new  ArrayList<String>();
		//ArrayList<Integer> degreeList= new  ArrayList<Integer>();
		
	   // private TextView tvMonth; 
	   
	     
	      
	   
	
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);
        init();
        series1 = new XYSeries(title); 
        mDataset1 = new XYMultipleSeriesDataset();
        series2 = new XYSeries(title); 
        mDataset2 = new XYMultipleSeriesDataset();
        series3 = new XYSeries(title); 
        mDataset3 = new XYMultipleSeriesDataset();
        series4 = new XYSeries(title); 
        mDataset4 = new XYMultipleSeriesDataset();
        tvStyle=(TextView)findViewById(R.id.graphic_tv_style);
		tvStyle.setText(StrConfig.AnalysisStyle[1]);
		tvRecord=(TextView)findViewById(R.id.graphic_tv_record);
		textTitle=(TextView)findViewById(R.id.text_title);
		text2=(TextView)findViewById(R.id.text2);
		text3=(TextView)findViewById(R.id.text3);
		text4=(TextView)findViewById(R.id.text4);
		
		
		//tvMonth=(TextView)findViewById(R.id.aa);
		//tvMonth.setText(StrConfig.MonthStyle[7]);
        
        
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	Log.d("onResume", "onResume Method is executed");
    	
    	  month=UserDAO.getInstance().getSelectMonth();
    	  month=month.substring(0,7);
    	
    	
    	 
    	 tvRecord.setText(month);
    	 HeadacheAnalysis[] headacheAnalysisList=HeadacheDiaryDAO.getInstance().getHeadacheAnalysisList();
    	 HeadacheAnalysis headacheAnalysis = headacheAnalysisList[3];
    	 String frequency=TimeManager.getStrDate(headacheAnalysis.getStartAnalysisTime())+" ~ "+TimeManager.getStrDate(headacheAnalysis.getEndAnalysisTime());    	 
         textTitle.setText(month);
    	 text2.setText(month);
    	 text3.setText(month.substring(0, 4)+"��1��~12��");
    	 text4.setText(month.substring(0, 4)+"��1��~12��");
    	 initGraph1();
    	 initGraph2();
    	 initGraph3();
    	 initGraph4();
    }
 
    private void init(){
    	final LinearLayout ll1 = (LinearLayout) findViewById(R.id.linechart1);
    	final LinearLayout ll11 = (LinearLayout) findViewById(R.id.linechart11);
    	final ImageView arrow1 = (ImageView) findViewById(R.id.arrow1);
		LinearLayout tl1 = (LinearLayout) findViewById(R.id.templl);
		

		tl1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ll1.getVisibility() == 0) {
					ll1.setVisibility(View.GONE);
					ll11.setVisibility(View.GONE);
					arrow1.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow1));
					
				} else {
					ll1.setVisibility(View.VISIBLE);
					ll11.setVisibility(View.VISIBLE);
					arrow1.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow2));// 设置可见
					
				}

			}
		});
		final LinearLayout ll2 = (LinearLayout) findViewById(R.id.linechart2);
		final LinearLayout ll21 = (LinearLayout) findViewById(R.id.linechart21);
		LinearLayout tl2 = (LinearLayout) findViewById(R.id.windll);
		final ImageView arrow2 = (ImageView) findViewById(R.id.arrow2);

		

		tl2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ll2.getVisibility() == 0) {
					ll2.setVisibility(View.GONE);
					ll21.setVisibility(View.GONE);
					arrow2.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow1));
					
				} else {
					ll2.setVisibility(View.VISIBLE);
					ll21.setVisibility(View.VISIBLE);
					arrow2.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow2));// 设置可见
					
				}

			}
		});
		final LinearLayout ll3 = (LinearLayout) findViewById(R.id.linechart3);
		LinearLayout tl3 = (LinearLayout) findViewById(R.id.rainll);
		final ImageView arrow3 = (ImageView) findViewById(R.id.arrow3);
		

		tl3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ll3.getVisibility() == 0) {
					ll3.setVisibility(View.GONE);
					arrow3.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow1));
					
				} else {
					ll3.setVisibility(View.VISIBLE);
					arrow3.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow2));// 设置可见
					
				}

			}
		});
		final LinearLayout ll4 = (LinearLayout) findViewById(R.id.linechart4);
		LinearLayout tl4 = (LinearLayout) findViewById(R.id.wetll);
		final ImageView arrow4 = (ImageView) findViewById(R.id.arrow4);
		
		// final PinChart geomark4 = (PinChart)
		// findViewById(R.id.geomark_view4);

		tl4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// float[] init = {};
				if (ll4.getVisibility() == 0) {
					ll4.setVisibility(View.GONE);
					arrow4.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow1));
					
				} 
				else {
					ll4.setVisibility(View.VISIBLE);
					arrow4.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow2));// 设置可见
					
				}

			}
		});
    }

    private void initGraph1() {
		// TODO Auto-generated method stub
    	context1 = getApplicationContext();  
        
       LinearLayout layout = (LinearLayout)findViewById(R.id.linechart1);  
        //series = new XYSeries(title); 
        mDataset1.removeSeries(series1);
        series1.clear();
        
       int xSeries = getXSeriesRange();
        
        
        ArrayList<Graphic> getDegree = getGraphicByDay();
       
        int[] monthDegree=new int[xSeries];
        for (int i=0;i<getDegree.size();i++){
        	Graphic tDegree= getDegree.get(i);
        	
        	monthDegree[Integer.parseInt(tDegree.getDate())]=tDegree.getDegree();
        	Log.i("day"+i, "day="+Integer.parseInt(tDegree.getDate()));
        }
        for (int i=0;i<monthDegree.length;i++){
        	if(i!=0)
        	{series1.add(i, monthDegree[i]); }       	
        }
        	              
          
        //���㼯��ӵ�������ݼ���  
        mDataset1.addSeries(series1);  
        Log.d("addSeries", "addSeries Method is executed");
          
        //���¶������ߵ���ʽ�����Եȵȵ����ã�renderer�൱��һ��������ͼ������Ⱦ�ľ��  
        int color = Color.BLUE;  
        PointStyle style = PointStyle.DIAMOND;  
        renderer1 = buildRenderer(color, style, true);  
        
        int xRange=31;
        if(xSeries==29){
        	xRange=29;
        }
        
        
        //���ú�ͼ�����ʽ  
        setChartSettings(renderer1, "X", "Y", 1,xRange, 0, 10, Color.BLACK, Color.BLACK);  
          
        //����ͼ��  
        chart1 = ChartFactory.getLineChartView(context1, mDataset1, renderer1);  
          
        //�Ƚ�ԭ���Ĳ���ȥ�����ټ����µĲ���
        layout.removeAllViewsInLayout();
        layout.addView(chart1, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
          }   

    private void initGraph2() {
		// TODO Auto-generated method stub
    	context2 = getApplicationContext();  
        
       LinearLayout layout = (LinearLayout)findViewById(R.id.linechart2);  
        //series = new XYSeries(title); 
        mDataset2.removeSeries(series2);
        series2.clear();
        
        int xSeries = getXSeriesRange();
        
        
        ArrayList<Graphic> getDuration = getGraphicByDay();
       
        int[] monthDuration=new int[xSeries];
        for (int i=0;i<getDuration.size();i++){
        	Graphic tDegree= getDuration.get(i);
        	
        	monthDuration[Integer.parseInt(tDegree.getDate())]=(int) tDegree.getInterval();
        	Log.i("day"+i, "day="+Integer.parseInt(tDegree.getDate()));
        }
        for (int i=0;i<monthDuration.length;i++){
        	if(i!=0){
        	series2.add(i, monthDuration[i]);  }
        	
        }
        	 
        int max = monthDuration[0];

       for (int i : monthDuration) {

       max = max>i?max:i;
       
     
      }
       
          
        //���㼯��ӵ�������ݼ���  
        mDataset2.addSeries(series2);  
        Log.d("addSeries", "addSeries Method is executed");
          
        //���¶������ߵ���ʽ�����Եȵȵ����ã�renderer�൱��һ��������ͼ������Ⱦ�ľ��  
        int color = Color.BLUE;  
        PointStyle style = PointStyle.DIAMOND;  
        renderer2 = buildRenderer(color, style, true);  
        //��һ���򵥵����ã���ֹ����ʱ���������32������
        int xRange=31;
        if(xSeries==29){
        	xRange=29;
        }
          
        //���ú�ͼ�����ʽ  
        setChartSettings(renderer2, "X", "Y", 1, xRange, 0, max*1.2, Color.BLACK, Color.BLACK);  
          
        //����ͼ��  
        chart2 = ChartFactory.getLineChartView(context2, mDataset2, renderer2);  
          
        //�Ƚ�ԭ���Ĳ���ȥ�����ټ����µĲ���
        layout.removeAllViewsInLayout();
        layout.addView(chart2, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
          }   
    
    private void initGraph3() {
		// TODO Auto-generated method stub
    	context3 = getApplicationContext();  
        
       LinearLayout layout = (LinearLayout)findViewById(R.id.linechart3);  
        //series = new XYSeries(title); 
        mDataset3.removeSeries(series3);
        series3.clear();
              
        
        int[] monthDegree=getMonthFrequency();
      
        for (int i=0;i<monthDegree.length;i++){
        	series3.add(i, monthDegree[i]);        	
        }
        	              
        int max = monthDegree[0];

        for (int i :monthDegree) {

        max = max>i?max:i;

      
       } 
        
        int ymax;
        if(max<=10){
        	 ymax=10;
        }
        if(max>=10 && max<=20){
        	 ymax=20;
        }else{
        	 ymax=max;
        }
        //���㼯��ӵ�������ݼ���  
        mDataset3.addSeries(series3);  
        Log.d("addSeries", "addSeries Method is executed");
          
        //���¶������ߵ���ʽ�����Եȵȵ����ã�renderer�൱��һ��������ͼ������Ⱦ�ľ��  
        BarChart.Type type = BarChart.Type.DEFAULT;
        int color =Color.rgb(0, 128, 0);
        PointStyle style = PointStyle.DIAMOND;  
        renderer3 = buildRenderer(color, style, true);  
          
        //���ú�ͼ�����ʽ  
        setChartSettings(renderer3, "X", "Y", 1, 12, 0,10, Color.BLACK, Color.BLACK);  
          
        //����ͼ��  
        chart3 = ChartFactory.getBarChartView(context3, mDataset3, renderer3,type);  
          
        //�Ƚ�ԭ���Ĳ���ȥ�����ټ����µĲ���
        layout.removeAllViewsInLayout();
        layout.addView(chart3, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
          }   
    
    private void initGraph4() {
		// TODO Auto-generated method stub
    	context4 = getApplicationContext();  
        
       LinearLayout layout = (LinearLayout)findViewById(R.id.linechart4);  
        //series = new XYSeries(title); 
        mDataset4.removeSeries(series4);
        series4.clear();
              
        
        int[] drugFrequency=getDrugFrequency();
      
        for (int i=0;i<drugFrequency.length;i++){
        	series4.add(i, drugFrequency[i]);        	
        }
        	              
        int max = drugFrequency[0];

        for (int i :drugFrequency) {

        max = max>i?max:i;

      
       }  
        int ymax;
        if(max<10){
        	 ymax=10;
        }
        if(max>=10 && max<20){
        	 ymax=20;
        }else{
        	 ymax=max;
        }
          
        //���㼯��ӵ�������ݼ���  
        mDataset4.addSeries(series4);  
        Log.d("addSeries", "addSeries Method is executed");
          
        //���¶������ߵ���ʽ�����Եȵȵ����ã�renderer�൱��һ��������ͼ������Ⱦ�ľ��  
        BarChart.Type type = BarChart.Type.DEFAULT;
        int color = Color.rgb(0, 128, 0);  
        PointStyle style = PointStyle.DIAMOND;  
        renderer4 = buildRenderer(color, style, true);  
          
        //���ú�ͼ�����ʽ  
        setChartSettings(renderer4, "X", "Y", 1, 12, 0,10, Color.BLACK, Color.BLACK);  
          
        //����ͼ��  
        chart4 = ChartFactory.getBarChartView(context4, mDataset4, renderer4,type);  
          
        //�Ƚ�ԭ���Ĳ���ȥ�����ټ����µĲ���
        layout.removeAllViewsInLayout();
        layout.addView(chart4, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
          }   
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {  
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();  
          
        //����ͼ�������߱������ʽ��������ɫ����Ĵ�С�Լ��ߵĴ�ϸ��  
        XYSeriesRenderer r = new XYSeriesRenderer();  
        r.setColor(color);  
        r.setPointStyle(style);  
        r.setFillPoints(fill);  
        r.setLineWidth(3);  
        renderer.addSeriesRenderer(r);  
          
        return renderer;  
    }  
      
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,  
                                    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {  
        //�йض�ͼ�����Ⱦ�ɲο�api�ĵ�  
    	//renderer.setApplyBackgroundColor(false);
       // renderer.setChartTitle("8�����ر仯����");  
        renderer.setXTitle(xTitle);  
        renderer.setYTitle(yTitle);  
        renderer.setXAxisMin(xMin);  
        renderer.setXAxisMax(xMax);  
        renderer.setYAxisMin(yMin);  
        renderer.setYAxisMax(yMax);  
        renderer.setAxesColor(axesColor);  
        //render.setLabelsColor(Color.BLACK);
        renderer.setLabelsColor(labelsColor);  
        renderer.setLabelsColor(Color.BLACK);
        renderer.setShowGrid(true);  
        renderer.setGridColor(Color.GRAY);  
        renderer.setXLabels(20);          
        renderer.setYLabels(10);  
        renderer.setXTitle(" "); 
        renderer.setYTitle(" ");  
        renderer.setMarginsColor(Color.argb(0, 0xff, 0, 0));        
        renderer.setBackgroundColor(Color.WHITE);
        //renderer.setMarginsColor(Color.GRAY);
        //renderer.setLabelsColor(Color.WHITE);
        renderer.setMargins(new int[] {20, 50, 10,20});
        renderer.setChartTitleTextSize((float) 40);
        renderer.setAxisTitleTextSize((float) 20);
        renderer.setLabelsTextSize((float) 30);
        renderer.setYLabelsAlign(Align.RIGHT);  
        renderer.setPointSize((float) 8);  
        renderer.setShowLegend(false);  
        //renderer.setPanLimits(new double[]{0,31,0,200});
       // renderer.setZoomLimits(new double[]{0,12,0,10});
        renderer.setClickEnabled(false);
        renderer.setZoomButtonsVisible(false);
        renderer.setFitLegend(false);
        
    }  
    
    public void onClickBack(View v){
    	this.finish();
	}
    
    public void onClickStyle(View v){
		new AlertDialog.Builder(this)
		.setTitle("��ʾ��ʽ")		
		.setSingleChoiceItems(StrConfig.AnalysisStyle, UserDAO.getInstance().getGraphicStyle(),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (UserDAO.getInstance().getGraphicStyle()!=which){
							UserDAO.getInstance().setListStyle(which);
							Intent intent = new Intent (GraphicActivity.this,MainAnalysisActivity.class);	
							startActivity(intent);
							finish();
						}
						dialog.dismiss();
					}
		}).show();
		
	}	
   public void onClickBefore(View v){
	   Button btn_before= (Button)findViewById(R.id.btn_before);
	   month=UserDAO.getInstance().getSelectMonth().substring(5, 7);
	   Log.i("jymMonth1before",month);
	   int getMonth=Integer.parseInt(month);
	  
    if(getMonth>1)	{
    	//btn_before.setClickable(true);
    	month=StrConfig.MonthList[getMonth-2];
        UserDAO.getInstance().setSelectMonth(month);        
    	Log.i("jymMonth2before",month);	
    	tvRecord.setText(month);
        }
    else{
    	month=StrConfig.MonthList[0];
    	UserDAO.getInstance().setSelectMonth(month);    	
    	//btn_before.setClickable(false);
    	tvRecord.setText(month);
       }
     textTitle.setText(month);
	 text2.setText(month);
     initGraph1();
	 initGraph2();
    
    }
    
   public void onClickNext(View v) {
	   Button btn_next= (Button)findViewById(R.id.btn_next);
	   month=UserDAO.getInstance().getSelectMonth().substring(5, 7);
	   Log.i("jymMonth1next",month);
	   int getMonth=Integer.parseInt(month);
	   if(getMonth<12){
		  // btn_next.setClickable(true);
		   month=StrConfig.MonthList[getMonth];
	       UserDAO.getInstance().setSelectMonth(month);
	       Log.i("jymMonth2next",month);	
	       tvRecord.setText(month);}	   	           
	   else{
		   month=StrConfig.MonthList[11];
	       UserDAO.getInstance().setSelectMonth(month);	    	
	       //btn_next.setClickable(false);
	       tvRecord.setText(month);
	   }
	   textTitle.setText(month);
	   text2.setText(month);
	   initGraph1();
  	   initGraph2();
	
}
 
   public void onClickBefore1(View v){
	  
	   month=UserDAO.getInstance().getSelectMonth().substring(5, 7);
	   Log.i("jymMonth1before",month);
	   int getMonth=Integer.parseInt(month);
	  
    if(getMonth>1)	{
    	//btn_before.setClickable(true);
    	month=StrConfig.MonthList[getMonth-2];
        UserDAO.getInstance().setSelectMonth(month);        
    	Log.i("jymMonth2before",month);	
    	tvRecord.setText(month);
        }
    else{
    	month=StrConfig.MonthList[0];
    	UserDAO.getInstance().setSelectMonth(month);    	
    	//btn_before.setClickable(false);
    	tvRecord.setText(month);
       }
     textTitle.setText(month);
	 text2.setText(month);
     initGraph1();
	 initGraph2();
    
    }
    
   public void onClickNext1(View v) {
	   
	   month=UserDAO.getInstance().getSelectMonth().substring(5, 7);
	   Log.i("jymMonth1next",month);
	   int getMonth=Integer.parseInt(month);
	   if(getMonth<12){
		  // btn_next.setClickable(true);
		   month=StrConfig.MonthList[getMonth];
	       UserDAO.getInstance().setSelectMonth(month);
	       Log.i("jymMonth2next",month);	
	       tvRecord.setText(month);}	   	           
	   else{
		   month=StrConfig.MonthList[11];
	       UserDAO.getInstance().setSelectMonth(month);	    	
	       //btn_next.setClickable(false);
	       tvRecord.setText(month);
	   }
	   textTitle.setText(month);
	   text2.setText(month);
	   initGraph1();
  	   initGraph2();
	
}
   
   
   
    public void onClickMonth(View v){
    	
    	Intent intent = new Intent (GraphicActivity.this,SelectMonthDialog.class);	
		startActivity(intent);	
    }
    
    private ArrayList<Graphic> getGraphicByDay(){
    	ArrayList<HeadacheDiary> headacheDiaries=HeadacheDiaryDAO.getInstance().getDocumentHDiaryList();
    	String month=UserDAO.getInstance().getSelectMonth();
   	    month=month.substring(0,7);
   	  Log.i("monthjym", "month="+month);
   	    ArrayList<Graphic> graphics = new  ArrayList<Graphic>() ;
   	   
    	for(int i=0;i<headacheDiaries.size();i++){
    		HeadacheDiary headacheDiary=headacheDiaries.get(i);
    		String startTime=headacheDiary.getStartTime();        
    		String startTimeShort=startTime.substring(0,7);
    		Calendar end, start;
    		 
    		 
    	// Log.i("startTImejym"+i, "tday="+startTimeShort);
   		 
    		if(startTimeShort.equals(month)){
    		  String tDay = startTime.substring(8,10); 
    		  Log.i("startTImejym"+i, "tday="+tDay);
    		  //Graphic addGraphic = new Graphic(headacheDiary.getDegree(), tDay);
    		  //graphics.add(addGraphic);
      		 // Log.i("graphicday"+i, "addGraphic.getDate()"+addGraphic.getDate());
    		//��ȡͷʹ�̶Ƚ���
      		String endTime=headacheDiary.getEndTime();  
      		end=TimeManager.parseStrDateTime(endTime);
      		start=TimeManager.parseStrDateTime(startTime);
      		long interval = end.getTimeInMillis()-start.getTimeInMillis();
      		long hour= interval/3600000;
      		
      		Graphic addGraphic = new Graphic(headacheDiary.getDegree(), tDay,hour);
  		    graphics.add(addGraphic);
      		
    		}
    		
    		
    		
              }
    	
    	
    	return graphics;
    	
    	    }
    
    private int[] getMonthFrequency(){
    	int[] monthList=new int[13];
    	ArrayList<HeadacheDiary> headacheDiaries=HeadacheDiaryDAO.getInstance().getDocumentHDiaryList();
    	String month=UserDAO.getInstance().getSelectMonth();
   	    month=month.substring(0,4);
   	 for(int i=0;i<headacheDiaries.size();i++){
   		HeadacheDiary headacheDiary=headacheDiaries.get(i);
		String startTime=headacheDiary.getStartTime();
		String startTimeShort=startTime.substring(0,4);
		if(startTimeShort.equals(month)){
		  int getMonth = Integer.parseInt(startTime.substring(5,7));
		  monthList[getMonth]= monthList[getMonth]+1;
			
		}
   		 
			
   	 }
    	
    	
    	return monthList;
    }
    
    private int[] getDrugFrequency(){
    	int[] drugFruquency = new int[12];
    	ArrayList<HeadacheDiary> headacheDiaries=HeadacheDiaryDAO.getInstance().getDocumentHDiaryList();
    	String month=UserDAO.getInstance().getSelectMonth();
    	month=month.substring(0,4);
    	for(int i=0;i<headacheDiaries.size();i++){
       		HeadacheDiary headacheDiary=headacheDiaries.get(i);
       		String startTime=headacheDiary.getStartTime();
    		String startTimeShort=startTime.substring(0,4);
    		int length=headacheDiary.getDrugListSize();
    		if(startTimeShort.equals(month) && length!=0){
    			 int getMonth = Integer.parseInt(startTime.substring(5,7));
    			 drugFruquency[getMonth]= drugFruquency[getMonth]+1;
    			
    		}
       		
    	
    	}
    	return drugFruquency;
    }
    
    private int getXSeriesRange(){
    	int xSeries=32;
        String month=UserDAO.getInstance().getSelectMonth();
        int a=Integer.parseInt(month.substring(5,7));
        if(a==4||a==6||a==9||a==11){
        	xSeries=31;
        }else if (a==2){
        	xSeries=29;
        }
          	
    	return xSeries;
    }
    
     
    }
    

