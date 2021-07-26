package jp.co.stnet.cms.base.domain.model.filemanage;


import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import lombok.*;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * ファイルマネージドエンティティ.
 *
 * <p>
 * 保存するファイルの属性を管理する。
 */
@SuppressWarnings({"LombokDataInspection", "LombokEqualsAndHashCodeInspection"})
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(columnList = "uuid", unique = true)})
public class FileManaged extends AbstractEntity<Long> implements Serializable {

    /**
     * 内部ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ファイルを一意に特定する番号。
     */
    private String uuid;

    /**
     * ファイル名
     */
    @KeywordField(sortable = Sortable.YES)
    private String originalFilename;

    /**
     * uri
     */
    @Column(unique = true)
    private String uri;

    /**
     * MIME TYPE
     */
    private String fileMime;

    /**
     * ファイルサイズ
     */
    private Long fileSize;

    /**
     * ファイルの種類
     */
    private String fileType;

    /**
     * ステータス
     * false: temporary(0), true: permanent(1)
     */
    @Column(columnDefinition = "varchar(255) default '0'")
    private String status;

    /**
     * @return MediaType
     */
    public MediaType getMediaType() {
        if (fileMime != null) {
            String[] mimeArray = fileMime.split("/");
            return new MediaType(mimeArray[0], mimeArray[1]);
        } else {
            return null;
        }
    }

    /**
     * @return ContentDisposition
     */
    public ContentDisposition getAttachmentContentDisposition() {
        if (originalFilename != null) {
            String encodedFilename = originalFilename;
            encodedFilename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8).replace("+", "%20");
            if (isOpenWindows()) {
                return ContentDisposition.builder("filename=\"" + encodedFilename + "\"").build();
            } else {
                return ContentDisposition.builder("attachment;filename=\"" + originalFilename + "\";filename*=UTF-8'ja'" + encodedFilename).build();
            }
        } else {
            return null;
        }
    }

    /**
     * ダウンロード時にタブで開くかファイル保存か
     *
     * @return true:タブで開く, false:ファイル保存
     */
    private boolean isOpenWindows() {
        return MediaType.APPLICATION_PDF_VALUE.equals(fileMime);
    }

    @Override
    public boolean isNew() {
        return getVersion() == null;
    }

}
