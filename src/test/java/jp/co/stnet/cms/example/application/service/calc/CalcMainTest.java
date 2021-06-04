package jp.co.stnet.cms.example.application.service.calc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalcMainTest {

    @Test
    void printFee() {
        CalcMain main = new CalcMain();
        main.printFee();
    }
}