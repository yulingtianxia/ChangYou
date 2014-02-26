/**
 * 
 */
package com.hit.changyou;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersAdSize;
import net.youmi.android.offers.OffersBanner;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsManager;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.view.RenrenAuthListener;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.sharesdk.BaiduShareException;
import com.baidu.sharesdk.BaiduSocialShare;
import com.baidu.sharesdk.ShareContent;
import com.baidu.sharesdk.ShareListener;
import com.baidu.sharesdk.Utility;
import com.baidu.sharesdk.ui.BaiduSocialShareUserInterface;
/**
 * 更多Activity
 * 
 * @author 飞雪无情
 * @since 2011-3-8
 */
public class M_MoreActivity extends Activity implements PointsChangeNotify {

	int[] imgId = {};
	int[] msgIds_title = { R.string.more_inf_title1, R.string.more_inf_title2,
			R.string.more_inf_title3, R.string.more_inf_title4,
			R.string.more_inf_title5, R.string.more_inf_title6,
			R.string.more_inf_title7, R.string.more_inf_title8,
			R.string.more_inf_title9 };
	int[] msgIds_img = { R.drawable.more_ifo_img1, R.drawable.more_ifo_img2,
			R.drawable.more_ifo_img3, R.drawable.more_ifo_img4,
			R.drawable.more_ifo_img5, R.drawable.more_ifo_img6,
			R.drawable.more_ifo_img7, R.drawable.more_ifo_img8,
			R.drawable.more_ifo_img9 };
	List<String> title_ifoList = new ArrayList<String>();
	// 你的应用ID
	private static final String APP_ID = "234153";
	// 应用的API Key
	private static final String API_KEY = "d3842f13ed9246a6aa56160abbeb7c4c";
	// 应用的Secret Key
	private static final String SECRET_KEY = "149594f01c2242c5af99230a7e9aacc9";
	final String appid = "d27d2f60b54a7bfe";
	final String appkey = "e181c0ead8cad5b7";
	private final static String ApiKey = BaiduSocialShareConfig.mbApiKey;
	private Renren renren;
	private Handler handler;
	MyRenren myRenren;
	OffersBanner mBanner;
	 protected void dialog() { 
	        AlertDialog.Builder builder = new Builder(M_MoreActivity.this); 
	        builder.setMessage("确定要退出吗?"); 
	        builder.setTitle("提示"); 
	        builder.setPositiveButton("确认", 
	                new android.content.DialogInterface.OnClickListener() { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        dialog.dismiss(); 
	                        M_MoreActivity.this.finish(); 
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
		setContentView(R.layout.m_more);
		GridView gridview = (GridView) findViewById(R.id.gridview);
		myRenren = (MyRenren)getApplication();
		renren = ((MyRenren) getApplication()).getRenren();
		AdManager.getInstance(this).init(appid,
				appkey, true);
		// 如果使用积分广告，请务必调用积分广告的初始化接口:
		OffersManager.getInstance(this).onAppLaunch();
		PointsManager.getInstance(this).registerNotify(this);
		// 生成数据源 一个List
		ArrayList<HashMap<String, Object>> DateList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 9; i++) {
			// 医用HashMap来存储
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			// 添加图像资源的ID
			hashMap.put("Image", msgIds_img[i]);
			// 按序号做ItemText
			hashMap.put("Text", getResources().getText(msgIds_title[i]));
			// 添加到List去
			DateList.add(hashMap);
		}
		// 生成适配器的simpleAdapter
		// DateList 数据源
		// R.layout.item 来自于item。xml 控制GridView中每一项的布局（一个ImageView 一个TextView）
		// 第三个参数是hashoMap的键值
		// 第四个参数是item。xml中ImageView，TextView的ID
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, DateList,
				R.layout.gride_item, new String[] { "Image", "Text" },
				new int[] { R.id.ItemImage, R.id.ItemText });
		// 添加并且显示
		gridview.setAdapter(simpleAdapter);
		// 添加消息处理
		gridview.setOnItemClickListener(new ItemClickListener());
		mBanner = new OffersBanner(this, OffersAdSize.SIZE_MATCH_SCREENx60);
		RelativeLayout layoutOffersBanner = (RelativeLayout) findViewById(R.id.offersBannerLayout);
		layoutOffersBanner.addView(mBanner);
	}

