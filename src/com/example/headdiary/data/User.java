/**
 * 
 */
package com.example.headdiary.data;

import java.util.ArrayList;
import java.util.UUID;

import android.R.integer;

/**
 * POJO
 * @author Jane Huang
 *
 */
public class User {
	private int UserId,HDVersion,DoctorId;
	private String UserUUID;
	private ArrayList<Integer> HDNeedUpload,HDNeedDelete;
	private int RemPswd,BirthYear,AutoUpload;
	private String UserName,Password,Phone,Sex,DoctorName;
	
	public User(){
		clearData();
	}
	
	private void clearData(){
		UserId=DoctorId=-1;
		HDVersion=0;  //本地版本从0开始，网络版本从1开始
		RemPswd=AutoUpload=1;
		HDNeedUpload=new ArrayList<Integer>();
		HDNeedDelete=new ArrayList<Integer>();
	}
	
	public User(int remPswd, String password, String phone) {
		super();
		clearData();
		RemPswd = remPswd;
		Password = password;
		Phone = phone;
	}

	
	public User(String userName,String phone, String password) {
		super();
		clearData();
		UserName = userName;
		Password = password;
		Phone = phone;
	}

	public User(int userId, int doctorId,int hDVersion, String userUUID,
			ArrayList<Integer> hDNeedUpload, ArrayList<Integer> hDNeedDelete,
			int remPswd, int birthYear, String userName, String password,
			String phone, String sex, String doctorName,int autoUpload) {
		super();
		clearData();
		UserId = userId;
		HDVersion = hDVersion;
		UserUUID = userUUID;
		HDNeedUpload = hDNeedUpload;
		HDNeedDelete = hDNeedDelete;
		RemPswd = remPswd;
		BirthYear = birthYear;
		UserName = userName;
		Password = password;
		Phone = phone;
		Sex = sex;
		DoctorId=doctorId;
		DoctorName=doctorName;
		AutoUpload=autoUpload;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public int getHDVersion() {
		return HDVersion;
	}

	public void setHDVersion(int hDVersion) {
		HDVersion = hDVersion;
	}

	public String getUserUUID() {
		return UserUUID;
	}

	public void setUserUUID(String userUUID) {
		UserUUID = userUUID;
	}

	public ArrayList<Integer> getHDNeedUpload() {
		return HDNeedUpload;
	}

	public void setHDNeedUpload(ArrayList<Integer> hDNeedUpload) {
		HDNeedUpload = hDNeedUpload;
	}
	
	public void addToHDNeedUpload(Integer recId){
		if (HDNeedUpload==null)
			HDNeedUpload=new ArrayList<Integer>();
		if (!HDNeedUpload.contains(recId))
			HDNeedUpload.add(recId);
	}

	public ArrayList<Integer> getHDNeedDelete() {
		return HDNeedDelete;
	}

	public void setHDNeedDelete(ArrayList<Integer> hDNeedDelete) {
		HDNeedDelete = hDNeedDelete;
	}
	
	public void addToHDNeedDelete(Integer recId){
		if (HDNeedDelete==null)
			HDNeedDelete=new ArrayList<Integer>();
		if (!HDNeedDelete.contains(recId))
			HDNeedDelete.add(recId);
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public int getRemPswd() {
		return RemPswd;
	}

	public void setRemPswd(int remPswd) {
		RemPswd = remPswd;
	}

	public int getBirthYear() {
		return BirthYear;
	}

	public void setBirthYear(int birthYear) {
		BirthYear = birthYear;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public int getDoctorId() {
		return DoctorId;
	}

	public void setDoctorId(int doctorId) {
		DoctorId = doctorId;
	}

	public String getDoctorName() {
		return DoctorName;
	}

	public void setDoctorName(String doctorName) {
		DoctorName = doctorName;
	}

	public int getAutoUpload() {
		return AutoUpload;
	}

	public void setAutoUpload(int autoUpload) {
		AutoUpload = autoUpload;
	}

	//-------------------方法-------------------------------//
	public Boolean ifHasUUID(){
		return UserUUID!=null && !UserUUID.equals("");
	}
	
	public Boolean ifNeedSynchronize(){
		if ((HDNeedUpload!=null && HDNeedUpload.size()>0)||(HDNeedDelete!=null && HDNeedDelete.size()>0))
			return true;
		return false;
		
	}
	
	
}
