package jp.co.stnet.cms.example.domain.model.lombok;

import lombok.Data;

@Data
public abstract class AbstractLombokEntity {
    private String field001;

    public AbstractLombokEntity() {
        System.out.println("AbstractLombokEntity instanced!");
    }
}