	// 初始化按钮和Renren实例
	private void initRenren() {
		renren = new Renren(API_KEY, SECRET_KEY, APP_ID, M_MoreActivity.this);
		handler = new Handler();

		// loginBtn = (Button) findViewById(R.id.loginBtn);
		// logoutBtn = (Button) findViewById(R.id.logoutBtn);
		// loginText = (TextView) findViewById(R.id.loginText);
		// btnCamera = (Button) findViewById(R.id.btncamera);
		// showLoginBtn(true);
	}

	private class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			// arg0就是那个DateList
			HashMap<String, Object> item = (HashMap<String, Object>) arg0
					.getItemAtPosition(arg2);
			// 显示应用标题的那个TextView
			String titleString = (String) item.get("Text");
			setTitle(titleString);
			if (titleString.equals("人人登陆")) {
				initRenren();
				final RenrenAuthListener listener = new RenrenAuthListener() {
					// 登录成功
					public void onComplete(Bundle values) {
						// showLoginBtn(false);
						// loginText.setText(R.string.auth_success);

						handler.post(new Runnable() {
							@Override
							public void run() {
								MyRenren myrenren = (MyRenren) getApplication(); // ����Զ����Ӧ�ó���MyApp
								myrenren.setRenren(renren);
								Toast.makeText(
										M_MoreActivity.this,
										M_MoreActivity.this
												.getString(R.string.auth_success),
										Toast.LENGTH_SHORT).show();
								 Intent intent = new Intent();
								 intent.setClass(M_MoreActivity.this,
								 PersonalActivity.class);
								 startActivity(intent);
							}
						});
					}

					// 登录失败
					@Override
					public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
						// loginText.setText(R.string.auth_failed);
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(
										M_MoreActivity.this,
										M_MoreActivity.this
												.getString(R.string.auth_failed),
										Toast.LENGTH_SHORT).show();
							}
						});
					}

					@Override
					public void onCancelLogin() {
					}

					@Override
					public void onCancelAuth(Bundle values) {
					}
				};
				startSharing();
				renren.authorize(M_MoreActivity.this, listener);
				
			}
			else if (titleString.equals("登陆")) {
				Intent intent =new Intent();
				intent.setClass(M_MoreActivity.this, LoginActivity_1.class);
				startActivity(intent);
			}
			else if (titleString.equals("个人信息")) {
				Intent intent =new Intent();
//				intent.setClass(M_MoreActivity.this, PersonalActivity.class);
				intent.setClass(M_MoreActivity.this, PersonalActivity.class);
				startActivity(intent);
			}
			else if (titleString.equals("畅游推荐")) {
				
				OffersManager.getInstance(M_MoreActivity.this).showOffersWallDialog(M_MoreActivity.this);
			}
			else if (titleString.equals("自动分享")) {
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.logo);
					//create a file to write bitmap data
					SimpleDateFormat sdf = new SimpleDateFormat(
		    	            "yyyy-MM-dd_HH-mm-ss", Locale.US);
					File file =new File(getApplicationContext().getCacheDir(), sdf.format(new Date()) + ".png");
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i("yxy", "1");
					//Convert bitmap to byte array
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
					byte[] bitmapdata = bos.toByteArray();

					//write the bytes in file
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(file);
						fos.write(bitmapdata);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
//					startSharing();
		        	renren.publishPhoto(M_MoreActivity.this, file, "我正在使用畅游-室内导航专家，大家快来加入我吧！");
            }
		}
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	@Override
	public void onPointBalanceChange(int arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(M_MoreActivity.this, "游米余额:"+arg0, Toast.LENGTH_SHORT).show();
	}
	private void startSharing(){
		
		// create baidu social share instance
		BaiduSocialShare bss = BaiduSocialShare.getInstance(this, ApiKey);
		
		// create content to share
		ShareContent content = new ShareContent();
		content.setTitle("畅游一键分享");
		content.setContent("精彩畅游，舒心生活.我正在使用畅游-室内导航专家，大家快来加入我吧！");
//		content.setUrl("http://www.baidu.com");
		
		// create the user interface
		BaiduSocialShareUserInterface bssui = bss.getSocialShareUserInterfaceInstance();
		
		// start to share
		bssui.showShareMenu(this, content, Utility.SHARE_THEME_STYLE, new ShareListener(){

			@Override
			public void onApiComplete(String arg0) {
				Toast.makeText(M_MoreActivity.this, "on api complete " + arg0, 1000).show();
			}

			@Override
			public void onAuthComplete(Bundle arg0) {
				Toast.makeText(M_MoreActivity.this, "on auth complete", 1000).show();
			}

			@Override
			public void onError(BaiduShareException arg0) {
				Toast.makeText(M_MoreActivity.this, "on error", 1000).show();
			}
		});
	}
}
