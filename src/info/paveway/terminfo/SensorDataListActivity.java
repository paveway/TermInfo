package info.paveway.terminfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SensorDataListActivity extends Activity {

    /** ADビュー */
    private AdView mAdView;

    private static final Map<Integer, String> mSensorInfoMap3;
    private static final Map<Integer, String> mSensorInfoMap9;
    private static final Map<Integer, String> mSensorInfoMap14;
    private static final Map<Integer, String> mSensorInfoMap18;
    private static final Map<Integer, String> mSensorInfoMap19;
    static {
        mSensorInfoMap3 = new HashMap<Integer, String>();
        mSensorInfoMap3.put(Sensor.TYPE_ACCELEROMETER,  "加速度センサー");
        mSensorInfoMap3.put(Sensor.TYPE_GYROSCOPE,      "ジャイロスコープ");
        mSensorInfoMap3.put(Sensor.TYPE_LIGHT,          "照度センサー");
        mSensorInfoMap3.put(Sensor.TYPE_MAGNETIC_FIELD, "地磁気センサー");
        mSensorInfoMap3.put(Sensor.TYPE_PRESSURE,       "圧力センサー");
        mSensorInfoMap3.put(Sensor.TYPE_PROXIMITY,      "近接センサー");

        mSensorInfoMap9 = new HashMap<Integer, String>();
        mSensorInfoMap9.put(Sensor.TYPE_GRAVITY,             "重力センサー");
        mSensorInfoMap9.put(Sensor.TYPE_LINEAR_ACCELERATION, "線形加速センサー");
        mSensorInfoMap9.put(Sensor.TYPE_ROTATION_VECTOR,     "回転ベクトルセンサー");

        mSensorInfoMap14 = new HashMap<Integer, String>();
        mSensorInfoMap14.put(Sensor.TYPE_AMBIENT_TEMPERATURE, "照度センサー");
        mSensorInfoMap14.put(Sensor.TYPE_RELATIVE_HUMIDITY,   "相対温度センサー");

        mSensorInfoMap18 = new HashMap<Integer, String>();
        mSensorInfoMap18.put(Sensor.TYPE_GAME_ROTATION_VECTOR,        "回転センサー");
        mSensorInfoMap18.put(Sensor.TYPE_GYROSCOPE_UNCALIBRATED,      "ジャイロスコープセンサー(未補正)");
        mSensorInfoMap18.put(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, "地磁気センサー(未補正)");
        mSensorInfoMap18.put(Sensor.TYPE_SIGNIFICANT_MOTION,          "モーションセンサー");

        mSensorInfoMap19 = new HashMap<Integer, String>();
        mSensorInfoMap19.put(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, "地磁気回転ベクトルセンサー");
        mSensorInfoMap19.put(Sensor.TYPE_STEP_COUNTER,                "ステップカウンターセンサー");
        mSensorInfoMap19.put(Sensor.TYPE_STEP_DETECTOR,               "ステップ検出センサー");
    }

    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_data_list_activity);

        // AdView をリソースとしてルックアップしてリクエストを読み込む
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest =
                new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
        mAdView.loadAd(adRequest);

        mSensorManager = (SensorManager)getSystemService(Activity.SENSOR_SERVICE);

        List<Data> list = new ArrayList<Data>();
        setList(list, 3,  mSensorInfoMap3);
        setList(list, 9,  mSensorInfoMap9);
        setList(list, 14, mSensorInfoMap14);
        setList(list, 18, mSensorInfoMap18);
        setList(list, 19, mSensorInfoMap19);

        DataArrayAdapter adapter = new DataArrayAdapter(SensorDataListActivity.this, 0, list);
        ListView listView = (ListView)findViewById(R.id.sensorDataListView);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        if (null != mAdView) {
            mAdView.destroy();
        }

        super.onDestroy();
    }

    private void setList(List<Data> list, int apiLevel, Map<Integer, String> sensorMap) {
        for (Integer type : sensorMap.keySet()) {
            Data info = new Data();
            info.setName(sensorMap.get(type));
            if (apiLevel <= Build.VERSION.SDK_INT) {
                List<Sensor> sensorList = mSensorManager.getSensorList(type);
                if (0 < sensorList.size()) {
                    info.setValid(true);

                } else {
                    info.setValid(false);
                }

            } else {
                info.setValid(false);
            }
            list.add(info);
        }
    }

    private class Data {

        private String mName;

        private boolean mValid;

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public void setValid(boolean valid) {
            mValid = valid;
        }

        public boolean getValid() {
            return mValid;
        }
    }

    private class DataArrayAdapter extends ArrayAdapter<Data> {

        public DataArrayAdapter(Context context, int resource, List<Data> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = new TextView(SensorDataListActivity.this);
            }

            Data data = (Data)getItem(position);
            TextView sensorLabel = (TextView)convertView;
            sensorLabel.setText(data.getName());
            if (!data.getValid()) {
                sensorLabel.setTextColor(Color.GRAY);
            }

            return convertView;
        }
    }
}
