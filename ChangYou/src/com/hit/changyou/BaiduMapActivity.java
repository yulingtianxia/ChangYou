package com.hit.changyou;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
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
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class BaiduMapActivity extends Activity {
	static MapView mMapView = null;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private MapController mMapController = null;
	public MKMapViewListener mMapListener = null;
	public static Activity instance;
	public MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	public static String mStrSuggestions[] = {};
	//Button mBtnSearch = null; // 搜索按钮
	Button mBtnDetailSearch = null; // 详细搜搜按钮
	Button mSuggestionSearch = null; // suggestion搜索
//	Button nextData = null;
	public int load_Index;
	//ListView mSuggestionList = null;
	MyRenren app;
	private Button but_menu;
	private Button btn1,btn2,btn3,btn4,btn5;
	private ImageView locImageView;
	View contentView;
	private PopupWindow m_popupWindow;
	EditText editSearchKey;
	
	LocationData locData = null;
	MyLocationOverlay myLocationOverlay = null;

	 protected void dialog() { 
	        AlertDialog.Builder builder = new Builder(BaiduMapActivity.this); 
	        builder.setMessage("确定要退出吗?"); 
	        builder.setTitle("提示"); 
	        builder.setPositiveButton("确认", 
	                new android.content.DialogInterface.OnClickListener() { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        dialog.dismiss(); 
	                        BaiduMapActivity.this.finish(); 
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
//		if(ARNavigatorActivity.instance!=null)
//		ARNavigatorActivity.instance.finish();
		MapFactory.mapActivity=this;
		app = (MyRenren) this.getApplication();
		
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(MyRenren.strKey,
					new MyRenren.MyGeneralListener());
		}
		setContentView(R.layout.map);
		Log.i("yxy", "maponCreate");
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
//		initMapView();
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	    mLocationClient.registerLocationListener( myListener );    //注册监听函数
		LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
//        option.setScanSpan(5000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				// Log.d("hjtest", "hjtest"+"onMapMoveFinish");
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(BaiduMapActivity.this, title,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub
				// Log.d("hjtest", "hjtest"+"onMapAnimationFinish");

			}
		};
		mMapView.regMapViewListener(MyRenren.getInstance().mBMapManager,
				mMapListener);
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
	    myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();
		mMapView.getController().enableClick(true);
		mMapView.getController().setZoom(14);
		initMapView();
		editSearchKey = (EditText) findViewById(R.id.suggestionkey);
		// 初始化搜索模块，注册事件监听
		mSearch = new MKSearch();
		MKSearch.setPoiPageCapacity(50);
		mSearch.init(app.mBMapManager, new MKSearchListener() {

			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
				if (error != 0) {
					Log.i("yxy", "shibai");
					Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.i("yxy", "chenggong");
					Toast.makeText(BaiduMapActivity.this, "成功，查看详情页面",
							Toast.LENGTH_SHORT).show();
				}
			}

            @Override
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
                // 错误号可参考MKEvent中的定义
                if (error != 0 || res == null) {
                    Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }
                // 将地图移动到第一个POI中心点
                if (res.getCurrentNumPois() > 0) {
                    // 将poi结果显示到地图上
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(BaiduMapActivity.this, mMapView, mSearch);
                    poiOverlay.setData(res.getAllPoi());
                    mMapView.getOverlays().clear();
                    mMapView.getOverlays().add(myLocationOverlay);
                    mMapView.getOverlays().add(poiOverlay);
                    mMapView.refresh();
                    //当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
                    for( MKPoiInfo info : res.getAllPoi() ){
                    	if ( info.pt != null ){
                    		mMapView.getController().animateTo(info.pt);
                    		break;
                    	}
                    }
                } else if (res.getCityListNum() > 0) {
                    String strInfo = "在";
                    for (int i = 0; i < res.getCityListNum(); i++) {
                        strInfo += res.getCityListInfo(i).city;
                        strInfo += ",";
                    }
                    strInfo += "找到结果";
                    Toast.makeText(BaiduMapActivity.this, strInfo, Toast.LENGTH_LONG).show();
                }
            }

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			@Override
			public void onGetAddrResult(MKAddrInfo res, int error) {
			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				if (arg1 != 0 || res == null) {
					Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果",
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
						BaiduMapActivity.this,
						android.R.layout.simple_list_item_1, mStrSuggestions);
				//mSuggestionList.setAdapter(suggestionString);
				Toast.makeText(BaiduMapActivity.this, "suggestion callback",
						Toast.LENGTH_LONG).show();
				
			}

		});
		//mSuggestionList = (ListView) findViewById(R.id.listView1);
		// 设定搜索按钮的响应
		but_menu = (Button) findViewById(R.id.btn_sitekind_search);

		/*OnClickListener clickListener = new OnClickListener() {
			public void onClick(View v) {
				SearchButtonProcess(v);
			}
		};
		mBtnSearch.setOnClickListener(clickListener);*/

//		nextData = (Button) findViewById(R.id.map_next_data);
//		nextData.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 搜索下一组poi
//				int flag = mSearch.goToPoiPage(++load_Index);
//				if (flag != 0) {
//					Toast.makeText(BaiduMapActivity.this, "先搜索开始，然后再搜索下一组数据",
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//		});

		// 设定suggestion响应
//		mSuggestionSearch = (Button) findViewById(R.id.suggestionsearch);
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
				mMapView.getOverlays().clear();
				
				mMapView.getOverlays().add(myLocationOverlay);
				mSearch.poiSearchNearBy(editSearchKey.getText().toString(), mMapView.getMapCenter(), 1000);
			}
		});
