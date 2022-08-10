package ru.netology.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPayPage {

    private static SelenideElement headingCredit = $$(".heading").find(exactText("Кредит по данным карты"));
    private static SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private static SelenideElement monthInputField = $("[placeholder='08']");
    private static SelenideElement yearInputField = $("[placeholder='23']");
    private static SelenideElement cvcInputField = $("[placeholder='999']");
    private static SelenideElement ownerField = $("div:nth-child(3) > span > span:nth-child(1) > span > span > span.input__box > input");
    private static SelenideElement continueButton = $$("button").find(exactText("Продолжить"));
    private static SelenideElement successNotification = $(withText("Операция одобрена Банком."));
    private static SelenideElement errorNotification = $(withText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement crossButtonInErrorNotification = $$(".notification_theme_alfa-on-white > button").last();

    private ElementsCollection wrongFormatError = $$(byText("Неверный формат"));
    private SelenideElement mustBeFieldError = $(byText("Поле обязательно для заполнения"));
    private SelenideElement cardPeriodError = $(byText("Истёк срок действия карты"));
    private SelenideElement invalidCardPeriodError = $(byText("Неверно указан срок действия карты"));

    public CreditPayPage() {
        headingCredit.shouldBe(visible);
    }

    public void putCardData(String number, String month, String year, String owner, String code) {
        cardNumberField.setValue(number);
        monthInputField.setValue(month);
        yearInputField.setValue(year);
        ownerField.setValue(owner);
        cvcInputField.setValue(code);
        continueButton.click();
    }

    public void putValidDataApprovedCard(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getYear(),
                info.getOwner(), info.getCvc());
        successNotification.should(visible, Duration.ofSeconds(35));
    }

    public void putValidDataDeclinedCard(DataHelper.CardInfo info) {
        putCardData(DataHelper.declinedCardInfo().getCardNumber(), info.getMonth(), info.getYear(),
                info.getOwner(), info.getCvc());
        errorNotification.should(visible, Duration.ofSeconds(35));
    }

    public void checkValidDataDeclinedCard(DataHelper.CardInfo info) {
        putCardData(DataHelper.declinedCardInfo().getCardNumber(), info.getMonth(), info.getYear(),
                info.getOwner(), info.getCvc());
        successNotification.should(visible, Duration.ofSeconds(35));
    }

    public void checkTheWrongNumberOfDigitsInTheCardNumberField(DataHelper.CardInfo info) {
        putCardData("1233 5552 3352 122", info.getMonth(), info.getYear(), info.getOwner(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldBe(Condition.visible);
    }

    public void checkTheSendingOfAnEmptyCardNumberField(DataHelper.CardInfo info) {
        putCardData(" ", info.getMonth(), info.getYear(), info.getOwner(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldBe(Condition.visible);
    }

    public void checkTextInCardNumberField(DataHelper.CardInfo info) {
        putCardData(info.getOwner(), info.getMonth(), info.getYear(), info.getOwner(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(1));
    }

    public void checkUnrealCardNumber(DataHelper.CardInfo info) {
        putCardData(info.getUnrealCardNum(), info.getMonth(), info.getYear(), info.getOwner(), info.getCvc());
        errorNotification.should(visible, Duration.ofSeconds(35));
        crossButtonInErrorNotification.click();
        successNotification.shouldNotBe(Condition.visible);
    }

    public void checkTheEntryOfZerosCardNumber(DataHelper.CardInfo info) {
        putCardData("0000_0000_0000_0000", info.getMonth(), info.getYear(), info.getOwner(), info.getCvc());
        errorNotification.should(visible, Duration.ofSeconds(35));
        crossButtonInErrorNotification.click();
        successNotification.shouldNotBe(Condition.visible);
    }

    public void checkTheInputOfAnInsufficientNumberOfDigitsInTheMonth(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), "3", info.getYear(), info.getOwner(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldBe(Condition.visible);
    }

    public void checkPastMonth(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getPastMonth(), info.getTodayYear(), info.getOwner(), info.getCvc());
        invalidCardPeriodError.shouldBe(Condition.visible);
    }

    public void checkOfAnEmptyFieldMonth(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), " ", info.getYear(), info.getOwner(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldBe(Condition.visible);
    }

    public void checkTheTextInTheMonthField(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getOwner(), info.getYear(), info.getOwner(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(1));
    }

    public void checkTheEntryOfZerosMonth(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), "00", info.getYear(), info.getOwner(), info.getCvc());
        errorNotification.should(visible, Duration.ofSeconds(35));
        crossButtonInErrorNotification.click();
        successNotification.shouldNotBe(Condition.visible);
    }

    public void checkTheInputOfAnInsufficientNumberOfDigitsInTheYear(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), "9", info.getOwner(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldBe(Condition.visible);
    }

    public void checkInvalidYear(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getPastYear(), info.getOwner(), info.getCvc());
        cardPeriodError.shouldBe(Condition.visible);
        invalidCardPeriodError.shouldBe(Condition.visible);
    }

    public void checkFutureYear(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getFutureYear(), info.getOwner(), info.getCvc());
        invalidCardPeriodError.shouldBe(Condition.visible);
    }

    public void checkTheTextInTheYearField(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getOwner(), info.getOwner(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(1));
    }

    public void checkTheEntryOfZerosYear(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), "00", info.getOwner(), info.getCvc());
        errorNotification.should(visible, Duration.ofSeconds(35));
        crossButtonInErrorNotification.click();
        successNotification.shouldNotBe(Condition.visible);
    }

    public void checkOfAnEmptyFieldYear(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), " ", info.getOwner(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldBe(Condition.visible);
    }

    public void checkRussianOwnerName(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getYear(), info.getOwnerNameRus(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(1));
    }

    public void checkingTheSendingOfAnEmptyFieldOwnerName(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getYear(), " ", info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldBe(Condition.visible);
    }

    public void checkSymbolsInOwnerField(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getYear(),
                info.getSymbolOwnerName(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(1));
    }

    public void checkNumbersInOwnerField(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getYear(),
                info.getCvc(), info.getCvc());
        wrongFormatError.shouldHave(CollectionCondition.size(1));
    }

    public void checkTheInputOfAnInsufficientNumberOfDigitsCVC(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getYear(), info.getOwner(), "6");
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldBe(Condition.visible);
    }

    public void checkingTheSendingOfAnEmptyFieldCVC(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getYear(), info.getOwner(), " ");
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldBe(Condition.visible);
    }

    public void checkingTheInputOfAlphabeticCharactersInTheInputFieldCVC(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getYear(), info.getOwner(), info.getOwner());
        wrongFormatError.shouldHave(CollectionCondition.size(4));
        mustBeFieldError.shouldNotBe(Condition.visible);
    }

    public void checkTheEntryOfZerosCVC(DataHelper.CardInfo info) {
        putCardData(DataHelper.approvedCardInfo().getCardNumber(), info.getMonth(), info.getYear(), info.getOwner(), "000");
        errorNotification.should(visible, Duration.ofSeconds(35));
        crossButtonInErrorNotification.click();
        successNotification.shouldNotBe(Condition.visible);
    }
}