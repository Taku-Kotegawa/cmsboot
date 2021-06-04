package jp.co.stnet.cms.example.application.service.calc;

import java.util.ArrayList;
import java.util.List;

/**
 * 料金印刷処理
 */
public class CalcMain {

    private final List<Customer> customers = new ArrayList<>();

    CalcMain() {
        // 最初にお客様毎の名前、使用量、料金プランを取得する
        customers.add(Customer.builder()
                .customerName("kawato").amountOfUsage(101L).calcPlan(CalcPlanSet.planA.name()).build());
        customers.add(Customer.builder()
                .customerName("satozaki").amountOfUsage(121L).calcPlan(CalcPlanSet.planB.name()).build());
        customers.add(Customer.builder()
                .customerName("kagawa").amountOfUsage(201L).calcPlan(CalcPlanSet.planC.name()).build());
    }

    /**
     * 料金印刷
     */
    public void printFee() {
        for (Customer customer : customers) {
            CalcPlanSet calcPlan = CalcPlanSet.valueOf(customer.getCalcPlan());
            System.out.println(
                    customer.getCustomerName() // お客様名
                            + "," + customer.getAmountOfUsage() //使用量
                            + "," + calcPlan.getPlanName() // 料金プラン名
                            + "," + calcPlan.getCalcCharge().getTotal(customer.getAmountOfUsage()) // 料金合計金額
            );
        }
    }
}
