package jp.co.stnet.cms.base.domain.model.variable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

/**
 * 変数タイプ.
 * <p>
 * 変数のタイプと利用するフィールドのラベルを設定する。使わないフィールドのラベルは設定しない(空白文字)
 */
@AllArgsConstructor
@Getter
public enum VariableType implements EnumCodeList.CodeListItem {

    SHORT_MESSAGE("ショートメッセージ", "", "", "", "", "", "", "", "", "", "", "公開開始日", "公開終了日", "", "", "", "", "", "", "", "", "メッセージ", "", ""),
    NEWS("お知らせ", "件名", "カテゴリ", "", "", "", "", "", "", "", "", "公開開始日", "公開終了日", "", "", "", "", "", "", "", "", "メッセージ", "添付ファイル", ""),
    MESSAGE_TEMPLATE("メッセージテンプレート", "タイトル", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "本文", "", ""),
    DOC_CATEGORY1("文書区分1", "区分１", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "CL_DOC_CATEGORY1"),
    DOC_CATEGORY2("文書区分2", "区分２", "親(区分1コード)", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "CL_DOC_CATEGORY2"),
    DOC_SERVICE1("事業領域", "事業領域", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "CL_DOC_SERVICE1"),
    DOC_SERVICE2("サービス種別", "サービス種別", "親(事業領域コード)", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "CL_DOC_SERVICE2"),
    DOC_SERVICE3("サービス", "サービス", "親(サービス種別コード)", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "CL_DOC_SERVICE3"),
    DOC_STAGE("活用シーン", "名称", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "並び順", "", "", "", "", "", "", "CL_DOC_STAGE"),
    EMPLOYEE("従業員", "従業員名", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "並び順", "", "", "", "", "", "", "CL_EMPLOYEE"),
    DEPARTMENT("部署", "部署名", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "並び順", "", "", "", "", "", "", "CL_DEPARTMENT");


    private final String label;
    private final String labelValue1;
    private final String labelValue2;
    private final String labelValue3;
    private final String labelValue4;
    private final String labelValue5;
    private final String labelValue6;
    private final String labelValue7;
    private final String labelValue8;
    private final String labelValue9;
    private final String labelValue10;
    private final String labelDate1;
    private final String labelDate2;
    private final String labelDate3;
    private final String labelDate4;
    private final String labelDate5;
    private final String labelValint1;
    private final String labelValint2;
    private final String labelValint3;
    private final String labelValint4;
    private final String labelValint5;
    private final String labelTextarea;
    private final String labelFile1;
    private final String codeListBeanName;

    @Override
    public String getCodeLabel() {
        return label;
    }

    @Override
    public String getCodeValue() {
        return name();
    }
}
