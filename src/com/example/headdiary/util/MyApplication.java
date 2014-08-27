package com.example.headdiary.util;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Context CONTEXT = null;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
        
    }
}
