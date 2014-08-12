package info.paveway.terminfo;

import info.paveway.log.Logger;
import info.paveway.util.ClassUtil;
import info.paveway.util.StringUtil;

import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 端末情報
 * ディスプレイデータリスト画面クラス
 *
 * @version 1.0 新規作成
 * @author paveway.info@gmail.com
 * Copyright (C) 2014 paveway.info. All rights reserved.
 *
 */
public class DisplayDataListActivity extends AbstractBaseListActivity {

    /** ロガー */
    private Logger mLogger = new Logger(DisplayDataListActivity.class);

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

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        setData(        list,          "ディスプレイID",      String.valueOf(display.getDisplayId()));
        setData(        list,          "リフレッシュレート",  String.valueOf(display.getRefreshRate()));
        setData(        list,          "ローテーション",      String.valueOf(display.getRotation()));
        setData(        list,          "xdpi",                String.valueOf(displayMetrics.xdpi));
        setData(        list,          "ydpi",                String.valueOf(displayMetrics.ydpi));
        setData(        list,          "幅(Pixel)",           String.valueOf(displayMetrics.widthPixels));
        setData(        list,          "高さ(Pixel)",         String.valueOf(displayMetrics.heightPixels));
        setData(        list,          "密度",                String.valueOf(displayMetrics.density));
        setData(        list,          "密度(dpi)",           String.valueOf(displayMetrics.densityDpi));
        setData(        list,          "スケーリング密度",    String.valueOf(displayMetrics.scaledDensity));
        setDataRectSize(list, display, "矩形サイズ(幅x高さ)", "getRectSize", 13);
        setDataSize(    list, display, "サイズ(幅x高さ)",     "getSize",     13);
        setData(        list, display, "名称",                "getName",     17);
        setDataSize(    list, display, "実サイズ(幅x高さ)",   "getRealSize", 17);
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
        return layoutInflator.inflate(R.layout.row_display_data, null);
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

        TextView name = (TextView)convertView.findViewById(R.id.displayDataRowName);
        name.setText(data.getName());

        TextView valueLabel = (TextView)convertView.findViewById(R.id.displayDataRowValue);
        valueLabel.setText(data.getValue());

        mLogger.d("OUT(OK)");
    }

    /**
     * データを設定する。
     *
     * @param list リスト
     * @param name 名前
     * @param value 値
     */
    @Override
    protected void setData(List<Data> list, String name, String value) {
        mLogger.d("IN");

        Data data = new Data();

        data.setName(name);
        if (StringUtil.isNullOrEmpty(value)) {
            value = "未設定";
        }
        data.setValue(value);

        list.add(data);

        mLogger.d("OUT(OK)");
    }

    /**
     * データを設定する。
     *
     * @param list リスト
     * @param display ディスプレイ
     * @param name 名前
     * @param method メソッド
     * @param apiLevel APIレベル
     */
    private void setData(List<Data> list, Display display, String name, String method, int apiLevel) {
        mLogger.d("IN");

        Data data = new Data();
        data.setName(name);
        String value = "未設定";
        if (apiLevel <= Build.VERSION.SDK_INT) {
            Object result = ClassUtil.invokeMethod(display, method, null, null);
            if (null != result) {
                if (String.class != result.getClass()) {
                    if (Boolean.class == result.getClass()) {
                        value = ((Boolean)result) ? "YES" : "NO";
                    } else {
                        value = String.valueOf(result);
                    }

                } else if (String.class == result.getClass()){
                    value = (String)result;
                }
            }

        } else {
            value = "未対応";
        }
        data.setValue(value);

        list.add(data);

        mLogger.d("OUT(OK)");
    }

    /**
     * サイズデータを設定する。
     *
     * @param list リスト
     * @param display ディスプレイ
     * @param name 名前
     * @param method メソッド
     * @param apiLevel APIレベル
     */
    @SuppressWarnings("rawtypes")
    private void setDataSize(List<Data> list, Display display, String name, String method, int apiLevel) {
        mLogger.d("IN");

        Data data = new Data();
        data.setName(name);

        String value = "未設定";
        if (apiLevel <= Build.VERSION.SDK_INT) {
            Class[] parameterTypes = new Class[]{Point.class};
            Point point = new Point();
            Object[] args = new Object[]{point};
            ClassUtil.invokeMethod(display, method, parameterTypes, args);
            value = String.valueOf(point.x) + "x" + String.valueOf(point.y);
        }
        data.setValue(value);

        list.add(data);

        mLogger.d("OUT(OK)");
    }

    /**
     * 矩形サイズデータを設定する。
     *
     * @param list リスト
     * @param display ディスプレイ
     * @param name 名前
     * @param method メソッド
     * @param apiLevel APIレベル
     */
    @SuppressWarnings("rawtypes")
    private void setDataRectSize(List<Data> list, Display display, String name, String method, int apiLevel) {
        mLogger.d("IN");

        Data data = new Data();
        data.setName(name);

        String value = "未設定";
        if (apiLevel <= Build.VERSION.SDK_INT) {
            Class[] parameterTypes = new Class[]{Rect.class};
            Rect rect = new Rect();
            Object[] args = new Object[]{rect};
            ClassUtil.invokeMethod(display, method, parameterTypes, args);
            value = String.valueOf(rect.width()) + "x" + String.valueOf(rect.height());
        }
        data.setValue(value);

        list.add(data);

        mLogger.d("OUT(OK)");
    }
}
