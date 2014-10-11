package com.example.headdiary.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class WebServiceManager {
	//public static final String URL="http://115.28.42.235/HeadacheWebsite/Service1.svc";
	public static final String URL="http://115.28.42.235/HeadacheDiaryWCF_Suggestion/Service1.svc";
	//public static final String URL="http://115.28.42.235/HeadacheDiaryWCF/Service1.svc"; 
	//public static final String URL="http://115.28.42.235/APPBigGame/Service1.svc";  //APP����
	public static final String NAMESPACE = "http://tempuri.org/";   
    public static final String SOAP_ACTION_PART1="http://tempuri.org/IService1/";
	private static Boolean debug=true;
	private static final String TAG="WebServiceManager-";
	public static final String KEY_PROPERTY_NAME="PropertyName";
	public static final String KEY_PROPERTY_VALUE="PropertyValue";
	private static ArrayList<HashMap<String, Object>>properties=new ArrayList<HashMap<String,Object>>();
	public WebServiceManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static void clearProperties() {
		properties.clear();
	}

	/**
	 * ���Web Service�Ĵ������
	 * @param key ������
	 * @param value ����ֵ
	 */
	public static void addProperties(String key, Object value) {
		HashMap<String, Object> map;
		map=new HashMap<String, Object>();
		map.put(KEY_PROPERTY_NAME, key);
		map.put(KEY_PROPERTY_VALUE, value);
		properties.add(map);
	}


	public static String callWebServiceForString(String methodName){
		String SOAP_ACTION=SOAP_ACTION_PART1+methodName;
		SoapObject rpc = new SoapObject(NAMESPACE, methodName);  
		for (HashMap<String, Object>map:properties){
			rpc.addProperty((String) map.get(KEY_PROPERTY_NAME), map.get(KEY_PROPERTY_VALUE));  
		}
        
		// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾  
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
        // �����Ƿ���õ���dotNet������WebService  
        envelope.dotNet = true; 
        envelope.bodyOut = rpc;  
        envelope.setOutputSoapObject(rpc);  
        HttpTransportSE transport = new HttpTransportSE(URL); 
        
        //����Web Service
        try {
        	transport.call(SOAP_ACTION, envelope);
        	if (debug) Log.i(TAG+methodName, "Call Successful!");
        	
        	if (envelope.getResponse()!=null){
				SoapObject object = (SoapObject) envelope.bodyIn;
				String result=object.getProperty(0).toString();
				
				Log.i(TAG+methodName, "result="+result);		    	
	    		return result;
	    	}
        	
        } catch (IOException e) {
        	if (debug) Log.e(TAG+methodName, "IOException");
        	e.printStackTrace();
        } catch (XmlPullParserException e) {
        	if (debug) Log.e(TAG+methodName, "XmlPullParserException");
        	e.printStackTrace();
        }
        return null;
	}

}
