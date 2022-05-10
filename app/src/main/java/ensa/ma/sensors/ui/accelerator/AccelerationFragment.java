package ensa.ma.sensors.ui.accelerator;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;

import ensa.ma.sensors.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccelerationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccelerationFragment extends Fragment implements View.OnClickListener, SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LineChart chart;
    private RadioGroup group;
    private RadioButton X;
    private RadioButton Y;
    private RadioButton Z;
    private  static int value;
    private SensorManager mSensorManager;
    private Sensor mAccelarationSensor;
    static ArrayList<Entry> entries = new ArrayList<>();

    public AccelerationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccelerationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccelerationFragment newInstance(String param1, String param2) {
        AccelerationFragment fragment = new AccelerationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelarationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if( mAccelarationSensor == null){
            Toast.makeText(getContext(), R.string.message_neg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_acceleration, container, false);

        X=v.findViewById(R.id.x);
        Y=v.findViewById(R.id.y);
        Z=v.findViewById(R.id.z);
        X.setOnClickListener(this);
        Y.setOnClickListener(this);
        Z.setOnClickListener(this);
        chart = (LineChart) v.findViewById(R.id.chart);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v==X)  value=0;
        else if (v==Y) value=1;
        else value=1;
        entries.clear();
    }

    private void addEntry(SensorEvent event) {
        Date d = new Date();
        entries.add(new Entry(entries.size(), event.values[value]));
        LineDataSet dataSet = new LineDataSet(entries, "Accelartion - Time series");
        LineData data = new LineData(dataSet);
        Log.d("size", entries.size()+"");
        XAxis xAxis = chart.getXAxis();

        chart.setData(data);
        chart.notifyDataSetChanged();
        //refresh
        chart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelarationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        entries.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        entries.clear();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("Accelration", String.valueOf(event.values[0]));
        addEntry(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}