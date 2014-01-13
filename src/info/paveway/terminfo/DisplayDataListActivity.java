package info.paveway.terminfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayDataListActivity extends Activity {

    /** ロガー */
    private Logger mLogger = new Logger(DisplayDataListActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_data_list_activity);

        List<Data> list = new ArrayList<Data>();

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


        ArrayAdapter<Data> adapter = new DataArrayAdapter(this, 0, list);
        ListView listView = (ListView)findViewById(R.id.displayDataListView);
        listView.setAdapter(adapter);

    }

    private void setData(List<Data> list, String name, String value) {
        Data data = new Data();

        data.setName(name);
        if ((null == value) || ("".equals(value))) {
            data.setValue("未設定");

        } else {
            data.setValue(value);
        }

        list.add(data);
    }

    private void setData(List<Data> list, Display display, String name, String method, int apiLevel) {
        Data data = new Data();
        data.setName(name);
        if (apiLevel <= Build.VERSION.SDK_INT) {
            Object result = ClassUtil.invokeMethod(display, method, null, null);
            if (null != result) {
                if (String.class != result.getClass()) {
                    if (Boolean.class == result.getClass()) {
                        data.setValue(((Boolean)result) ? "YES" : "NO");
                    } else {
                        data.setValue(String.valueOf(result));
                    }

                } else if (String.class == result.getClass()){
                    data.setValue((String)result);

                } else {
                    data.setValue("未設定");
                }

            } else {
                data.setValue("未設定");
            }

        } else {
            data.setValue("未対応");
        }

        list.add(data);
    }

    @SuppressWarnings("rawtypes")
    private void setDataSize(List<Data> list, Display display, String name, String method, int apiLevel) {
        Data data = new Data();
        data.setName(name);

        if (apiLevel <= Build.VERSION.SDK_INT) {
            Class[] parameterTypes = new Class[]{Point.class};
            Point point = new Point();
            Object[] args = new Object[]{point};
            ClassUtil.invokeMethod(display, method, parameterTypes, args);
            data.setValue(String.valueOf(point.x) + "x" + String.valueOf(point.y));

        } else {
            data.setValue("未対応");
        }

        list.add(data);
    }

    @SuppressWarnings("rawtypes")
    private void setDataRectSize(List<Data> list, Display display, String name, String method, int apiLevel) {
        Data data = new Data();
        data.setName(name);

        if (apiLevel <= Build.VERSION.SDK_INT) {
            Class[] parameterTypes = new Class[]{Rect.class};
            Rect rect = new Rect();
            Object[] args = new Object[]{rect};
            ClassUtil.invokeMethod(display, method, parameterTypes, args);
            data.setValue(String.valueOf(rect.width()) + "x" + String.valueOf(rect.height()));

        } else {
            data.setValue("未対応");
        }

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
                convertView = mLayoutInflater.inflate(R.layout.display_data_row, null);
            }

            Data data = (Data)getItem(position);
            TextView name = (TextView)convertView.findViewById(R.id.displayDataRowName);
            name.setText(data.getName());

            TextView valueLabel = (TextView)convertView.findViewById(R.id.displayDataRowValue);
            valueLabel.setText(data.getValue());

            return convertView;
        }
    }
}
