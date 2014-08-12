package info.paveway.terminfo;

import info.paveway.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 端末情報
 * センサーデータリスト画面クラス
 *
 * @version 1.0 新規作成
 * @author paveway.info@gmail.com
 * Copyright (C) 2014 paveway.info. All rights reserved.
 *
 */
public class SensorDataListActivity extends AbstractBaseListActivity {

    /** ロガー */
    private Logger mLogger = new Logger(SensorDataListActivity.class);

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

    /** センサーマネージャ */
    private SensorManager mSensorManager;

    /**
     * 生成された時に呼び出される。
     *
     * @param savedInstanceState 保存した時のインスタンスの状態
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLogger.d("IN");

        // スーパークラスのメソッドを呼び出す。
        super.onCreate(savedInstanceState);

        mLogger.d("OUT(OK)");
    }

    /**
     * 終了した時に呼び出される。
     */
    @Override
    public void onDestroy() {
        mLogger.d("IN");

        // スーパークラスのメソッドを呼び出す。
        super.onDestroy();

        mLogger.d("OUT(OK)");
    }

    /**
     * リストデータを設定する。
     *
     * @param list リスト
     */
    @Override
    protected void setListData(List<Data> list) {
        mLogger.d("IN");

        mSensorManager = (SensorManager)getSystemService(Activity.SENSOR_SERVICE);

        setData(list, 3,  mSensorInfoMap3);
        setData(list, 9,  mSensorInfoMap9);
        setData(list, 14, mSensorInfoMap14);
        setData(list, 18, mSensorInfoMap18);
        setData(list, 19, mSensorInfoMap19);

        mLogger.d("OUT(OK)");
    }

    /**
     * ビューを設定する。
     *
     * @param convertView ビュー
     * @param data データ
     */
    @SuppressLint("InflateParams")
    @Override
    protected View getConvertView(LayoutInflater layoutInflator) {
        mLogger.d("IN");

        mLogger.d("OUT(OK)");
        return new TextView(SensorDataListActivity.this);
    }

    /**
     * ビューを設定する。
     *
     * @param convertView ビュー
     * @param data データ
     */
    @Override
    protected void setConvertView(View convertView, Data data) {
        mLogger.d("IN");

        TextView sensorLabel = (TextView)convertView;
        sensorLabel.setText(data.getName());
        if (!Boolean.parseBoolean(data.getValue())) {
            sensorLabel.setTextColor(Color.GRAY);
        }

        mLogger.d("OUT(OK)");
    }

    /**
     * データを設定する。
     *
     * @param list リスト
     * @param apiLevel APIレベル
     * @param sensorMap センサーマップ
     */
    private void setData(List<Data> list, int apiLevel, Map<Integer, String> sensorMap) {
        mLogger.d("IN");

        for (Integer type : sensorMap.keySet()) {
            Data data = new Data();
            data.setName(sensorMap.get(type));
            if (apiLevel <= Build.VERSION.SDK_INT) {
                List<Sensor> sensorList = mSensorManager.getSensorList(type);
                if (0 < sensorList.size()) {
                    data.setValue(String.valueOf(true));

                } else {
                    data.setValue(String.valueOf(false));
                }

            } else {
                data.setValue(String.valueOf(false));
            }
            list.add(data);
        }

        mLogger.d("OUT(OK)");
    }
}
