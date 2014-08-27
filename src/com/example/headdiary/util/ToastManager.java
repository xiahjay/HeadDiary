package com.example.headdiary.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class ToastManager {
	private static android.widget.Toast mToast = null;
	public ToastManager() {
		// TODO Auto-generated constructor stub
	}

	 /** ��֤��UI�߳�����ʾToast */
    private static Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            if (mToast != null) {
                mToast.cancel();
            }
            String text = (String) msg.obj;
            int duration = msg.arg2;
            mToast = Toast.makeText(MyApplication.CONTEXT, text, duration);
            mToast.show();
        }
    };
    
    public static void showShortToast(String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(int textResId) {
        showToast(textResId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(String text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    public static void showLongToast(int textResId) {
        showToast(textResId, Toast.LENGTH_LONG);
    }

    public static void showToast(int textResId, int duration) {
        showToast(MyApplication.CONTEXT.getString(textResId), duration);
    }

    public static void showToast(String text, int duration) {
        mHandler.sendMessage(mHandler.obtainMessage(0, 0, duration, text));
    }
    
    //---------------------�����TOAST--------------------------//
    public static void showNoWeb(){
    	showToast("�ֻ���δ��������", Toast.LENGTH_SHORT);
    }
    
    public static void showCallWebServiceError(){
    	showToast("��Ǹ�����ӷ�����ʧ�ܡ������Ƿ�������æ�����Ժ����ԡ�", Toast.LENGTH_SHORT);
    }

    
}