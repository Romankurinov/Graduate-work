package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class PaymentChoosePage {

    private SelenideElement cashButton = $$("button").find(Condition.exactText("Купить"));
    private SelenideElement creditButton = $$("button").find(Condition.exactText("Купить в кредит"));


    public void openPaymentChoosePage() {
        String suturl = System.getProperty("test.suturl");
        open(suturl);
    }

    public void openCashPaymentPage() {
        cashButton.click();
    }

    public void openCreditPayPage() {
        creditButton.click();
    }
}
