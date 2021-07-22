package jp.co.stnet.cms.base.application.repository.variable;


import jp.co.stnet.cms.base.domain.model.variable.Variable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Variableリポジトリ.
 */
@Repository
public interface VariableRepository extends JpaRepository<Variable, Long> {


    List<Variable> findAllByType(String type);


    List<Variable> findAllByTypeAndCode(String type, String code);


    List<Variable> findAllByTypeAndValue1(String type, String value1);

    List<Variable> findAllByTypeAndValue2(String type, String value2);

    List<Variable> findAllByTypeAndValue3(String type, String value3);

    List<Variable> findAllByTypeAndValue4(String type, String value4);

    List<Variable> findAllByTypeAndValue5(String type, String value5);

    List<Variable> findAllByTypeAndValue6(String type, String value6);

    List<Variable> findAllByTypeAndValue7(String type, String value7);

    List<Variable> findAllByTypeAndValue8(String type, String value8);

    List<Variable> findAllByTypeAndValue9(String type, String value9);

    List<Variable> findAllByTypeAndValue10(String type, String value10);

    List<Variable> findAllByTypeAndValint1(String type, Integer valint1);

    List<Variable> findAllByTypeAndValint2(String type, Integer valint2);

    List<Variable> findAllByTypeAndValint3(String type, Integer valint3);

    List<Variable> findAllByTypeAndValint4(String type, Integer valint4);

    List<Variable> findAllByTypeAndValint5(String type, Integer valint5);

    List<Variable> findAllByTypeAndDate1(String type, LocalDate date1);

    List<Variable> findAllByTypeAndDate2(String type, LocalDate date2);

    List<Variable> findAllByTypeAndDate3(String type, LocalDate date3);

    List<Variable> findAllByTypeAndDate4(String type, LocalDate date4);

    List<Variable> findAllByTypeAndDate5(String type, LocalDate date5);

}
