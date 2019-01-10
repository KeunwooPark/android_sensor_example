package kr.ac.kaist.hcil.keunwoo.magsensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private TextView mxTextView;
    private TextView myTextView;
    private TextView mzTextView;
    private TextView accuracyTextView;
    private TextView mTotalTextView;

    private Sensor mMagSensor;
    private SensorManager mSensorManager;
    private final float[] mMagnetometerReading = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mxTextView = (TextView) findViewById(R.id.mx_text);
        myTextView = (TextView) findViewById(R.id.my_text);
        mzTextView = (TextView) findViewById(R.id.mz_text);
        mTotalTextView = (TextView) findViewById(R.id.mTotal_text);
        accuracyTextView = findViewById(R.id.accuracy_text);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mSensorManager.registerListener(this, mMagSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            System.arraycopy(event.values, 0, mMagnetometerReading,  0, mMagnetometerReading.length);
            mxTextView.setText(String.format("%.2f", mMagnetometerReading[0]));
            myTextView.setText(String.format("%.2f", mMagnetometerReading[1]));
            mzTextView.setText(String.format("%.2f", mMagnetometerReading[2]));
            float magSquare = mMagnetometerReading[0] *  mMagnetometerReading[0] +  mMagnetometerReading[1] *  mMagnetometerReading[1] +  mMagnetometerReading[2] *  mMagnetometerReading[2];
            double magnitude = Math.sqrt((double) magSquare);
            mTotalTextView.setText(String.format("%.2f", magnitude));

            String accuracyText = accuracyToText(event.accuracy);
            accuracyTextView.setText(accuracyText);
        }
    }

    private String accuracyToText(int accuracy)
    {
        String accText = "";
        switch (accuracy) {
            case 0:
                accText = "unreliable";
                break;
            case 1:
                accText = "low accuracy";
                break;
            case 2:
                accText = "medium accuracy";
                break;
            case 3:
                accText = "high accuracy";
                break;
        }
        return accText;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }
}
