package jp.co.stnet.cms.example.domain.model.lombok;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LombokBuilderEntity {
    private String firstName;
    private String lastName;
    private Integer age;
}
