package com.hit.changyou;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.hit.changyou.push.Utils;
import com.renren.api.connect.android.Renren;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author yxy
 * @since 2013-5-13
 */
public class MainTabActivity extends TabActivity implements
		OnCheckedChangeListener {
	private RadioGroup mainTab;
	private TabHost mTabHost;

	// ����Intent
	private Intent mSearchIntent;
	private Intent mMapIntent;
	private Intent mInfoIntent;
	private Intent mARIntent;
	private Intent mMoreIntent;
	private Renren renren;
	// 你的应用ID
	private static final String APP_ID = "234153";
	// 应用的API Key
	private static final String API_KEY = "d3842f13ed9246a6aa56160abbeb7c4c";
	// 应用的Secret Key
	private static final String SECRET_KEY = "149594f01c2242c5af99230a7e9aacc9";
	private final static String TAB_TAG_SEARCH = "tab_tag_search";
	private final static String TAB_TAG_MAP = "tab_tag_map";
	private final static String TAB_TAG_AR = "tab_tag_ar";
	private final static String TAB_TAG_INFO = "tab_tag_info";
	private final static String TAB_TAG_MORE = "tab_tag_more";
	/** Called when the activity is first created. */
	final static int WRAP_CONTENT = -2;// ��ʾWRAP_CONTENT�ĳ���

	// ������Դ�ַ�city��id������
	int[] msgIds = { R.string.city1, R.string.city2, R.string.city3,
			R.string.city4, R.string.city5, R.string.city6, R.string.city7,
			R.string.city8, R.string.city9, R.string.city10, R.string.city11,
			R.string.city12, R.string.city13, R.string.city14, R.string.city15,
			R.string.city16, R.string.city17, R.string.city18, R.string.city19,
			R.string.city20, R.string.city21, R.string.city22 };
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(state);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	 /*protected void dialog() { 
	        AlertDialog.Builder builder = new Builder(MainTabActivity.this); 
	        builder.setMessage("确定要退出吗?"); 
	        builder.setTitle("提示"); 
	        builder.setPositiveButton("确认", 
	                new android.content.DialogInterface.OnClickListener() { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        dialog.dismiss(); 
	                        MainTabActivity.this.finish(); 
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
	    }*/
	/*@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
//        	Intent intent = new Intent();
//			intent.setClass(MainTabActivity.this, AirportActivity.class);
//			startActivity(intent);
        	Toast.makeText(MainTabActivity.this, "按第二次后退键推出", Toast.LENGTH_SHORT).show();
            return false; 
        } 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 1) { 
//        	Intent intent = new Intent();
//			intent.setClass(MainTabActivity.this, AirportActivity.class);
//			startActivity(intent);
//        	Toast.makeText(MainTabActivity.this, "按第二次后退键推出", Toast.LENGTH_SHORT).show();
        	android.os.Process.killProcess(android.os.Process.myPid()); //获取PID
        	System.exit(0);
            return false; 
        } 
        return false; 
    }*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapFactory.mActivity=this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		renren = new Renren(API_KEY, SECRET_KEY, APP_ID, MainTabActivity.this);
		MyRenren myrenren = (MyRenren) getApplication();
		myrenren.setRenren(renren);
		
		mainTab = (RadioGroup) findViewById(R.id.main_tab);
		mainTab.setOnCheckedChangeListener(this);
		prepareIntent();
		setupIntent();
		setDefaultTab(TAB_TAG_SEARCH);
		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, 
				Utils.getMetaValue(MainTabActivity.this, "api_key"));
		
		
		//设置自定义的通知样式，如果想使用系统默认的可以不加这段代码
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
        		resource.getIdentifier("notification_custom_builder", "layout", pkgName), 
        		resource.getIdentifier("notification_icon", "id", pkgName), 
        		resource.getIdentifier("notification_title", "id", pkgName), 
        		resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier("simple_notification_icon", "drawable", pkgName));
		PushManager.setNotificationBuilder(this, 1, cBuilder);
		// ��ʼ��Spinner
		Spinner sp = (Spinner) this.findViewById(R.id.Spinner01);
		// ΪSpinner׼������������
		BaseAdapter ba = new BaseAdapter() {
			// @Override
			@Override
			public int getCount() {
				return 22;// �ܹ����ѡ��
			}

			// @Override
			@Override
			public Object getItem(int arg0) {
				return null;
			}

			// @Override
			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			// @Override
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				/*
				 * ��̬���ÿ���������Ӧ��View��ÿ��������View��LinearLayout
				 * �а�һ��ImageView��һ��TextView����
				 */

				// ��ʼ��LinearLayout
				LinearLayout ll = new LinearLayout(MainTabActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL); // ���ó���

				// ��ʼ��ImageView
				ImageView ii = new ImageView(MainTabActivity.this);
				ii.setImageDrawable(getResources().getDrawable(
						R.drawable.site_city));// ����ͼƬ
				ll.addView(ii);// ��ӵ�LinearLayout��

				// ��ʼ��TextView
				TextView tv = new TextView(MainTabActivity.this);
				tv.setText(" " + getResources().getText(msgIds[arg0]));// ��������
				tv.setTextSize(24);// ���������С
				tv.setTextColor(getResources().getColor(R.color.bgColor));// ����������ɫ
				ll.addView(tv);// ��ӵ�LinearLayout��

				return ll;
			}
		};

		sp.setAdapter(ba);// ΪSpinner��������������

		// ����ѡ��ѡ�еļ�����
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			// @Override
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {// ��дѡ�ѡ���¼��Ĵ��?��
				Log.i("yxy", "MainTabOnItemSeleted");
				TextView tvn;
				if (arg1!=null) {
					LinearLayout ll = (LinearLayout) arg1;// ��ȡ��ǰѡ��ѡ���Ӧ��LinearLayout
					if (ll.getChildCount()>0) {
						tvn = (TextView) ll.getChildAt(1);// ��ȡ���е�TextView
						Intent intent = new Intent();  
						intent.setAction("com.hit.changyou");  
						intent.putExtra("city", tvn.getText().toString());  
						MainTabActivity.this.sendBroadcast(intent);
						
						StringBuilder sb = new StringBuilder();// ��StringBuilder��̬�����Ϣ
						sb.append(getResources().getText(R.string.city1));
						sb.append(":");
						sb.append(tvn.getText());
					}
				}
			}
			
			// @Override
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/**
	 * ׼��tab������Intent
	 */
	private void prepareIntent() {
//		mSearchIntent = new Intent(this, MySearchListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mSearchIntent = new Intent(this, MySearchListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mMapIntent = new Intent(this, BaiduMapActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mARIntent = new Intent(this, ARNavigatorActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mInfoIntent = new Intent(this, NewsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
		mMoreIntent = new Intent(this, M_MoreActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	/**
	 * 
	 */
	private void setupIntent() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;
		localTabHost.addTab(buildTabSpec(TAB_TAG_SEARCH, R.string.main_search,
				R.drawable.icon_1_n, mSearchIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_MAP, R.string.main_map,
				R.drawable.icon_2_n, mMapIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_AR, R.string.main_camera,
				R.drawable.icon_3_n, mARIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_INFO, R.string.main_info,
				R.drawable.icon_4_n, mInfoIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_MORE, R.string.main_more,
				R.drawable.icon_5_n, mMoreIntent));
	}
	private void handleIntent(Intent intent) {
		String action = intent.getAction();

		if (Utils.ACTION_RESPONSE.equals(action)) {

			String method = intent.getStringExtra(Utils.RESPONSE_METHOD);

			if (PushConstants.METHOD_BIND.equals(method)) {
				String toastStr = "";
				int errorCode = intent.getIntExtra(Utils.RESPONSE_ERRCODE, 0);
				if (errorCode == 0) {
					String content = intent
							.getStringExtra(Utils.RESPONSE_CONTENT);
					String appid = "";
					String channelid = "";
					String userid = "";

					try {
						JSONObject jsonContent = new JSONObject(content);
						JSONObject params = jsonContent
								.getJSONObject("response_params");
						appid = params.getString("appid");
						channelid = params.getString("channel_id");
						userid = params.getString("user_id");
					} catch (JSONException e) {
						Log.e(Utils.TAG, "Parse bind json infos error: " + e);
					}

					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(this);
					Editor editor = sp.edit();
					editor.putString("appid", appid);
					editor.putString("channel_id", channelid);
					editor.putString("user_id", userid);
					editor.commit();

//					showChannelIds();

					toastStr = "Bind Success";
				} else {
					toastStr = "Bind Fail, Error Code: " + errorCode;
					if (errorCode == 30607) {
						Log.d("Bind Fail", "update channel token-----!");
					}
				}

				Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
			}
		} else if (Utils.ACTION_LOGIN.equals(action)) {
			String accessToken = intent
					.getStringExtra(Utils.EXTRA_ACCESS_TOKEN);
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_ACCESS_TOKEN, accessToken);
//			isLogin = true;
//			initButton.setText("更换百度账号初始化Channel");
		} else if (Utils.ACTION_MESSAGE.equals(action)) {
			String message = intent.getStringExtra(Utils.EXTRA_MESSAGE);
			String summary = "Receive message from server:\n\t";
			Log.e(Utils.TAG, summary + message);
			JSONObject contentJson = null;
			String contentStr = message;
			try {
				contentJson = new JSONObject(message);
				contentStr = contentJson.toString(4);
			} catch (JSONException e) {
				Log.d(Utils.TAG, "Parse message json exception.");
			}
			summary += contentStr;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(summary);
			builder.setCancelable(true);
			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			Log.i(Utils.TAG, "Activity normally start!");
		}
	}
	/**
	 * ����TabHost��Tabҳ
	 * 
	 * @param tag
	 *            ���
	 * @param resLabel
	 *            ��ǩ
	 * @param resIcon
	 *            ͼ��
	 * @param content
	 *            ��tabչʾ������
	 * @return һ��tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return this.mTabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_button0:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_SEARCH);
			break;
		case R.id.radio_button1:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_MAP);
			break;
		case R.id.radio_button2:
//			this.mTabHost.setCurrentTabByTag(TAB_TAG_AR);
			startActivity(mARIntent);
			break;
		case R.id.radio_button3:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_INFO);
			break;
		case R.id.radio_button4:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_MORE);
			break;
		}
	}

}
