package jp.co.stnet.cms.common.util;

import java.util.*;

/**
 * 画面表示におけるフィールド単位のアクセス制御を支援するユーティリティ。
 * Formクラスから「フィールド名__要素名」をキーとするhashMapを作成する。<br>
 * <br>
 * <br>
 * (使い方)<br>
 * 1. このクラスをインスタンスにする。
 * 2. set〜()で、フィールド単位の操作を変更する。
 * 3. asMap()で、hashMapを取得する。
 * <br>
 * (hashMapの構造)<br>
 * { fieldName__disabled: true, fieldName__readonly: false, ...}
 * <br>
 * <ul>
 * <li>__view: フィールドの値を表示する</li>
 * <li>__input: input要素を表示する</li>
 * <li>__disabled: input要素のdisabled属性を有効にする/しない</li>
 * <li>__readonly: input要素のreadonly属性を有効にする/しない</li>
 * <li>__hidden: input要素をhiddenにする</li>
 * <li>__label: ラベルを表示する</li>
 * </ul>
 */
public class StateMap {

    private final Map<String, Boolean> authMap = new HashMap<>();

    private static final String DISABLED = "disabled";
    private static final String READONLY = "readonly";
    private static final String HIDDEN = "hidden";
    private static final String INPUT = "input";
    private static final String VIEW = "view";
    private static final String LABEL = "label";
    private static final String[] attributes = {DISABLED, READONLY, HIDDEN, VIEW, INPUT, LABEL};

    /**
     * 初期化
     *
     * @param clazz       Formクラス
     * @param includeKeys 追加するフィールド名のリスト
     * @param excludeKeys 除外するフィールド名のリスト
     * @throws IllegalArgumentException いずれかの引数がnullの場合
     */
    public StateMap(Class<?> clazz, List<String> includeKeys, List<String> excludeKeys) {

        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null.");
        }
        if (includeKeys == null) {
            throw new IllegalArgumentException("includeKeys must not be null.");
        }
        if (excludeKeys == null) {
            throw new IllegalArgumentException("excludeKeys must not be null.");
        }

