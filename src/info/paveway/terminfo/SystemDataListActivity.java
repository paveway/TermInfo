package info.paveway.terminfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SystemDataListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_data_list_activity);

        List<Data> list = new ArrayList<Data>();
        setList(list, "ボード名称",                      Build.BOARD);
        setList(list, "ブートローダーバージョン番号",    Build.BOOTLOADER);
        setList(list, "ブランド名",                      Build.BRAND);
        setList(list, "ネイティブコードの命令セット",    Build.CPU_ABI);
        setList(list, "ネイティブコードの第2命令セット", Build.CPU_ABI2);
        setList(list, "デバイス名",                      Build.DEVICE);
        setList(list, "ビルドID",                        Build.DISPLAY);
        setList(list, "識別子",                          Build.FINGERPRINT);
        setList(list, "ハードウェア名",                  Build.HARDWARE);
        setList(list, "ホスト名",                        Build.HOST);
        setList(list, "変更番号",                        Build.ID);
        setList(list, "製造者名",                        Build.MANUFACTURER);
        setList(list, "モデル名",                        Build.MODEL);
        setList(list, "製品名",                          Build.PRODUCT);
        setList(list, "ビルドタグ名",                    Build.TAGS);
        setList(list, "ビルドタイプ",                    Build.TYPE);
        setList(list, "ユーザ情報",                      Build.USER);
        setList(list, "開発コードネーム",                Build.VERSION.CODENAME);
        setList(list, "ソースコード管理番号",            Build.VERSION.INCREMENTAL);
        setList(list, "バージョン番号",                  Build.VERSION.RELEASE);
        setList(list, "フレームワークバージョン情報",    String.valueOf(Build.VERSION.SDK_INT));

        ArrayAdapter<Data> adapter = new DataArrayAdapter(this, 0, list);
        ListView listView = (ListView)findViewById(R.id.systemDataListView);
        listView.setAdapter(adapter);
    }

    private void setList(List<Data> list, String name, String value) {
        Data data = new Data();
        data.setName(name);
        data.setValue(value);
        list.add(data);
    }

    private class Data {

        private String mName;

        private String mValue;

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public void setValue(String value) {
            mValue = value;
        }

        public String getValue() {
            return mValue;
        }
    }

    private class DataArrayAdapter extends ArrayAdapter<Data> {

        private LayoutInflater mLayoutInflater;

        public DataArrayAdapter(Context context, int resource, List<Data> objects) {
            super(context, resource, objects);

            mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = mLayoutInflater.inflate(R.layout.system_data_row, null);
            }

            Data data = (Data)getItem(position);
            TextView name = (TextView)convertView.findViewById(R.id.systemDataRowName);
            name.setText(data.getName());

            TextView value = (TextView)convertView.findViewById(R.id.systemDataRowValue);
            value.setText(data.getValue());

            return convertView;
        }
    }
}
