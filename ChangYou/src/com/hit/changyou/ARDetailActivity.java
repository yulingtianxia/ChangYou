package com.hit.changyou;

import android.app.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ARDetailActivity extends Activity {
	
	private static final String TAG="ARDetailActivity";
	protected static final int TASK_DONE = 0;
	protected static final int TASK_CANCEL = 1;
	protected static final int TASK_UNDONE = 2;
	Handler handle;
	String id=null;
	private ImageView iv;
	private TextView tv_desc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ardetail);
		
		id = this.getIntent().getExtras().getString("ID");
		String name = this.getIntent().getExtras().getString("NAME");
		String desc = this.getIntent().getExtras().getString("DESC");
		
		((TextView) this.findViewById(R.id.tv_Name)).setText(name);
		tv_desc=((TextView) this.findViewById(R.id.tv_Desc));
		tv_desc.setText(desc);
		tv_desc.setMovementMethod(ScrollingMovementMethod.getInstance());
		iv=(ImageView) findViewById(R.id.iv);
		/*如果可用空间小于500KB，那么提示用户空间不足*/
		if(getAvailaleSize()<500){
			Toast lowStorageToast=Toast.makeText(this, "检测到您手机的存储空间不足,所以不进行图片缓存(这样每次加载图片时会耗费一些流量)", 
					Toast.LENGTH_LONG);
			lowStorageToast.show();
		}
		wsAsyncTask wsTask = new wsAsyncTask(this);
	    wsTask.execute(id);
	    handle=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what){
				case TASK_DONE:
					Log.v(TAG, "task is done");
					iv.setImageBitmap((Bitmap)msg.obj);
//					((Bitmap)msg.obj).recycle();      
					/*
					 * 不加这句已经运行正常,加上后会异常终止,因为在这里
					 *释放它太早了,log显示此时Canvas还在使用它
					 */
					break;
				case TASK_CANCEL:
					Log.v(TAG, "task is cancelled");
					iv.setImageResource(R.drawable.defaultimg);
					break;
				case TASK_UNDONE:
					Log.v(TAG, "task is undone");
					break;
				}
			}
	    	
	    };
		
	}

	/*获取外部存储可用空间*/
	public static long getAvailaleSize(){
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath()); 
		long blockSize = stat.getBlockSize(); 
		long availableBlocks = stat.getAvailableBlocks();
		return (availableBlocks * blockSize)/1024;     //以KIB 单位
		//(availableBlocks * blockSize)/1024 /1024  MIB单位
		}
	/*
	 * 这个函数会对图片的大小进行判断，并得到合适的缩放比例，比如2即1/2,3即1/3   
	 */
	static int computeSampleSize(BitmapFactory.Options options, int tarWidth, int tarHeight) {   
	    int realWidth = options.outWidth;   
	    int realHeight = options.outHeight;   
	    int candidateW = realWidth / tarWidth;   
	    int candidateH = realHeight / tarHeight;   
	    int candidate = Math.max(candidateW, candidateH);   
	    if (candidate == 0){
	    	Log.v(TAG, "realWidth:"+realWidth+" realHeight:" + realHeight +
		    		" return " + candidate);   
	    	return 1;   
	    }
	    
	    /*以下部分是为了防止这种情况:如果width或height只有一个特别大,另一个与real值接近,那么得到的candidate
	     * 值是不准确,是过大的,所以要调整,适当将其减一*/
	    if (candidate > 1) {   
	        if ((realWidth > tarWidth) && (realWidth / candidate) < tarWidth)   
	            candidate -= 1;   
	    }   
	    if (candidate > 1) {   
	        if ((realHeight > tarHeight) && (realHeight / candidate) < tarHeight)   
	            candidate -= 1;   
	    }   
	    Log.v(TAG, "realWidth:"+realWidth+" realHeight:" + realHeight +
	    		" return " + candidate);   
	    return candidate;   
	}  


	private class wsAsyncTask extends AsyncTask<String, Integer, Bitmap> {
		//应用在sd卡上的图片保存目录
		String dir=Environment.getExternalStorageDirectory()+"/ARNavigator/images";
		File mydir=null;
		File file=null;
		private Toast toast;
		private Bitmap img=null;
		private Drawable drawable=null;
		private ProgressDialog progress;
		
		/*
		 * 检查某图片是否已经在sdcard存在的方法
		 */
		boolean checkIfExist(String param){
			mydir=new File(dir);
    		Log.v(TAG, "mydir is "+dir);
			if(mydir.exists()){
				Log.v(TAG, "mydir exists!");
				file=new File(mydir, param+".jpg");
				if(file.exists()){
					Log.v(TAG, "file exists!");
					drawable = Drawable.createFromPath(dir+"/"+param+".jpg");

					if(drawable!=null){
						Log.v(TAG, "drawable is not null!");
						return true;
					}
					else{ 
						Log.v(TAG, "drawable is null!");
						return false;
					}
					
				}
			}
			return false;
		}
		
		/*
		 * 保存图片到sd卡
		 */
		void saveToSD(String param){
			boolean tag_save=false;
			if(!mydir.exists()){
				if(!mydir.mkdirs()){
					Log.v(TAG, "Cannot create the dir!");
				}
				else
					tag_save=true;
			}
			else{
				tag_save=true;
			}
			
			/*如果可用空间小于500KB，则不进行缓存*/
			if(getAvailaleSize()<500){
				tag_save=false;
			}
			if(tag_save==true){
				//目录已经建好或已经存在
				File file=new File(mydir, param+".jpg");
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(file);
					img.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					Log.v(TAG, "save image to sdcard!");
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		
		public wsAsyncTask(Context context){
			progress=new ProgressDialog(context);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setIndeterminate(false);
			progress.setCancelable(true);
//			progress.setCanceledOnTouchOutside(true);   //设置是否可以点击屏幕停止加载,这样容易误操作,所以不用了
			progress.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					toast.setText("已取消图片加载");
					toast.show();
					wsAsyncTask.this.cancel(true);
				}
			});
			progress.setMessage("图片加载中...");
			progress.show();
			toast=Toast.makeText(context, "按返回键可取消图片加载(如果您的网络状况很差)", Toast.LENGTH_LONG);
		}
    	@Override
    	protected Bitmap doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		if((checkIfExist(params[0]))==true){
    			img=((BitmapDrawable)drawable).getBitmap();
    			return img;
    		}
    		toast.show();
			Log.v(TAG, "load img from web service!");
    		/*
    		 * 用虚拟器运行时，IP可以是电脑的真实IP或者10.0.2.2；用真机运行时，要用真实IP而且与电脑要在同一个网段
			 * 127.0.0.1和local host代表虚拟器本身
			 */
