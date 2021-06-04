package jp.co.stnet.cms.example.application.service.calc;

import lombok.Builder;
import lombok.Data;

/**
 * お客さまと使用量と料金計算プラン
 */
@Data
@Builder
public class Customer {
    private String customerName;
    private Long amountOfUsage;
    private String calcPlan;
}
