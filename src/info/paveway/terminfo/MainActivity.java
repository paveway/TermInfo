package info.paveway.terminfo;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * 端末情報
 * メイン画面クラス
 *
 * @version 1.0 新規作成
 * @author paveway.info@gmail.com
 * Copyright (C) 2014 paveway.info. All rights reserved.
 *
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    /** リソース */
    private Resources mResources;

    /** タブホスト */
    private TabHost mTabHost;

    /**
     * 生成された時に呼び出される。
     *
     * @param savedInstanceState 保存された時のインスタンスの状態
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // スーパークラスのメソッドを呼び出す。
        super.onCreate(savedInstanceState);

        // レイアウトを設定する。
        setContentView(R.layout.activity_main);

        // リソースを取得する。
        mResources = getResources();

        // タブを設定する。
        mTabHost = getTabHost();
        addTab(SystemDataListActivity.class,  R.string.tag_system,  R.string.label_system,  R.drawable.ic_tab_system);
        addTab(DisplayDataListActivity.class, R.string.tag_display, R.string.label_display, R.drawable.ic_tab_display);
        addTab(SensorDataListActivity.class,  R.string.tag_sensor,  R.string.label_sensor,  R.drawable.ic_tab_sensor);
        addTab(CameraDataListActivity.class,  R.string.tag_camera,  R.string.label_camera,  R.drawable.ic_tab_camera);
    }

    /**
     * タブを追加する。
     *
     * @param dst タブに設定するクラス
     * @param tag タグID
     * @param label ラベルID
     * @param id ID
     */
    @SuppressWarnings("rawtypes")
    private void addTab(Class dst, int tagId, int labelId, int id) {
        Intent intent = new Intent().setClass(this, dst);
        TabHost.TabSpec spec = mTabHost.newTabSpec(getResourceString(tagId));
        spec.setIndicator(getResourceString(labelId), mResources.getDrawable(id));
        spec.setContent(intent);
        mTabHost.addTab(spec);
    }

    /**
     * リソース文字列を取得する。
     *
     * @param id リソースID
     * @return リソース文字列
     */
    private String getResourceString(int id) {
        return mResources.getString(id);
    }
}
