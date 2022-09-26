package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;


public class PaymentChoosePage {

    private static final SelenideElement payWithCardButton = $$("button").find(exactText("Купить"));
    private static final SelenideElement payWithCreditButton = $$("button").find(exactText("Купить в кредит"));


    public CashPaymentPage payWithCard() {
        payWithCardButton.click();
        return new CashPaymentPage();
    }

    public CreditPayPage payWithCredit() {
        payWithCreditButton.click();
        return new CreditPayPage();
    }
}