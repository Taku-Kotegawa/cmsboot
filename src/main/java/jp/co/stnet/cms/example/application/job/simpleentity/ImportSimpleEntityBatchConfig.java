package jp.co.stnet.cms.example.application.job.simpleentity;

import jp.co.stnet.cms.common.mapper.NullBindBeanWrapperFieldSetMapper;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntityCsv;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@EnableBatchProcessing
@Configuration
public class ImportSimpleEntityBatchConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    ImportSimpleEntityTasklet importSimpleEntityTasklet;

    private static final String[] columns = {"id", "status", "text01", "text02", "text03", "text04", "text05", "radio01", "radio02", "checkbox01", "checkbox02", "textarea01", "date01", "datetime01", "select01", "select02", "select03", "select04", "combobox01", "combobox02", "combobox03", "attachedFile01Uuid"};

    private static final String jobId = "job03";

    @Bean
    @StepScope
    public FlatFileItemReader csvReader(@Value("#{jobParameters[inputFile]}") String inputFile, @Value("#{jobParameters[encoding]}") String encoding) {

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setNames(columns);
        delimitedLineTokenizer.setQuoteCharacter('"');

        NullBindBeanWrapperFieldSetMapper nullBindBeanWrapperFieldSetMapper = new NullBindBeanWrapperFieldSetMapper();
        nullBindBeanWrapperFieldSetMapper.setTargetType(SimpleEntityCsv.class);


        DefaultLineMapper defaultLineMapper = new DefaultLineMapper();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(nullBindBeanWrapperFieldSetMapper);

        return new FlatFileItemReaderBuilder<SimpleEntityCsv>()
                .name("flatFileItemReader")
                .strict(true)
                .linesToSkip(1)
                .resource(new FileSystemResource(inputFile))
                .encoding(encoding)
                .lineMapper(defaultLineMapper)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader tsvReader(@Value("#{jobParameters[inputFile]}") String inputFile, @Value("#{jobParameters[encoding]}") String encoding) {

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_TAB);
        delimitedLineTokenizer.setNames(columns);

        NullBindBeanWrapperFieldSetMapper nullBindBeanWrapperFieldSetMapper = new NullBindBeanWrapperFieldSetMapper();
        nullBindBeanWrapperFieldSetMapper.setTargetType(SimpleEntityCsv.class);


        DefaultLineMapper defaultLineMapper = new DefaultLineMapper();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(nullBindBeanWrapperFieldSetMapper);

        return new FlatFileItemReaderBuilder<SimpleEntityCsv>()
                .name("flatFileItemReader")
                .strict(true)
                .linesToSkip(1)
                .resource(new FileSystemResource(inputFile))
                .encoding(encoding)
                .lineMapper(defaultLineMapper)
                .build();
    }

    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(importSimpleEntityTasklet)
                .build();
    }

    @Bean(name = "job03")
    public Job job() {
        return jobBuilderFactory.get(jobId)
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }
}
