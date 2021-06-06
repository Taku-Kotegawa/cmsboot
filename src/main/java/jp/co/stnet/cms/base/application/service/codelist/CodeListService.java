package jp.co.stnet.cms.base.application.service.codelist;

public interface CodeListService {

    /**
     * 指定したコードリストを更新する(JdbcCodeListのみ更新可能)
     * @param codeListName コードリスト名
     */
    void refresh(String codeListName);
}
