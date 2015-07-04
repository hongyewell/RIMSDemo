package com.rims_new;

import com.why.SysApplication.SysApplication;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("BaseActivity", getClass().getSimpleName());
	    SysApplication.getInstance().addActivity(this);//�ڻ�����������Activity
	}
        @Override 
	   protected void onDestroy(){
        super.onDestroy();
        SysApplication.getInstance().removeActivity(this);//�ڻ���������Ƴ��������ٵ�Activity
         }
}
