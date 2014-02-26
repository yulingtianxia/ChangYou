/**
 * 
 */
package com.hit.changyou;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 我的资料Activity
 * @author 飞雪无情
 * @since 2011-3-8
 */
public class MySearchListActivity extends Activity {
	public MKMapViewListener mMapListener = null;
	public MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	public static String mStrSuggestions[] = {};
	String currentcityString = "北京";
	
	BaseAdapter ba;
	ListView lv;
	List<Integer> list_int;
	List<MKPoiInfo> list_MKPoiInfo; 
	private Button but_menu;
	private Button btn1,btn2,btn3,btn4,btn5;
	View contentView;
	private PopupWindow m_popupWindow;
	MyRenren app;
	EditText editSearchKey;
	 protected void dialog() { 
	        AlertDialog.Builder builder = new Builder(MySearchListActivity.this); 
	        builder.setMessage("确定要退出吗?"); 
	        builder.setTitle("提示"); 
	        builder.setPositiveButton("确认", 
	                new android.content.DialogInterface.OnClickListener() { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        dialog.dismiss(); 
	                        MySearchListActivity.this.finish(); 
	                    } 
	                }); 
	        builder.setNegativeButton("取消", 
	                new android.content.DialogInterface.OnClickListener() { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        dialog.dismiss(); 
	                    } 
	                }); 
	        builder.create().show(); 
	    } 
	    @Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	 if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
		            dialog(); 
		            return false; 
		        } 
		        return false; 
		    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_sitelist);
		list_MKPoiInfo = new ArrayList<MKPoiInfo>();
		MapFactory.mySearchListActivity=this;		
		app = (MyRenren) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(MyRenren.strKey,
					new MyRenren.MyGeneralListener());
		}
		editSearchKey = (EditText) findViewById(R.id.suggestionkey);
		editSearchKey.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String key =new String("餐厅");
				if(editSearchKey.getText().length()>0)
					key = editSearchKey.getText().toString();
				mSearch.poiSearchInCity(currentcityString,key);
			}
		});
		mSearch = new MKSearch();
		MKSearch.setPoiPageCapacity(50);
		mSearch.init(app.mBMapManager, new MKSearchListener() {

			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
				if (error != 0) {
					Log.i("yxy", "shibai");
					Toast.makeText(MySearchListActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.i("yxy", "chenggong");
					Toast.makeText(MySearchListActivity.this, "成功，查看详情页面",
							Toast.LENGTH_SHORT).show();
				}
			}

            public void onGetPoiResult(MKPoiResult res, int type, int error) {
            	Log.i("sfy", "onGetPoiResult 被调用");
                // 错误号可参考MKEvent中的定义
                if (error != 0 || res == null) {
                    Toast.makeText(MySearchListActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }
                // 将地图移动到第一个POI中心点
                if (res.getCurrentNumPois() > 0) {
                    // 将poi结果显示到地图上
//                    MyPoiOverlay poiOverlay = new MyPoiOverlay(MySearchListActivity.this, mMapView, mSearch);
//                    poiOverlay.setData(res.getAllPoi());
//                    mMapView.getOverlays().clear();
//                    mMapView.getOverlays().add(poiOverlay);
//                    mMapView.refresh();
                    //当ePoiType为2（公交线路）或4（地铁线路）时， p
                	Log.i("city", currentcityString);
                	list_MKPoiInfo.clear();
                	 for( MKPoiInfo info : res.getAllPoi() ){
                     	if ( info.pt != null ){
                     		list_MKPoiInfo.add(info);
//                     		mMapView.getController().animateTo(info.pt);
                     	}
                     }
                	//list_MKPoiInfo = res.getAllPoi();
                	ba=new BaseAdapter_search(MySearchListActivity.this,list_MKPoiInfo);  
         	    	lv.setAdapter(ba);
         	    	Log.i("sfy", "BaseAdapte");
                	if(ba!=null)
                	ba.notifyDataSetChanged();
                   
                } else if (res.getCityListNum() > 0) {
                    String strInfo = "在";
                    for (int i = 0; i < res.getCityListNum(); i++) {
                        strInfo += res.getCityListInfo(i).city;
                        strInfo += ",";
                    }
                    strInfo += "找到结果";
                    Toast.makeText(MySearchListActivity.this, strInfo, Toast.LENGTH_LONG).show();
                }
            }

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			public void onGetAddrResult(MKAddrInfo res, int error) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				Log.i("sfy", "onGetSuggestionResult 被调用");
				if (arg1 != 0 || res == null) {
					Toast.makeText(MySearchListActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_LONG).show();
					return;
				}
				int nSize = res.getSuggestionNum();
				mStrSuggestions = new String[nSize];
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
				for (int i = 0; i < nSize; i++) {
					mStrSuggestions[i] = res.getSuggestion(i).city
							+ res.getSuggestion(i).key;
				}
				ArrayAdapter<String> suggestionString = new ArrayAdapter<String>(
						MySearchListActivity.this,
						android.R.layout.simple_list_item_1, mStrSuggestions);
				//mSuggestionList.setAdapter(suggestionString);
				//lv.setAdapter(suggestionString);
				Toast.makeText(MySearchListActivity.this, "suggestion callback",
						Toast.LENGTH_LONG).show();
				
			}

		});
