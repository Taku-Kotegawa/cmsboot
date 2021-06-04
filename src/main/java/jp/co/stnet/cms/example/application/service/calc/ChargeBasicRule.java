package jp.co.stnet.cms.example.application.service.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 基本的な料金計算ルールを定義する
 */
public class ChargeBasicRule {

    private final Double rate;

    ChargeBasicRule(Double rate) {
        this.rate = rate;
    }

    /**
     * 基本料金計算ルール(使用量 * 率)、小数点以下切り捨て
     *
     * @param amountOfUsage 使用量
     * @return 基本料金
     */
    public BigDecimal getBasic(Long amountOfUsage) {
        return BigDecimal.valueOf(amountOfUsage * rate).setScale(0, RoundingMode.DOWN);
    }

}
