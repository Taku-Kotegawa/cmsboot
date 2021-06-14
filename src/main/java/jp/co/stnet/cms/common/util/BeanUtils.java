package jp.co.stnet.cms.common.util;


import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Apache Commons BeanUtils の拡張
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    private static final Set<String> PRIMITIVE = Set.of(
            "java.lang.String",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Float",
            "java.lang.Double",
            "java.lang.BigDecimal",
            "java.time.LocalDate",
            "java.time.LocalDateTime",
            "java.util.Date",
            "java.lang.Enum"
    );

    private static final Set<String> COLLECTION = Set.of(
            "java.util.Map",
            "java.util.List",
            "java.util.Set",
            "java.util.Collection"
    );

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

                if (PRIMITIVE.contains(fieldClass.getName())) {
                    // 何もしない

                } else if (COLLECTION.contains(fieldClass.getName())) {
                    String c = getClassFromSig(getSignature(m));

                    if (PRIMITIVE.contains(c)) {

                    } else  {
                        try {
                            fieldsMap.putAll(getFields(Class.forName(c), fieldName));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

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
        return "";

    }

    /**
     * "(Ljava/util/List<Ljava/lang/String;>;)V" から "java.lang.String" を取得する。。
     * @param sig sig
     * @return クラス名
     */
    private static String getClassFromSig(String sig) {
        int start = sig.indexOf("<L");
        int end = sig.indexOf(";>");
        return sig.substring(start + 2, end).replace("/", ".");
    }

    // インスタンス化禁止
    private BeanUtils() {}

}