//		OnClickListener clickListener1 = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				SuggestionSearchButtonProcess(v);
//				
//				//mSearch.suggestionSearch(editSearchKey.getText().toString());
////				mMapView.getOverlays().clear();
////				mSearch.poiSearchNearBy(editSearchKey.getText().toString(), mMapView.getMapCenter(), 1000);
//			}
//		};
//		mSuggestionSearch.setOnClickListener(clickListener1);
		
		init();
        setListener();
	}

	@Override
	protected void onPause() {
		
		
		super.onPause();
		mMapView.onPause();
		Log.i("yxy", "maponPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("yxy", "maponResume");
		if (mMapView!=null) {
			mMapView.onResume();
			SearchCity(app.cityString);
		}
		else {
			mMapView = (MapView) findViewById(R.id.bmapView);
		}
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		Log.i("yxy", "maponStart");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Log.i("yxy", "maponStop");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.destroy();
		MyRenren app = (MyRenren) this.getApplication();
		if (app.mBMapManager != null) {
			app.mBMapManager.destroy();
			app.mBMapManager = null;
		}
		
		Log.i("yxy", "maponDestroy");
	}

	private void initMapView() {
		mMapView.setLongClickable(true);
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
	}

	/*void SearchButtonProcess(View v) {
		if (mBtnSearch.equals(v)) {
			mMapView.getOverlays().clear();
			EditText editSearchKey = (EditText) findViewById(R.id.searchkey);
//			mSearch.poiSearchInCity(editCity.getText().toString(),
//					editSearchKey.getText().toString());
			mSearch.poiSearchNearBy(editSearchKey.getText().toString(), mMapView.getMapCenter(), 1000);
		}
	}*/
	void SearchCity(String city)
	{
		mSearch.poiSearchInCity(city,city);
	}
	void SuggestionSearchButtonProcess(View v) {
		editSearchKey = (EditText) findViewById(R.id.suggestionkey);
		//mSearch.suggestionSearch(editSearchKey.getText().toString());
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(myLocationOverlay);
		mSearch.poiSearchNearBy(editSearchKey.getText().toString(), mMapView.getMapCenter(), 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	private void init() {
		Log.i("sfy", "init()begin");
		contentView = getLayoutInflater().inflate(R.layout.searchlistmenu, null,
				true);
		but_menu = (Button) findViewById(R.id.btn_sitekind_search);
		btn1= (Button) contentView.findViewById(R.id.btn_site_kind1);
		btn2= (Button) contentView.findViewById(R.id.btn_site_kind2);
		btn3= (Button) contentView.findViewById(R.id.btn_site_kind3);
		btn4= (Button) contentView.findViewById(R.id.btn_site_kind4);
		btn5= (Button) contentView.findViewById(R.id.btn_site_kind5);
		locImageView = (ImageView)findViewById(R.id.btn_site_location);

		// PopupWindow弹出的窗口显示的view,第二和第三参数：分别表示此弹出窗口的大小
		m_popupWindow = new PopupWindow(contentView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		Log.i("sfy", "init()");
		m_popupWindow.setBackgroundDrawable(new BitmapDrawable());// 有了这句才可以点击返回（撤销）按钮dismiss()popwindow
		m_popupWindow.setOutsideTouchable(true);
	}

	private void setListener() {
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				m_popupWindow.dismiss();
			}
		});
		// m_popupWindow = new PopupWindow();
		but_menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (m_popupWindow.isShowing()) {

						m_popupWindow.dismiss();
					}
					m_popupWindow.showAsDropDown(v);

				} catch (Exception e) {
					Toast.makeText(BaiduMapActivity.this, e.getMessage(),
							Toast.LENGTH_SHORT);
				}
			}
		});
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind1);
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(myLocationOverlay);
				mSearch.poiSearchNearBy(getText(R.string.site_kind1).toString(), mMapView.getMapCenter(), 1000);
		        
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind2);
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(myLocationOverlay);
				mSearch.poiSearchNearBy(getText(R.string.site_kind2).toString(), mMapView.getMapCenter(), 1000);
				}
		});
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind3);
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(myLocationOverlay);
				mSearch.poiSearchNearBy(getText(R.string.site_kind3).toString(), mMapView.getMapCenter(), 1000);
		        
			}
		});
		btn4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind4);
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(myLocationOverlay);
				mSearch.poiSearchNearBy(getText(R.string.site_kind4).toString(), mMapView.getMapCenter(), 1000);
		        
			}
		});
		btn5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_popupWindow.dismiss();
				but_menu.setText(R.string.site_kind5);
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(myLocationOverlay);
				mSearch.poiSearchNearBy(getText(R.string.site_kind5).toString(), mMapView.getMapCenter(), 1000);
		        
			}
		});
		locImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
            	mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
			}
		});
		
	}
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (m_popupWindow != null && m_popupWindow.isShowing()) {
				m_popupWindow.dismiss();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}*/
	public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            Intent intent = new Intent();  
			intent.setAction("com.hit.changyou");  
			intent.putExtra("city",location.getCity() );
			BaiduMapActivity.this.sendBroadcast(intent);
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            myLocationOverlay.setData(locData);
            mMapView.refresh();
            Log.i("yxy", locData.latitude+";"+locData.longitude);
            //定位预留
            if (app.cityString.equals("")) {
            	mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
			}
            
            
        }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
}