//    		String wsUrl="http://10.0.2.2:8080/axis2/services/picTransport?wsdl";
//    		String wsUrl="http://192.168.1.103:8080/axis2/services/picTransport?wsdl";
//    		String wsUrl="http://192.168.44.1:8080/axis2/services/picTransport?wsdl";
    		String wsUrl="http://photo.hit.edu.cn/axis2/services/picTransport?wsdl";
    		String wsNamespace="http://ws.apache.org/axis2";
    		String wsMethodname="fileDownload";
    		SoapObject request=new SoapObject(wsNamespace, wsMethodname);
    		request.addProperty("id", params[0]);
    		SoapSerializationEnvelope envelop=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		envelop.bodyOut=request;
    		HttpTransportSE ht=new HttpTransportSE(wsUrl);	
    		try {
    			ht.call(null, envelop);
    			if(envelop.getResponse()!=null){
    				Object result=envelop.getResponse();
    				byte[] buffer=Base64.decode(result.toString());
    				BitmapFactory.Options options=new Options();
    				 options.inJustDecodeBounds = true;   
    				    //调用此方法得到options得到图片的大小   
    				 BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
    				    //我的目标是在大约400*480 pixel的画面上显示,所以调用computeSampleSize得到图片缩放的比例   
    				options.inSampleSize = computeSampleSize(options, 480, 400);   
    				    //OK,得到了缩放的比例，现在开始正式读入BitMap数据   
    				options.inJustDecodeBounds = false;   
    				options.inDither=false;    /*不进行图片抖动处理*/
    				options.inPreferredConfig=null;  /*设置让解码器以最佳方式解码*/
    				img=BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
    				saveToSD(params[0]);
    				try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    	    		return img;
    			}
    			else{}
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (XmlPullParserException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return null;
    	}
    	
    	
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.v(TAG, "in on postExcute!");
			progress.dismiss();
			toast.cancel();
			if(result!=null){
				Message message=handle.obtainMessage();
				message.what=TASK_DONE;
				message.obj=result;
				message.sendToTarget();
			}
			else{
				Message message=handle.obtainMessage();
				message.what=TASK_UNDONE;
				message.sendToTarget();
			}
		}
		@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    		Log.v(TAG, "task is cancelled!");
    		Message message=handle.obtainMessage();
			message.what=TASK_CANCEL;
			message.sendToTarget();
    		
    	}
    }
}
