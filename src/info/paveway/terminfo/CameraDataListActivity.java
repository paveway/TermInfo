package info.paveway.terminfo;

import info.paveway.log.Logger;
import info.paveway.util.ClassUtil;

import java.util.List;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;

/**
 * 端末情報
 * カメラデータリスト画面クラス
 *
 * @version 1.0 新規作成
 * @author paveway.info@gmail.com
 * Copyright (C) 2014 paveway.info. All rights reserved.
 *
 */
public class CameraDataListActivity extends AbstractBaseListActivity {

    /** ロガー */
    private Logger mLogger = new Logger(CameraDataListActivity.class);

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
        if ((null == value) || ("".equals(value))) {
            data.setValue("未設定");

        } else {
            data.setValue(value);
        }

        list.add(data);
        mLogger.d("OUT(OK)");
    }

    /**
     * データを設定する。
     *
     * @param list リスト
     * @param name 名前
     * @param size サイズ
     */
    private void setData(List<Data> list, String name, Camera.Size size) {
        mLogger.d("IN");

        Data data = new Data();

        data.setName(name);
        data.setValue(String.valueOf(size.width) + "x" + String.valueOf(size.height));

        list.add(data);
        mLogger.d("OUT(OK)");
    }

    /**
     * データを設定する。
     *
     * @param list リスト
     * @param params パラメータ
     * @param name 名前
     * @param method メソッド
     * @param apiLevel APIレベル
     */
    private void setData(List<Data> list, Camera.Parameters params, String name, String method, int apiLevel) {
        mLogger.d("IN");

        Data data = new Data();
        data.setName(name);
        String value = "未設定";
        if (apiLevel <= Build.VERSION.SDK_INT) {
            Object result = ClassUtil.invokeMethod(params, method, null, null);
            if (null != result) {
                if (String.class != result.getClass()) {
                    if (Boolean.class == result.getClass()) {
                        value = (Boolean)result ? "YES" : "NO";
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
     * @param params パラメータ
     * @param name 名前
     * @param method メソッド
     * @param apiLevel APIレベル
     */
    private void setDataSize(List<Data> list, Camera.Parameters params, String name, String method, int apiLevel) {
        mLogger.d("IN");

        Data data = new Data();
        data.setName(name);

        String value = "未設定";
        if (apiLevel <= Build.VERSION.SDK_INT) {
            Object result = ClassUtil.invokeMethod(params, method, null, null);
            if ((null != result) && (Camera.Size.class == result.getClass())){
                Camera.Size size = (Camera.Size)result;
                value = String.valueOf(size.width) + "x" + String.valueOf(size.height);
            }

        } else {
            data.setValue("未対応");
        }
        data.setValue(value);

        list.add(data);
        mLogger.d("OUT(OK)");
    }

    /**
     * イメージフォーマット文字列を返却する。
     *
     * @param imageFormat イメージフォーマット
     * @return イメージフォーマット文字列
     */
    private String getImageFormatString(int imageFormat) {
        mLogger.d("IN");

        mLogger.d("OUT(OK)");
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
}
