package com.example.headdiary.util;

import java.io.File;
import java.security.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;


import com.example.headdiary.MainActivity;
import com.example.headdiary.MainDocumentActivity;
import com.example.headdiary.R.string;
import com.example.headdiary.data.Config;
import com.example.headdiary.data.Diagnose;
import com.example.headdiary.data.Drug;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.Suggestion;
import com.example.headdiary.data.User;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.data.Config.DBConfig;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import android.R.anim;
import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private static String TAG="DBManager";
	
	public DBManager() {
		// TODO Auto-generated constructor stub
	}

	private static SQLiteDatabase openDB(String dbfile) {
		if (!(new File(dbfile).exists())) {
	           //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
	            	Log.e("DBManager","Have not found the database!"); 
	            	return null;
	        }
	        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
	        return db;
	}

		//------------------------------------------------------------------------//
		//                                                                        //
		//                        头痛日志的存取  
		//                                                                        //
		//------------------------------------------------------------------------//

	/**
	 * 取出上次写下的不完整的头痛日志
	 */
	private static void getLastHeadacheDiary() {
		// TODO Auto-generated method stub
		HeadacheDiary headacheDiary=getHeadacheDiaryByUserIdAndRecId(UserDAO.getInstance().getUser().getUserId(), 0);
		HeadacheDiaryDAO.getInstance().setLastHeadacheDiary(headacheDiary);
	}

	public static String saveHDiaryToDB(HeadacheDiary headacheDiary) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		
		String hint="头痛日志保存失败";
		int lastRecId=headacheDiary.getRecId();
		User user=UserDAO.getInstance().getUser();
		Boolean ifComplete=headacheDiary.getIfComplete();
		Boolean ifInsert=headacheDiary.getRecId()==0 && HeadacheDiaryDAO.getInstance().getIfLastDiaryComplete();  //新创建一个头痛日志
		
		if (headacheDiary.getRecId()==0 && ifComplete){
			headacheDiary.setRecId(getNewRecId());  //新加入一条完整的头痛日志，先取出最小的（负数）RecId，然后-1作为新的RecId
			HeadacheDiaryDAO.getInstance().setLastHeadacheDiary(null);
		}
		
		if (ifComplete){
			user.addToHDNeedUpload(headacheDiary.getRecId());
			updateHDNeedUpload();
			headacheDiary.makeAidDiagnosis();
			hint="保存成功!"+headacheDiary.getStrAidDiagnosis();
		}
		else{
			HeadacheDiaryDAO.getInstance().setLastHeadacheDiary(headacheDiary);
			headacheDiary.makeAidDiagnosis();
			hint="保存成功!日志尚不完整，记得及时补全"+headacheDiary.getStrAidDiagnosis();
		}
		
		Gson gson=new Gson();
		ContentValues args = new ContentValues();
		args.put(DBConfig.COL_HDiary_UserId, user.getUserId());
		args.put(DBConfig.COL_HDiary_RecId, headacheDiary.getRecId());
		args.put(DBConfig.COL_HDiary_RecordTime, headacheDiary.getRecordTime());
		args.put(DBConfig.COL_HDiary_StartTime, headacheDiary.getStartTime());
		args.put(DBConfig.COL_HDiary_EndTime, headacheDiary.getEndTime());
		args.put(DBConfig.COL_HDiary_Position, headacheDiary.getPosition());
		args.put(DBConfig.COL_HDiary_IfAroundEye, headacheDiary.getIfAroundEye());
		args.put(DBConfig.COL_HDiary_Type, headacheDiary.getType());
		args.put(DBConfig.COL_HDiary_TypeComment, headacheDiary.getTypeComment());
		args.put(DBConfig.COL_HDiary_Degree, headacheDiary.getDegree());
		args.put(DBConfig.COL_HDiary_IfActivityAggravate, headacheDiary.getIfActivityAggravate());
		args.put(DBConfig.COL_HDiary_Prodrome, gson.toJson(headacheDiary.getProdrome()));
		args.put(DBConfig.COL_HDiary_Companion, gson.toJson(headacheDiary.getCompanion()));
		args.put(DBConfig.COL_HDiary_Precipiating, gson.toJson(headacheDiary.getPrecipiating()));
		args.put(DBConfig.COL_HDiary_PrecipiatingComment, headacheDiary.getPrecipiatingComment());
		args.put(DBConfig.COL_HDiary_Mitigating, gson.toJson(headacheDiary.getMitigating()));
		args.put(DBConfig.COL_HDiary_MitigatingComment, headacheDiary.getMitigatingComment());
		args.put(DBConfig.COL_HDiary_DrugList, gson.toJson(headacheDiary.getDrugList()));
		args.put(DBConfig.COL_HDiary_AidDiagnosis, headacheDiary.getAidDiagnosis());
		
		if (ifInsert){
			//进行Insert	
			db= openDB(DBConfig.DB_FULLNAME);
			db.insert(DBConfig.TB_HeadacheDiary, null, args);
			db.close();
		}
		else{
			//进行Update
			db= openDB(DBConfig.DB_FULLNAME);
			db.update(DBConfig.TB_HeadacheDiary,args,DBConfig.COL_UserInfo_UserId+"='"+user.getUserId()+"' and "+DBConfig.COL_HDiary_RecId+"='"+lastRecId+"'" ,null);
			db.close();
		}

		HeadacheDiaryDAO.getInstance().setDocumentHDiaryList(null);
		HeadacheDiaryDAO.getInstance().setHeadacheAnalysisList(null);
		return hint;
	}

	private static void updateHDNeedUpload() {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		
		Gson gson=new Gson();
		User user=UserDAO.getInstance().getUser();
		ContentValues args = new ContentValues();
		args.put(DBConfig.COL_UserInfo_HDNeedUpload, gson.toJson(user.getHDNeedUpload()));
		
		db= openDB(DBConfig.DB_FULLNAME);
		db.update(DBConfig.TB_UserInfo, args,DBConfig.COL_UserInfo_UserId+"='"+user.getUserId()+"'" ,null);
		db.close();
	}

	private static int getNewRecId() {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		Cursor cursor;
		String sql;
		
		int recId=-1;
		db= openDB(DBConfig.DB_FULLNAME);
		sql="select min(RecId) as RecId from HeadacheDiary where UserId='"+UserDAO.getInstance().getUser().getUserId()+"'";
		cursor=db.rawQuery(sql,null);
		if (cursor!=null && cursor.moveToFirst()){		
			recId= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_RecId));
			if (recId>=0)
				recId=-1;
			else {
				recId--;
			}
		}	
		
		db.close();
		
		return recId;
	}

	public static void getDocumentHDiaryFromDBToDAO() {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		Cursor cursor;
		String sql;
		
		int recId=0;
		String recordTime,startTime,endTime;
		int position,ifAroundEye,type,degree,ifActivityAggravate,aidDiagnosis;
		String positionComment,typeComment,precipiatingComment,mitigatingComment;
		int[] prodrome,companion,precipiating,mitigating;
		ArrayList<Drug> drugList;
		String jsonProdrome,jsonCompanion,jsonPrecipiating,jsonMitigating,jsonDrugList;
		
		ArrayList<HeadacheDiary> hDiaryList=new ArrayList<HeadacheDiary>();
		
		db= openDB(DBConfig.DB_FULLNAME);
		sql="select * from HeadacheDiary where UserId='"+UserDAO.getInstance().getUser().getUserId()+"' and RecId<>'0' order by StartTime desc";
		
		cursor=db.rawQuery(sql,null);
		if (cursor!=null && cursor.moveToFirst()){
			do{
				recId=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_RecId));
				recordTime= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_RecordTime));
				startTime= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_StartTime));
				endTime= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_EndTime));
				position= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_Position));
				ifAroundEye= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_IfAroundEye));
				type= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_Type));
				degree= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_Degree));
				ifActivityAggravate= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_IfActivityAggravate));
				aidDiagnosis= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_AidDiagnosis));
				positionComment= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_TypeComment));
				typeComment= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_TypeComment));
				precipiatingComment= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_PrecipiatingComment));
				mitigatingComment= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_MitigatingComment));
				jsonProdrome= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_Prodrome));
				jsonCompanion= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_Companion));
				jsonPrecipiating= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_Precipiating));
				jsonMitigating= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_Mitigating));
				jsonDrugList= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_DrugList));
				
				Gson gson=new Gson();
				prodrome=gson.fromJson(jsonProdrome, new TypeToken<int[]>(){}.getType());
				companion=gson.fromJson(jsonCompanion, new TypeToken<int[]>(){}.getType());
				precipiating=gson.fromJson(jsonPrecipiating, new TypeToken<int[]>(){}.getType());
				mitigating=gson.fromJson(jsonMitigating, new TypeToken<int[]>(){}.getType());
				drugList=gson.fromJson(jsonDrugList, new TypeToken<ArrayList<Drug>>(){}.getType());
				HeadacheDiary headacheDiary=new HeadacheDiary(recId, recordTime, startTime, endTime, position, ifAroundEye, 
						type, degree, ifActivityAggravate, aidDiagnosis, positionComment, typeComment, precipiatingComment, mitigatingComment, 
						prodrome, companion, precipiating, mitigating, drugList);
				hDiaryList.add(headacheDiary);
			}while(cursor.moveToNext());		
		}

		HeadacheDiaryDAO.getInstance().setDocumentHDiaryList(hDiaryList);
		db.close();
	}

	public static void deleteHDiaryByRecId(HeadacheDiary headacheDiary) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		Cursor cursor;
		String sql;
		
		int recId=headacheDiary.getRecId();
		db= openDB(DBConfig.DB_FULLNAME);
		sql="delete from HeadacheDiary where UserId='"+UserDAO.getInstance().getUser().getUserId()+"' and RecId='"+recId+"'";
		db.execSQL(sql);
		if (recId>0){
			UserDAO.getInstance().getUser().addToHDNeedDelete(recId);
		}
		db.close();
		updateHDNeedDelete();
	}
	
	private static void updateHDNeedDelete() {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		
		Gson gson=new Gson();
		User user=UserDAO.getInstance().getUser();
		ContentValues args = new ContentValues();
		args.put(DBConfig.COL_UserInfo_HDNeedDelete, gson.toJson(user.getHDNeedDelete()));
		
		db= openDB(DBConfig.DB_FULLNAME);
		db.update(DBConfig.TB_UserInfo, args,DBConfig.COL_UserInfo_UserId+"='"+user.getUserId()+"'" ,null);
		db.close();
	}

	
		//------------------------------------------------------------------------//
		//                                                                        //
		//                            注册登录   
		//                                                                        //
		//------------------------------------------------------------------------//
	/**
	 * 在MainActivity中调用，欢迎界面的时候取出上次登录的用户信息
	 * @return true表示上次登录的用户记住密码，则直接进入主界面；false表示上次登录的用户不记住密码，则转到登录界面
	 */
	public static Boolean getLastUser() {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		Cursor cursor;
		String sql;
		Boolean result=false;
		
		db= openDB(DBConfig.DB_FULLNAME);
		sql="select UserId from LoginInfo order by RecId desc limit 1";
		
		cursor=db.rawQuery(sql,null);
		if (cursor!=null && cursor.moveToFirst()){		
			int userId=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_UserId));
			User loginUser=getUserByUserIdFromDB(userId);
			UserDAO.getInstance().setLoginUser(loginUser);
			if (loginUser.getRemPswd()==1){
				result=true;
				UserDAO.getInstance().setUser(loginUser);
				DBManager.loginSuccess();
			}
		}	
		db.close();
		return result;
	}
	
	public static Boolean tryLocalLogin(User loginUser) {
		// TODO Auto-generated method stub
		Boolean result=false;
		User user=getUserByPhoneFromDB(loginUser.getPhone());
		if (user!=null){
			if(user.getPassword().equals(loginUser.getPassword())){
				if(user.getRemPswd()!=loginUser.getRemPswd()){
					user.setRemPswd(loginUser.getRemPswd());
					updateUserInfo_RemPswdByUserId(user);
				}
				UserDAO.getInstance().setUser(user);
				DBManager.loginSuccess();
				result=true;
			}
			loginUser.setUserId(user.getUserId());	
		}
		return result;
	}

	public static void updateUserInfo_RemPswdByUserId(User user) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		
		ContentValues args = new ContentValues();
		args.put(DBConfig.COL_UserInfo_RemPswd, user.getRemPswd());
		
		db= openDB(DBConfig.DB_FULLNAME);
		db.update(DBConfig.TB_UserInfo, args,DBConfig.COL_UserInfo_UserId+"='"+user.getUserId()+"'" ,null);
		db.close();
	}

	/**
	 * LoginInfo表中插入一条记录；获取该用户的headache信息
	 */
	public static void loginSuccess() {
		// TODO Auto-generated method stub
		insertLoginRecord();
		getLastHeadacheDiary();
	}

	private static void insertLoginRecord() {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		User user=UserDAO.getInstance().getUser();
		ContentValues args = new ContentValues();
		args.put(DBConfig.COL_LoginInfo_UserId,user.getUserId() );
		args.put(DBConfig.COL_LoginInfo_LoginTime,TimeManager.getStrDateTime() );
		db= openDB(DBConfig.DB_FULLNAME);
		db.insert(DBConfig.TB_LoginInfo, null, args);
		db.close();
	}
	
	public static User getUserByUserIdFromDB(int userId){
		SQLiteDatabase db;
		Cursor cursor;
		String sql;
		User user=null;
		
		db= openDB(DBConfig.DB_FULLNAME);
		sql="select * from UserInfo where UserId='"+userId+"'";
		cursor=db.rawQuery(sql,null);
		if (cursor!=null && cursor.moveToFirst()){	
			String userUUID=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_UserUUID));
			String userName=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_UserName));
			String password=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_Password));
			int remPswd=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_RemPswd));
			String phone=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_Phone));
			String sex=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_Sex));
			int birthYear=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_BirthYear));
			int doctorId=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_DoctorId));
			String doctorName=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_DoctorName));
			int autoUpload=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_AutoUpload));
			
			int hDVersion=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_HDVersion));
			String jsonHDNeedUpload=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_HDNeedUpload));
			String jsonHDNeedDelete=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_HDNeedDelete));
			
			Gson gson=new Gson();
			ArrayList<Integer> hDNeedUpload=gson.fromJson(jsonHDNeedUpload, new TypeToken<ArrayList<Integer>>(){}.getType());
			ArrayList<Integer> hDNeedDelete=gson.fromJson(jsonHDNeedDelete, new TypeToken<ArrayList<Integer>>(){}.getType());
			user=new User(userId, doctorId, hDVersion, userUUID, hDNeedUpload, hDNeedDelete, remPswd, birthYear, userName, password, phone, sex, doctorName,autoUpload);
		}	
		db.close();
		return user;
	}

	private static User getUserByPhoneFromDB(String phone) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		Cursor cursor;
		String sql;
		User user=null;
		
		db= openDB(DBConfig.DB_FULLNAME);
		sql="select * from UserInfo where Phone='"+phone+"'";
		cursor=db.rawQuery(sql,null);
		if (cursor!=null && cursor.moveToFirst()){	
			int userId=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_UserId));
			String userUUID=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_UserUUID));
			String userName=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_UserName));
			String password=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_Password));
			int remPswd=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_RemPswd));
			String sex=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_Sex));
			int birthYear=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_BirthYear));
			int doctorId=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_DoctorId));
			String doctorName=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_DoctorName));
			int autoUpload=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_AutoUpload));
			
			int hDVersion=cursor.getInt(cursor.getColumnIndex(DBConfig.COL_UserInfo_HDVersion));
			String jsonHDNeedUpload=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_HDNeedUpload));
			String jsonHDNeedDelete=cursor.getString(cursor.getColumnIndex(DBConfig.COL_UserInfo_HDNeedDelete));
			
			Gson gson=new Gson();
			ArrayList<Integer> hDNeedUpload=gson.fromJson(jsonHDNeedUpload, new TypeToken<ArrayList<Integer>>(){}.getType());
			ArrayList<Integer> hDNeedDelete=gson.fromJson(jsonHDNeedDelete, new TypeToken<ArrayList<Integer>>(){}.getType());
			user=new User(userId, doctorId, hDVersion, userUUID, hDNeedUpload, hDNeedDelete, remPswd, birthYear, userName, password, phone, sex, doctorName,autoUpload);
		}	
		db.close();
		return user;
	}
	
	
	public static void putUsertoDB(User user) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		
		Gson gson=new Gson();
		ContentValues args = new ContentValues();
		args.put(DBConfig.COL_UserInfo_UserUUID,user.getUserUUID());
		args.put(DBConfig.COL_UserInfo_UserName,user.getUserName());
		args.put(DBConfig.COL_UserInfo_Password,user.getPassword());
		args.put(DBConfig.COL_UserInfo_RemPswd,user.getRemPswd());
		args.put(DBConfig.COL_UserInfo_Phone,user.getPhone());
		args.put(DBConfig.COL_UserInfo_Sex,user.getSex());
		args.put(DBConfig.COL_UserInfo_BirthYear,user.getBirthYear());
		args.put(DBConfig.COL_UserInfo_DoctorId,user.getDoctorId());
		args.put(DBConfig.COL_UserInfo_DoctorName,user.getDoctorName());
		args.put(DBConfig.COL_UserInfo_AutoUpload,user.getAutoUpload());
		args.put(DBConfig.COL_UserInfo_HDVersion,user.getHDVersion());
		args.put(DBConfig.COL_UserInfo_HDNeedUpload,gson.toJson(user.getHDNeedUpload()));
		args.put(DBConfig.COL_UserInfo_HDNeedDelete,gson.toJson(user.getHDNeedDelete()));
		
		if (user.getUserId()==-1){ 
			//进行Insert	
			db= openDB(DBConfig.DB_FULLNAME);
			db.insert(DBConfig.TB_UserInfo, null, args);
			Cursor cursor = db.rawQuery("select max(UserId) from UserInfo",null);     //取出最后一个插入的记录的id              
			int userId = -1;
			if(cursor!=null && cursor.moveToFirst())    
				userId= cursor.getInt(0);  
			user.setUserId(userId);
			db.close();
		}
		else{
			//进行Update
			db= openDB(DBConfig.DB_FULLNAME);
			db.update(DBConfig.TB_UserInfo,args,DBConfig.COL_UserInfo_UserId+"='"+user.getUserId()+"'" ,null);
			db.close();
		}
		
	}

	public static Boolean ifLocalAccountExists(User registerUser) {
		// TODO Auto-generated method stub
		Boolean result=false;
		SQLiteDatabase db;
		Cursor cursor;
		String sql;
		
		db= openDB(DBConfig.DB_FULLNAME);
		sql="select * from UserInfo where Phone='"+registerUser.getPhone()+"'";
		cursor=db.rawQuery(sql,null);
		if (cursor!=null && cursor.moveToFirst()){	
			result=true;
		}	
		db.close();
		return result;
	}

	//---------------------------------------------------//
	//
	//                       同步                    
	//
	//----------------------------------------------------//
	
	public static ArrayList<HeadacheDiary> getDiaryList(ArrayList<Integer> hdNeedUpload) {
		// TODO Auto-generated method stub
		if (hdNeedUpload==null)
			return null;
		ArrayList<HeadacheDiary> hDiaryList=new ArrayList<HeadacheDiary>();
		HeadacheDiary headacheDiary;
		int userId=UserDAO.getInstance().getUser().getUserId();
		for(int recId:hdNeedUpload){
			headacheDiary=getHeadacheDiaryByUserIdAndRecId(userId, recId);
			hDiaryList.add(headacheDiary);
		}
		return hDiaryList;
	}
	
	private static HeadacheDiary getHeadacheDiaryByUserIdAndRecId(int userId,int recId) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		Cursor cursor;
		String sql;
		
		String recordTime,startTime,endTime;
		int position,ifAroundEye,type,degree,ifActivityAggravate,aidDiagnosis;
		String positionComment,typeComment,precipiatingComment,mitigatingComment;
		int[] prodrome,companion,precipiating,mitigating;
		ArrayList<Drug> drugList;
		String jsonProdrome,jsonCompanion,jsonPrecipiating,jsonMitigating,jsonDrugList;
		HeadacheDiary headacheDiary=null;
		db= openDB(DBConfig.DB_FULLNAME);
		sql="select * from HeadacheDiary where UserId='"+userId+"' and RecId='"+recId+"'";
		cursor=db.rawQuery(sql,null);
		if (cursor!=null && cursor.moveToFirst()){			
			recordTime= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_RecordTime));
			startTime= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_StartTime));
			endTime= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_EndTime));
			position= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_Position));
			ifAroundEye= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_IfAroundEye));
			type= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_Type));
			degree= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_Degree));
			ifActivityAggravate= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_IfActivityAggravate));
			aidDiagnosis= cursor.getInt(cursor.getColumnIndex(DBConfig.COL_HDiary_AidDiagnosis));
			positionComment= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_TypeComment));
			typeComment= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_TypeComment));
			precipiatingComment= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_PrecipiatingComment));
			mitigatingComment= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_MitigatingComment));
			jsonProdrome= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_Prodrome));
			jsonCompanion= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_Companion));
			jsonPrecipiating= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_Precipiating));
			jsonMitigating= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_Mitigating));
			jsonDrugList= cursor.getString(cursor.getColumnIndex(DBConfig.COL_HDiary_DrugList));
			
			Gson gson=new Gson();
			prodrome=gson.fromJson(jsonProdrome, new TypeToken<int[]>(){}.getType());
			companion=gson.fromJson(jsonCompanion, new TypeToken<int[]>(){}.getType());
			precipiating=gson.fromJson(jsonPrecipiating, new TypeToken<int[]>(){}.getType());
			mitigating=gson.fromJson(jsonMitigating, new TypeToken<int[]>(){}.getType());
			drugList=gson.fromJson(jsonDrugList, new TypeToken<ArrayList<Drug>>(){}.getType());
			headacheDiary=new HeadacheDiary(recId, recordTime, startTime, endTime, position, ifAroundEye, 
					type, degree, ifActivityAggravate, aidDiagnosis, positionComment, typeComment, precipiatingComment, mitigatingComment, 
					prodrome, companion, precipiating, mitigating, drugList);
		}
		
		db.close();
		return headacheDiary;
	}

	
	public static void updateAllHeadDiary(ArrayList<HeadacheDiary> hDiaryList) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		String sql;
		Gson gson=new Gson();
		int userId=UserDAO.getInstance().getUser().getUserId();
		
		db= openDB(DBConfig.DB_FULLNAME);
		sql="delete from HeadacheDiary where UserId='"+userId+"' and RecId>0";
		db.execSQL(sql);

		for(HeadacheDiary headacheDiary:hDiaryList){
			ContentValues args = new ContentValues();
			args.put(DBConfig.COL_HDiary_UserId, userId);
			args.put(DBConfig.COL_HDiary_RecId, headacheDiary.getRecId());
			args.put(DBConfig.COL_HDiary_RecordTime, headacheDiary.getRecordTime());
			args.put(DBConfig.COL_HDiary_StartTime, headacheDiary.getStartTime());
			args.put(DBConfig.COL_HDiary_EndTime, headacheDiary.getEndTime());
			args.put(DBConfig.COL_HDiary_Position, headacheDiary.getPosition());
			args.put(DBConfig.COL_HDiary_IfAroundEye, headacheDiary.getIfAroundEye());
			args.put(DBConfig.COL_HDiary_Type, headacheDiary.getType());
			args.put(DBConfig.COL_HDiary_TypeComment, headacheDiary.getTypeComment());
			args.put(DBConfig.COL_HDiary_Degree, headacheDiary.getDegree());
			args.put(DBConfig.COL_HDiary_IfActivityAggravate, headacheDiary.getIfActivityAggravate());
			args.put(DBConfig.COL_HDiary_Prodrome, gson.toJson(headacheDiary.getProdrome()));
			args.put(DBConfig.COL_HDiary_Companion, gson.toJson(headacheDiary.getCompanion()));
			args.put(DBConfig.COL_HDiary_Precipiating, gson.toJson(headacheDiary.getPrecipiating()));
			args.put(DBConfig.COL_HDiary_PrecipiatingComment, headacheDiary.getPrecipiatingComment());
			args.put(DBConfig.COL_HDiary_Mitigating, gson.toJson(headacheDiary.getMitigating()));
			args.put(DBConfig.COL_HDiary_MitigatingComment, headacheDiary.getMitigatingComment());
			args.put(DBConfig.COL_HDiary_DrugList, gson.toJson(headacheDiary.getDrugList()));
			args.put(DBConfig.COL_HDiary_AidDiagnosis, headacheDiary.getAidDiagnosis());
			
			db.insert(DBConfig.TB_HeadacheDiary, null, args);
		}
		db.close();
	}

	public static void updateUploadHeadDiaryRecId(
			ArrayList<Integer> hdNeedUpload, ArrayList<Integer> newRecIdList) {
		// TODO Auto-generated method stub
		int oldRecId,newRecId;
		SQLiteDatabase db= openDB(DBConfig.DB_FULLNAME);
		int userId=UserDAO.getInstance().getUser().getUserId();
		for (int i=0;i<hdNeedUpload.size();i++){
			oldRecId=hdNeedUpload.get(i);
			newRecId=newRecIdList.get(i);
			if (oldRecId>0)
				continue;
			ContentValues args = new ContentValues();
			args.put(DBConfig.COL_HDiary_RecId, newRecId);
			db.update(DBConfig.TB_HeadacheDiary,args,DBConfig.COL_UserInfo_UserId+"='"+userId+"' and "+DBConfig.COL_HDiary_RecId+"='"+oldRecId+"'" ,null);
		}
		db.close();
	}

	public static Diagnose getDiagnoseInfor(int dignoseId){
		
		SQLiteDatabase db;
		Diagnose diagnose=null;
		
		db= openDB(DBConfig.DB_FULLNAME);
		Cursor mCursor=db.query(DBConfig.DB_TABLENAME,null, "DiagnoseId='"+dignoseId+"'", null, null, null, null);
		if (mCursor!=null && mCursor.moveToFirst()){	
			String suggestion = mCursor.getString(mCursor.getColumnIndex(DBConfig.DB_SUGGESTION));
		    String guidelines = mCursor.getString(mCursor.getColumnIndex(DBConfig.DB_GUIDELINES));	
		   
		    diagnose = new Diagnose("", suggestion, guidelines);
		}

		db.close();
		
		return diagnose;
	}

	public static void saveDruglistToDB(ArrayList<Drug> druglist){
		SQLiteDatabase db = openDB(DBConfig.DB_FULLNAME);
		Cursor cursor;
		for(int i=0; i<druglist.size(); i++){
		Boolean flag = true;
		cursor = db.query("DrugInfor", null,"DrugName='"+druglist.get(i).getName()+"'", null,null,null,null);
		if(cursor!=null && cursor.moveToFirst()){
			flag = false;
			String recTime = druglist.get(i).getRecordTime();
			String sql ="update DrugInfor set RecordTime='"+recTime+"' where DrugName='"+druglist.get(i).getName()+"'";
			db.execSQL(sql);			
		    }
		if (flag){
			
			String sql ="insert into DrugInfor (UserId, DrugName, RecordTime) values('"+UserDAO.getInstance().getUser().getUserId()+"','"+druglist.get(i).getName()+"','"+druglist.get(i).getRecordTime()+"')";
			db.execSQL(sql);
			
			Log.i("UserId", "UserId="+UserDAO.getInstance().getUser().getUserId());
			Log.i("DrugName", "DrugName="+druglist.get(i).getName());
			Log.i("RecordTime", "RecordTime="+druglist.get(i).getRecordTime());
		    }
					
		}		
		
		db.close();
	}
	
	public static ArrayList<String> getDruglistFromDB() {
	   ArrayList<String> druglist = new ArrayList<String>();
	   SQLiteDatabase db = openDB(DBConfig.DB_FULLNAME);
	   Cursor cursor;
	   String sql ="select * from DrugInfor where UserId='"+UserDAO.getInstance().getUser().getUserId()+"' order by RecordTime desc";
	   cursor=db.rawQuery(sql,null);
	   if(cursor!=null && cursor.moveToFirst())
	   do{
		  druglist.add(cursor.getString(cursor.getColumnIndex("DrugName")));
	   }while (cursor.moveToNext());
		
	   db.close();
		return druglist;
	}

	public static void getSuggestionlistFromDB() {
		// TODO Auto-generated method stub
		ArrayList<Suggestion> suggestionList = new ArrayList<Suggestion>();
		SQLiteDatabase db = openDB(DBConfig.DB_FULLNAME);
		Cursor cursor;
		String sql ="select * from SuggestionInfor where UserId='"+UserDAO.getInstance().getUser().getUserId()+"' order by SuggestionTime desc";
		cursor=db.rawQuery(sql,null);
		if(cursor!=null && cursor.moveToFirst())
		  do{
			  Suggestion suggestion = new Suggestion();
			  suggestion.setSuggestion(cursor.getString(cursor.getColumnIndex("Suggestion")));
			  suggestion.setSuggestionTime(cursor.getString(cursor.getColumnIndex("SuggestionTime")));
			  suggestion.setRecId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("RecId"))));
			  suggestionList.add(suggestion);			  			  
		  }while (cursor.moveToNext());
		
		UserDAO.getInstance().setSuggestionList(suggestionList);
		db.close();
	}

	public static void getUnreadCountFromDB() {
		// TODO Auto-generated method stub
		int count=0;
		SQLiteDatabase db = openDB(DBConfig.DB_FULLNAME);
		Cursor cursor;
		String sql ="select * from SuggestionInfor where UserId='"+UserDAO.getInstance().getUser().getUserId()+"' and IfNew=1";
		cursor = db.rawQuery(sql, null);
		if(cursor!=null && cursor.moveToFirst())
		  do{
			 count++;
     		}while (cursor.moveToNext());
		
		UserDAO.getInstance().setUnreadSuggestion(count);
		db.close();
		
	}
	
	public static int getAllCountFromDB() {
		// TODO Auto-generated method stub
		int count=0;
		SQLiteDatabase db = openDB(DBConfig.DB_FULLNAME);
		Cursor cursor;
		String sql ="select * from SuggestionInfor where UserId='"+UserDAO.getInstance().getUser().getUserId()+"'";
		cursor = db.rawQuery(sql, null);
		if(cursor!=null && cursor.moveToFirst())
		  do{
			 count++;
     		}while (cursor.moveToNext());
		
		
		db.close();
		return count;
	}

	public static void setSelectedSuggestionRead(int RecId) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openDB(DBConfig.DB_FULLNAME);
		Cursor cursor=db.query("SuggestionInfor",null, "UserId='"+UserDAO.getInstance().getUser().getUserId()+"'", null, null, null, null);
		if(cursor!=null && cursor.moveToFirst()){
		  int a = 0;
		  String sql ="update SuggestionInfor set IfNew='"+a+"' where RecId='"+RecId+"'";
		  db.execSQL(sql);			
		}
		
		db.close();
	}

	/*public static void getLastSuggestionTimeFromDB() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openDB(DBConfig.DB_FULLNAME);
		Cursor cursor;
		String lastTime = null;
		String sql="select SuggestionTime from SuggestionInfor where UserId='"+UserDAO.getInstance().getUser().getUserId()+"' and SuggestionTime >= all (select SuggestionTime from SuggestionInfor where UserId='"+UserDAO.getInstance().getUser().getUserId()+"')";
	    
		cursor= db.rawQuery(sql, null);
	    if(cursor!=null && cursor.moveToFirst()){
	       lastTime = cursor.getString(cursor.getColumnIndex("SuggestionTime"));
	       String sqll ="update UserInfo set LastSuggestionTime='"+lastTime+"' where UserId='"+UserDAO.getInstance().getUser().getUserId()+"'";
	       db.execSQL(sqll);
	    }
	  
	   UserDAO.getInstance().getUser().setLastSuggestionTime(lastTime);  
	   db.close();
	}*/

	public static void updateSuggestionList(ArrayList<Suggestion> nSuggestionList) {
		// TODO Auto-generated method stub		
		SQLiteDatabase db;
		User user=UserDAO.getInstance().getUser();
		int IfNew=1;
		for(Suggestion suggestion:nSuggestionList){
		ContentValues args = new ContentValues();
		args.put(DBConfig.COL_SuggestionInfor_UserId,user.getUserId());
		args.put(DBConfig.COL_SuggestionInfor_RecId, suggestion.getRecId());
		args.put(DBConfig.COL_SuggestionInfor_Suggestion, suggestion.getSuggestion());
		args.put(DBConfig.COL_SuggestionInfor_SuggestionTime, suggestion.getSuggestionTime());
		args.put(DBConfig.COL_SuggestionInfor_IfNew, IfNew);
								
		db= openDB(DBConfig.DB_FULLNAME);
		db.insert(DBConfig.TB_SuggestionInfor, null, args);
		db.close();
		}
		
		
		
	}

	public static void updateLastTimetoDB(String lastSuggestionTime) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openDB(DBConfig.DB_FULLNAME);
		String sql = "update UserInfo set LastSuggestionTime='"+lastSuggestionTime+"' where UserId='"+UserDAO.getInstance().getUser().getUserId()+"'";
		db.execSQL(sql);
		
		db.close();
	}
	
	
}
