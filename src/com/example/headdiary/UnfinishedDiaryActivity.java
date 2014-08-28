package com.example.headdiary;

import java.util.Calendar;

import com.example.headdiary.R;
import com.example.headdiary.R.id;
import com.example.headdiary.R.layout;
import com.example.headdiary.R.menu;
import com.example.headdiary.R.string;
import com.example.headdiary.data.Drug;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.hddialog.AcheDegreeDialog;
import com.example.headdiary.hddialog.AchePositionDialog;
import com.example.headdiary.hddialog.AcheTypeDialog;
import com.example.headdiary.hddialog.ActivityAggravateDialog;
import com.example.headdiary.hddialog.AddDrugDialog;
import com.example.headdiary.hddialog.CompanionDialog;
import com.example.headdiary.hddialog.EndTimeDialog;
import com.example.headdiary.hddialog.MitigatingDialog;
import com.example.headdiary.hddialog.PrecipiatingDialog;
import com.example.headdiary.hddialog.ProdromeDialog;
import com.example.headdiary.hddialog.StartTimeDialog;
import com.example.headdiary.hddialog.StartTimeQuestion;
import com.example.headdiary.util.AllExit;
import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.TimeManager;
import com.example.headdiary.util.ToastManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//display headacheDiarySelected in HeadacheDiaryDAO
public class UnfinishedDiaryActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unfinished_diary);
		Intent intent = new Intent (UnfinishedDiaryActivity.this,StartTimeQuestion.class);	
		startActivity(intent);
	}


	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	          
	    
	}

	
}
