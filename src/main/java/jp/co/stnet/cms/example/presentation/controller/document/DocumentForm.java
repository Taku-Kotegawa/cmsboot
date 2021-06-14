package jp.co.stnet.cms.example.presentation.controller.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.co.stnet.cms.example.domain.model.document.DocPublicScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentForm implements Serializable {

    @NotNull(groups = Update.class)
    private Long id;

    @NotNull(groups = Update.class)
    private Long version;

    /**
     * タイトル
     */
    private String title;

    /**
     * 本文
     */
    private String body;

    /**
     * 公開区分
     */
    private String publicScope = DocPublicScope.ALL.getValue();

    /**
     * 管理部門
     */
    private String chargeDepartment;

    /**
     * 管理担当者
     */
    private String chargePerson;

    /**
     * 制定日
     */
    private LocalDate enactmentDate;

    /**
     * 最終改定日
     */
    private LocalDate lastRevisedDate;

    /**
     * 実施日
     */
    private LocalDate implementationDate;

    /**
     * 制定箇所
     */
    private String enactmentDepartment;

    /**
     * 変更理由
     */
    private String reasonForChange;

    private Collection<@Valid FileForm> files = new ArrayList<>();

    public interface Create {
    }

    public interface Update {
    }

}
