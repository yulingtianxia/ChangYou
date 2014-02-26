package com.hit.changyou;

import java.util.List;

import com.baidu.mapapi.search.MKPoiInfo;

import android.R.id;
import android.R.string;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseAdapter_search extends BaseAdapter {
	private Activity context_Activity;
	private List<MKPoiInfo> msgIds;

	public BaseAdapter_search(Activity context, List<MKPoiInfo> list)
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
		LinearLayout ll=new LinearLayout(context_Activity);
		ll.setOrientation(LinearLayout.HORIZONTAL);		//设置朝向	
		if(arg0 %2 == 0) 
		{
			ll.setBackgroundColor(context_Activity.getResources().getColor(R.color.listbgColor1));
		}
		else
			ll.setBackgroundColor(context_Activity.getResources().getColor(R.color.listbgColor2));
		ll.setPadding(5,5,5,5);//设置四周留白			
		//初始化ImageView
		ImageView  ii=new ImageView(context_Activity);
		ii.setImageDrawable(context_Activity.getResources().getDrawable(R.drawable.site_icon));//设置图片
		ii.setScaleType(ImageView.ScaleType.FIT_XY);
		ii.setLayoutParams(new Gallery.LayoutParams(60,40));
		ll.setVerticalGravity(Gravity.CENTER);
		ll.addView(ii);//添加到LinearLayout中
		//初始化TextView
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		//lp.setMargins(0, 0, 60, 0);
		LinearLayout ll_text=new LinearLayout(context_Activity);		
		
		lp.weight=150;
		ll_text.setLayoutParams(lp);
		ll_text.setWeightSum(1);
		ll_text.setOrientation(LinearLayout.VERTICAL);		//设置朝向	
		TextView tv=new TextView(context_Activity);
		String tempnane = msgIds.get(arg0).name;
//		if(tempnane.length()>9)
//		{
//			tempnane =tempnane.substring(0, 9);
//			tempnane =tempnane+"...";
//		}
		tv.setText(tempnane);//设置内容
	
		tv.setTextSize(18);//设置字体大小
		tv.setTextColor(context_Activity.getResources().getColor(R.color.ListTextColor));//设置字体颜色
		tv.setPadding(5,5,5,5);//设置四周留白
	    tv.setGravity(Gravity.LEFT);
	    
	    TextView tv_1=new TextView(context_Activity);//设定距离
	   String tempadd = msgIds.get(arg0).name;
//		if(tempadd.length()>11)
//			tempadd =tempadd.substring(0, 11)+"...";
	    tv_1.setText("地址: "+tempadd);//设置内容
	    tv_1.setTextSize(12);//设置字体大小
	    tv_1.setTextColor(context_Activity.getResources().getColor(R.color.Sites_distanct_TextColor));//设置字体颜色
	    tv_1.setPadding(5,0,0,0);//设置四周留白
	    
	    ll_text.addView(tv);
	    ll_text.addView(tv_1);
		ll.addView(ll_text);//添加到LinearLayout中		
		
		LinearLayout ll_love=new LinearLayout(context_Activity);
		ll_love.setOrientation(LinearLayout.VERTICAL);		//设置朝向			
		ll_love.setWeightSum(2);
		ImageView  i_love=new ImageView(context_Activity);
		if(arg0%5==0 ||arg0%7==0)
		i_love.setImageDrawable(context_Activity.getResources().getDrawable(R.drawable.love));//设置图片
		else {
			i_love.setImageDrawable(context_Activity.getResources().getDrawable(R.drawable.loved));//设置图片
		}
		i_love.setScaleType(ImageView.ScaleType.FIT_XY);
		i_love.setLayoutParams(new Gallery.LayoutParams(42,42));
		i_love.setPadding(5, 5, 5, 5);
		ll.addView(i_love);
		ll.addView(ll_love);//添加到LinearLayout中
		
		return ll;
	}        	
}
