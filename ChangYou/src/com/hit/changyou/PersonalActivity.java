package com.hit.changyou;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewLinstener;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsManager;

import com.baidu.sociallogin.BaiduSocialLogin;
import com.hit.changyou.model.User;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenException;
import com.renren.api.connect.android.users.UserInfo;
import com.renren.api.connect.android.users.UsersGetInfoRequestParam;
import com.renren.api.connect.android.users.UsersGetInfoResponseBean;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalActivity extends Activity implements PointsChangeNotify {
//	private Renren renren;
	private User user= new User();
	private static final String PRE_NAME = "WrongAnswer";
	private static final String PREFS_NAME = "MyUserInfo";
	private Button logoutBtn;
	MyRenren myRenren;
	Button btnnullButton;
	TextView tvwrong;
	private BaiduSocialLogin socialLogin;

	private final static String appKey = BaiduSocialShareConfig.mbApiKey;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal);
		RelativeLayout adLayout = (RelativeLayout) findViewById(R.id.adLayout);
		AdView adView = new AdView(this, AdSize.SIZE_320x50);
		adLayout.addView(adView);
		// 实例化baidu社会化登录，传入appkey
		socialLogin = BaiduSocialLogin.getInstance(this, appKey);

		// 设置支持腾讯微博单点登录的appid
		socialLogin.supportQQSso(BaiduSocialShareConfig.QQ_SSO_APP_KEY);

		// 设置支持新浪微博单点登录的appid
		socialLogin.supportWeiBoSso(BaiduSocialShareConfig.SINA_SSO_APP_KEY);
		// 监听广告条接口
		adView.setAdListener(new AdViewLinstener() {

			@Override
			public void onSwitchedAd(AdView arg0) {
				Log.i("YoumiSample", "广告条切换");
			}

			@Override
			public void onReceivedAd(AdView arg0) {
				Log.i("YoumiSample", "请求广告成功");

			}

			@Override
			public void onFailedToReceivedAd(AdView arg0) {
				Log.i("YoumiSample", "请求广告失败");
			}
		});
		myRenren = (MyRenren) getApplication();
		logoutBtn = (Button) findViewById(R.id.logoutBtn);
		logoutBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				renren.logout(getApplicationContext());
				socialLogin.cleanAllAccessToken();
				logoutBtn.setVisibility(View.GONE);
				Intent intent = new Intent(); // �ٿ�һ������һ��Activity����ȡ����ʼ����ֵ������ȡ���޸ĺ��ֵ
				intent.setClass(PersonalActivity.this, MainTabActivity.class);
				startActivity(intent);
				finish();
			}

		});

		tvwrong = (TextView) findViewById(R.id.score);
		PointsManager.getInstance(this).registerNotify(this);
		int pointsBalance = PointsManager.getInstance(this).queryPoints();// 查询积分余额
		tvwrong.setText("游米余额:" + pointsBalance);

		// (可选)注册积分监听-随时随地获得积分的变动情况
		PointsManager.getInstance(this).registerNotify(this);
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		if (userInfo.getString("IsFirstAward", "true").equals("true")) {
			PointsManager.getInstance(this).awardPoints(10000);
			userInfo.edit().putString("IsFirstAward", "false").commit();
		}
//		renren = ((MyRenren) getApplication()).getRenren();
		Log.i("yxy", user.toString());
		user = ((MyRenren) getApplication()).getUser();
		Log.i("yxy", user.toString());
//		if (renren.isSessionKeyValid()) {
//			logoutBtn.setVisibility(View.VISIBLE);
//		} else {
//			logoutBtn.setVisibility(View.GONE);
//		}
//		long uid = renren.getCurrentUid();
		SharedPreferences sp = getSharedPreferences(PRE_NAME, 0);
		SharedPreferences sp1 = getSharedPreferences(PREFS_NAME, 0);
		String[] uids = new String[1];
//		uids[0] = "" + renren.getCurrentUid();
		UsersGetInfoRequestParam param = new UsersGetInfoRequestParam(uids);
		try {
//			UsersGetInfoResponseBean userinfo = renren.getUsersInfo(param);
//			ArrayList<UserInfo> infos = userinfo.getUsersInfo();
//			UserInfo me = infos.get(0);

			ImageView vhead = (ImageView) findViewById(R.id.rhead);
			TextView vname = (TextView) findViewById(R.id.rname);
			TextView vage = (TextView) findViewById(R.id.rage);
			TextView vsex = (TextView) findViewById(R.id.rsex);
			TextView vschool = (TextView) findViewById(R.id.rschool);
//			vhead.setImageBitmap(returnBitMap(me.getHeadurl()));
			vhead.setImageBitmap(returnBitMap(user.getTinyurl()));
//			vname.setText(me.getName());
			vname.setText("用  户  名：" +user.getUsername());
//			vage.setText("出生年月：" + me.getBirthday());
			vage.setText("出生年月：" + user.getBirthday());
			if (user.getSex() == 1)
				vsex.setText("性       别：男");
			else if(user.getSex() == 2)
				vsex.setText("性       别：女");
			else {
				vsex.setText("性       别：未知");
			}
			
			vschool.setText("学       校：暂无信息");
			// tvwrong.setText("游米：10000");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ImageView renrenout = (ImageView)findViewById(R.id.imagerenrenout);
		// renrenout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		ImageView back = (ImageView) findViewById(R.id.personalback);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	public Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 注销积分监听-如果在onCreate注册了，那这里必须得注销
		PointsManager.getInstance(this).unRegisterNotify(this);
	}

	@Override
	public void onPointBalanceChange(int pointsBalance) {
		// TODO Auto-generated method stub
		// 积分SDK是在UI线程上回调该函数的，因此可直接操作UI，但切勿进行其他的长时间操作
		tvwrong.setText("游米余额:" + pointsBalance);
	}
}
