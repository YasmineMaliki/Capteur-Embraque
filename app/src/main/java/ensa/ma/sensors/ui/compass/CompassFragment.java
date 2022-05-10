package ensa.ma.sensors.ui.compass;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;

import ensa.ma.sensors.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompassFragment extends Fragment implements SensorEventListener {

    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    private Sensor sensorAcc;
    private Sensor sensorAmf;
    TextView tvHeading;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries=new ArrayList<>();
    float degree=0;

    private float[] rotationMatri =new float[9];
    private float[]arrayAcc = new float[3];
    private float[] arrayAmf = new float[3];

    public CompassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_compass, container, false);

        image = (ImageView) root.findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) root.findViewById(R.id.tvHeading);
        lineChart = root.findViewById(R.id.lineChart);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // for the system's orientation sensor registered listeners
       // mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
         //       SensorManager.SENSOR_DELAY_GAME);

        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null && mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!=null ){
            sensorAcc=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorAmf = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, sensorAmf, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            arrayAcc =event.values;
        }
        else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            arrayAcc =event.values;
        }




        degree = Math.round(event.values[0]);
        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
        image.setRotation(degree);

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        addEntry(event);
      //  image.animate().rotation(360f).setDuration(2000);

    }

    private void addEntry(SensorEvent event) {
        Date d = new Date();
        lineEntries.add(new Entry(lineEntries.size(), degree));
        LineDataSet dataSet = new LineDataSet(lineEntries, "Accelartion - Time series");
        LineData data = new LineData(dataSet);
        Log.d("size", lineEntries.size()+"");
        XAxis xAxis = lineChart.getXAxis();

        lineChart.setData(data);
        lineChart.notifyDataSetChanged();
        //refresh
        lineChart.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
