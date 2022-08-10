package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.CashPaymentPage;
import ru.netology.page.CreditPayPage;
import ru.netology.page.PaymentChoosePage;
import ru.netology.sqlUtils.SQLutils;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.netology.sqlUtils.SQLutils.*;

public class PaymentPageTest {
    static DataHelper.CardInfo cardInfo;

    @AfterEach
    @DisplayName("Чистит базу данных перед каждым тестом")
    void cleanBase() throws SQLException {
        SQLutils.cleanDB();
    }

    @BeforeAll
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        cardInfo = DataHelper.getCardInfo();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    static CashPaymentPage getCashPaymentPage() {
        val paymentChoosePage = new PaymentChoosePage();
        paymentChoosePage.openPaymentChoosePage();
        paymentChoosePage.openCashPaymentPage();
        return new CashPaymentPage();
    }

    static CreditPayPage getCreditPayPage() {
        val paymentChoosePage = new PaymentChoosePage();
        paymentChoosePage.openPaymentChoosePage();
        paymentChoosePage.openCreditPayPage();
        return new CreditPayPage();
    }

    //HAPPY PATH

    //APPROVED card
    @Test
    @DisplayName("DB OrderEntity should not be empty, " +
            "should get success notification with APPROVED card, valid card data when pay by debit")
    void shouldBuyTourWithValidDataApprovedCardInDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        val actual = DataHelper.approvedCardInfo().getStatus();
        cashPaymentPage.putValidDataApprovedCard(cardInfo);
        val expected = getDebitCardStatus();
        assertEquals(expected, actual);
        val paymentEntityId = getPaymentEntityId(actual);
        assertNotEquals("", paymentEntityId);
        val orderId = getOrderEntityId(paymentEntityId);
        assertNotEquals("", orderId);
    }

    @Test
    @DisplayName("DB OrderEntity should not be empty, " +
            "should get success notification with APPROVED card and valid card data when pay by credit")
    void shouldBuyTourWithCreditValidDataApprovedCard() throws SQLException {
        val creditPayPage = getCreditPayPage();
        val actual = DataHelper.approvedCardInfo().getStatus();
        creditPayPage.putValidDataApprovedCard(cardInfo);
        val expected = getCreditCardStatus();
        assertEquals(expected, actual);
        val creditRequestEntityId = getCreditRequestEntityId(actual);
        assertNotEquals("", creditRequestEntityId);
        val orderId = getOrderEntityId(creditRequestEntityId);
        assertNotEquals("", orderId);
    }


    //DECLINED card

    //BUG должно появиться сообщение "Банк отказал в проведении операции" и в базе данных не должно быть записей
    @Test
    @DisplayName("should get error notification with DECLINED card and valid card data when pay by debit, " +
            "DB OrderEntity should be empty")
    void shouldGetErrorIfBuyWithCashValidDataAndDeclinedCard() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.putValidDataDeclinedCard(cardInfo);
        val paymentEntityId = getPaymentEntityId(DataHelper.declinedCardInfo().getStatus());
        assertNotEquals("", paymentEntityId);
        checkEmptyOrderEntity();
    }

    //BUG
    @Test
    @DisplayName("should get error notification with DECLINED card and valid card data when pay by credit, " +
            "DB OrderEntity should be empty")
    void shouldGetErrorWithCreditValidDataDeclinedCard() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.putValidDataDeclinedCard(cardInfo);
        val creditRequestEntityId = getCreditRequestEntityId(DataHelper.declinedCardInfo().getStatus());
        assertNotEquals("", creditRequestEntityId);
        checkEmptyOrderEntity();
    }

    //BUG Статус карты в базе должен быть DECLINED, но в таблице OrderEntity не должны появляться данные
    @Test
    @DisplayName("DB OrderEntity should be empty, " +
            "DECLINED card status should be equals with status in data base with valid card data when pay by debit")
    void declinedCardStatusShouldBeEqualsWithDBInDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        val actual = DataHelper.declinedCardInfo().getStatus();
        cashPaymentPage.checkValidDataDeclinedCard(cardInfo);
        val expected = getDebitCardStatus();
        assertEquals(expected, actual);
        val paymentEntityId = getPaymentEntityId(actual);
        assertNotEquals("", paymentEntityId);
        checkEmptyOrderEntity();
    }

    //BUG Статус карты в базе должен быть DECLINED, но в таблице OrderEntity не должны появляться данные
    @Test
    @DisplayName("DB OrderEntity should be empty, " +
            "DECLINED card status should be equals with status in data base with valid card data when pay by credit")
    void declinedCardStatusShouldBeEqualsWithDBInCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        val actual = DataHelper.declinedCardInfo().getStatus();
        creditPayPage.checkValidDataDeclinedCard(cardInfo);
        val expected = getCreditCardStatus();
        assertEquals(expected, actual);
        val creditEntityId = getCreditRequestEntityId(actual);
        assertNotEquals("", creditEntityId);
        checkEmptyOrderEntity();
    }

    //SAD PATH
    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextIfYouEnterAnInsufficientNumberOfDigitsInTheCardNumberDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheWrongNumberOfDigitsInTheCardNumberField(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextIfYouEnterAnInsufficientNumberOfDigitsInTheCardNumberCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkTheWrongNumberOfDigitsInTheCardNumberField(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextWithAnEmptyFieldInTheCardNumberDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheSendingOfAnEmptyCardNumberField(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextWithAnEmptyFieldInTheCardNumberCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkTheSendingOfAnEmptyCardNumberField(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put text in card number field when pay by debit card")
    void shouldHaveErrorTextIfPutTextInCardNumberFieldInDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTextInCardNumberField(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put text in card number field when pay by credit card")
    void shouldHaveErrorTextIfPutTextInCardNumberFieldInCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkTextInCardNumberField(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    //BUG после сообщения об ошибочной операции появляется сообщение "Успешно! Операция одобрена банком"
    @Test
    @DisplayName("should get error notification if put unreal card number when pay by debit card")
    void shouldHaveErrorNotificationIfPutUnrealCardNumberInDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkUnrealCardNumber(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get error notification if put unreal card number when pay by credit card")
    void shouldHaveErrorNotificationIfPutUnrealCardNumberInCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkUnrealCardNumber(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get error notification if put unreal card number when pay by debit card")
    void shouldAnErrorAppearWhenEnteringZerosInTheCardNumberFieldDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheEntryOfZerosCardNumber(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get error notification if put unreal card number when pay by credit card")
    void shouldAnErrorAppearWhenEnteringZerosInTheCardNumberFieldCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkTheEntryOfZerosCardNumber(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfYouDoNotEnterEnoughDigitsInTheMonthFieldDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheInputOfAnInsufficientNumberOfDigitsInTheMonth(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfYouDoNotEnterEnoughDigitsInTheMonthFieldCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkTheInputOfAnInsufficientNumberOfDigitsInTheMonth(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put past month when pay by debit card")
    void shouldHaveErrorTextIfPutPastMonthInDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkPastMonth(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put past month when pay by credit")
    void shouldHaveErrorTextIfPutPastMonthInCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkPastMonth(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfTheMonthFieldIsEmptyDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkOfAnEmptyFieldMonth(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfTheMonthFieldIsEmptyCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkOfAnEmptyFieldMonth(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearWhenEnteringTheTextInTheMonthFieldDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheTextInTheMonthField(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearWhenEnteringTheTextInTheMonthFieldCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkTheTextInTheMonthField(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get error notification if put unreal card number when pay by debit card")
    void shouldAnErrorAppearWhenEnteringZerosInTheMonthFieldDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheEntryOfZerosMonth(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get error notification if put unreal card number when pay by credit card")
    void shouldAnErrorAppearWhenEnteringZerosInTheMonthFieldCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkTheEntryOfZerosMonth(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfYouDoNotEnterEnoughDigitsInTheYearFieldDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheInputOfAnInsufficientNumberOfDigitsInTheYear(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfYouDoNotEnterEnoughDigitsInTheYearFieldCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkTheInputOfAnInsufficientNumberOfDigitsInTheYear(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid year and month when pay by debit card")
    void shouldBeErrorTextWithInvalidYearAndMonthByDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkInvalidYear(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid year and month when pay by credit card")
    void shouldBeErrorTextWithInvalidYearAndMonthByCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkInvalidYear(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put future year when pay by debit card")
    void shouldHaveErrorTextIfPutFutureYearInDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkFutureYear(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put future year when pay by credit")
    void shouldHaveErrorTextIfPutFutureYearInCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkFutureYear(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearWhenEnteringTheTextInTheYearFieldDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheTextInTheYearField(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearWhenEnteringTheTextInTheYearFieldCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkTheTextInTheYearField(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get error notification if put unreal card number when pay by debit card")
    void shouldAnErrorAppearWhenEnteringZerosInTheYearFieldDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheEntryOfZerosYear(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get error notification if put unreal card number when pay by credit card")
    void shouldAnErrorAppearWhenEnteringZerosInTheYearFieldCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkTheEntryOfZerosYear(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfTheYearFieldIsEmptyDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkOfAnEmptyFieldYear(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfTheYearFieldIsEmptyCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkOfAnEmptyFieldYear(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put owner name in cyrillic when pay by debit card")
    void shouldHaveErrorTextIfPutOwnerNameByCyrillicInDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkRussianOwnerName(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put owner name in cyrillic when pay by credit card")
    void shouldHaveErrorTextIfPutOwnerNameByCyrillicInCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkRussianOwnerName(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeErrorTextWithInvalidCardDataByDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkingTheSendingOfAnEmptyFieldOwnerName(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by credit APPROVED card")
    void shouldBeErrorTextWithInvalidCardDataByCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkingTheSendingOfAnEmptyFieldOwnerName(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put symbols in owner field when pay by debit card")
    void shouldHaveErrorTextIfPutSymbolsInOwnerFieldInDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkSymbolsInOwnerField(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put symbols in owner field when pay by credit card")
    void shouldHaveErrorTextIfPutSymbolsInOwnerFieldInCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkSymbolsInOwnerField(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get text with error if put numbers in owner field when pay by debit card")
    void shouldHaveErrorTextIfPutNumbersInOwnerFieldInDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkNumbersInOwnerField(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get text with error if put numbers in owner field when pay by credit card")
    void shouldHaveErrorTextIfPutNumbersInOwnerFieldInCredit() throws SQLException {
        val creditPayPage = getCreditPayPage();
        creditPayPage.checkNumbersInOwnerField(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfYouDoNotEnterEnoughDigitsInTheFieldCVCDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheInputOfAnInsufficientNumberOfDigitsCVC(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldAnErrorAppearIfYouDoNotEnterEnoughDigitsInTheFieldCVCCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkTheInputOfAnInsufficientNumberOfDigitsCVC(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextWithAnEmptyFieldCVCDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkingTheSendingOfAnEmptyFieldCVC(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextWithAnEmptyFieldCVCCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkingTheSendingOfAnEmptyFieldCVC(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextWhenEnteringTextInTheFieldCVCDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkingTheInputOfAlphabeticCharactersInTheInputFieldCVC(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextWhenEnteringTextInTheFieldCVCCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkingTheInputOfAlphabeticCharactersInTheInputFieldCVC(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextWhenEnteringZerosInTheFieldCVCDebit() throws SQLException {
        val cashPaymentPage = getCashPaymentPage();
        cashPaymentPage.checkTheEntryOfZerosCVC(cardInfo);
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("should get red text with error notification if put invalid data in card fields when pay by debit APPROVED card")
    void shouldBeAnErrorTextWhenEnteringZerosInTheFieldCVCCredit() throws SQLException {
        val cashPaymentPage = getCreditPayPage();
        cashPaymentPage.checkTheEntryOfZerosCVC(cardInfo);
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }
}









