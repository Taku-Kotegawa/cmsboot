package jp.co.stnet.cms.common.util;


import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Apache Commons BeanUtils の拡張
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    /**
     * クラスのフィールド一覧を取得する。(Map)
     *
     * @param clazz           クラス
     * @param parentClassName 親クラス名
     * @return Map(key : フィールド名, value : クラス名)
     */
    public static Map<String, String> getFields(Class clazz, String parentClassName) {

        if (clazz == null) {
            throw new IllegalArgumentException();
        }

        Map<String, String> fieldsMap = new LinkedHashMap<>();

        String prefix = "";
        if (parentClassName != null && !parentClassName.isEmpty()) {
            prefix = parentClassName + "-";
        }

        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("set")) {
                Class fieldClass = m.getParameterTypes()[0];
                String fieldName = Introspector.decapitalize(m.getName().substring(3));

                if ("java.lang.String".equals(fieldClass.getName())
                        || "java.util.List".equals(fieldClass.getName())
                        || "java.util.Map".equals(fieldClass.getName())
                        || "java.lang.Enum".equals(fieldClass.getName())) {
                    // 何もしない

                    //TODO モデルクラスのListの場合のネスト対応が未対応
                    //getSigunature()を使う予定


                } else {
                    fieldsMap.putAll(getFields(fieldClass, fieldName));
                }
                // 他のクラスがフィールド定義されていた場合は、そのクラスのフィールドも取得する
                fieldsMap.put(prefix + fieldName, fieldClass.getName());
            }
        }

        return fieldsMap;
    }

    /**
     * クラスのフィールド一覧を取得する。(List)
     *
     * @param clazz           クラス
     * @param parentClassName 親クラス
     * @return フィールド一覧
     */
    private static List<String> getFieldList(Class clazz, String parentClassName) {
        Map<String, String> fields = getFields(clazz, parentClassName);
        List<String> fieldList = new ArrayList<>();

        for (String fieldName : fields.keySet()) {
            fieldList.add(fieldName);
        }
        return fieldList;
    }

    /**
     * クラスのフィールド一覧を取得する。(List)
     *
     * @param clazz クラス
     * @return フィールド一覧
     */
    public static List<String> getFieldList(Class clazz) {
        return getFieldList(clazz, "");
    }


    /**
     * 指定したアノテーションが設定されているフィールドを取得する。
     *
     * @param clazz           クラス
     * @param parentClassName 親クラス(スーパークラス)
     * @param annotationClass アノテーションクラス
     * @return フォールド名とアノテーションの組み合わせMap
     */
    public static Map<String, Annotation> getFieldByAnnotation(Class clazz, String parentClassName, Class annotationClass) {

        if (clazz == null) {
            throw new IllegalArgumentException();
        }

        Map<String, Annotation> annotationMap = new LinkedHashMap<>();

        String prefix = "";
        if (parentClassName != null && !parentClassName.isEmpty()) {
            prefix = parentClassName + "-";
        }

        Map<String, String> fields = getFields(clazz, parentClassName);

        for (String fieldName : fields.keySet()) {
            Field field = null;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                continue;
            }
            Annotation annotation = field.getAnnotation(annotationClass);
            if (annotation != null) {
                annotationMap.put(fieldName, annotation);
            }
        }

        return annotationMap;
    }

    /**
     * シグネチャを取得する。(未使用)
     * https://stackoverflow.com/questions/45072268/how-can-i-get-the-signature-field-of-java-reflection-method-object
     * @param m メソッド
     * @return シグネチャ
     */
    public static String getSignature(Method m) {
        String sig;
        try {
            Field gSig = Method.class.getDeclaredField("signature");
            gSig.setAccessible(true);
            sig = (String) gSig.get(m);
            if (sig != null) return sig;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder("(");
        for (Class<?> c : m.getParameterTypes())
            sb.append((sig = Array.newInstance(c, 0).toString()), 1, sig.indexOf('@'));
        return sb.append(')')
                .append(
                        m.getReturnType() == void.class ? "V" :
                                (sig = Array.newInstance(m.getReturnType(), 0).toString()).substring(1, sig.indexOf('@'))
                )
                .toString();
    }

    // インスタンス化禁止
    private BeanUtils() {}

}
