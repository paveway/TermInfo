package info.paveway.terminfo;

import info.paveway.log.Logger;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 端末情報
 * 抽象基底リスト画面クラス
 *
 * @version 1.0 新規作成
 * @author paveway.info@gmail.com
 * Copyright (C) 2014 paveway.info. All rights reserved.
 *
 */
public abstract class AbstractBaseListActivity extends Activity {

    /** ロガー */
    private Logger mLogger = new Logger(AbstractBaseListActivity.class);

    /** リソース */
    protected Resources mResources;

    /** ADビュー */
    protected AdView mAdView;

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

        // レイアウトを設定する。
        setContentView(R.layout.activity_data_list);

        // リソースを取得する。
        mResources = getResources();

        // ADビューをロードする。
        loadAdView();

        // リストビューを設定する。
        setListView();

        mLogger.d("OUT(OK)");
    }

    /**
     * 終了した時に呼び出される。
     */
    @Override
    public void onDestroy() {
        mLogger.d("IN");

        // ADビューが有効な場合
        if (null != mAdView) {
            // ADビューを終了する。
            mAdView.destroy();
        }

        // スーパークラスのメソッドを呼び出す。
        super.onDestroy();

        mLogger.d("OUT(OK)");
    }

    /**
     * リソース文字列を取得する。
     *
     * @param id リソースID
     * @return リソース文字列
     */
    protected String getResourceString(int id) {
        return mResources.getString(id);
    }

    /**
     * ADビューをロードする。
     */
    protected void loadAdView() {
        mLogger.d("IN");

        // AdView をリソースとしてルックアップしてリクエストを読み込む
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        AdRequest adRequest = builder.build();
        mAdView.loadAd(adRequest);

        mLogger.d("OUT(OK)");
    }

    /**
     * リストビューを設定する。
     */
    protected void setListView() {
        mLogger.d("IN");

        List<Data> list = new ArrayList<Data>();
        setListData(list);
        ArrayAdapter<Data> adapter = new DataArrayAdapter(this, 0, list);
        ListView listView = (ListView)findViewById(R.id.dataListView);
        listView.setAdapter(adapter);

        mLogger.d("OUT(OK)");
    }

    /**
     * データを設定する。
     *
     * @param list リスト
     * @param name 名前
     * @param value 値
     */
    protected void setData(List<Data> list, String name, String value) {
        mLogger.d("IN");

        Data data = new Data();
        data.setName(name);
        data.setValue(value);
        list.add(data);

        mLogger.d("OUT(OK)");
    }

    /**
     * リストデータを設定する。
     *
     * @param list リスト
     */
    protected abstract void setListData(List<Data> list);

    /**
     * ビューを設定する。
     *
     * @param convertView ビュー
     * @param data データ
     */
    @SuppressLint("InflateParams")
    protected View getConvertView(LayoutInflater layoutInflator) {
        mLogger.d("IN");

        mLogger.d("OUT(OK)");
        return layoutInflator.inflate(R.layout.row_data, null);
    }

    /**
     * ビューを設定する。
     *
     * @param convertView ビュー
     * @param data データ
     */
    protected void setConvertView(View convertView, Data data) {
        mLogger.d("IN");

        TextView name = (TextView)convertView.findViewById(R.id.rowDataName);
        name.setText(data.getName());

        TextView value = (TextView)convertView.findViewById(R.id.rowDataValue);
        value.setText(data.getValue());

        mLogger.d("OUT(OK)");
    }

    /*************************************************************************/
    /**
     * データクラス
     *
     */
    protected class Data {

        /** 名前 */
        private String mName;

        /** 値 */
        private String mValue;

        /**
         * 名前を設定する。
         *
         * @param name 名前
         */
        public void setName(String name) {
            mName = name;
        }

        /**
         * 名前を返却する。
         *
         * @return 名前
         */
        public String getName() {
            return mName;
        }

        /**
         * 値を設定する。
         *
         * @param value 値
         */
        public void setValue(String value) {
            mValue = value;
        }

        /**
         * 値を返却する。
         *
         * @return 値
         */
        public String getValue() {
            return mValue;
        }
    }

    /*************************************************************************/
    /**
     * データアレーアダプタクラス
     *
     */
    protected class DataArrayAdapter extends ArrayAdapter<Data> {

        /** ロガー */
        private Logger mLogger = new Logger(DataArrayAdapter.class);

        /*＊ レイアウトインフレーター */
        private LayoutInflater mLayoutInflater;

        /**
         * コンストラクタ
         *
         * @param context コンテキスト
         * @param resource リソース
         * @param objects オブジェクト
         */
        public DataArrayAdapter(Context context, int resource, List<Data> objects) {
            // スーパークラスのコンストラクタを呼び出す。
            super(context, resource, objects);

            mLogger.d("IN");

            // インフレーターを取得する。
            mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            mLogger.d("OUT(OK)");
        }

        /**
         * ビューを取得する。
         *
         * @param position 位置
         * @param convertView 設定されているビュー
         * @param parent 親のビュー
         * @return 設定したビュー
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mLogger.d("IN");

            // ビューが未設定の場合
            if (null == convertView) {
                // ビューを生成する。
                convertView = getConvertView(mLayoutInflater);
            }

            // ビューを設定する。
            setConvertView(convertView, (Data)getItem(position));

            mLogger.d("OUT(OK)");
            return convertView;
        }
    }
}