//		EditText editSearchKey = (EditText) findViewById(R.id.suggestionkey);
		String key =new String("餐厅");
		/*if(editSearchKey.getText().length()>0)
			key = editSearchKey.getText().toString();*/
		//Log.i("sfy", key);
		Log.i("sfy", currentcityString+key);
		mSearch.poiSearchInCity(currentcityString,key);
		lv=(ListView)this.findViewById(R.id.ListView01);
		lv=(ListView)MySearchListActivity.this.findViewById(R.id.ListView01);	
		if(list_MKPoiInfo!=null)
		{
	    	ba=new BaseAdapter_search(MySearchListActivity.this,list_MKPoiInfo);  
	    	lv.setAdapter(ba);
		}
		else {
			Log.i("sfy", "Adaper is NULL");
		}
		
		//mSuggestionList.setAdapter(suggestionString);
		/*Intent intent=getIntent(); 
        String value_type=intent.getStringExtra("val"); 
        //int type_tag = Integer.getInteger(value_type);
        //int size = type_size[type_tag];
		lv=(ListView)this.findViewById(R.id.ListView01);	
		Toast.makeText(MySearchListActivity.this,value_type,Toast.LENGTH_SHORT).show(); 
		if(value_type == "0")
			msgIds = msgIds_shop;
		else if(value_type == "1")
			msgIds = msgIds_airport;
		else if(value_type == "2")
			msgIds = msgIds_railway;
		else if(value_type == "3")
			msgIds = msgIds_coach;
		else if(value_type == "4")
			msgIds = msgIds_hospital1;
		else
			msgIds = msgIds_shop;
		
        //为ListView设置适配器
		list_int = new ArrayList<Integer>();
		for(int i : msgIds)
		{
			list_int.add(i);
		}
		ba=new my_BaseAdapter(MySearchListActivity.this,list_int);*/
        //lv.setAdapter(ba);//为ListView设置内容适配器
        //设置选项选中的监听器
        lv.setOnItemSelectedListener
        (
           new OnItemSelectedListener()
           {
			//@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {//重写选项被选中事件的处理方法
				Intent intent = new Intent(MySearchListActivity.this, AirportActivity.class);				  
				 MySearchListActivity.this.startActivity(intent);  	
			}
			//@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
           }
        );   
        //设置选项被单击的监听器
        lv.setOnItemClickListener(
           new OnItemClickListener()
           {
			//@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {//重写选项被单击事件的处理方法
				Intent intent = new Intent(MySearchListActivity.this, AirportActivity.class);				  
				 MySearchListActivity.this.startActivity(intent);  	
			}        	   
           }
        );
