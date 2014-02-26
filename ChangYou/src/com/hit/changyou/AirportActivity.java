package com.hit.changyou;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AirportActivity extends Activity{
	float point_x = -1, point_y = -1;
	ImageView btnback;
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
        	Intent intent = new Intent();
			intent.setClass(AirportActivity.this, MainTabActivity.class);
			startActivity(intent);
			finish();
            return false; 
        } 
        return false; 
    }
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.bejing_airport);
	        btnback = (ImageView)findViewById(R.id.back_port);
	        btnback.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(AirportActivity.this, MainTabActivity.class);
					startActivity(intent);
					finish();
				}
			});
	      //布局响应点击事件  
	        LinearLayout layOut = (LinearLayout)findViewById(R.id.LinearLayout_airport1);  
	        layOut.setOnClickListener(new View.OnClickListener(){  
	        	
	            @Override  
	            public void onClick(View v) {  
	  
	            	 int top = v.getTop();
		               int bottom = v.getBottom();
		               int left = v.getLeft();
		               int right = v.getRight();
		               int heigh = bottom - top;
		               if(point_x < left+20 || point_x>right - 20)
		            	   return;
	            	if(point_y>top+10 && point_y < top+heigh/3-10)
	            	{
	            		
	            		Intent intent = new Intent(AirportActivity.this, FloorViewer.class);
	            		intent.putExtra("val", "floor03"); 
	            		AirportActivity.this.startActivity(intent);
	            		finish();
	            	}
	            	else if(point_y>top+heigh/3  && point_y < top+heigh/3*2-20)
	            	{
	            		
	            		Intent intent = new Intent(AirportActivity.this, FloorViewer.class);
	            		intent.putExtra("val", "floor02"); 
	            		AirportActivity.this.startActivity(intent);
	            		finish();
	            	}
	            	else if(point_y>top+heigh/3*2  && point_y < bottom-20)
	            	{
	            		
	            		Intent intent = new Intent(AirportActivity.this, FloorViewer.class);
	            		intent.putExtra("val", "floor01"); 
	            		AirportActivity.this.startActivity(intent);
	            		finish();
	            	}

	            	//Toast.makeText(AirportActivity.this,point_y+"布局被点击",Toast.LENGTH_SHORT).show();  
	            }  
	  
	        });  
	        layOut.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					point_x = arg1.getX();
					point_y = arg1.getY();
					return false;
				}});
	        
	 }

}
