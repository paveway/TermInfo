package info.paveway.terminfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CameraDataListActivity extends Activity {

    /** ロガー */
    private Logger mLogger = new Logger(CameraDataListActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_data_list_activity);

        List<Data> list = new ArrayList<Data>();

        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Data data = new Data();
            data.setName("カメラ" + String.valueOf(i + 1));
            data.setValue("");
            list.add(data);

            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);

            if (CameraInfo.CAMERA_FACING_BACK == info.facing) {
                setData(list, "取付位置", "背面");

            } else if (CameraInfo.CAMERA_FACING_FRONT == info.facing) {
                setData(list, "取付位置", "前面");
            }

            setData(list, "方向", String.valueOf(info.orientation));

            Camera camera;
            try {
                camera = Camera.open(i);
            } catch (Exception e) {
                mLogger.e(e);

                Data dataError = new Data();
                dataError.setName("エラー");
                dataError.setValue("カメラ情報が取得できません");
                list.add(data);

                continue;
            }
            Camera.Parameters params = camera.getParameters();
            // API Level 1
            setData(list, "画像フォーマット",              String.valueOf(params.getPictureFormat()));
            setData(list, "画像サイズ(幅x高さ)",           params.getPictureSize());
            setData(list, "プレビューフォーマット",        getImageFormatString(params.getPreviewFormat()));
            setData(list, "プレビューサイズ(幅x高さ)",     params.getPreviewSize());

            // API Level 5
            setData(list, "JPEG品質",                      String.valueOf(params.getJpegQuality()));
            setData(list, "JPEGサムネイル品質",            String.valueOf(params.getJpegThumbnailQuality()));
            setData(list, "JPEGサムネイルサイズ(幅x高さ)", params.getJpegThumbnailSize());
            setData(list, "アンチバンディング",            params.getAntibanding());
            setData(list, "カラーエフェクト",              params.getColorEffect());
            setData(list, "フラッシュモード",              params.getFlashMode());
            setData(list, "フォーカスモード",              params.getFocusMode());
            setData(list, "シーンモード",                  params.getSceneMode());
            setData(list, "ホワイトバランス",              params.getWhiteBalance());

            // API Level 8
            setData(list, "露出補正",                      String.valueOf(params.getExposureCompensation()));
            setData(list, "露出補正ステップ",              String.valueOf(params.getExposureCompensationStep()));
            setData(list, "最大露出補正",                  String.valueOf(params.getMaxExposureCompensation()));
            setData(list, "焦点距離",                      String.valueOf(params.getFocalLength()));
            setData(list, "水平視野角",                    String.valueOf(params.getHorizontalViewAngle()));
            setData(list, "垂直視野角",                    String.valueOf(params.getVerticalViewAngle()));
            setData(list, "ズーム",                        String.valueOf(params.getZoom()));

            // API Level 11
            setDataSize(list, params, "ビデオ最適プレビューサイズ(幅x高さ)", "getPreferredPreviewSizeForVideo", 11);

            // API Level 14
            setData(list, params, "自動露出ロック",             "getAutoExposureLock",         14);
            setData(list, params, "自動ホワイトバランスロック", "getAutoWhiteBalanceLock",     14);
            setData(list, params, "顔検出最大数",               "getMaxNumDetectedFaces",      14);
            setData(list, params, "フォーカスエリア最大数",     "getMaxNumFocusAreas",         14);
            setData(list, params, "測光エリア最大数",           "getMaxNumMeteringAreas",      14);

            // API Level 15
            setData(list, params, "ビデオ安定化",               "getVideoStabilization",       15);
        }

        ArrayAdapter<Data> adapter = new DataArrayAdapter(this, 0, list);
        ListView listView = (ListView)findViewById(R.id.cameraDataListView);
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

    private void setData(List<Data> list, String name, Camera.Size size) {
        Data data = new Data();

        data.setName(name);
        data.setValue(String.valueOf(size.width) + "x" + String.valueOf(size.height));

        list.add(data);
    }

    private void setData(List<Data> list, Camera.Parameters params, String name, String method, int apiLevel) {
        Data data = new Data();
        data.setName(name);
        if (apiLevel <= Build.VERSION.SDK_INT) {
            Object result = ClassUtil.invokeMethod(params, method, null, null);
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

    private void setDataSize(List<Data> list, Camera.Parameters params, String name, String method, int apiLevel) {
        Data data = new Data();
        data.setName(name);

        if (apiLevel <= Build.VERSION.SDK_INT) {
            Object result = ClassUtil.invokeMethod(params, method, null, null);
            if ((null != result) && (Camera.Size.class == result.getClass())){
                Camera.Size size = (Camera.Size)result;
                data.setValue(String.valueOf(size.width) + "x" + String.valueOf(size.height));

            } else {
                data.setValue("未設定");
            }

        } else {
            data.setValue("未対応");
        }

        list.add(data);
    }



    private String getImageFormatString(int imageFormat) {
        switch (imageFormat) {
        case ImageFormat.JPEG:        return "JPEG";
        case ImageFormat.NV16:        return "NV16";
        case ImageFormat.NV21:        return "NV21";
        case ImageFormat.RGB_565:     return "RGB 565";
        case ImageFormat.YUV_420_888: return "YUV 420 888";
        case ImageFormat.YUY2:        return "YUY2";
        case ImageFormat.YV12:        return "YV12";
        default:                      return "UNKNOWN";
        }
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
                convertView = mLayoutInflater.inflate(R.layout.camera_data_row, null);
            }

            Data data = (Data)getItem(position);
            TextView name = (TextView)convertView.findViewById(R.id.cameraDataRowName);
            name.setText(data.getName());

            TextView valueLabel = (TextView)convertView.findViewById(R.id.cameraDataRowValue);
            valueLabel.setText(data.getValue());

            return convertView;
        }
    }
}
