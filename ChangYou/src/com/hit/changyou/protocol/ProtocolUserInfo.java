
package com.hit.changyou.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.hit.changyou.MyRenren;
import com.hit.changyou.model.User;

import Database.DAOHelper;
import Protocol.ProtocolBase;
import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.app.Activity;

public class ProtocolUserInfo extends ProtocolBase {
	//////////////////////////////////////////////////////////
	// 常量（宏）定义区
	// 网络获取数据的url
	public static final String URL = "http://changyouhit.duapp.com";
	// 网络获取数据的指令，与url组合使用
	public static final String COMMAND = "/user!addUser.action";
	
	public static  String VALUE;
	public ProtocolUserInfo(User user) {
		// TODO Auto-generated constructor stub
		VALUE="?user.social_uid="+user.getSocial_uid()+"&user.username="+user.getUsername()+"&user.sex="+user.getSex()+"&user.tinyurl="+user.getTinyurl()+"&user.birthday="+user.getBirthday()+"&user.access_token="+user.getAccess_token()+"&user.media_type="+user.getMedia_type();
	}

	/**
	 * 使用ProtocolUserListDelegate观察获取和解析数据是否成功
	 * @author v_zhengyan
	 *
	 */
	public interface ProtocolUserInfoDelegate {
		public void commitUserInfoSuccess();
		public void commitUserInfoFailed();
	}

	//创建ProtocolCurriculumDelegate对象
	ProtocolUserInfoDelegate delegate;
	Context context;
	
	public ProtocolUserInfo setContext(Context context){
		this.context = context;
		return this;
	}

	public ProtocolUserInfo setDelegate(ProtocolUserInfoDelegate delegate) {
		this.delegate = delegate;
		return this;
	}

	@Override
	public String packageProtocol() {
		// TODO Auto-generated method stub
		return "/user!addUser.action";
	}

	/**
	 * 解析传入的JSON数据字符串
	 */
	@Override
	public boolean parseProtocol(String json) {
		// TODO Auto-generated method stub
		///////////////////////////////////////////////////////
		// json数据解析区
//			Log.i("yxy", json);
			delegate.commitUserInfoSuccess();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return true;
	}

}
