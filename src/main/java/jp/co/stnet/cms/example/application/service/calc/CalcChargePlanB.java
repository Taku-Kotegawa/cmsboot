package jp.co.stnet.cms.example.application.service.calc;

import java.math.BigDecimal;

/**
 * 特別料金計算 (使用量) * (特別料率)
 */
public class CalcChargePlanB implements CalcChargeInterface {

    // 特別料金の率
    public static final double rate = Constants.Rate.special;

    private ChargeBasicRule rule = new ChargeBasicRule(rate);

    @Override
    public BigDecimal getTotal(Long amountOfUsage) {
        return rule.getBasic(amountOfUsage);
    }
}
