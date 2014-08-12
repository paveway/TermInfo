package info.paveway.terminfo;

import info.paveway.log.Logger;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 端末情報
 * システムデータリスト画面クラス
 *
 * @version 1.0 新規作成
 * @author paveway.info@gmail.com
 * Copyright (C) 2014 paveway.info. All rights reserved.
 *
 */
public class SystemDataListActivity extends AbstractBaseListActivity {

    /** ロガー */
    private Logger mLogger = new Logger(SystemDataListActivity.class);

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

        setData(list, "ボード名称",                      Build.BOARD);
        setData(list, "ブートローダーバージョン番号",    Build.BOOTLOADER);
        setData(list, "ブランド名",                      Build.BRAND);
        setData(list, "ネイティブコードの命令セット",    Build.CPU_ABI);
        setData(list, "ネイティブコードの第2命令セット", Build.CPU_ABI2);
        setData(list, "デバイス名",                      Build.DEVICE);
        setData(list, "ビルドID",                        Build.DISPLAY);
        setData(list, "識別子",                          Build.FINGERPRINT);
        setData(list, "ハードウェア名",                  Build.HARDWARE);
        setData(list, "ホスト名",                        Build.HOST);
        setData(list, "変更番号",                        Build.ID);
        setData(list, "製造者名",                        Build.MANUFACTURER);
        setData(list, "モデル名",                        Build.MODEL);
        setData(list, "製品名",                          Build.PRODUCT);
        setData(list, "ビルドタグ名",                    Build.TAGS);
        setData(list, "ビルドタイプ",                    Build.TYPE);
        setData(list, "ユーザ情報",                      Build.USER);
        setData(list, "開発コードネーム",                Build.VERSION.CODENAME);
        setData(list, "ソースコード管理番号",            Build.VERSION.INCREMENTAL);
        setData(list, "バージョン番号",                  Build.VERSION.RELEASE);
        setData(list, "フレームワークバージョン情報",    String.valueOf(Build.VERSION.SDK_INT));

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
        return layoutInflator.inflate(R.layout.row_system_data, null);
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

        TextView name = (TextView)convertView.findViewById(R.id.systemDataRowName);
        name.setText(data.getName());

        TextView value = (TextView)convertView.findViewById(R.id.systemDataRowValue);
        value.setText(data.getValue());

        mLogger.d("OUT(OK)");
    }
}
