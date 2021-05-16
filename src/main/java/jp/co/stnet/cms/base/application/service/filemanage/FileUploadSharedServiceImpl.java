package jp.co.stnet.cms.base.application.service.filemanage;


import jp.co.stnet.cms.base.application.repository.filemanage.TempFileRepository;
import jp.co.stnet.cms.base.domain.model.filemanage.TempFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import java.time.LocalDateTime;
import java.util.UUID;

import static jp.co.stnet.cms.common.message.MessageKeys.E_SL_FW_5001;

@Deprecated
@Slf4j
@Service
@Transactional
public class FileUploadSharedServiceImpl implements FileUploadSharedService {

    @Autowired
    TempFileRepository tempFileRepository;

    @Override
    public String uploadTempFile(TempFile tempFile) {
        tempFile.setId(UUID.randomUUID().toString());
        tempFileRepository.save(tempFile);
        return tempFile.getId();
    }

    @Override
    public TempFile findTempFile(String id) {
        return tempFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ResultMessages.error().add(E_SL_FW_5001, id)));
    }

    @Override
    public void deleteTempFile(String id) {
        tempFileRepository.deleteById(id);
    }

    @Override
    public void cleanUp(LocalDateTime deleteTo) {
        tempFileRepository.deleteByUploadedDateLessThan(deleteTo);
    }
}
