package jp.co.stnet.cms.example.presentation.controller.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.stnet.cms.example.domain.model.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DocumentListBean extends Document {

    private String operations;

    private String DT_RowId;

    private String DT_RowClass;

    private Map<String, String> DT_RowAttr;

    @JsonProperty("DT_RowId")
    public String getDT_RowId() {
        return DT_RowId;
    }

    @JsonProperty("DT_RowClass")
    public String getDT_RowClass() {
        return DT_RowClass;
    }

    @JsonProperty("DT_RowAttr")
    public Map<String, String> getDT_RowAttr() {
        return DT_RowAttr;
    }

    private String statusLabel;

    private String useStageLabel;

    private String filesLabel;

}
