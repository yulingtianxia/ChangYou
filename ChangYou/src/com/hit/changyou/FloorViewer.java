package com.hit.changyou;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class FloorViewer extends Activity implements OnTouchListener {

	public static final int RESULT_CODE_NOFOUND = 200;

	ViewFlipper viewFlipper = null;
	float startX, endX;

	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	DisplayMetrics dm;
	ImageView imgView;
	Bitmap bitmap;
	ImageView btnback;
	EditText beginEditText;
	EditText endEditText;
	ImageView exchangeImageView;
	private ZoomImageView zoomImg;
	//ImageView navibtn;
	/** 最小缩放比例 */
	float minScaleR = 1.0f;
	/** 最大缩放比例 */
	static final float MAX_SCALE = 10f;

	/** 初始状态 */
	static final int NONE = 0;
	/** 拖动 */
	static final int DRAG = 1;
	/** 缩放 */
	static final int ZOOM = 2;

	/** 当前模式 */
	int mode = NONE;
	String value;
	PointF prev = new PointF();
	PointF mid = new PointF();
	float dist = 1f;
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
        	Intent intent = new Intent();
			intent.setClass(FloorViewer.this, AirportActivity.class);
			startActivity(intent);
			finish();
            return false; 
        } 
        return false; 
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}
	@Override
	public void onCreate(Bundle savedInstanceState) throws OutOfMemoryError{
		super.onCreate(savedInstanceState);
		// 获取图片资源
		Intent intent = getIntent();
		value = intent.getStringExtra("val");
		Log.i("yxy", value);
		Toast.makeText(FloorViewer.this, value, Toast.LENGTH_SHORT).show();
		if (value.equals("floor01")) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.floor_01);
		} else if (value.equals("floor02")) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.floor_02);
		} else {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.floor_03);
		}

		setContentView(R.layout.floorshow);
//		BitmapFactory.Options.this.inTempStorage=;
		imgView = (ImageView) findViewById(R.id.imageView1);// 获取控件
