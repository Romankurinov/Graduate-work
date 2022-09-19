package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.data.APIHelper.*;
import static ru.netology.data.DataHelper.getApprovedNumber;
import static ru.netology.data.DataHelper.getDeclinedNumber;

public class APITest {

    //Оплата валидной картой, статус карты APPROVED (тест прошел)
    @Test
    void shouldStatusPayWithValidApprovedCardNumber() {
        val validApprovedCardNumber = getApprovedNumber();
        val status = payWithCardForm(validApprovedCardNumber);
        assertTrue(status.contains("APPROVED"));
    }

    //Оплата невалидной картой, статус карты DECLINED (тест прошел)
    @Test
    void shouldStatusPayWithValidDeclinedCardNumber() {
        val validDeclinedCardNumber = getDeclinedNumber();
        val status = payWithCardForm(validDeclinedCardNumber);
        assertTrue(status.contains("DECLINED"));
    }

    //Оплата валидной картой в кредит, статус карты APPROVED (тест прошел)
    @Test
    void shouldStatusPayWithCreditValidApprovedCardNumber() {
        val validApprovedCardNumber = getApprovedNumber();
        val status = payWithCreditForm(validApprovedCardNumber);
        assertTrue(status.contains("APPROVED"));
    }

    //Оплата невалидной картой в кредит, статус карты DECLINED (тест прошел)
    @Test
    void shouldStatusPayWithCreditValidDeclinedCardNumber() {
        val validDeclinedCardNumber = getDeclinedNumber();
        val status = payWithCreditForm(validDeclinedCardNumber);
        assertTrue(status.contains("DECLINED"));
    }
}
