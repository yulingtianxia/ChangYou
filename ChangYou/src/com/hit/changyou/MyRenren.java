package com.hit.changyou;


import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.hit.changyou.model.User;
import com.renren.api.connect.android.Renren;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class MyRenren  extends Application{ 
	private Renren renren;
	private static MyRenren mInstance = null;
    public boolean m_bKeyRight = true;
    BMapManager mBMapManager = null;
    public String cityString="";
    public static final String strKey = "0A3B1255B4255B190C26D77AC9DF33DE625C0E1C";
    
    private User user=new User();
    
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Renren getRenren() {
		return renren;
	}

	public void setRenren(Renren renren) {
		this.renren = renren;
	}
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		initEngineManager(this);
	}
	
	@Override
	//建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
	    if (mBMapManager != null) {
            mBMapManager.destroy();
            mBMapManager = null;
        }
		super.onTerminate();
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
            Toast.makeText(MyRenren.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	
	public static MyRenren getInstance() {
		return mInstance;
	}
	
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(MyRenren.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(MyRenren.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
                Toast.makeText(MyRenren.getInstance().getApplicationContext(), 
                        "请开启互联网", Toast.LENGTH_LONG).show();
                MyRenren.getInstance().m_bKeyRight = false;
            }
        }
    }
}
