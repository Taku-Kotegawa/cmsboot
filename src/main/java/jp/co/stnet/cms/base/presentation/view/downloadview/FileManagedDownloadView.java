package jp.co.stnet.cms.base.presentation.view.downloadview;


import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.terasoluna.gfw.web.download.AbstractFileDownloadView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
public class FileManagedDownloadView extends AbstractFileDownloadView {

    @Autowired
    FileManagedSharedService fileManagedSharedService;
    @Value("${file.store.basedir}")
    private String STORE_BASEDIR;

    @Override
    protected InputStream getInputStream(Map<String, Object> model, HttpServletRequest request) throws IOException {
        FileManaged fileManaged = (FileManaged) model.get("fileManaged");
        return new FileInputStream(new File(STORE_BASEDIR + fileManaged.getUri()));
    }

    @Override
    protected void addResponseHeader(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        FileManaged fileManaged = (FileManaged) model.get("fileManaged");
        response.setContentType(fileManaged.getFileMime());
        response.setHeader("Content-Disposition", fileManaged.getAttachmentContentDisposition().toString());
    }
}
