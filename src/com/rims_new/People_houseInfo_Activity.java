package com.rims_new;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.why.util.ProgressDialogUtils;
import com.why.util.WebServiceUtils;
import com.why.util.WebServiceUtils.WebServiceCallBack;

import android.R.string;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class People_houseInfo_Activity extends BaseActivity {
	private ListView lv;
	private String ID="";
	List<String> list =new ArrayList<String>();
	private List<Map<String, Object>> nList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_house_info);
		Bundle bundle=this.getIntent().getExtras();
		ID=bundle.getString("nID");		
		init(ID);
	}

	
	/**
	 * 初始化ListView
	 */
	private void init(String MID) {
		HashMap<String,String>properties=new HashMap<String, String>();
		properties.put("nID", MID);
		lv=(ListView) findViewById(R.id.listView2);
		ProgressDialogUtils.showProgressDialog(this, "正在努力加载中....");
	    WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "selectAllhouseInfor", properties, new WebServiceCallBack() {
			
			@Override
			public void callBack(SoapObject result) {
				ProgressDialogUtils.dismissProgressDialog();
				if(result!=null){
					list=parseSoapObeject(result);
					setListView(lv);										
				}else{
					Toast.makeText(People_houseInfo_Activity.this, "程序开小差了....", Toast.LENGTH_SHORT).show();
				}				
			}				
		});	
	    	 	       	    
	}
		
	
	/**
	 * 解析数据
	 * @param result
	 * @return
	 */
	private List<String> parseSoapObeject(SoapObject result) {
		List<String> list = new ArrayList<String>();
		SoapObject provinceSoapObject = (SoapObject) result
				.getProperty("selectAllhouseInforResult");
		if (provinceSoapObject == null) {
			return null;
		}
		for (int i = 0; i < provinceSoapObject.getPropertyCount(); i++) {
			list.add(provinceSoapObject.getProperty(i).toString());
		}
		return list;
	}
	
	
	/**
	 * setListView
	 * @param lv
	 */
	protected void setListView(ListView lv) {
		nList = new ArrayList<Map<String, Object>>();
		nList.clear();
		Map<String, Object> map;
		for (int i = 0; i < list.size(); i += 4) {
			map = new HashMap<String, Object>();
			map.put("nID", list.get(i));
			map.put("name", list.get(i + 1));
			map.put("sex", list.get(i + 2));
			map.put("birth", list.get(i + 3));
			nList.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(
				People_houseInfo_Activity.this, nList, R.layout.listviewrow,
				new String[] { "nID", "name", "sex", "birth" }, new int[] {
						R.id.peopleNumber, R.id.peopleName, R.id.peopleSex,
						R.id.peopleBirth });
		lv.setAdapter(adapter);
		
		//点击跳转到详情界面
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Intent intent=new Intent(People_houseInfo_Activity.this,PeopledetailActivity.class);
				intent.putExtra("nID", nList.get(position).get("nID").toString());
				startActivity(intent);
			}
		});

	}
	
}
