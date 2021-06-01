package jp.co.stnet.cms.example.application.job.job03;

import jp.co.stnet.cms.common.mapper.NullBindBeanWrapperFieldSetMapper;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntityCsv;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
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
public class importEmployeeBatchConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    Job03Tasklet job03Tasklet;


//    <!-- CSVファイルリーダー -->
//    <bean id="csvReader"
//    class="org.springframework.batch.item.file.FlatFileItemReader" scope="step"
//    p:resource="file:#{jobParameters['inputFile']}"
//    p:encoding="#{jobParameters['encoding']}"
//    p:linesToSkip="1"
//    p:strict="true">
//        <property name="lineMapper">
//            <bean class="org.springframework.batch.item.file.mapping.DefaultLineMapper">
//                <property name="lineTokenizer">
//                    <bean class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer"
//    p:names="id,status,text01,text02,text03,text04,text05,radio01,radio02,checkbox01,checkbox02,textarea01,date01,datetime01,select01,select02,select03,select04,combobox01,combobox02,combobox03,attachedFile01"
//    p:delimiter=","
//    p:quoteCharacter='"'/>
//                </property>
//                <property name="fieldSetMapper">
//                    <bean class="jp.co.stnet.cms.batch.common.mapper.NullBindBeanWrapperFieldSetMapper"
//    p:targetType="jp.co.stnet.cms.batch.job03.SimpleEntityCsv"/>
//                </property>
//            </bean>
//        </property>
//    </bean>

    @Bean
    @StepScope
    public FlatFileItemReader csvReader(@Value("#{jobParameters[inputFile]}") String inputFile, @Value("#{jobParameters[encoding]}") String encoding) {

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setNames("id","status","text01","text02","text03","text04","text05","radio01","radio02","checkbox01","checkbox02","textarea01","date01","datetime01","select01","select02","select03","select04","combobox01","combobox02","combobox03","attachedFile01Uuid");
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

//    <!-- TSVファイルリーダー -->
//    <bean id="tsvReader"
//    class="org.springframework.batch.item.file.FlatFileItemReader" scope="step"
//    p:resource="file:#{jobParameters['inputFile']}"
//    p:encoding="#{jobParameters['encoding']}"
//    p:linesToSkip="1"
//    p:strict="true">
//        <property name="lineMapper">
//            <bean class="org.springframework.batch.item.file.mapping.DefaultLineMapper">
//                <property name="lineTokenizer">
//                    <bean class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer"
//    p:names="id,status,text01,text02,text03,text04,text05,radio01,radio02,checkbox01,checkbox02,textarea01,date01,datetime01,select01,select02,select03,select04,combobox01,combobox02,combobox03,attachedFile01"
//    p:delimiter="&#09;" />
//                </property>
//                <property name="fieldSetMapper">
//                    <bean class="jp.co.stnet.cms.batch.common.mapper.NullBindBeanWrapperFieldSetMapper"
//    p:targetType="jp.co.stnet.cms.batch.job03.SimpleEntityCsv"/>
//                </property>
//            </bean>
//        </property>
//    </bean>

    @Bean
    @StepScope
    public FlatFileItemReader tsvReader(@Value("#{jobParameters[inputFile]}") String inputFile, @Value("#{jobParameters[encoding]}") String encoding) {

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_TAB);
        delimitedLineTokenizer.setNames("id","status","text01","text02","text03","text04","text05","radio01","radio02","checkbox01","checkbox02","textarea01","date01","datetime01","select01","select02","select03","select04","combobox01","combobox02","combobox03","attachedFile01Uuid");

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
                .tasklet(job03Tasklet)
                .build();
    }

    @Bean
    public Job job03() {
        return jobBuilderFactory.get("job03")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }




}
