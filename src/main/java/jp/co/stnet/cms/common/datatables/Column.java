package jp.co.stnet.cms.common.datatables;


import jp.co.stnet.cms.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DataTables(Server-Side)からのリクエストを格納するクラス(列単位のフィルタ条件など)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    /**
     * Column's data source
     */
    @NotBlank
    @CodePointLength
    private String data;

    /**
     * Column's name
     */
    private String name;

    /**
     * Flag to indicate if this column is searchable (true) or not (false).
     */
    @NotNull
    private Boolean searchable;

    /**
     * Flag to indicate if this column is orderable (true) or not (false).
     */
    @NotNull
    private Boolean orderable;

    /**
     * Search value to apply to this specific column.
     */
    @NotNull
    private Search search;

    /**
     * Set the search value to apply to this column
     *
     * @param searchValue if any, the search value to apply
     */
    public void setSearchValue(String searchValue) {
        this.search.setValue(searchValue);
    }

    public String getDbname() {
        return StringUtils.toLowerSnakeCase(this.data);
    }
}
