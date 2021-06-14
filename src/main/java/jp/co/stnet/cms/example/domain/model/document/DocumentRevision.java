package jp.co.stnet.cms.example.domain.model.document;

import jp.co.stnet.cms.base.domain.model.AbstractRevisionEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DocumentRevision extends AbstractRevisionEntity implements Serializable {

    // Documentエンティティとフィールドを一致させること
    // @id, @GeneratedValue は外すこと

    /**
     * 内部ID
     */
    @Column(nullable = false)
    private Long id;

    /**
     * ステータス
     */
    @Column(nullable = false)
    private String status;

    /**
     * ファイル
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<File> files;


}
