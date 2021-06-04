package jp.co.stnet.cms.example.application.service.calc;

import java.math.BigDecimal;

/**
 * 基本料金＋オプション 使用量 * (基本料率) + (オプション料金)
 */
public class CalcChargePlanC implements CalcChargeInterface {

    // 基本料金の率
    public static final double rate = Constants.Rate.basic;

    private ChargeBasicRule rule = new ChargeBasicRule(rate);

    // オプション料金の設定
    public static BigDecimal optionCharge = new BigDecimal(Constants.Charge.option);

    @Override
    public BigDecimal getTotal(Long amountOfUsage) {
        return rule.getBasic(amountOfUsage).add(optionCharge);
    }

}
