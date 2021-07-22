package jp.co.stnet.cms.common.batch;

import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;

/**
 * FileInputStreamで、空白レコードを無視するポリシー
 */
public class BlankLineRecordSeparatorPolicy extends DefaultRecordSeparatorPolicy {

    @Override
    public boolean isEndOfRecord(final String line) {
        return line.trim().length() != 0 && super.isEndOfRecord(line);
    }

    @Override
    public String postProcess(final String record) {
        if (record == null || record.trim().length() == 0) {
            return null;
        }
        return super.postProcess(record);
    }

}
