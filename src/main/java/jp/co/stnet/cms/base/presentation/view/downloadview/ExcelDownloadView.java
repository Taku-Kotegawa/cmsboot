package jp.co.stnet.cms.base.presentation.view.downloadview;


import jp.co.stnet.cms.common.util.PoiUtils;
import jp.co.stnet.cms.common.util.StringUtils;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.terasoluna.gfw.web.download.AbstractFileDownloadView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

@Component
public class ExcelDownloadView extends AbstractFileDownloadView {

    @Value(value = "classpath:template/test.xlsx")
    private Resource templateFile;

    @Override
    protected InputStream getInputStream(Map<String, Object> model, HttpServletRequest request) throws IOException {

        File file = new File("/home/taku/sites/cms-java/cms/cms-web/src/main/resources/template/test.xlsx");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<SimpleEntity> list = (List<SimpleEntity>) model.get("list");

        Workbook wb = new XSSFWorkbook(templateFile.getInputStream()); // Resourceフォルダ配下を指定するとテンプレートの修正後に再起動が必要
//        Workbook wb = new XSSFWorkbook(new FileInputStream(file)); // 絶対パスを指定するとテンプレートの修正が即時に反映する
        Sheet sheet = wb.getSheetAt(0);

        sheet.getRow(0).getCell(1).setCellValue("あいう");

        int startRow = 8;
        for (int i = 0; i < list.size(); i++) {
            int j = startRow + i;
            if (sheet.getRow(j) == null) {
                PoiUtils.copyRow(wb, sheet, startRow, j);
            }
            setValue(sheet.getRow(j), i, list.get(i));
        }

        wb.write(baos);

        return new ByteArrayInputStream(baos.toByteArray());
    }

    private void setValue(Row row, int i, SimpleEntity entity) {
        row.getCell(0).setCellValue(entity.getId());
        row.getCell(1).setCellValue(StringUtils.nvl(entity.getText01()));
        row.getCell(2).setCellValue(StringUtils.nvl(entity.getDate01().toString()));
        // getRow/getCellを使う場合、値が入っていないセルを参照するとNullPointerExceptionが発生する。(参照先のセルに罫線を引いておけばOK)
    }

    @Override
    protected void addResponseHeader(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s", model.get("excelFileName")));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}


