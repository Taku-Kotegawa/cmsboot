package jp.co.stnet.cms.example.application.job.job01;

import jp.co.stnet.cms.example.domain.model.job01.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ImportEmployeeBatchConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    // Reader
    public ListItemReader<Employee> reader() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee(1, "scott"));
        list.add(new Employee(2, "virgil"));
        list.add(new Employee(3, "gordon"));
        list.add(new Employee(4, "john"));
        list.add(new Employee(5, "alan"));

        return new ListItemReader<Employee>(list);

    }


    // Writer
    public FlatFileItemWriter<Employee> writer() {

        DelimitedLineAggregator<Employee> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<Employee> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[]{"empId", "empName"});
        beanWrapperFieldExtractor.afterPropertiesSet();
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Employee>()
                .name("importSimpleEntity")
                .lineAggregator(delimitedLineAggregator)
                .resource(new FileSystemResource("target/output.csv"))
                .build();
    }

    // Processor

    public EmployeeProcessor processor() {
        return new EmployeeProcessor();
    }

    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Employee, Employee> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importSimpleEntityJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

}
