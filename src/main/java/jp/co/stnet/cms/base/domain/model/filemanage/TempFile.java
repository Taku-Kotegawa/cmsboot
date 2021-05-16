package jp.co.stnet.cms.base.domain.model.filemanage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 一時ファイルエンティティ.
 * <p>
 * Terasolunaガイドからの移植のため残していますが、使用は推奨しません。
 * FileManagedを使ってください。
 */
@Deprecated
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempFile implements Serializable {

    /**
     * 内部ID
     */
    @Id
    private String id;

    /**
     * オリジナルファイル名
     */
    private String originalName;

    /**
     * アップロード日時
     */
    @CreatedDate
    private LocalDateTime uploadedDate;

    /**
     * ファイル本体
     */
    @Lob
    private byte[] body;

}
