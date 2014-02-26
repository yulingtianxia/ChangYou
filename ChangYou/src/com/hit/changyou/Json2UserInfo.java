package com.hit.changyou;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import com.hit.changyou.model.User;

import android.util.Log;
import android.util.SparseArray;

public class Json2UserInfo {
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User parseUser(String json){
		JSONTokener jsonParser = new JSONTokener(json);
		String tinyurl="";
		String birthday="";
		String media_type="";
		int sex=0;
		String username="";
		String social_uid="";
		String access_token="";
		try {
			
			
			user = new User();
//			SparseArray<User> _arrayUser = new SparseArray<User>();
			// JSONTokener对象开始读取json数据
			Log.i("yxy", json);
			JSONObject _joRoot = (JSONObject) jsonParser.nextValue();
			tinyurl = _joRoot.getString("tinyurl");
			birthday = _joRoot.getString("birthday");
			media_type = _joRoot.getString("media_type");
			sex = _joRoot.getInt("sex");
			username = _joRoot.getString("username");
			social_uid = _joRoot.getString("social_uid");
			access_token = _joRoot.getString("access_token");
			user.setAccess_token(access_token);
			user.setBirthday(birthday);
			user.setMedia_type(media_type);
			user.setSex(sex);
			user.setSocial_uid(social_uid);
			user.setTinyurl(tinyurl);
			user.setUsername(username);
			//提示数据获取及数据解析成功
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			user.setAccess_token(access_token);
			user.setBirthday(birthday);
			user.setMedia_type(media_type);
			user.setSex(sex);
			user.setSocial_uid(social_uid);
			user.setTinyurl(tinyurl);
			user.setUsername(username);
		}
		return user;
	}
}
