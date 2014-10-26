package com.gmail.sachin.tottoricompass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
 
// ////////////////////////////////////////////////////////////
// 方位を取り出す
// ////////////////////////////////////////////////////////////
public class MainActivity extends Activity implements LocationListener, SensorEventListener {
 
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagneticField;
    private SurfaceViewExt mSurfaceViewExt;
    private LocationManager mLocationManager;
    
    // 加速度センサの値
    private float[] mAccelerometerValue = new float[3];
    // 磁気センサの値
    private float[] mMagneticFieldValue = new float[3];
    // 磁気センサの更新がすんだか
    private boolean mValidMagneticFiled = false;
    
    private int degreeToTottori = 0;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
 
        // センサーを取り出す
        this.mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mMagneticField = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.mSurfaceViewExt = new SurfaceViewExt(this);
        this.mLocationManager = 
        	     (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        // Criteriaオブジェクトを生成
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        
        LinearLayout parent = (LinearLayout)this.findViewById(R.id.surfaceView1);
        parent.setBackgroundColor(Color.YELLOW);
        
		FrameLayout fl = (FrameLayout)this.findViewById(R.id.kanban);

        ImageView iv = new ImageView(this);
        iv.setBackgroundColor(Color.BLUE);
        iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.back));
        fl.addView(iv);
        
        TextView tv = new TextView(this);
        tv.setText("test");
        tv.setTextColor(Color.WHITE);
        fl.addView(tv);
        
        Log.v("test", "" + fl.getHeight() + ", " + fl.getWidth());
 
        // LocationListenerを登録
        String provider = mLocationManager.getBestProvider(criteria, true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, this);
        parent.addView(mSurfaceViewExt);
    }
    
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
		FrameLayout fl = (FrameLayout)this.findViewById(R.id.kanban);
        
        int c = fl.getHeight() * Constatns.CENTER / Constatns.IMAGE_HEIGHT;
        int r = fl.getWidth() * Constatns.RIGHT / Constatns.IMAGE_WIDTH;
        int l = fl.getWidth() * Constatns.LEFT / Constatns.IMAGE_WIDTH;
        
        
        
    } 
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(
                this, this.mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        this.mSensorManager.registerListener(
                this, this.mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
        case Sensor.TYPE_ACCELEROMETER:
            this.mAccelerometerValue = event.values.clone();
            break;
        case Sensor.TYPE_MAGNETIC_FIELD:
            this.mMagneticFieldValue = event.values.clone();
            this.mValidMagneticFiled = true;
            break;
        }
 
        // 値が更新された角度を出す準備ができた
        if (this.mValidMagneticFiled) {
            // 方位を出すための変換行列
            float[] rotate = new float[16];			// 傾斜行列？
            float[] inclination = new float[16];	// 回転行列
 
            // うまいこと変換行列を作ってくれるらしい
            SensorManager.getRotationMatrix(
                    rotate, inclination,
                    this.mAccelerometerValue,
                    this.mMagneticFieldValue);
 
            // 方向を求める
            float[] orientation = new float[3];
            this.getOrientation(rotate, orientation);
 
            // 北を0°として、-180°～180°
            float degreeFromNorth = (float)Math.toDegrees(orientation[0]);
//            Log.i("onSensorChanged", "角度:" + degreeFromNorth + ", 角度:" + (degreeFromNorth - degreeToTottori));
 
            // アローを回転させる
            this.mSurfaceViewExt.addArrowDir(degreeFromNorth - degreeToTottori);
        }
    } 
 
    // ////////////////////////////////////////////////////////////
    // 画面が回転していることを考えた方角の取り出し
    public void getOrientation(float[] rotate, float[] out) {
 
        // ディスプレイの回転方向を求める(縦もちとか横持ちとか)
        Display disp = this.getWindowManager().getDefaultDisplay();
        int dispDir = disp.getRotation();
 
        // 画面回転してない場合はそのまま
        if (dispDir == Surface.ROTATION_0) {
            SensorManager.getOrientation(rotate, out);
 
            // 回転している
        } else {
            float[] outR = new float[16];
 
            // 90度回転
            if (dispDir == Surface.ROTATION_90) {
                SensorManager.remapCoordinateSystem(
                        rotate, SensorManager.AXIS_Y,SensorManager.AXIS_MINUS_X, outR);
                // 180度回転
            } else if (dispDir == Surface.ROTATION_180) {
                float[] outR2 = new float[16];
 
                SensorManager.remapCoordinateSystem(
                        rotate, SensorManager.AXIS_Y,SensorManager.AXIS_MINUS_X, outR2);
                SensorManager.remapCoordinateSystem(
                        outR2, SensorManager.AXIS_Y,SensorManager.AXIS_MINUS_X, outR);
                // 270度回転
            } else if (dispDir == Surface.ROTATION_270) {
                SensorManager.remapCoordinateSystem(
                        outR, SensorManager.AXIS_MINUS_Y,SensorManager.AXIS_MINUS_X, outR);
            }
            SensorManager.getOrientation(outR, out);
        }
    }

	@Override
	public void onLocationChanged(Location arg0) {
        degreeToTottori = Util.convertTo180(
        		Util.getDirection(arg0.getLatitude(), arg0.getLongitude(), Constatns.TOTTORI_LAT, Constatns.TOTTORI_LNG));
	}

	@Override
	public void onProviderDisabled(String arg0) {}
	@Override
	public void onProviderEnabled(String arg0) {}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
 
 
 
