package info.paveway.util;

import info.paveway.log.Logger;

import java.lang.reflect.Method;

/**
 * クラスユーティリティ
 *
 * @version 1.0 新規作成
 * @author paveway.info@gmail.com
 * Copyright (C) 2014 paveway.info. All rights reserved.
 *
 */
public class ClassUtil {

    /** ロガー */
    private static Logger mLogger = new Logger(ClassUtil.class);

    /**
     * コンストラクタ
     * インスタンス化させない。
     */
    private ClassUtil() {
        // 何もしない。
    }

    /**
     * メソッドを実行する。
     *
     * @param cls クラス
     * @param name メソッド名
     * @param parameterTypes パラメータタイプ
     * @param args パラメータ値
     * @return 実行結果
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T invokeStaticMethod(Class cls, String name, Class[] parameterTypes, Object[] args) {
        T result = null;
        try {
            Method method = cls.getMethod(name, parameterTypes);
            if (null != method) {
                result = (T)method.invoke(null, args);

            } else {
                mLogger.w("Method not found. Method Name=[" + name + "]");
            }
        } catch (Exception e) {
            mLogger.e(e);
        }

        return result;
    }

    /**
     * メソッドを実行する。
     *
     * @param object オブジェクト
     * @param name メソッド名
     * @param parameterTypes パラメータタイプ
     * @param args パラメータ値
     * @return 実行結果
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T invokeMethod(Object object, String name, Class[] paramterTypes, Object[] args) {
        T result = null;
        try {
            Method method = object.getClass().getMethod(name, paramterTypes);
            if (null != method) {
                result = (T)method.invoke(object, args);

            } else {
                mLogger.w("Method not found. Method Name=[" + name + "]");
            }
        } catch (Exception e) {
            mLogger.e(e);
        }

        return result;
    }
}