//		zoomImg = (ZoomImageView)findViewById(R.id.image);
//        zoomImg.setImage(bitmap);
		if (!bitmap.isRecycled()) {
			imgView.setImageBitmap(bitmap);// 填充控件
		}
		/*navibtn = (ImageView)findViewById(R.id.ImageView01);
		navibtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});*/
		imgView.setOnTouchListener(this);// 设置触屏监听
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
		minZoom();
		center();
		imgView.setImageMatrix(matrix);
		//
		// viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
		btnback = (ImageView)findViewById(R.id.back_port);
		btnback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(FloorViewer.this, AirportActivity.class);
				startActivity(intent);
			}
		});
		beginEditText = (EditText)findViewById(R.id.editText1);
		beginEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Log.i("yxy", "editbegin");
				if (!beginEditText.getText().toString().equals("")&&!endEditText.getText().toString().equals("")) {
					exchangeImageView.setVisibility(ImageView.VISIBLE);
				}
				else {
					exchangeImageView.setVisibility(ImageView.INVISIBLE);
				}
			}
		});
		endEditText = (EditText)findViewById(R.id.editText2);
		endEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Log.i("yxy", "editend");
				if (!beginEditText.getText().toString().equals("")&&!endEditText.getText().toString().equals("")) {
					exchangeImageView.setVisibility(ImageView.VISIBLE);
				}
				else {
					exchangeImageView.setVisibility(ImageView.INVISIBLE);
				}
			}
		});
		exchangeImageView = (ImageView)findViewById(R.id.imageView2);
		exchangeImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (exchangeImageView.getVisibility()==ImageView.VISIBLE) {
					String temp=beginEditText.getText().toString();
					beginEditText.setText(endEditText.getText());
					endEditText.setText(temp);
				}
				
			}
		});
	}

	public void SureOnClick(View v) {

	}

	/**
	 * 触屏监听
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) throws OutOfMemoryError{

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 主点按下
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			prev.set(event.getX(), event.getY());
			mode = DRAG;
			startX = event.getX();
			break;
		// 副点按下
		case MotionEvent.ACTION_POINTER_DOWN:
			dist = spacing(event);
			// 如果连续两点距离大于10，则判定为多点模式
			if (spacing(event) > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			endX = event.getX();
			mode = NONE;
			// savedMatrix.set(matrix);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - prev.x, event.getY()
						- prev.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = newDist / dist;
					matrix.postScale(tScale, tScale, mid.x, mid.y);
				}
			}
			break;
		}
		imgView.setImageMatrix(matrix);
		CheckView();
		return true;
	}

	/**
	 * 限制最大最小缩放比例，自动居中
	 */
	private void CheckView() throws OutOfMemoryError{
		float f[] = new float[9];
		matrix.getValues(f);
		float x1 = f[0] * 0 + f[1] * 0 + f[2];
		float y1 = f[3] * 0 + f[4] * 0 + f[5];
		float x2 = f[0] * bitmap.getWidth() + f[1] * 0 + f[2];
		float y2 = f[3] * bitmap.getWidth() + f[4] * 0 + f[5];
		float x3 = f[0] * 0 + f[1] * bitmap.getHeight() + f[2];
		float y3 = f[3] * 0 + f[4] * bitmap.getHeight() + f[5];
		float x4 = f[0] * bitmap.getWidth() + f[1] * bitmap.getHeight() + f[2];
		float y4 = f[3] * bitmap.getWidth() + f[4] * bitmap.getHeight() + f[5];
		// 图片现宽度
		double width = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		// 缩放比率判断
		int widthScreen = dm.widthPixels;
		int heightScreen = dm.heightPixels;
		// if (width < widthScreen / 3 || width > widthScreen * 3) {
		//
		// }
		// 出界判断
		if (mode == DRAG) {
			if ((x1 < widthScreen / 3 && x2 < widthScreen / 3
					&& x3 < widthScreen / 3 && x4 < widthScreen / 3)
					|| (y1 < heightScreen / 3 && y2 < heightScreen / 3
							&& y3 < heightScreen / 3 && y4 < heightScreen / 3)) {
				Intent intent = new Intent(FloorViewer.this, FloorViewer.class);
				String value = getIntent().getStringExtra("val");
				if (value.equals("floor01"))
					intent.putExtra("val", "floor02");
				if (value.equals("floor02"))
					intent.putExtra("val", "floor03");
				if (value.equals("floor03"))
					intent.putExtra("val", "floor01");
				startActivityForResult(intent, 0);

			}
			if ((x1 > widthScreen * 2 / 3 && x2 > widthScreen * 2 / 3
					&& x3 > widthScreen * 2 / 3 && x4 > widthScreen * 2 / 3)
					|| (y1 > heightScreen * 2 / 3 && y2 > heightScreen * 2 / 3
							&& y3 > heightScreen * 2 / 3 && y4 > heightScreen * 2 / 3)) {
				Intent intent = new Intent(FloorViewer.this, FloorViewer.class);
				String value = getIntent().getStringExtra("val");
				if (value.equals("floor01"))
					intent.putExtra("val", "floor03");
				if (value.equals("floor02"))
					intent.putExtra("val", "floor01");
				if (value.equals("floor03"))
					intent.putExtra("val", "floor02");
				startActivityForResult(intent, 0);
			}
		}

		if (mode == ZOOM) {
			if (f[0] < minScaleR) {
				// Log.d("", "当前缩放级别:"+p[0]+",最小缩放级别:"+minScaleR);
				matrix.setScale(minScaleR, minScaleR);
			}
			if (f[0] > MAX_SCALE) {
				// Log.d("", "当前缩放级别:"+p[0]+",最大缩放级别:"+MAX_SCALE);
				matrix.set(savedMatrix);
			}
		}
		center();
	}

	/**
	 * 最小缩放比例，最大为100%
	 */
	private void minZoom() {
		minScaleR = Math.min(
				(float) dm.widthPixels / (float) bitmap.getWidth(),
				(float) dm.heightPixels / (float) bitmap.getHeight());
		if (minScaleR < 1.0) {
			matrix.postScale(minScaleR, minScaleR);
		}
	}

	private void center() {
		/*
		 * if (endX > startX) { // 向右滑动 viewFlipper.setInAnimation(this,
		 * R.anim.push_right_in); viewFlipper.setOutAnimation(this,
		 * R.anim.push_right_out); viewFlipper.showNext(); } if (event.getX() <
		 * startX) { // 向左滑动 viewFlipper.setInAnimation(this,
		 * R.anim.push_left_in); viewFlipper.setOutAnimation(this,
		 * R.anim.push_left_out); viewFlipper.showPrevious(); }
		 */

		center(true, true);
	}

	/**
	 * 横向、纵向居中
	 */
	protected void center(boolean horizontal, boolean vertical) throws OutOfMemoryError{

		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
			int screenHeight = dm.heightPixels;
			if (height < screenHeight) {
				deltaY = (screenHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < screenHeight) {
				deltaY = imgView.getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int screenWidth = dm.widthPixels;
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < screenWidth) {
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY-100);
	}

	/**
	 * 两点的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 两点的中点
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	public static class ImageViewOnClickListener implements
			View.OnClickListener {
		private Context context;
		private String img_path;

		public ImageViewOnClickListener(Context context, String img_path) {
			this.img_path = img_path;
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			if (img_path != null) {
				Intent intent = new Intent(context, FloorViewer.class);
				intent.putExtra("PhotoPath", img_path);
				context.startActivity(intent);
			}

		}
	}
}