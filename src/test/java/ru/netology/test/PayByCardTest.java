package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.pages.CashPaymentPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;

public class PayByCardTest extends BaseUITest {
    private CashPaymentPage cashPaymentPage;

    // Успешная покупка, карта со статусом APPROVED (тест прошел)
    @Test
    public void shouldSuccessPayIfValidApprovedCard() {
        val cardData = getApprovedNumber();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.successResultNotification();

        val expectedStatus = "APPROVED";
        val actualStatus = getCardStatusForPayWithCard();
        assertEquals(expectedStatus, actualStatus);

        val expectedAmount = "4500000";
        val actualAmount = getAmountPurchase();
        assertEquals(expectedAmount, actualAmount);

        val transactionIdExpected = getTransactionId();
        val paymentIdActual = getPaymentIdForPayWithCard();
        assertNotNull(transactionIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(transactionIdExpected, paymentIdActual);
    }

    // Неуспешная покупка, карта со статусом DECLINED (тест не прошел, оплата успешная)
    @Test
    public void shouldFailurePayIfValidDeclinedCard() {
        val cardData = getDeclinedNumber();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();

        val expectedStatus = "DECLINED";
        val actualStatus = getCardStatusForPayWithCard();
        assertEquals(expectedStatus, actualStatus);

        val transactionIdExpected = getTransactionId();
        val paymentIdActual = getPaymentIdForPayWithCard();
        assertNotNull(transactionIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(transactionIdExpected, paymentIdActual);
    }

    // Пустое поле Номер карты (тест не прошел, появляется ошибка "Неверный формат" вместо "Поле обязательно для
    // заполнения")
    @Test
    public void shouldHaveEmptyNumber() {
        val cardData = getEmptyNumber();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.emptyFieldError();
    }

    // Ввод в поле Номер карты недостаточного количества цифр (тест прошел, но лучше указывать ошибку "Указано
    // недостаточно цифр")
    @Test
    public void shouldHaveNumberIfFewDigits() {
        val cardData = getNumberIfFewDigits();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Номер карты буквенных символов (тест прошел, поле осталось незаполненным)
    @Test
    public void shouldHaveErrorTextIfPutTextInCardNumber() {
        val cardData = getLetters();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Номер карты несуществующий номер карты (тест прошел)
    @Test
    public void shouldHaveErrorNotificationIfPutUnrealCardNumber() {
        val cardData = getNonExistentCardNumber();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();
    }

    // Ввод нулей в поле Номер карты (тест прошел)
    @Test
    public void shouldAnErrorAppearWhenEnteringZerosInTheCardNumber() {
        val cardData = getEnteringZeros();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();
    }

    // Оплата картой, которой нет в БД (тест прошел)
    @Test
    public void shouldHaveNumberIfOutOfBase() {
        val cardData = getNumberIfNotExistInBase();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();
    }

    // Оплата картой разных форматов, которых нет в БД (тест не проходит, если количество цифр в карте меньше или больше
    // 16, хотя существуют карты от 13 до 19 цифр)
    @Test
    public void shouldHaveNumberIfFakerCard() {
        val cardData = getNumberFaker();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();
    }

    // Пустое поле Месяц (тест не прошел, неверная ошибка "Неверный формат" вместо "Поле обязательно для заполнения")
    @Test
    public void shouldHaveEmptyMonth() {
        val cardData = getEmptyMonth();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.emptyFieldError();
    }

    // Ввод в поле Месяц нулевых значений (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveMonthWithZero() {
        val cardData = getMonthWithZero();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц значения больше 12 (тест прошел)
    @Test
    public void shouldHaveMonthMore12() {
        val cardData = getMonthMore12();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц 1 цифры (тест прошел)
    @Test
    public void shouldHaveMonthWithOneDigit() {
        val cardData = getMonthWithOneDigit();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Месяц буквенных символов (тест прошел, поле осталось незаполненным)
    @Test
    public void shouldMonthFieldWithLetters() {
        val cardData = getLettersMonth();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Пустое поле Год (тест не прошел, ошибка Неверный формат, а не Поле обязательно для заполнения)
    @Test
    public void shouldHaveEmptyYear() {
        val cardData = getEmptyYear();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.emptyFieldError();
    }

    // Истек срок действия карты (тест не прошел, неверная ошибка "Неверно указан срок действия карты" вместо
    // "Истек срок действия карты")
    @Test
    public void shouldHaveYearBeforeCurrentYear() {
        val cardData = getExpiredCard();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.expiredDatePassError();
    }

    // Год намного позднее текущего (тест прошел)
    @Test
    public void shouldHaveYearInTheFarFuture() {
        val cardData = getInvalidYearIfInTheFarFuture();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.invalidCardExpirationDateError();
    }

    // Поле Год с одной цифрой (тест прошел)
    @Test
    public void shouldHaveYearWithOneDigit() {
        val cardData = getYearWithOneDigit();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Год буквенных символов (тест прошел, поле осталось незаполненным)
    @Test
    public void shouldYearFieldWithLetters() {
        val cardData = getYearLetters();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Поле Год с нулевыми значениями (тест прошел)
    @Test
    public void shouldHaveYearWithZero() {
        val cardData = getYearWithZero();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.expiredDatePassError();
    }

    // Пустое поле Владелец (тест прошел)
    @Test
    public void shouldHaveEmptyHolder() {
        val cardData = getEmptyHolder();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.emptyFieldError();
    }

    // Ввод в поле Владелец только фамилии (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveHolderWithoutName() {
        val cardData = getHolderWithoutName();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Указание Владельца кириллицей (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveRussianHolder() {
        val cardData = getRussianHolder();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Владелец цифр (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveDigitsInHolder() {
        val cardData = getDigitsInHolder();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Поле Владелец с указанием спецсимволов (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveSpecialCharactersInHolder() {
        val cardData = getSpecialCharactersInHolder();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества пробелов между фамилией и именем (тест не прошел, оплата успешная.
    // Необходима доработка в виде появляющегося предупреждения "Допустим только один пробел между именем и фамилией")
    @Test
    public void shouldHaveHolderWithManySpaces() {
        val cardData = getHolderWithManySpaces();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества символов (тест не прошел, оплата успешная. Необходима доработка в виде
    // появляющегося предупреждения "Допустимо не более ** символов")
    @Test
    public void shouldHaveHolderWithManyLetters() {
        val cardData = getHolderWithManyLetters();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Указание в поле Владелец фамилии через дефис (тест прошел)
    @Test
    public void shouldHaveHolderSurnameWithDash() {
        val cardData = getHolderSurnameWithDash();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.successResultNotification();
    }

    // Указание в поле Владелец имени через дефис (тест прошел)
    @Test
    public void shouldHaveHolderNameWithDash() {
        val cardData = getHolderNameWithDash();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.successResultNotification();
    }

    // Пустое поле CVC-код (тест не прошел, ошибка появляется под полем Владелец)
    @Test
    public void shouldHaveEmptyCvcCode() {
        val cardData = getEmptyCvcCode();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.notificationOfAnEmptyCVCField();
    }

    // Поле CVC-код с 2 цифрами (тест прошел, но необходима доработка в виде сообщения "Поле должно состоять из 3 цифр")
    @Test
    public void shouldHaveCvcCodeWithTwoDigits() {
        val cardData = getCvcCodeWithTwoDigits();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Поле CVC-код с буквенными символами (тест прошел)
    @Test
    public void shouldCvcCodeFieldWithLetters() {
        val cardData = getCvcCodeLetters();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Поле CVC-код с нулевыми значениями (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveCvcCodeWithZero() {
        val cardData = getCvcCodeWithZero();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();
    }

    // Все поля формы пустые (тест не прошел, ошибка "Неверный формат" во всех полях кроме поля Владелец)
    @Test
    public void shouldHaveEmptyAllFields() {
        val cardData = getCardDataIfEmptyAllFields();
        cashPaymentPage = paymentChoosePage.payWithCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
        cashPaymentPage.notificationOfAllEmptyFields();
    }
}