package com.example.headdiary.data;

public class Config {

	public Config() {
		// TODO Auto-generated constructor stub
	}
	public static abstract class DBConfig {
		//DB Path
		public static final String PACKAGE_NAME = "com.example.headdiary";
	    //public static final String DB_PATH = "/data"+ Environment.getDataDirectory().getAbsolutePath() + "/"+ PACKAGE_NAME;  //在手机里存放数据库的位置*/
	    public static final String DB_PATH = "/sdcard";
	    public static final String DB_NAME = "headdiary.db"; //保存的数据库文件名
	    public static final String DB_FULLNAME = DB_PATH + "/" + DB_NAME;
	    
	    //DB TABLE & COLUMN
        public static final String TB_UserInfo = "UserInfo";
        public static final String COL_UserInfo_UserId = "UserId";
        public static final String COL_UserInfo_UserUUID = "UserUUID";
        public static final String COL_UserInfo_UserName = "UserName";
        public static final String COL_UserInfo_Password = "Password";
        public static final String COL_UserInfo_RemPswd = "RemPswd";
        public static final String COL_UserInfo_Phone = "Phone";
        public static final String COL_UserInfo_Sex = "Sex";
        public static final String COL_UserInfo_BirthYear = "BirthYear";
        public static final String COL_UserInfo_DoctorId = "DoctorId";
        public static final String COL_UserInfo_DoctorName = "DoctorName";
        public static final String COL_UserInfo_AutoUpload = "AutoUpload";
        public static final String COL_UserInfo_HDVersion = "HDVersion";
        public static final String COL_UserInfo_HDNeedUpload = "HDNeedUpload";
        public static final String COL_UserInfo_HDNeedDelete = "HDNeedDelete";
        
        public static final String TB_LoginInfo = "LoginInfo";
        public static final String COL_LoginInfo_UserId = "UserId";
        public static final String COL_LoginInfo_LoginTime = "LoginTime";
        public static final String COL_LoginInfo_LogoutTime = "LogoutTime";

        
        public static final String TB_HeadacheDiary = "HeadacheDiary";
        public static final String COL_HDiary_UserId = "UserId";
        public static final String COL_HDiary_RecId = "RecId";
        public static final String COL_HDiary_RecordTime = "RecordTime";
        public static final String COL_HDiary_StartTime = "StartTime";
        public static final String COL_HDiary_EndTime = "EndTime";
        public static final String COL_HDiary_Position = "Position";
        public static final String COL_HDiary_IfAroundEye = "IfAroundEye";
        public static final String COL_HDiary_Type = "Type";
        public static final String COL_HDiary_TypeComment = "TypeComment";
        public static final String COL_HDiary_Degree = "Degree";
        public static final String COL_HDiary_IfActivityAggravate = "IfActivityAggravate";
        public static final String COL_HDiary_Prodrome = "Prodrome";
        public static final String COL_HDiary_Companion = "Companion";
        public static final String COL_HDiary_Precipiating = "Precipiating";
        public static final String COL_HDiary_PrecipiatingComment = "PrecipiatingComment";
        public static final String COL_HDiary_Mitigating = "Mitigating";
        public static final String COL_HDiary_MitigatingComment = "MitigatingComment";
        public static final String COL_HDiary_DrugList = "DrugList";
        public static final String COL_HDiary_AidDiagnosis = "AidDiagnosis";
        
    }
	
	public static abstract class MsgConfig {
	    public static final String KEY_RESULT = "KEY_RESULT";
	    public static final int MSG_SYNCHRONIZE_FINISH = 0;
	    
	    public static final String NO_USER = "No User"; 
	    public static final String PASSWORD_ERROR = "Password Error";
	    
	    public static final String USER_EXISTS = "User Exists";
	    public static final String USERNAME_ERROR = "UserName Error";
	    public static final String NO_PHONE_MATCH = "No Phone Match";
	    
	    public static final String NO_DOCTOR_MATCH = "No Doctor Match";
	    public static final String NO_DATA = "No Data";
	    
	    
	}
}
