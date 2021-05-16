package jp.co.stnet.cms.base.application.repository.filemanage;


import jp.co.stnet.cms.base.domain.model.filemanage.TempFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Deprecated
@Repository
public interface TempFileRepository extends JpaRepository<TempFile, String> {

    long deleteByUploadedDateLessThan(LocalDateTime deleteTo);

}
