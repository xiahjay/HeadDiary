package com.example.headdiary.hddialog;

import com.example.headdiary.R;
import com.example.headdiary.R.id;
import com.example.headdiary.R.layout;
import com.example.headdiary.R.menu;
import com.example.headdiary.R.string;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AcheDegreeDialog extends Activity {

	private EditText etRating;
	private TextView tvCategory;
	private ImageView ivPointer;
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ache_degree_dialog);

		initView();
	}

	private void initView(){
		etRating=(EditText)findViewById(R.id.degree_et_rating);
		tvCategory=(TextView)findViewById(R.id.degree_tv_category);
		ivPointer=(ImageView)findViewById(R.id.degree_iv_pointer);
		
		String mText;
		int rating=headacheDiary.getDegree();
		if (rating!=-1){
			mText=String.format("%d", rating);
			tvCategory.setText(StrConfig.HDAcheDegreeGrade[rating]);
			tvCategory.setVisibility(View.VISIBLE);
			animatePointer(0,rating);
		}
		else{
			mText="";
			tvCategory.setVisibility(View.GONE);
		}
		etRating.setText(mText);
		
		etRating.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                    // TODO Auto-generated method stub
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub   
            	String tempstr=etRating.getText().toString(); 
            	if (tempstr.equals("")){
            		tvCategory.setVisibility(View.GONE);
            		animatePointer(0,0);
            	}
            	else{
            		int rating=Integer.parseInt(tempstr);
            		if (rating>0 && rating<=10){
            			tvCategory.setText(StrConfig.HDAcheDegreeGrade[rating]);
            			tvCategory.setVisibility(View.VISIBLE);
            			animatePointer(0,rating);
            		}
            		
            	}
            }
		});
	}
	
    
	private void animatePointer(int startRating, int endRating){
		float startdeg=17.5f*startRating;
		float enddeg=17.5f*endRating;
		int lastTime;
		lastTime=Math.abs(endRating-startRating)*80;	
		RotateAnimation animation = new RotateAnimation(startdeg,enddeg, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);  
        animation.setDuration(lastTime);  
        animation.setFillAfter(true);  
        ivPointer.startAnimation(animation);  
	}

	
	public void onClickCancel(View v){
		finish();
	}
	
	public void onClickRatingRight(View v){
		String tempstr=etRating.getText().toString();
		if (tempstr.equals("")){
			etRating.setText("1");
			tvCategory.setText(StrConfig.HDAcheDegreeGrade[1]);
			tvCategory.setVisibility(View.VISIBLE);
			animatePointer(0,1);
		}
		else{
			int rating=Integer.parseInt(tempstr);
			if (rating>=10)
				Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_degree_invalid), Toast.LENGTH_SHORT).show();
			else{
				rating++;
				etRating.setText(String.format("%d", rating));
				tvCategory.setText(StrConfig.HDAcheDegreeGrade[rating]);
				tvCategory.setVisibility(View.VISIBLE);
				animatePointer(rating-1,rating);
			}
		}
	}
	
	public void onClickRatingLeft(View v){
		String tempstr=etRating.getText().toString();
		if (tempstr.equals("")){
			etRating.setText("10");
			tvCategory.setText(StrConfig.HDAcheDegreeGrade[10]);
			tvCategory.setVisibility(View.VISIBLE);
			animatePointer(0,10);
		}
		else{
			int rating=Integer.parseInt(tempstr);
			if (rating<=1)
				Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_degree_invalid), Toast.LENGTH_SHORT).show();
			else{
				rating--;
				etRating.setText(String.format("%d", rating));
				tvCategory.setText(StrConfig.HDAcheDegreeGrade[rating]);
				tvCategory.setVisibility(View.VISIBLE);
				animatePointer(rating+1,rating);
			}
		}
	}
	
	
	
	
	public void onClickConfirm(View v){
		String tempstr=etRating.getText().toString();
		
		if (tempstr.equals("")){
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_no_input), Toast.LENGTH_SHORT).show();
		}
		else{
			int rating=Integer.parseInt(tempstr);
			if (rating==0) 
				Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_degree_zero), Toast.LENGTH_SHORT).show();
			else if (rating<0 || rating>10)
				Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_degree_invalid), Toast.LENGTH_SHORT).show();
			else{
				headacheDiary.setDegree(rating);
				finish();				
			}	
		}

	}
}
