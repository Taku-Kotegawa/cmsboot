package jp.co.stnet.cms.example.domain.model.document;

import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File implements Serializable {

    private String type;

    private String fileUuid;

//    @Transient
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "fileUuid", referencedColumnName = "uuid", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
    private FileManaged fileManaged;

    private String pdfUuid;

//    @Transient
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "pdfUuid", referencedColumnName = "uuid", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private FileManaged pdfManaged;

    @FullTextField(analyzer = "japanese")
    @Column(columnDefinition = "TEXT")
    private String content;

}