        List<String> filedNames = BeanUtils.getFieldList(clazz);
        filedNames.removeIf(excludeKeys::contains);
        filedNames.addAll(includeKeys);
        init(filedNames);
    }

    /**
     * fieldName__input → true
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setInputTrue(String fieldName) {
        setAttribute(fieldName, INPUT, true);
        return setLabelFromInputAndView(fieldName);
    }

    /**
     * fieldName__input → false
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setInputFalse(String fieldName) {
        setAttribute(fieldName, INPUT, false);
        return setLabelFromInputAndView(fieldName);
    }

    /**
     * fieldName__disabled → true
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setDisabledTrue(String fieldName) {
        return setAttribute(fieldName, DISABLED, true);
    }

    /**
     * fieldName__disabled → false
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setDisabledFalse(String fieldName) {
        return setAttribute(fieldName, DISABLED, false);
    }

    /**
     * fieldName__readonly → true
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setReadOnlyTrue(String fieldName) {
        return setAttribute(fieldName, READONLY, true);
    }

    /**
     * fieldName__readonly → false
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setReadOnlyFalse(String fieldName) {
        return setAttribute(fieldName, READONLY, false);
    }

    /**
     * fieldName__hidden → true
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setHiddenTrue(String fieldName) {
        return setAttribute(fieldName, HIDDEN, true);
    }

    /**
     * fieldName__hidden → false
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setHiddenFalse(String fieldName) {
        return setAttribute(fieldName, HIDDEN, false);
    }

    /**
     * fieldName__view → true
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setViewTrue(String fieldName) {
        setAttribute(fieldName, VIEW, true);
        return setLabelFromInputAndView(fieldName);

    }

    /**
     * fieldName__view → false
     *
     * @param fieldName フィールド名
     * @return StateMap
     * @throws IllegalArgumentException 指定したフィールド名が存在しない
     */
    public StateMap setViewFalse(String fieldName) {
        setAttribute(fieldName, VIEW, false);
        return setLabelFromInputAndView(fieldName);
    }

    /**
     * 全てのfieldName__disabled → true
     *
     * @return StateMap
     */
    public StateMap setDisabledTrueAll() {
        return setAttributeAll(DISABLED, true);
    }

    /**
     * 全てのfieldName__disabled → false
     *
     * @return StateMap
     */
    public StateMap setDisabledFalseAll() {
        return setAttributeAll(DISABLED, false);
    }

    /**
     * 全てのfieldName__readonly → true
     *
     * @return StateMap
     */
    public StateMap setReadOnlyTrueAll() {
        return setAttributeAll(READONLY, true);
    }

    /**
     * 全てのfieldName__readonly → false
     *
     * @return StateMap
     */
    public StateMap setReadOnlyFalseAll() {
        return setAttributeAll(READONLY, false);
    }

    /**
     * 全てのfieldName__hidden → true
     *
     * @return StateMap
     */
    public StateMap setHiddenTrueAll() {
        return setAttributeAll(HIDDEN, true);
    }

    /**
     * 全てのfieldName__hidden → false
     *
     * @return StateMap
     */
    public StateMap setHiddenFalseAll() {
        return setAttributeAll(HIDDEN, false);
    }

    /**
     * 全てのfieldName__input → true
     *
     * @return StateMap
     */
    public StateMap setInputTrueAll() {
        return setAttributeAll(INPUT, true);
    }

    /**
     * 全てのfieldName__input → false
     *
     * @return StateMap
     */
    public StateMap setInputFalseAll() {
        return setAttributeAll(INPUT, false);
    }

    /**
     * 全てのfieldName__view → true
     *
     * @return StateMap
     */
    public StateMap setViewTrueAll() {
        return setAttributeAll(VIEW, true);
    }

    /**
     * 全てのfieldName__view → false
     *
     * @return StateMap
     */
    public StateMap setViewFalseAll() {
        return setAttributeAll(VIEW, false);
    }

    /**
     * フィールドを追加
     *
     * @param fieldName フィールド名
     * @return StateMap
     */
    public StateMap addField(String fieldName) {
        for (String attribute : attributes) {
            authMap.put(fieldName + "__" + attribute, Boolean.FALSE);
        }
        return this;
    }

    private Set<String> getFieldSet() {
        Set<String> fieldSet = new LinkedHashSet<>();
        // フィールドの一覧を取得するため、__INPUTをループ
        for (Map.Entry<String, Boolean> entry : authMap.entrySet()) {
            if (entry.getKey().endsWith(INPUT)) {
                //フィールド名のみを取り出す
                fieldSet.add(entry.getKey().replace("__" + INPUT, ""));
            }
        }
        return fieldSet;
    }

    private StateMap setAttributeAll(String attribute, Boolean status) {

        for (String fieldName : getFieldSet()) {
            switch (attribute) {
                case INPUT:
                    if (status) {
                        setInputTrue(fieldName);
                    } else {
                        setInputFalse(fieldName);
                    }
                    break;

                case DISABLED:
                    if (status) {
                        setDisabledTrue(fieldName);
                    } else {
                        setDisabledFalse(fieldName);
                    }
                    break;

                case READONLY:
                    if (status) {
                        setReadOnlyTrue(fieldName);
                    } else {
                        setReadOnlyFalse(fieldName);
                    }
                    break;

                case HIDDEN:
                    if (status) {
                        setHiddenTrue(fieldName);
                    } else {
                        setHiddenFalse(fieldName);
                    }
                    break;

                case VIEW:
                    if (status) {
                        setViewTrue(fieldName);
                    } else {
                        setViewFalse(fieldName);
                    }
                    break;

                // LABELは、INPUT,VIEWに連動
            }
        }

        return this;
    }

    private StateMap setAttribute(String fieldName, String attribute, Boolean status) {
        String key = fieldName + "__" + attribute;
        if (authMap.get(key) != null) {
            authMap.put(key, status);
            return this;
        } else {
            throw new IllegalArgumentException(key + " not found");
        }
    }

    private boolean getStatus(String fieldName, String attribute) {
        String key = fieldName + "__" + attribute;
        if (authMap.get(key) != null) {
            return authMap.get(key);
        } else {
            throw new IllegalArgumentException(key + " not found");
        }
    }

    private void init(List<String> fieldNames) {
        for (String fieldName : fieldNames) {
            for (String attribute : attributes) {
                authMap.put(fieldName + "__" + attribute, Boolean.FALSE);
            }
        }
    }

//    public List<String> getFileds(Class clazz) {
//        return getFileds(clazz, "");
//    }
//
//    public List<String> getFileds(Class clazz, String parentClassName) {
//        List<String> fieldNames = new ArrayList<>();
//
//        if (clazz != null) {
//            String prefix = "";
//            if (parentClassName != null && !parentClassName.isEmpty()) {
//                prefix = parentClassName + "-";
//            }
//
//            Method[] methods = clazz.getMethods();
//            for (Method m : methods) {
//                if (m.getName().startsWith("set")) {
//                    Class fieldClass = m.getParameterTypes()[0];
//                    String fieldName = Introspector.decapitalize(m.getName().substring(3));
//
//                    if ("java.lang.String".equals(fieldClass.getName())
//                            || "java.util.List".equals(fieldClass.getName())
//                            || "java.util.Map".equals(fieldClass.getName())) {
//                        // 何もしない
//
//                    } else {
//                        fieldNames.addAll(getFileds(fieldClass, fieldName));
//                    }
//
//                    fieldNames.add(prefix + fieldName);
//                }
//            }
//        }
//
//        return fieldNames;
//    }


    /**
     * ハッシュマップを取得する
     *
     * @return authMap
     */
    public Map<String, Boolean> asMap() {
        return authMap;
    }


    /**
     * __input, __viewの状態から、field__label を設定する。
     *
     * @param fieldName フィールド名
     * @return StateMap
     */
    private StateMap setLabelFromInputAndView(String fieldName) {
        setAttribute(fieldName, LABEL, getStatus(fieldName, INPUT) || getStatus(fieldName, VIEW));
        return this;
    }

}
