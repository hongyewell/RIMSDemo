package com.why.SysApplication;

import java.util.LinkedList; 
import java.util.List; 
import android.app.Activity; 
import android.app.AlertDialog; 
import android.app.Application; 
import android.content.DialogInterface; 
import android.content.Intent; 
 
public class SysApplication extends Application { 
    private List<Activity> mList = new LinkedList<Activity>(); 
    private static SysApplication instance; 
 
    private SysApplication() {   
    } 
    public synchronized static SysApplication getInstance() { 
        if (null == instance) { 
            instance = new SysApplication(); 
        } 
        return instance; 
    } 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
    
    //移除即将销毁的活动
    public void removeActivity(Activity activity){
    	mList.remove(activity);
    }
   //退出程序
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
  
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
}
