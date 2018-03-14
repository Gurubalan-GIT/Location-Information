package a1.latitudeandlongitude;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import a1.test.R;

public class Compass extends AppCompatActivity implements SensorEventListener{
    private ImageView imageView;
    private  float[] mGravity=new float[3];
    private float[] mGeomagnetic=new float[3];
    private float Azimuth=0f;
    private float CurrectAzimuth=0f;
    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        imageView=(ImageView)findViewById(R.id.compass);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);

    }
    @Override
    protected void onPause()
    {
            super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha=0.97f;
        synchronized (this){
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                mGravity[0]=alpha*mGravity[0]+(1-alpha)*event.values[0];
                mGravity[1]=alpha*mGravity[1]+(1-alpha)*event.values[1];
                mGravity[2]=alpha*mGravity[2]+(1-alpha)*event.values[2];
            }
            if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                mGeomagnetic[0]=alpha*mGeomagnetic[0]+(1-alpha)*event.values[0];
                mGeomagnetic[1]=alpha*mGeomagnetic[1]+(1-alpha)*event.values[1];
                mGeomagnetic[2]=alpha*mGeomagnetic[2]+(1-alpha)*event.values[2];
            }
            float R[]=new float[9];
            float I[]=new float[9];
            boolean success=SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic);
            if(success)
            {
                float orientation[]=new float[3];
                SensorManager.getOrientation(R,orientation);
                Azimuth=(float)Math.toDegrees(orientation[0]);
                Azimuth=(Azimuth+360)%360;
                Animation animation=new RotateAnimation(-CurrectAzimuth,-Azimuth,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                CurrectAzimuth=Azimuth;
                animation.setDuration(100);
                animation.setRepeatCount(0);
                animation.setFillAfter(true);
                imageView.startAnimation(animation);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
