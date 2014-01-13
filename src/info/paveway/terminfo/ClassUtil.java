package info.paveway.terminfo;

import java.lang.reflect.Method;

public class ClassUtil {

    private static Logger mLogger = new Logger(ClassUtil.class);

    private ClassUtil() {

    }

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
