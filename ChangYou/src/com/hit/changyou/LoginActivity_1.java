package com.hit.changyou;

import java.io.IOException;

import Network.Network;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.social.core.BaiduSocialException;
import com.baidu.social.core.BaiduSocialListener;
import com.baidu.social.core.Utility;
import com.baidu.sociallogin.BaiduSocialLogin;
import com.baidu.solution.client.service.ServiceException;
import com.baidu.solution.pcs.sd.impl.ErrorInfo;
import com.baidu.solution.pcs.sd.model.Table;
import com.hit.changyou.model.User;
import com.hit.changyou.protocol.ProtocolUserInfo;
import com.hit.changyou.protocol.ProtocolUserInfo.ProtocolUserInfoDelegate;


public class LoginActivity_1 extends Activity implements ProtocolUserInfoDelegate{

	final Handler handler = new Handler(Looper.getMainLooper());

	private BaiduSocialLogin socialLogin;

	private final static String appKey = BaiduSocialShareConfig.mbApiKey;

	private RelativeLayout sinaWeibo;

	private RelativeLayout qqzone;

	private RelativeLayout qqWeibo;

	private RelativeLayout kaixin;
	
	private RelativeLayout renren;

	private Button clean;
	private Button backbutton;
	
	private RelativeLayout baidu;

	private EditText info;

	protected static final int MESSAGE_ADDUSER_SUCCESS = 0;
	protected static final int MESSAGE_ADDUSER_FAILED = 1;
	Handler datahandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case MESSAGE_ADDUSER_SUCCESS: {
				// displayList();
				

			}
				break;
			case MESSAGE_ADDUSER_FAILED: {

			}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

		setContentView(R.layout.m_share);
		
		// 实例化baidu社会化登录，传入appkey
		socialLogin = BaiduSocialLogin.getInstance(this, appKey);

		// 设置支持腾讯微博单点登录的appid
		socialLogin.supportQQSso(BaiduSocialShareConfig.QQ_SSO_APP_KEY);

		// 设置支持新浪微博单点登录的appid
		socialLogin.supportWeiBoSso(BaiduSocialShareConfig.SINA_SSO_APP_KEY);

		sinaWeibo = (RelativeLayout) findViewById(R.id.share_sinaRelativeLayout);
		qqzone = (RelativeLayout) findViewById(R.id.share_qzoneRelativeLayout);
		qqWeibo = (RelativeLayout) findViewById(R.id.share_qqweiboRelativeLayout);
		kaixin = (RelativeLayout) findViewById(R.id.share_kaixinRelativeLayout);
		renren=(RelativeLayout)findViewById(R.id.share_renrenRelativeLayout);
		
		
		clean = (Button) findViewById(R.id.button_clear);
		backbutton = (Button) findViewById(R.id.btn_back);
		baidu=(RelativeLayout)findViewById(R.id.share_baiduRelativeLayout);
		
		clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				socialLogin.cleanAllAccessToken();
			}
		});

		sinaWeibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (socialLogin
						.isAccessTokenValid(Utility.SHARE_TYPE_SINA_WEIBO)) {
					socialLogin.getUserInfoWithShareType(LoginActivity_1.this,
							Utility.SHARE_TYPE_SINA_WEIBO,
							new UserInfoListener());
				} else
					socialLogin.authorize(LoginActivity_1.this,
							Utility.SHARE_TYPE_SINA_WEIBO,
							new UserInfoListener());

			}
		});

		qqzone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (socialLogin.isAccessTokenValid(Utility.SHARE_TYPE_QZONE)) {
					socialLogin.getUserInfoWithShareType(LoginActivity_1.this,
							Utility.SHARE_TYPE_QZONE, new UserInfoListener());
				} else
					socialLogin.authorize(LoginActivity_1.this,
							Utility.SHARE_TYPE_QZONE, new UserInfoListener());

			}
		});

		qqWeibo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (socialLogin.isAccessTokenValid(Utility.SHARE_TYPE_QQ_WEIBO)) {
					socialLogin
							.getUserInfoWithShareType(LoginActivity_1.this,
									Utility.SHARE_TYPE_QQ_WEIBO,
									new UserInfoListener());
				} else
					socialLogin
							.authorize(LoginActivity_1.this,
									Utility.SHARE_TYPE_QQ_WEIBO,
									new UserInfoListener());
			}
		});

		kaixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (socialLogin.isAccessTokenValid(Utility.SHARE_TYPE_KAIXIN)) {
					socialLogin.getUserInfoWithShareType(LoginActivity_1.this,
							Utility.SHARE_TYPE_KAIXIN, new UserInfoListener());
				} else
					socialLogin.authorize(LoginActivity_1.this,
							Utility.SHARE_TYPE_KAIXIN, new UserInfoListener());
			}
		});
		
		
		renren.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (socialLogin
						.isAccessTokenValid(Utility.SHARE_TYPE_RENREN)) {
					socialLogin.getUserInfoWithShareType(LoginActivity_1.this,
							Utility.SHARE_TYPE_RENREN,
							new UserInfoListener());
				} else
					socialLogin.authorize(LoginActivity_1.this,
							Utility.SHARE_TYPE_RENREN,
							new UserInfoListener());

			}
		});
		
		
		baidu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (socialLogin
						.isAccessTokenValid(Utility.SHARE_TYPE_BAIDU)) {
					socialLogin.getUserInfoWithShareType(LoginActivity_1.this,
							Utility.SHARE_TYPE_BAIDU,
							new UserInfoListener());
				} else
					socialLogin.authorize(LoginActivity_1.this,
							Utility.SHARE_TYPE_BAIDU,
							new UserInfoListener());

			}
		});
		backbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity_1.this, MainTabActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	
	
	

	class UserInfoListener implements BaiduSocialListener {

		@Override
		public void onAuthComplete(Bundle values) {
			// TODO Auto-generated method stubis
		}

		@Override
		public void onApiComplete(String responses) {
			// TODO Auto-generated method stub
			final String responseStr = responses;
			handler.post(new Runnable() {
				@Override
				public void run() {
					Json2UserInfo jUserInfo = new Json2UserInfo();
					User user = jUserInfo.parseUser(Utility.decodeUnicode(responseStr));
					MyRenren myrenren = (MyRenren) getApplication(); 
					myrenren.setUser(user);
					Toast.makeText(
							LoginActivity_1.this,
							LoginActivity_1.this
									.getString(R.string.auth_success),
							Toast.LENGTH_SHORT).show();
					adduserandlogin(user);
					String log="";
					Table table;
					try {
						// Create favorite song table with columns and indexes.
						table = PCS.createFavoritePOITable();
						log = "Step 1: Create " + PCS.FAVORITE_TABLE
								+ " table done.";
					} catch (ServiceException e) {
						ErrorInfo info = e.toErrorInformation(ErrorInfo.class);
						long code = info.getErrorCode();
						if (code == 31476 || code == 31472) {
							log = "Step 1: " + PCS.FAVORITE_TABLE
									+ " table already exist.";
						}
						log = "Step 1: Create " + PCS.FAVORITE_TABLE
								+ " table failed:" + e.getMessage();
						try {
							throw e;
						} catch (ServiceException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i("PCScreattable", log);
//					info.setText(user.getSocial_uid()+"\n"+user.getUsername()+"\n"+user.getSex()+"\n"+user.getMedia_type());
//					Log.i("userinfo", info.getText().toString());
					
				}
			});
		}

		@Override
		public void onError(BaiduSocialException e) {
			final String error = e.toString();
			handler.post(new Runnable() {
				@Override
				public void run() {
					
				}
			});
		}
	}





	@Override
	public void commitUserInfoSuccess() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		 intent.setClass(LoginActivity_1.this,
		 PersonalActivity.class);
		 startActivity(intent);
	}





	@Override
	public void commitUserInfoFailed() {
		// TODO Auto-generated method stub
		
	}
	void adduserandlogin(User user)
	{
		ProtocolUserInfo _protocol = new ProtocolUserInfo(user).setDelegate(this)
				.setContext(this.getApplicationContext());
		 // 通过网络请求
		 Network _network = new Network();
		 _network.setURL(ProtocolUserInfo.URL + ProtocolUserInfo.COMMAND+ProtocolUserInfo.VALUE);
		 _network.send(_protocol, Network.GET);
	}
}
