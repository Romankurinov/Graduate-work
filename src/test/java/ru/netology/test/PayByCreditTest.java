package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.pages.CreditPayPage;
import ru.netology.pages.PaymentChoosePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;
import static ru.netology.data.SQLHelper.getBankId;

public class PayByCreditTest extends BaseUITest {

    private CreditPayPage creditPayPage;
    PaymentChoosePage paymentChoosePage = new PaymentChoosePage();

    // Успешная покупка тура за счет кредитных средств, карта со статусом APPROVED (тест прошел)
    @Test
    public void shouldSuccessCreditRequestIfValidApprovedCard() {
        val cardData = getApprovedNumber();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.successResultNotification();

        val expectedStatus = "APPROVED";
        val actualStatus = getCardStatusForPayWithCredit();
        assertEquals(expectedStatus, actualStatus);

        val bankIdExpected = getBankId();
        val paymentIdActual = getPaymentIdForPayWithCredit();
        assertNotNull(bankIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(bankIdExpected, paymentIdActual);
    }

    // Неуспешная покупка за счет кредитных средств. Карта со статусом DECLINED (тест не прошел, оплата успешная)
    @Test
    public void shouldFailureCreditRequestIfValidDeclinedCard() {
        val cardData = getDeclinedNumber();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.failureResultNotification();

        val expectedStatus = "DECLINED";
        val actualStatus = getCardStatusForPayWithCredit();
        assertEquals(expectedStatus, actualStatus);

        val bankIdExpected = getBankId();
        val paymentIdActual = getPaymentIdForPayWithCredit();
        assertNotNull(bankIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(bankIdExpected, paymentIdActual);
    }

    // Пустое поле Номер карты (тест не прошел, появляется ошибка "Неверный формат" вместо "Поле обязательно для
    // заполнения")
    @Test
    public void shouldHaveEmptyNumber() {
        val cardData = getEmptyNumber();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.emptyFieldError();
    }

    // Ввод в поле Номер карты недостаточного количества цифр (тест прошел, но лучше указывать ошибку "Указано
    // недостаточно цифр")
    @Test
    public void shouldHaveNumberIfFewDigits() {
        val cardData = getNumberIfFewDigits();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Ввод в поле Номер карты буквенных символов (тест прошел, поле осталось незаполненным)
    @Test
    public void shouldHaveErrorTextIfPutTextInCardNumber() {
        val cardData = getLetters();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Ввод в поле Номер карты несуществующий номер карты (тест прошел)
    @Test
    public void shouldHaveErrorNotificationIfPutUnrealCardNumber() {
        val cardData = getNonExistentCardNumber();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.failureResultNotification();
    }

    // Ввод нулей в поле Номер карты (тест прошел)
    @Test
    public void shouldAnErrorAppearWhenEnteringZerosInTheCardNumber() {
        val cardData = getEnteringZeros();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.failureResultNotification();
    }

    // Оплата картой, которой нет в БД (тест прошел)
    @Test
    public void shouldHaveNumberIfOutOfBase() {
        val cardData = getNumberIfNotExistInBase();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.failureResultNotification();
    }

    // Оплата картой разных форматов, которых нет в БД (тест не проходит, если количество цифр в карте меньше или больше
    // 16, хотя существуют карты от 13 до 19 цифр)
    @Test
    public void shouldHaveNumberIfFakerCard() {
        val cardData = getNumberFaker();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.failureResultNotification();
    }

    // Пустое поле Месяц (тест не прошел, неверная ошибка "Неверный формат" вместо "Поле обязательно для заполнения")
    @Test
    public void shouldHaveEmptyMonth() {
        val cardData = getEmptyMonth();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.emptyFieldError();
    }

    // Ввод в поле Месяц нулевых значений (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveMonthWithZero() {
        val cardData = getMonthWithZero();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц значения больше 12 (тест прошел, но в качестве пожелания лучше указать ошибку "Введите срок
    // действия как указано на карте")
    @Test
    public void shouldHaveMonthMore12() {
        val cardData = getMonthMore12();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц 1 цифры (тест прошел)
    @Test
    public void shouldHaveMonthWithOneDigit() {
        val cardData = getMonthWithOneDigit();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Ввод в поле Месяц буквенных символов (тест прошел, поле осталось незаполненным)
    @Test
    public void shouldMonthFieldWithLetters() {
        val cardData = getLettersMonth();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Пустое поле Год (тест не прошел, ошибка Неверный формат, а не Поле обязательно для заполнения)
    @Test
    public void shouldHaveEmptyYear() {
        val cardData = getEmptyYear();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.emptyFieldError();
    }

    // Истек срок действия карты (тест не прошел, неверная ошибка "Неверно указан срок действия карты" вместо
    // "Истек срок действия карты")
    @Test
    public void shouldHaveYearBeforeCurrentYear() {
        val cardData = getExpiredCard();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.expiredDatePassError();
    }

    // Год намного позднее текущего (тест прошел)
    @Test
    public void shouldHaveYearInTheFarFuture() {
        val cardData = getInvalidYearIfInTheFarFuture();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.invalidCardExpirationDateError();
    }

    // Поле Год с одной цифрой (тест прошел)
    @Test
    public void shouldHaveYearWithOneDigit() {
        val cardData = getYearWithOneDigit();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Ввод в поле Год буквенных символов (тест прошел, поле осталось незаполненным)
    @Test
    public void shouldYearFieldWithLetters() {
        val cardData = getYearLetters();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Поле Год с нулевыми значениями (тест прошел)
    @Test
    public void shouldHaveYearWithZero() {
        val cardData = getYearWithZero();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.expiredDatePassError();
    }

    // Пустое поле Владелец (тест прошел)
    @Test
    public void shouldHaveEmptyHolder() {
        val cardData = getEmptyHolder();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.emptyFieldError();
    }

    // Ввод в поле Владелец только фамилии (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveHolderWithoutName() {
        val cardData = getHolderWithoutName();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Указание Владельца кириллицей (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveRussianHolder() {
        val cardData = getRussianHolder();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Ввод в поле Владелец цифр (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveDigitsInHolder() {
        val cardData = getDigitsInHolder();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Поле Владелец с указанием спецсимволов (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveSpecialCharactersInHolder() {
        val cardData = getSpecialCharactersInHolder();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества пробелов между фамилией и именем (тест не прошел, оплата успешная.
    // Необходима доработка в виде появляющегося предупреждения "Допустим только один пробел между именем и фамилией")
    @Test
    public void shouldHaveHolderWithManySpaces() {
        val cardData = getHolderWithManySpaces();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества символов (тест не прошел, оплата успешная. Необходима доработка в виде
    // появляющегося предупреждения "Допустимо не более ** символов")
    @Test
    public void shouldHaveHolderWithManyLetters() {
        val cardData = getHolderWithManyLetters();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Указание в поле Владелец фамилии через дефис (тест прошел)
    @Test
    public void shouldHaveHolderSurnameWithDash() {
        val cardData = getHolderSurnameWithDash();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.successResultNotification();
    }

    // Указание в поле Владелец имени через дефис (тест прошел)
    @Test
    public void shouldHaveHolderNameWithDash() {
        val cardData = getHolderNameWithDash();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.successResultNotification();
    }

    // Пустое поле CVC-код (тест не прошел, ошибка появляется под полем Владелец)
    @Test
    public void shouldHaveEmptyCvcCode() {
        val cardData = getEmptyCvcCode();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.notificationOfAnEmptyCVCField();
    }

    // Поле CVC-код с 2 цифрами (тест прошел, но необходима доработка в виде сообщения "Поле должно состоять из 3 цифр")
    @Test
    public void shouldHaveCvcCodeWithTwoDigits() {
        val cardData = getCvcCodeWithTwoDigits();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Поле CVC-код с буквенными символами (тест прошел)
    @Test
    public void shouldCvcCodeFieldWithLetters() {
        val cardData = getCvcCodeLetters();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Поле CVC-код с нулевыми значениями (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveCvcCodeWithZero() {
        val cardData = getCvcCodeWithZero();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.incorrectFormatError();
    }

    // Все поля формы пустые (тест не прошел, ошибка "Неверный формат" во всех полях кроме поля Владелец)
    @Test
    public void shouldHaveEmptyAllFields() {
        val cardData = getCardDataIfEmptyAllFields();
        creditPayPage = paymentChoosePage.payWithCredit();
        creditPayPage.completedPurchaseForm(cardData);
        creditPayPage.emptyFieldError();
        creditPayPage.notificationOfAllEmptyFields();
    }
}
