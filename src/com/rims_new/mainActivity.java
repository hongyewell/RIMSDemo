package com.rims_new;
import java.util.HashMap;

import org.ksoap2.serialization.SoapObject;

import com.why.SysApplication.SysApplication;
import com.why.util.ProgressDialogUtils;
import com.why.util.WebServiceUtils;
import com.why.util.WebServiceUtils.WebServiceCallBack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class mainActivity extends BaseActivity {

	private AutoCompleteTextView uname;
	EditText upsd; TextView text_phone,text_web;
	private CheckBox ckb;
	private Button btnlogin;
	private Button btnregister;
	SharedPreferences pref;
	Editor editor;
	private boolean userExist;
	private String[] res = {"gu","why","红叶"};
	
	protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
	  	    
       btnlogin=(Button) findViewById(R.id.login);
       btnregister=(Button) findViewById(R.id.register);  
      uname=(AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
      upsd=(EditText) findViewById(R.id.password_text);
       ckb=(CheckBox)findViewById(R.id.rememberPsd);
       text_phone=(TextView) findViewById(R.id.text_phone);     
       text_phone.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );//设置下划线
       text_web=(TextView) findViewById(R.id.text_web);
       text_web.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );//设置下划线
       
       ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, res);
       uname.setAdapter(adapter);
       
       
       pref=getSharedPreferences("UserInfo", MODE_PRIVATE);
       editor=pref.edit();
    	String name=pref.getString("uname", "");
	    String psd=pref.getString("upsd", "");				
	   if(name!=null){			
		uname.setText(name);
     	}
	   if(psd.equals("")){
		ckb.setChecked(false);
	   }else{
		ckb.setChecked(true);
	upsd.setText(psd);
	}
	
	   btnregister.setOnClickListener(getClickEvent_1());
	   btnlogin.setOnClickListener(getClickEvent_2());	
	   text_web.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent intent=new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("http://210.45.176.147/ZPGWEB/Default1.aspx"));
			startActivity(intent);	
		}
	});
	 
	   text_phone.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent intent=new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:18255172917"));
			startActivity(intent);			
		}
	});
		
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private View.OnClickListener getClickEvent_1(){
	   return new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(mainActivity.this, secondActivity.class);
			startActivity(intent);		
		}
	};
		
		
	}
	
	
	private View.OnClickListener getClickEvent_2(){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		if((TextUtils.isEmpty(uname.getText()))
			||(TextUtils.isEmpty(upsd.getText()))){
			Toast.makeText(mainActivity.this, "请输入用户名和密码！", Toast.LENGTH_SHORT).show();
		}else {		
			userLogin(uname.getText().toString(),upsd.getText().toString());
		}
				
			}
		};
		
	}
  public void userLogin(String name,String psd){
	  final TextView tvresult = (TextView)findViewById(R.id.tvTestResult);
	  //显示进度条
	  ProgressDialogUtils.showProgressDialog(this, "正在验证........");
	  
	  
	  //添加参数
	  HashMap<String, String> properties =new HashMap<String, String>();
	  properties.put("uname", name);
	  properties.put("upsd",psd);
	  
		//输出用户名和密码
		Log.i("Tag","userName: ["+properties.get("uname")+"]");
		Log.i("Tag","userPsd: ["+properties.get("upsd")+"]");
		
		//调用webservice验证用户
	  WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "login", properties, new WebServiceCallBack(){
		  public void callBack(SoapObject result) {
				ProgressDialogUtils.dismissProgressDialog();
				if(result != null){
					userExist= parseSoapObject(result);
					if(userExist){
						tvresult.setText("");
						Toast.makeText(mainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();					
						//保存用户名		
						pref=getSharedPreferences("userInfo", MODE_PRIVATE);
						editor=pref.edit();
						editor.putString("uname", uname.getText().toString().trim());
						//是否保存密码
						if(ckb.isChecked()){								   
							editor.putString("upsd", upsd.getText().toString().trim());
							editor.commit();
						}else {
							//保存用户名，删除已保存的密码
							editor.remove("upsd");
							editor.commit();
						}
						String data=uname.getText().toString().trim();
						Log.i("data", data);
						Intent intent = new Intent(mainActivity.this, secondActivity.class);
						intent.putExtra("extra_data", data);
						startActivity(intent);
					}else{						
						tvresult.setText("用户名或密码错误！");
					}
					
				}else{
					Toast.makeText(mainActivity.this, "连接服务器失败 ==", Toast.LENGTH_SHORT).show();
				}
			}
	  });
  }
     
       
  private boolean parseSoapObject(SoapObject result){
		boolean exist = false;
		exist= Boolean.parseBoolean(result.getProperty("loginResult").toString());
		
		Log.i("Tag",String.valueOf(exist));
		return exist;		
	}

}
   


