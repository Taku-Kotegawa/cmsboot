package jp.co.stnet.cms.example.domain.model.simpleentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

@AllArgsConstructor
@Getter
public enum Agreement implements EnumCodeList.CodeListItem {

    YES("利用規約に合意します。");

    private final String label;

    @Override
    public String getCodeLabel() {
        return label;
    }

    @Override
    public String getCodeValue() {
        return name();
    }
}
