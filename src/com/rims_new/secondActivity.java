package com.rims_new;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import javax.security.auth.PrivateCredentialPermission;

import org.ksoap2.serialization.SoapObject;

import com.rims_new.ChangeColorIconWithText;
import com.rims_new.R;
import com.rims_new.R.style;
import com.why.SysApplication.SysApplication;
import com.why.util.ProgressDialogUtils;
import com.why.util.WebServiceUtils;
import com.why.util.WebServiceUtils.WebServiceCallBack;

import slidingmenu.SlidingMenu;
import android.R.array;
import android.R.bool;
import android.R.menu;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRouter.VolumeCallback;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class secondActivity extends BaseActivity implements OnClickListener {

	private ListView ListView1;
	private SlidingMenu mLeftMenu;
	List<String> list = new ArrayList<String>();
	private List<Map<String, Object>> nList;
	private TextView text_yonghuTextView;
	private Button btn_exit;
	private String ID="";
	private String sex="";
	private String h_hold="是";
	private String h_number="0003";
	private List<ChangeColorIconWithText> mTabIndicators=new ArrayList<ChangeColorIconWithText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.second);
		setOverflowButtonAlways();
		getActionBar().setDisplayShowHomeEnabled(false);	
		initView();
		init();
	}
	
	private void initView()
	{
		//mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
	    four.setOnClickListener(this);

		one.setIconAlpha(1.0f);

	}
	
	//创建菜单选项
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.main, menu);
		return true;		
	}
	private void setOverflowButtonAlways()
	{
		try
		{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
     //设置menu显示icon
      @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
	    if(featureId==Window.FEATURE_ACTION_BAR&& menu!=null){
		    if(menu.getClass().getSimpleName().equals("MenuBuilder")){
			try {
				Method m=menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
				m.setAccessible(true);
				m.invoke(menu, true);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	return super.onMenuOpened(featureId, menu);
}
	//添加菜单选项功能
  public boolean  onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.action_addpeople:	
    		showDiaLog1();   		
    	 default:
    	}
		return true;
	}
	private void init() {
	
		ListView1 = (ListView) findViewById(R.id.listView1);
		mLeftMenu = (SlidingMenu) findViewById(R.id.id_toggleMenu);
		text_yonghuTextView=(TextView) findViewById(R.id.text_yonghu);
		btn_exit=(Button) findViewById(R.id.btn_exit);
		//传递参数
	    Intent intent=getIntent();
		String data=intent.getStringExtra("extra_data");
		text_yonghuTextView.setText(data+",RIMS欢迎你~");
		
		//退出程序
		btn_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//弹出确认退出对话框
				AlertDialog.Builder dialog=new AlertDialog.Builder(secondActivity.this);
				dialog.setTitle("退出程序");
				dialog.setMessage("亲，真的要退出么？");				
				dialog.setIcon(R.drawable.delete);
				dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Toast.makeText(secondActivity.this, "您退出了该程序", Toast.LENGTH_SHORT).show();
			    		SysApplication.getInstance().exit();  	
					}
				});
				dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
					}
				});			 
		         dialog.show();				
			}
		});
		
		ProgressDialogUtils.showProgressDialog(this, "正在努力加载中喔......");

		// 通过工具类调用webservice接口
		WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL,
				"selectAllCargoInfor", null, new WebServiceCallBack() {
					// webservice接口返回的数据回调到这个方法中
					@Override
					public void callBack(SoapObject result) {
						// 关闭进度条
						ProgressDialogUtils.dismissProgressDialog();
						if (result != null) {
							list = parseSoapObject(result);
							setListView(ListView1);
						} else {
							// 吐司打印
							Toast.makeText(secondActivity.this,
									"啊哦....程序出错了....", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

		
		// 注册上下文菜单
		this.registerForContextMenu(ListView1);		 
		
	}
	//重写onCreatContextMenu方法
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//设置Menu显示的内容
		menu.setHeaderTitle("文件操作");
		menu.setHeaderIcon(R.drawable.delete);
		menu.add(1, 1, 1,"删除");
		menu.add(1, 2, 1,"复制");
		menu.add(1, 3, 1,"粘贴");
		menu.add(1, 4, 1,"剪切");

	}

    //重写onContextItemSelected方法
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		ID=String.valueOf(menuInfo.position);
	switch (item.getItemId()) {
		case 1:			
			showDialogDelete();
			break;	
		case 2:
			Map<String, Object> temp=nList.get(Integer.valueOf(ID));
			Toast.makeText(secondActivity.this, temp.get("nID")+"被复制了",Toast.LENGTH_SHORT).show();
			break;	
		case 3:
			Toast.makeText(secondActivity.this, "被粘贴了",Toast.LENGTH_SHORT).show();
			break;	
		case 4:
			Toast.makeText(secondActivity.this, "被剪切了",Toast.LENGTH_SHORT).show();
			break;	
		}
		return super.onContextItemSelected(item);
	}
	//显示删除对话框
	private void showDialogDelete(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("删除对话框？");
		builder.setIcon(R.drawable.delete);
		builder.setMessage("确定要删除这条人口信息么？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {		
				Map<String, Object> temp=nList.get(Integer.valueOf(ID));
				deletepeople(temp.get("nID").toString());
				
			}
		});
	   builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {   
			
		}
	});	
	   AlertDialog dialog=builder.create();
		dialog.show();
	}
	//删除一条人口信息
	private void deletepeople(String  MID) {
		HashMap<String, String>properties=new HashMap<String, String>();
		properties.put("nID", MID);
		WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "DeleteRemark", properties, new WebServiceCallBack() {
			
			@Override
			public void callBack(SoapObject result) {
				boolean ready = parseSoapObject3(result);
			if(ready){
				Toast.makeText(secondActivity.this, "已成功删除",Toast.LENGTH_SHORT).show();
				restoreListView();
				
			}else{
				Toast.makeText(secondActivity.this, "删除出现错误", Toast.LENGTH_SHORT).show();
			}
				
			}
		});
		
	}
	//解析
	private boolean parseSoapObject3(SoapObject result) {
		boolean exist = false;
		exist = Boolean.parseBoolean(result.getProperty("DeleteRemarkResult")
				.toString());
		return exist;
	}
	// 显示添加对话框
	private void showDiaLog1() {

		LayoutInflater inflater = LayoutInflater.from(this);
		final View view = inflater.inflate(R.layout.addpeople, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		dialog.show();
		view.findViewById(R.id.cancelbtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});

		final EditText edtName, edtBirth, edtAddress, edtCertificated, edtIDtype, edtMz,edtNumber,edtHold;		
		edtName = (EditText) view.findViewById(R.id.edt_name);
		edtBirth = (EditText) view.findViewById(R.id.edt_birth);
		edtAddress = (EditText) view.findViewById(R.id.edt_address);
		edtCertificated = (EditText) view.findViewById(R.id.edt_certificated);
		edtIDtype = (EditText) view.findViewById(R.id.edt_idtype);
		edtMz = (EditText) view.findViewById(R.id.edt_mz);
		edtNumber=(EditText) findViewById(R.id.edt_number);
		edtHold=(EditText) findViewById(R.id.edt_hold);
	
		final RadioGroup group = (RadioGroup)view.findViewById(R.id.radioGroup_sex_id);
		//final RadioButton  boy=
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				//获取变更后的选中项的ID
		       int radioButtonId = arg0.getCheckedRadioButtonId();
		        //根据ID获取RadioButton的实例
              RadioButton rb = (RadioButton)view.findViewById(radioButtonId);
			  //更新参数，以符合选中项
				sex= (String) rb.getText();
				Log.i("log", sex);
				}							
		});

        //提交添加的人口信息
		view.findViewById(R.id.submitbtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if ((TextUtils.isEmpty(edtName.getText()))
								|| (TextUtils.isEmpty(edtAddress.getText()))
								|| (TextUtils.isEmpty(edtBirth.getText()))
								|| (TextUtils.isEmpty(edtCertificated.getText()))
								|| TextUtils.isEmpty(edtIDtype.getText())) {
							Toast.makeText(secondActivity.this, "请输入人口信息~~~",
									Toast.LENGTH_SHORT).show();
						} else {
							submitRemark(
									edtName.getText().toString(),
									edtAddress.getText().toString(), 
									edtBirth.getText().toString(),
									edtCertificated.getText().toString(),
									edtIDtype.getText().toString(), 
									edtMz.getText().toString(),
								     h_hold.toString(),
									//edtHold.getText().toString(),
									//edtNumber.getText().toString(),
								      h_number.toString(),
									sex.toString()							
									);		
							dialog.dismiss();						
							restoreListView();
						}

					}
				});
	}
   
	  //添加人口后刷新人口信息列表
	  private void restoreListView() {
		
		// 通过工具类调用webservice接口
				WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL,
						"selectAllCargoInfor", null, new WebServiceCallBack() {
							// webservice接口返回的数据回调到这个方法中
							@Override
							public void callBack(SoapObject result) {
								// 关闭进度条
								ProgressDialogUtils.dismissProgressDialog();
								if (result != null) {
									list = parseSoapObject(result);
									setListView(ListView1);
								}
								else {
									// 吐司打印
									Toast.makeText(secondActivity.this,
											"啊哦....程序出错了....", Toast.LENGTH_SHORT)
											.show();
									}

							}
				});					
				}

	private void submitRemark(String s1, String s2, String s3, String s4,
			String s5, String s6, String s7,String s8,String s9) {
		// 添加参数
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("name",s1);
		properties.put("address",s2);
		properties.put("birth", s3);
		properties.put("certificated",s4);
		properties.put("idtype",s5);
		properties.put("mz",s6);
		properties.put("hhold",s7);
		properties.put("hid",s8);
		properties.put("sex",s9);	
		
		Log.i("why", "hhhh");
		WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL,
				"SubmitRemark", properties, new WebServiceCallBack() {

					@Override
					public void callBack(SoapObject result) {
						if (result != null) {
							boolean ready = parseSoapObject2(result);
							if (ready) {
								Toast.makeText(secondActivity.this, "添加成功~~~",
										Toast.LENGTH_SHORT).show();
								
							} else {
								Toast.makeText(secondActivity.this, "程序开小差了==",
										Toast.LENGTH_SHORT).show();
							}
						}

					}
				});
	}

	private boolean parseSoapObject2(SoapObject result) {
		boolean exist = false;
		exist = Boolean.parseBoolean(result.getProperty("SubmitRemarkResult")
				.toString());
		return exist;
	}

	public void toggleMenu(View view) {
		mLeftMenu.toggle();
	}

	/**
	 * 解析SoapObject对象
	 * 
	 * @param result
	 * @return
	 */
	private List<String> parseSoapObject(SoapObject result) {
		List<String> list = new ArrayList<String>();
		SoapObject provinceSoapObject = (SoapObject) result
				.getProperty("selectAllCargoInforResult");
		if (provinceSoapObject == null) {
			return null;
		}
		for (int i = 0; i < provinceSoapObject.getPropertyCount(); i++) {
			list.add(provinceSoapObject.getProperty(i).toString());
		}
		return list;
	}

	public void setListView(ListView lv) {
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
		
		
		SimpleAdapter adapter = new SimpleAdapter(secondActivity.this, nList,
				R.layout.listviewrow, new String[] { "nID", "name", "sex",
						"birth" }, new int[] { R.id.peopleNumber,
						R.id.peopleName, R.id.peopleSex, R.id.peopleBirth });
		lv.setAdapter(adapter);
        
		//点击跳转到人口详情页面
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {		
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setClass(secondActivity.this, People_houseInfo_Activity.class);
				bundle.putString("nID", nList.get(position).get("nID")
						.toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
	}
   
	/*
	 * 接口实现 底部tab的点击事件
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		resetOtherTabs();

		switch (v.getId())
		{
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			//mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
		//	mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			//mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
		//	mViewPager.setCurrentItem(3, false);
			break;
		}
	}

	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicators.size(); i++)
		{
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}

}