//        Button mSuggestionSearch = (Button) findViewById(R.id.suggestionsearch);

//		OnClickListener clickListener1 = new OnClickListener() {
//			public void onClick(View v) {
//				
//				String key =new String("餐厅");
//				if(editSearchKey.getText().length()>0)
//					key = editSearchKey.getText().toString();
//				mSearch.poiSearchInCity(currentcityString,key);
//			}
//		};
//		mSuggestionSearch.setOnClickListener(clickListener1);
        init();
        setListener();
	}
	void getCity(String city)
	{
//		EditText editSearchKey = (EditText) findViewById(R.id.suggestionkey);
//		String key =new String("餐厅");
//		if(editSearchKey.getText().length()>0)
//			key = editSearchKey.getText().toString();
		currentcityString = city;
//		mSearch.poiSearchInCity(,key);
	}
//	void SuggestionSearchButtonProcess(View v) {
//		EditText editSearchKey = (EditText) findViewById(R.id.suggestionkey);
//		//mSearch.suggestionSearch(editSearchKey.getText().toString());
////		mMapView.getOverlays().clear();
////		mSearch.poiSearchNearBy(editSearchKey.getText().toString(), mMapView.getMapCenter(), 1000);
//
//	}
	private void init() {
		contentView = getLayoutInflater().inflate(R.layout.searchlistmenu, null,
				true);
		but_menu = (Button) findViewById(R.id.btn_sitekind_search);
		btn1= (Button) contentView.findViewById(R.id.btn_site_kind1);
		btn2= (Button) contentView.findViewById(R.id.btn_site_kind2);
		btn3= (Button) contentView.findViewById(R.id.btn_site_kind3);
		btn4= (Button) contentView.findViewById(R.id.btn_site_kind4);
		btn5= (Button) contentView.findViewById(R.id.btn_site_kind5);
		// PopupWindow弹出的窗口显示的view,第二和第三参数：分别表示此弹出窗口的大小
		m_popupWindow = new PopupWindow(contentView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		m_popupWindow.setBackgroundDrawable(new BitmapDrawable());// 有了这句才可以点击返回（撤销）按钮dismiss()popwindow
		m_popupWindow.setOutsideTouchable(true);
	}

	private void setListener() {
		contentView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				m_popupWindow.dismiss();
			}
		});
		// m_popupWindow = new PopupWindow();
		but_menu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					if (m_popupWindow.isShowing()) {

						m_popupWindow.dismiss();
					}
					m_popupWindow.showAsDropDown(v);

				} catch (Exception e) {
					Toast.makeText(MySearchListActivity.this, e.getMessage(),
							Toast.LENGTH_SHORT);
				}
			}
		});
		btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind1);
				mSearch.poiSearchInCity(currentcityString,getText(R.string.site_kind1).toString());
		        
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind2);
				mSearch.poiSearchInCity(currentcityString,getText(R.string.site_kind2).toString());
		        
			}
		});
		btn3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind3);
				mSearch.poiSearchInCity(currentcityString,getText(R.string.site_kind3).toString());
		        
			}
		});
		btn4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind4);
				mSearch.poiSearchInCity(currentcityString,getText(R.string.site_kind4).toString());
		        
			}
		});
		btn5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind5);
				mSearch.poiSearchInCity(currentcityString,getText(R.string.site_kind5).toString());
			}
		});
		
		
	}
	void SearchCity(String city)
	{
		mSearch.poiSearchInCity(city,city);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		String key =new String("餐厅");
//		if(editSearchKey.getText().length()>0)
//			key = editSearchKey.getText().toString();
//		mSearch.poiSearchInCity(currentcityString,key);
		SearchCity(app.cityString);
	}
	/*public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (m_popupWindow != null && m_popupWindow.isShowing()) {
				m_popupWindow.dismiss();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}*/
	
}
