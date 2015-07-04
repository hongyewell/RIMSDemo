package com.rims_new;

import java.util.HashMap;

import org.ksoap2.serialization.SoapObject;

import com.why.SysApplication.SysApplication;
import com.why.util.WebServiceUtils;
import com.why.util.WebServiceUtils.WebServiceCallBack;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Modifypeople extends BaseActivity {
	private EditText  Name, Sex ,Birth ,Certificated ,Address ,MZ ,IDtype,h_hold,h_number;
	private TextView MemberID;
	private  Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_people);
		
		Bundle bundle=this.getIntent().getExtras();
		String nID=bundle.getString("nID");
		
		MemberID=(TextView) findViewById(R.id.edt_MemberID);
		Name=(EditText) findViewById(R.id.edt_Name);
		Sex=(EditText) findViewById(R.id.edt_Sex);
		Birth=(EditText) findViewById(R.id.edt_Birth);
		Certificated=(EditText) findViewById(R.id.edt_Certificated);
		Address=(EditText) findViewById(R.id.edt_Address);
		MZ=(EditText) findViewById(R.id.edt_MZ);
		IDtype=(EditText) findViewById(R.id.edt_IDtype);   
		h_hold=(EditText) findViewById(R.id.edt_h_hold);
		h_number=(EditText) findViewById(R.id.edt_h_number);
		submit=(Button) findViewById(R.id.submit);
		
		init(nID);
	}
	private void init(String MID) {
		//添加参数
				HashMap<String, String> properties=new HashMap<String, String>();
				properties.put("nID",MID);
				
			 WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "selectPeopleByID", properties, new WebServiceCallBack() {
				
				@Override
				public void callBack(SoapObject result) {
					if(result!=null){
						SoapObject so=(SoapObject )result
								.getProperty("selectPeopleByIDResult");
						parseSoapObject(so);
					}else {
						Toast.makeText(Modifypeople.this, "啊哦.....出错了", Toast.LENGTH_SHORT)
						.show();
					}			
				}
			});	
		
	}
	protected void parseSoapObject(SoapObject soapObject) {
		
		for(int i=0;i<soapObject.getPropertyCount();i+=11){
			MemberID.setText(soapObject.getProperty(i).toString());//MemberID
			Name.setText(soapObject.getProperty(i+1).toString());//Name
			Sex.setText(soapObject.getProperty(i+2).toString()); //Sex
			Birth.setText(soapObject.getProperty(i+3).toString()); //Birth
			Certificated.setText(soapObject.getProperty(i+4).toString()); //Certificated
			Address.setText(soapObject.getProperty(i+5).toString()); //Address
			MZ.setText(soapObject.getProperty(i+6).toString()); //MZ
			IDtype.setText(soapObject.getProperty(i+7).toString()); //IDtype
			h_number.setText(soapObject.getProperty(i+9).toString());//h_hold
			h_hold.setText(soapObject.getProperty(i+10).toString());//h_number		
		}		
	
	
	submit.setOnClickListener(new View.OnClickListener(){
		
		@Override
		public void onClick(View arg0) {	
			Log.i("why", "响应");
			if ((TextUtils.isEmpty(Name.getText()))
					|| (TextUtils.isEmpty(Address.getText()))
					|| (TextUtils.isEmpty(Birth.getText()))
					|| (TextUtils.isEmpty(Certificated.getText()))
					|| TextUtils.isEmpty(IDtype.getText())) 
			{
				Toast.makeText(Modifypeople.this, "请输入人口信息~~~",
						Toast.LENGTH_SHORT).show();
			} 
			else {
				UpdateRemark(
						Name.getText().toString(),
						Address.getText().toString(), 
						Birth.getText().toString(),
						Certificated.getText().toString(),
						IDtype.getText().toString(), 
						MZ.getText().toString(),
						Sex.getText().toString(),
				        MemberID.getText().toString(),
				        h_hold.getText().toString(),
				        h_number.getText().toString()
				        );
				      
						}				
		}
	});
	}

		private void UpdateRemark(String string, String string2,
				String string3, String string4, String string5, String string6,
				String string7,String string8 ,String string9,String string10) {
			//添加参数
			HashMap<String, String> properties=new HashMap<String, String>();
			properties.put("name", string);
			properties.put("address", string2);
			properties.put("birth", string3);
			properties.put("certificated", string4);
			properties.put("idtype", string5);
			properties.put("mz", string6);
			properties.put("sex", string7);
			properties.put("nID", string8);
			properties.put("hhold", string9);
			properties.put("hid", string10);
			WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL,
					"UpdateRemark", properties, new WebServiceCallBack() {

						@Override
						public void callBack(SoapObject result) {
							if (result != null) {
								boolean ready = parseSoapObject2(result);
								if (ready) {
									Toast.makeText(Modifypeople.this, "修改成功~~~",
											Toast.LENGTH_SHORT).show();
									
									Intent intent =new Intent();
									intent.setClass(Modifypeople.this, secondActivity.class);
									startActivity(intent);
									
								} else {
									Toast.makeText(Modifypeople.this, "程序开小差了==",
											Toast.LENGTH_SHORT).show();
									}
			}
	
            }	
			});
		}
		
		private boolean parseSoapObject2(SoapObject result) {
			boolean exist = false;
			exist = Boolean.parseBoolean(result.getProperty("UpdateRemarkResult")
					.toString());
			return exist;
		}
		
}
    