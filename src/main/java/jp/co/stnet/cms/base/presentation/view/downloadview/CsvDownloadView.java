package jp.co.stnet.cms.base.presentation.view.downloadview;

import com.orangesignal.csv.Csv;
import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.handlers.CsvEntityListHandler;
import org.springframework.stereotype.Component;
import org.terasoluna.gfw.web.download.AbstractFileDownloadView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
public class CsvDownloadView extends AbstractFileDownloadView {

    @Override
    protected void addResponseHeader(Map<String, Object> model, HttpServletRequest request,
                                     HttpServletResponse response) {
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s", model.get("csvFileName")));
        response.setContentType("text/plain");
    }

    @Override
    protected InputStream getInputStream(Map<String, Object> model, HttpServletRequest request) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Csv.save(model.get("exportCsvData"),
                baos,
                "UTF-8",
                (CsvConfig) model.get("csvConfig"),
                new CsvEntityListHandler<>((Class) model.get("class"))
//                (ColumnNameMappingBeanListHandler)model.get("handler")
        );

        return new ByteArrayInputStream(baos.toByteArray());
    }
}
