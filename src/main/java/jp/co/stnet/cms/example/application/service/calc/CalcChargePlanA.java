package jp.co.stnet.cms.example.application.service.calc;

import java.math.BigDecimal;

/**
 * 基本料金計算 (使用量) * (基本料率)
 */
public class CalcChargePlanA implements CalcChargeInterface {

    // 基本料金の率
    public static final double rate = Constants.Rate.basic;

    private ChargeBasicRule rule = new ChargeBasicRule(rate);

    @Override
    public BigDecimal getTotal(Long amountOfUsage) {
        return rule.getBasic(amountOfUsage);
    }

}
