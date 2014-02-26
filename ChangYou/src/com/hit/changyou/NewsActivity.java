/**
 * 
 */
package com.hit.changyou;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 信息Activity
 * @author 飞雪无情
 * @since 2011-3-8
 */
public class NewsActivity extends Activity {

	int[] msgIds={
			R.drawable.news_1,R.drawable.news_2,R.drawable.news_3,R.drawable.news_4,R.drawable.news_5,R.drawable.news_6,
			R.drawable.news_7,R.drawable.news_8,R.drawable.news_9,R.drawable.news_10,
			R.drawable.news_11,R.drawable.news_12,
			R.drawable.news_13,R.drawable.news_14,R.drawable.news_15,	R.drawable.news_16,R.drawable.news_17,
			R.drawable.news_18,R.drawable.news_19,R.drawable.news_20,R.drawable.news_21,R.drawable.news_22,
			R.drawable.news_23,R.drawable.news_24,R.drawable.news_25,	R.drawable.news_26,R.drawable.news_27,
			R.drawable.news_28,R.drawable.news_29,R.drawable.news_30,R.drawable.news_31,R.drawable.news_32,
			R.drawable.news_33,R.drawable.news_34,R.drawable.news_35,	R.drawable.news_36,R.drawable.news_37,
			R.drawable.news_38,R.drawable.news_39};
	BaseAdapter ba;
	ListView lv1,lv2,lv3;
	LinearLayout ll;
	DisplayMetrics dm;
	ImageView  i_love;
	List<Integer> list_int;
	 protected void dialog() { 
	        AlertDialog.Builder builder = new Builder(NewsActivity.this); 
	        builder.setMessage("确定要退出吗?"); 
	        builder.setTitle("提示"); 
	        builder.setPositiveButton("确认", 
	                new android.content.DialogInterface.OnClickListener() { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        dialog.dismiss(); 
	                        NewsActivity.this.finish(); 
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
		setContentView(R.layout.newslist);
		list_int = new ArrayList<Integer>();
		for(int i : msgIds)
		{
			list_int.add(i);
		}
		
		ba=new news_BaseAdapter(NewsActivity.this,list_int);
		lv1=(ListView)this.findViewById(R.id.ListView_1);
		lv1.setAdapter(ba);
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
		
		
	}
	
	private class news_BaseAdapter extends BaseAdapter {
		private Activity context_Activity;
		private List<Integer> msgIds;

		public news_BaseAdapter(Activity context, List<Integer> list)
		{
			context_Activity = context;
			msgIds = list;
			
		}
		@Override
		public int getCount() 
		{
			return msgIds.size();
		}
		@Override
		public Object getItem(int position) 
		{
			return null;
		}
		@Override
		public long getItemId(int position) 
		{
			return 0;
		}
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) 
		{
			/*
			 * 动态生成每个下拉项对应的View，每个下拉项View由LinearLayout
			 *中包含一个ImageView及一个TextView构成
			*/
			//初始化LinearLayout
			ll=new LinearLayout(context_Activity);
			ll.setOrientation(LinearLayout.HORIZONTAL);		//设置朝向		
			//初始化ImageView
			i_love=new ImageView(context_Activity);
			i_love.setImageDrawable(context_Activity.getResources().getDrawable(msgIds.get(arg0)));//设置图片
			i_love.setScaleType(ImageView.ScaleType.FIT_XY);
			i_love.setLayoutParams(new Gallery.LayoutParams(dm.widthPixels,dm.widthPixels*80/320));
			ll.addView(i_love);//添加到LinearLayout中
			return ll;
		}        	
	}
}
