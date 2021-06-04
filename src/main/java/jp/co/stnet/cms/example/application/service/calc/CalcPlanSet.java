package jp.co.stnet.cms.example.application.service.calc;

/**
 * 料金プラン一覧
 */
public enum CalcPlanSet {

    planA("基本料金計算", new CalcChargePlanA()),
    planB("特別料金計算", new CalcChargePlanB()),
    planC("基本料金＋オプション", new CalcChargePlanC());

    /**
     * 料金プラン名
     */
    private final String planName;

    /**
     * 料金計算クラスのオブジェクトを格納する
     */
    private final CalcChargeInterface calcCharge;

    CalcPlanSet(String plan, CalcChargeInterface calcCharge) {
        planName = plan;
        this.calcCharge = calcCharge;
    }

    public String getPlanName() {
        return planName;
    }

    public CalcChargeInterface getCalcCharge() {
        return calcCharge;
    }

}
