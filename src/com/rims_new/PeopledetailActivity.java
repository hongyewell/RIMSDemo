package com.rims_new;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ksoap2.serialization.SoapObject;
import com.why.SysApplication.SysApplication;
import com.why.util.WebServiceUtils;
import com.why.util.WebServiceUtils.WebServiceCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PeopledetailActivity extends BaseActivity {
	
    private List<String> teplist;
	private TextView MemberID, Name, Sex ,Birth ,Certificated ,Address ,MZ ,IDtype,Date,H_number,H_hold;	
	private String ID="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_detail);
	
	       Intent intent=getIntent();
	      ID=intent.getStringExtra("nID");
		
		MemberID=(TextView) findViewById(R.id.text_MemberID);
		Name=(TextView) findViewById(R.id.text_Name);
		Sex=(TextView) findViewById(R.id.text_Sex);
		Birth=(TextView) findViewById(R.id.text_Birth);
		Certificated=(TextView) findViewById(R.id.text_Certificated);
		Address=(TextView) findViewById(R.id.text_Address);
		MZ=(TextView) findViewById(R.id.text_MZ);
		IDtype=(TextView) findViewById(R.id.text_IDtype);
		Date = (TextView) findViewById(R.id.text_Date);	
		H_number=(TextView) findViewById(R.id.text_Number);
		H_hold=(TextView) findViewById(R.id.text_Hold);
		
		init(ID);
	}
	
	public void  init(String MID){
	//添加参数
		HashMap<String, String> properties=new HashMap<String, String>();
		properties.put("nID", MID);
		
	 WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "selectPeopleByID", properties, new WebServiceCallBack() {
		
		@Override
		public void callBack(SoapObject result) {
			if(result!=null){
				SoapObject so=(SoapObject )result
						.getProperty("selectPeopleByIDResult");
				parseSoapObject(so);
			}else {
				Toast.makeText(PeopledetailActivity.this, "啊哦.....出错了", Toast.LENGTH_SHORT)
				.show();
			}			
		}
	});	
	 
	 //点击修改按钮
	 findViewById(R.id.modify).setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {	
		Intent intent=new Intent();
		intent.setClass(PeopledetailActivity.this, Modifypeople.class);
		Bundle bundle=new Bundle();	
		Log.i("data",ID);
		bundle.putString("nID",ID);
		intent.putExtras(bundle);
		startActivity(intent);		
		}
	});
		 
	
  }

	protected void parseSoapObject(SoapObject soapObject) {
		teplist = new ArrayList<String>();
		for(int i=0;i<soapObject.getPropertyCount();i+=11){
			teplist.add(soapObject.getProperty(i).toString());//MemberID
			teplist.add(soapObject.getProperty(i+1).toString());//Name
			teplist.add(soapObject.getProperty(i+2).toString()); //Sex
			teplist.add(soapObject.getProperty(i+3).toString()); //Birth
			teplist.add(soapObject.getProperty(i+4).toString()); //Certificated
			teplist.add(soapObject.getProperty(i+5).toString()); //Address
			teplist.add(soapObject.getProperty(i+6).toString()); //MZ
			teplist.add(soapObject.getProperty(i+7).toString()); //IDtype
			teplist.add(soapObject.getProperty(i+8).toString());//Date
			teplist.add(soapObject.getProperty(i+9).toString());//H_number
			teplist.add(soapObject.getProperty(i+10).toString());//H_hold
		}
		
		for(int i=0; i<teplist.size();i+=11){
			MemberID.setText(teplist.get(i));
			Name.setText(teplist.get(i+1));
			Sex.setText(teplist.get(i+2)); 
			Birth.setText(teplist.get(i+3)); 
			Certificated.setText(teplist.get(i+4)); 
			Address.setText(teplist.get(i+5)); 
			MZ.setText(teplist.get(i+6)); 
			IDtype.setText(teplist.get(i+7));
			Date.setText(teplist.get(i+8));
			H_number.setText(teplist.get(i+9));
			H_hold.setText(teplist.get(i+10));
			
			
		}		
		
	}
	
	}
	

