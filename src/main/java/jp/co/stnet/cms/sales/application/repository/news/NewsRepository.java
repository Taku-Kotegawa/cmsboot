package jp.co.stnet.cms.sales.application.repository.news;

import jp.co.stnet.cms.base.domain.model.variable.Variable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<Variable,Long> {
    /**
     *VariableテーブルをIDで検索してデータを取得
     *
     * @param id  データのID
     * @return
     */
    Optional<Variable> findById(long id);


    /**
     *Variableテーブルから以下の条件に合致するものを検索
     * ・type：NEWS
     * ・STATUS：1
     * ・DATE1 <= 検索日　<= DATE2
     *
     * @param type　TYPE
     * @param status　STATUS
     * @param date1　検索日
     * @param date2　検索日
     * @return
     */
    List<Variable> findByTypeAndStatusAndDate1LessThanEqualAndDate2GreaterThanEqual(String type, String status, LocalDate date1,LocalDate date2);
}
