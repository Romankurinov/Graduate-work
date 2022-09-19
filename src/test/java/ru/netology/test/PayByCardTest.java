package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.pages.PaymentChoosePage;
import ru.netology.pages.CashPaymentPage;

import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;

public class PayByCardTest extends BaseUITest {

    PaymentChoosePage paymentChoosePage = new PaymentChoosePage();
    CashPaymentPage cashPaymentPage = new CashPaymentPage();

    @BeforeEach
    void setUpForPayWithCard() {
        paymentChoosePage.payPaymentChoosePage();
        paymentChoosePage.payWithCard();
    }



    // Успешная покупка, карта со статусом APPROVED (тест прошел)
    @Test
    public void shouldSuccessPayIfValidApprovedCard() {
        val cardData = getApprovedNumber();
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
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.emptyFieldError();
    }

    // Ввод в поле Номер карты недостаточного количества цифр (тест прошел, но лучше указывать ошибку "Указано
    // недостаточно цифр")
    @Test
    public void shouldHaveNumberIfFewDigits() {
        val cardData = getNumberIfFewDigits();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Номер карты буквенных символов (тест прошел, поле осталось незаполненным)
    @Test
    public void shouldHaveErrorTextIfPutTextInCardNumber() {
        val cardData = getLetters();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Номер карты несуществующий номер карты (тест прошел)
    @Test
    public void shouldHaveErrorNotificationIfPutUnrealCardNumber() {
        val cardData = getNonExistentCardNumber();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();
    }

    // Ввод нулей в поле Номер карты (тест прошел)
    @Test
    public void shouldAnErrorAppearWhenEnteringZerosInTheCardNumber() {
        val cardData = getEnteringZeros();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();
    }

    // Оплата картой, которой нет в БД (тест прошел)
    @Test
    public void shouldHaveNumberIfOutOfBase() {
        val cardData = getNumberIfNotExistInBase();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();
    }

    // Оплата картой разных форматов, которых нет в БД (тест не проходит, если количество цифр в карте меньше или больше
    // 16, хотя существуют карты от 13 до 19 цифр)
    @Test
    public void shouldHaveNumberIfFakerCard() {
        val cardData = getNumberFaker();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.failureResultNotification();
    }

    // Пустое поле Месяц (тест не прошел, неверная ошибка "Неверный формат" вместо "Поле обязательно для заполнения")
    @Test
    public void shouldHaveEmptyMonth() {
        val cardData = getEmptyMonth();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.emptyFieldError();
    }

    // Ввод в поле Месяц нулевых значений (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveMonthWithZero() {
        val cardData = getMonthWithZero();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц значения больше 12 (тест прошел)
    @Test
    public void shouldHaveMonthMore12() {
        val cardData = getMonthMore12();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц 1 цифры (тест прошел)
    @Test
    public void shouldHaveMonthWithOneDigit() {
        val cardData = getMonthWithOneDigit();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Месяц буквенных символов (тест прошел, поле осталось незаполненным)
    @Test
    public void shouldMonthFieldWithLetters() {
        val cardData = getLettersMonth();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Пустое поле Год (тест не прошел, ошибка Неверный формат, а не Поле обязательно для заполнения)
    @Test
    public void shouldHaveEmptyYear() {
        val cardData = getEmptyYear();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.emptyFieldError();
    }

    // Истек срок действия карты (тест не прошел, неверная ошибка "Неверно указан срок действия карты" вместо
    // "Истек срок действия карты")
    @Test
    public void shouldHaveYearBeforeCurrentYear() {
        val cardData = getExpiredCard();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.expiredDatePassError();
    }

    // Год намного позднее текущего (тест прошел)
    @Test
    public void shouldHaveYearInTheFarFuture() {
        val cardData = getInvalidYearIfInTheFarFuture();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.invalidCardExpirationDateError();
    }

    // Поле Год с одной цифрой (тест прошел)
    @Test
    public void shouldHaveYearWithOneDigit() {
        val cardData = getYearWithOneDigit();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Год буквенных символов (тест прошел, поле осталось незаполненным)
    @Test
    public void shouldYearFieldWithLetters() {
        val cardData = getYearLetters();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Поле Год с нулевыми значениями (тест прошел)
    @Test
    public void shouldHaveYearWithZero() {
        val cardData = getYearWithZero();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.expiredDatePassError();
    }

    // Пустое поле Владелец (тест прошел)
    @Test
    public void shouldHaveEmptyHolder() {
        val cardData = getEmptyHolder();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.emptyFieldError();
    }

    // Ввод в поле Владелец только фамилии (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveHolderWithoutName() {
        val cardData = getHolderWithoutName();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Указание Владельца кириллицей (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveRussianHolder() {
        val cardData = getRussianHolder();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Владелец цифр (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveDigitsInHolder() {
        val cardData = getDigitsInHolder();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Поле Владелец с указанием спецсимволов (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveSpecialCharactersInHolder() {
        val cardData = getSpecialCharactersInHolder();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества пробелов между фамилией и именем (тест не прошел, оплата успешная.
    // Необходима доработка в виде появляющегося предупреждения "Допустим только один пробел между именем и фамилией")
    @Test
    public void shouldHaveHolderWithManySpaces() {
        val cardData = getHolderWithManySpaces();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества символов (тест не прошел, оплата успешная. Необходима доработка в виде
    // появляющегося предупреждения "Допустимо не более ** символов")
    @Test
    public void shouldHaveHolderWithManyLetters() {
        val cardData = getHolderWithManyLetters();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Указание в поле Владелец фамилии через дефис (тест прошел)
    @Test
    public void shouldHaveHolderSurnameWithDash() {
        val cardData = getHolderSurnameWithDash();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.successResultNotification();
    }

    // Указание в поле Владелец имени через дефис (тест прошел)
    @Test
    public void shouldHaveHolderNameWithDash() {
        val cardData = getHolderNameWithDash();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.successResultNotification();
    }

    // Пустое поле CVC-код (тест не прошел, ошибка появляется под полем Владелец)
    @Test
    public void shouldHaveEmptyCvcCode() {
        val cardData = getEmptyCvcCode();
        cashPaymentPage.completedPurchaseForm(cardData);
        final ElementsCollection fieldSub = $$(".input__sub");
        final SelenideElement cvvFieldSub = fieldSub.get(2);
        cvvFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    // Поле CVC-код с 2 цифрами (тест прошел, но необходима доработка в виде сообщения "Поле должно состоять из 3 цифр")
    @Test
    public void shouldHaveCvcCodeWithTwoDigits() {
        val cardData = getCvcCodeWithTwoDigits();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Поле CVC-код с буквенными символами (тест не прошел)
    @Test
    public void shouldCvcCodeFieldWithLetters() {
        val cardData = getCvcCodeWithZero();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Поле CVC-код с нулевыми значениями (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveCvcCodeWithZero() {
        val cardData = getCvcCodeLetters();
        cashPaymentPage.completedPurchaseForm(cardData);
        cashPaymentPage.incorrectFormatError();
    }

    // Все поля формы пустые (тест не прошел, ошибка "Неверный формат" во всех полях кроме поля Владелец)
    @Test
    public void shouldHaveEmptyAllFields() {
        val cardData = getCardDataIfEmptyAllFields();
        cashPaymentPage.completedPurchaseForm(cardData);
        final ElementsCollection fieldSub = $$(".input__sub");
        final SelenideElement cardNumberFieldSub = fieldSub.get(1);
        final SelenideElement monthFieldSub = fieldSub.get(2);
        final SelenideElement yearFieldSub = fieldSub.get(3);
        final SelenideElement holderFieldSub = fieldSub.get(4);
        final SelenideElement cvvFieldSub = fieldSub.get(5);
        cardNumberFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        monthFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        yearFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        holderFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        cvvFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }
}
