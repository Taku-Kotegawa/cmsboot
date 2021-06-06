package jp.co.stnet.cms.common.batch;

import jp.co.stnet.cms.common.mapper.NullBindBeanWrapperFieldSetMapper;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

@AllArgsConstructor
public class ReaderFactory<T> {

    private final String[] columns;

    private final Class<T> clazz;

    /**
     * 選択したフォーマット用のItemStreamReaderを取得する
     *
     * @param fileType  fileType
     * @param inputFile inputFile
     * @param encoding  encoding
     * @return ItemStreamReader
     */
    public ItemStreamReader<T> getItemStreamReader(String fileType, String inputFile, String encoding) {
        FlatFileItemReader fileReader;
        if ("CSV".equals(fileType)) {
            fileReader = csvReader(inputFile, encoding);
        } else {
            fileReader = tsvReader(inputFile, encoding);
        }
        return fileReader;
    }

    /**
     * CSV用Reader
     *
     * @param inputFile inputFile
     * @param encoding  encoding
     * @return FlatFileItemReader
     */
    public FlatFileItemReader csvReader(String inputFile, String encoding) {

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setNames(columns);
        delimitedLineTokenizer.setQuoteCharacter('"');

        var nullBindBeanWrapperFieldSetMapper = new NullBindBeanWrapperFieldSetMapper();
        nullBindBeanWrapperFieldSetMapper.setTargetType(clazz);


        var defaultLineMapper = new DefaultLineMapper();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(nullBindBeanWrapperFieldSetMapper);

        return new FlatFileItemReaderBuilder<T>()
                .name("flatFileItemReader")
                .strict(true)
                .linesToSkip(1)
                .recordSeparatorPolicy(new BlankLineRecordSeparatorPolicy())
                .resource(new FileSystemResource(inputFile))
                .encoding(encoding)
                .lineMapper(defaultLineMapper)
                .build();
    }

    /**
     * TSV用Reader
     *
     * @param inputFile inputFile
     * @param encoding  encoding
     * @return FlatFileItemReader
     */
    public FlatFileItemReader tsvReader(String inputFile, String encoding) {

        var delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_TAB);
        delimitedLineTokenizer.setNames(columns);

        var nullBindBeanWrapperFieldSetMapper = new NullBindBeanWrapperFieldSetMapper();
        nullBindBeanWrapperFieldSetMapper.setTargetType(clazz);


        var defaultLineMapper = new DefaultLineMapper();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(nullBindBeanWrapperFieldSetMapper);

        return new FlatFileItemReaderBuilder<T>()
                .name("flatFileItemReader")
                .strict(true)
                .linesToSkip(1)
                .recordSeparatorPolicy(new BlankLineRecordSeparatorPolicy())
                .resource(new FileSystemResource(inputFile))
                .encoding(encoding)
                .lineMapper(defaultLineMapper)
                .build();
    }
}
