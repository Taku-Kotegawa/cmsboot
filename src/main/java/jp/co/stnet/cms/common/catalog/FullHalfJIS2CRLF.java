package jp.co.stnet.cms.common.catalog;

import org.terasoluna.gfw.common.codepoints.CodePoints;
import org.terasoluna.gfw.common.codepoints.catalog.CRLF;

/**
 * コードポイント集合体・半角英数字記号(JIS第１，２水準、改行を含む)
 * @See TERASOLUNA開発ガイドの「文字列操作」
 */
public class FullHalfJIS2CRLF extends CodePoints {

    public FullHalfJIS2CRLF() {
        super(new FullHalfJIS2()
                .union(new CRLF())
        );
    }
}
