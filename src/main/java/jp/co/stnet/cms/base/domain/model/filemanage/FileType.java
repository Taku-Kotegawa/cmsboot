package jp.co.stnet.cms.base.domain.model.filemanage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;


/**
 * ファイルタイプ.
 * <p>
 * 保存可能なファイルの拡張子、サイズを指定する。
 */
@AllArgsConstructor
@Getter
public enum FileType implements EnumCodeList.CodeListItem {

    UPLOAD_FILE("uploadFile", "tsv;csv;", "10"),
    PERSON("person", "txt;csv;", "2"),
    SIMPLE_ENTITY("simpleentity", "png;jpg;gif;", "5"),
    FILE_UPLOAD("fileupload", "png;jpg;gif;", "10"),
    DOCUMENT_FILE("document_file", "", "50"),
    DOCUMENT_PDF("document_pdf", "pdf;", "50"),
    VARIABLE("variable", "png;jpg;gif;", "10"),

    DEFAULT("default", "", "10");

    /**
     *
     */
    private final String label;

    /**
     * 許可拡張子.
     * <p>
     * 複数指定する場合は";"で区切る
     */
    private final String extensionPattern;

    /**
     * 最大サイズ(MB)
     */
    private final String fileSize;

    @Override
    public String getCodeLabel() {
        return label;
    }

    @Override
    public String getCodeValue() {
        return label;
    }

    public static FileType getByValue(String value) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getCodeValue().equals(value)) {
                return fileType;
            }
        }
        return null;
    }
}
