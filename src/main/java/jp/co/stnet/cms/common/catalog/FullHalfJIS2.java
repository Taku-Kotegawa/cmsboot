package jp.co.stnet.cms.common.catalog;

//import org.terasoluna.gfw.common.codepoints.CodePoints;
//import org.terasoluna.gfw.common.codepoints.catalog.ASCIIPrintableChars;

import org.terasoluna.gfw.common.codepoints.CodePoints;
import org.terasoluna.gfw.common.codepoints.catalog.*;

/**
 * コードポイント集合体・半角英数字記号(JIS第１，２水準、改行は含まない)
 * @See TERASOLUNA開発ガイドの「文字列操作」
 */
public class FullHalfJIS2 extends CodePoints {

    public FullHalfJIS2() {
        super(new ASCIIPrintableChars()                     // Ascii文字(制御文字除く)
                .union(new JIS_X_0201_Katakana())           // 半角カタカナ
                .union(new JIS_X_0201_LatinLetters())       // 半角英数記号
                .union(new JIS_X_0208_BoxDrawingChars())    // 全角罫線
                .union(new JIS_X_0208_CyrillicLetters())    // 全角キリル文字
                .union(new JIS_X_0208_GreekLetters())       // 全角ギリシャ文字
                .union(new JIS_X_0208_Hiragana())           // 全角ひらがな
                .union(new JIS_X_0208_Katakana())           // 全角カタカナ
                .union(new JIS_X_0208_LatinLetters())       // 全角英数字
                .union(new JIS_X_0208_SpecialChars())       // 全角記号
                .union(new JIS_X_0208_Kanji())              // 全角漢字JIS第１、２水準
        );
    }
}
