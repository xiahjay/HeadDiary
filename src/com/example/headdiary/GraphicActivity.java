package com.example.headdiary;



import java.util.ArrayList;
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

import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.UserDAO;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	    private XYMultipleSeriesRenderer renderer1;  
	    private XYMultipleSeriesRenderer renderer2; 
	    private Context context1; 
	    private Context context2; 
	    private TextView tvStyle;
	   
	     
	      
	   
	
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);
        init();
        series1 = new XYSeries(title); 
        mDataset1 = new XYMultipleSeriesDataset();
        series2 = new XYSeries(title); 
        mDataset2 = new XYMultipleSeriesDataset();
        tvStyle=(TextView)findViewById(R.id.graphic_tv_style);
		tvStyle.setText(StrConfig.AnalysisStyle[1]);
        
        
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	Log.d("onResume", "onResume Method is executed");
    	initGraph1();
    	 initGraph2();
    }
 
    private void init(){
    	final LinearLayout ll1 = (LinearLayout) findViewById(R.id.linechart1);
    	final ImageView arrow1 = (ImageView) findViewById(R.id.arrow1);
		LinearLayout tl1 = (LinearLayout) findViewById(R.id.templl);
		

		tl1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ll1.getVisibility() == 0) {
					ll1.setVisibility(View.GONE);
					arrow1.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow1));
					
				} else {
					ll1.setVisibility(View.VISIBLE);
					arrow1.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow2));// 璁剧疆鍙
					
				}

			}
		});
		final LinearLayout ll2 = (LinearLayout) findViewById(R.id.linechart2);
		LinearLayout tl2 = (LinearLayout) findViewById(R.id.windll);
		final ImageView arrow2 = (ImageView) findViewById(R.id.arrow2);

		

		tl2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ll2.getVisibility() == 0) {
					ll2.setVisibility(View.GONE);
					arrow2.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow1));
					
				} else {
					ll2.setVisibility(View.VISIBLE);
					arrow2.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow2));// 璁剧疆鍙
					
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
					arrow3.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow2));// 璁剧疆鍙
					
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
					arrow4.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow2));// 璁剧疆鍙
					
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
       
        //这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线         
        //ArrayList<Float> weightList= new  ArrayList<Float>();
       // String id= WeightDAO.getInstance().getUseId();
        //weightList=mUserDataManager.getWeightData(id);
       
        
        for(int i=0; i<24; i++){        	
        	float a=i;
        	float b;
        	if(a<10)
        	{ b=a;}
        	else {
        	 b = 8;
        	}
        	series1.add(a, b);
        	}
        
        //创建一个数据集的实例，这个数据集将被用来创建图表  
       // mDataset = new XYMultipleSeriesDataset();  
          
        //将点集添加到这个数据集中  
        mDataset1.addSeries(series1);  
          
        //以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄  
        int color = Color.BLUE;  
        PointStyle style = PointStyle.DIAMOND;  
        renderer1 = buildRenderer(color, style, true);  
          
        //设置好图表的样式  
        setChartSettings(renderer1, "X", "Y", 0, 24, 0, 10, Color.BLACK, Color.BLACK);  
          
        //生成图表  
        chart1 = ChartFactory.getLineChartView(context1, mDataset1, renderer1);  
          
        //将图表添加到布局中去  
        layout.addView(chart1, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
          }   
    
    private void initGraph2() {
		// TODO Auto-generated method stub
    	context2 = getApplicationContext();  
        
       LinearLayout layout = (LinearLayout)findViewById(R.id.linechart2);  
        //series = new XYSeries(title); 
        mDataset2.removeSeries(series2);
        series2.clear();
       
        //这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线         
        //ArrayList<Float> weightList= new  ArrayList<Float>();
       // String id= WeightDAO.getInstance().getUseId();
        //weightList=mUserDataManager.getWeightData(id);
       
        
        for(int i=0; i<24; i++){  
        	float x=i;
        	float y;
        	if(i==3){
        	  y=8;
             	}
        	else if(i==10){
        	  y=10;
        	}
        	else{
        	 y=0;
        	}
        	series2.add(x, y);
        	}
        
        //创建一个数据集的实例，这个数据集将被用来创建图表  
       // mDataset = new XYMultipleSeriesDataset();  
          
        //将点集添加到这个数据集中  
        mDataset2.addSeries(series2);  
          
        //以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄  
        int color = Color.BLUE;  
        BarChart.Type type = BarChart.Type.DEFAULT;
        PointStyle style = PointStyle.DIAMOND;  
        renderer2 = buildRenderer(color, style, true);  
          
        //设置好图表的样式  
        setChartSettings(renderer2, "X", "Y", 0, 24, 0, 10, Color.BLACK, Color.BLACK);  
          
        //生成图表  
        chart2 = ChartFactory.getBarChartView(context2, mDataset2, renderer2,type);  
          
        //将图表添加到布局中去  
        layout.addView(chart2, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
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
          
        //设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等  
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
        //有关对图表的渲染可参看api文档  
    	//renderer.setApplyBackgroundColor(false);
       // renderer.setChartTitle("8月体重变化趋势");  
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
        renderer.setXTitle("日期"); 
        renderer.setYTitle("体重");  
        renderer.setMarginsColor(Color.argb(0, 0xff, 0, 0));        
        renderer.setBackgroundColor(Color.WHITE);
        //renderer.setMarginsColor(Color.GRAY);
        //renderer.setLabelsColor(Color.WHITE);
        renderer.setMargins(new int[] {20, 30, 10,20});
        renderer.setChartTitleTextSize((float) 40);
        renderer.setAxisTitleTextSize((float) 20);
        renderer.setLabelsTextSize((float) 30);
        renderer.setYLabelsAlign(Align.RIGHT);  
        renderer.setPointSize((float) 8);  
        renderer.setShowLegend(false);  
        renderer.setPanLimits(new double[]{0,31,0,200});
        renderer.setZoomLimits(new double[]{0,31,0,200});
        
    }  
    
    public void onClickBack(View v){
    	this.finish();
	}
    
    public void onClickStyle(View v){
		new AlertDialog.Builder(this)
		.setTitle("显示方式")
		
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
    
    
}
