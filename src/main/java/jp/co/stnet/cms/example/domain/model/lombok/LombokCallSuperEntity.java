package jp.co.stnet.cms.example.domain.model.lombok;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LombokCallSuperEntity extends AbstractLombokEntity {

    private String field101;
    private Integer field102;
    private List<String> list103;
    private Map<String, String> map104;
    private LombokChildEntity childField105;
    private List<LombokChildEntity> childList106;
}
