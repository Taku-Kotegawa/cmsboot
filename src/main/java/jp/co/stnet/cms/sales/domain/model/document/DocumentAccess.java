package jp.co.stnet.cms.sales.domain.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 日単位のアクセス
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "UK_DOCUMENT_ACCESS_01", columnList = "accessDate, username, documentId", unique = true)})
public class DocumentAccess implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * アクセス日
     */
    private LocalDate accessDate;

    /**
     * ユーザ名
     */
    private String username;

    /**
     * ドキュメントのID
     */
    private Long documentId;

}
