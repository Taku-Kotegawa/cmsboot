package jp.co.stnet.cms.base.application.service.filemanage;


import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * FileManagedSharedService
 */
public interface FileManagedSharedService {

    /**
     * idでファイルを取得する。
     *
     * @param id id
     * @return ファイル
     */
    byte[] getFile(Long id);

    /**
     * uuidでファイルを取得する。
     *
     * @param uuid uuid
     * @return ファイル
     */
    byte[] getFile(String uuid);

    /**
     * idでFileManagedを検索する。
     *
     * @param id id
     * @return FileManaged
     */
    FileManaged findById(Long id);

    /**
     * uuidでFileManagedを検索する。
     *
     * @param uuid uuid
     * @return FileManaged
     */
    FileManaged findByUuid(String uuid);

    /**
     * ファイルを保存する。(一時保存)
     *
     * @param file MultipartFile
     * @param fileType ファイルタイプ
     * @return FileManaged
     * @throws IOException ファイル操作例外
     */
    FileManaged store(MultipartFile file, String fileType) throws IOException;

    /**
     * ファイルを保存する。(一時保存)
     *
     * @param file File
     * @param fileType ファイルタイプ
     * @return FileManaged
     * @throws IOException ファイル操作例外
     */
    FileManaged store(File file, String fileType) throws IOException;

    /**
     * ファイルのステータスを一時保存から永久保存に変更する。
     *
     * @param uuid uuid
     */
    void permanent(String uuid);

    /**
     * idでFileManagedとファイルを削除する。
     *
     * @param id id
     */
    void delete(Long id);

    /**
     * uuidでFileManagedとファイルを削除する。
     *
     * @param uuid uuid
     */
    void delete(String uuid);

    /**
     * 指定日時以前の一時保存状態のFileManagedとファイルを削除する。
     *
     * @param deleteTo 日時
     */
    void cleanup(LocalDateTime deleteTo);

    /**
     * ファイルを保存するディレクトリを取得する。
     *
     * @return ディレクトリ
     */
    String getFileStoreBaseDir();

    /**
     * ファイルの内容を取得する。
     *
     * @param uuid UUID
     * @return ファイルの内容
     * @throws IOException ファイルの読み込みに失敗する場合
     * @throws TikaException Tikaでファイルの内容を読み込みに失敗する場合
     */
    String getContent(String uuid) throws IOException, TikaException;

    /**
     * FileManagedとファイルをコピーし、新しいUUIDを発番する。
     *
     * @param uuid UUID
     * @return コピー後のFileManaged
     * @throws IOException ファイルの操作に失敗した場合
     */
    FileManaged copyFile(String uuid) throws IOException;

    /**
     * 指定したURIでファイルを削除する。(物理ファイルのみ)
     *
     * @param uri URI
     */
    void deleteFile(String uri);

}
