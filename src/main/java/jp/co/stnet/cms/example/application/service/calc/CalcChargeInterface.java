package jp.co.stnet.cms.example.application.service.calc;

import java.math.BigDecimal;

/**
 * 全ての料金計算処理が継承するインターフェース
 */
public interface CalcChargeInterface {
    public BigDecimal getTotal(Long amountOfUsage);
}
