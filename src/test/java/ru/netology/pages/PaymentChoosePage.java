package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class PaymentChoosePage {

    private static final SelenideElement payWithCardButton = $$("button").find(exactText("Купить"));
    private static final SelenideElement payWithCreditButton = $$("button").find(exactText("Купить в кредит"));

    public void payPaymentChoosePage() {
        String property = System.getProperty("test.property");
        open(property);
    }

    public void payWithCard() {
        payWithCardButton.click();
    }

    public void payWithCredit() {
        payWithCreditButton.click();
    }
}