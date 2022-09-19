package ru.netology.data;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Calendar;
import java.util.Locale;

@NoArgsConstructor
public class DataHelper {

    public static String approvedNumber = "4444 4444 4444 4441";
    public static String declinedNumber = "4444 4444 4444 4442";
    public static String notExistNumber = "4444 4444 4444 1234";
    public static String unrealCardNumber = "8888 8888 8888 8888";
    public static String fakerNumber = new Faker(new Locale("en-US")).finance().creditCard();
    public static Faker fakerEN = new Faker(new Locale("en-US"));
    public static Faker fakerRU = new Faker(new Locale("ru-RU"));

    @Value
    public static class CardData {
        String number, month, year, holder, cvc;
    }

    // Генерация данных по картам
    public static CardData getCardDataEn(String number) {
        String month = getRandomMonth();
        String year = getRandomYear();
        String holder = getRandomHolder();
        String cvc = getRandomCVC();
        return new CardData(number, month, year, holder, cvc);
    }

    private static String getRandomMonth() {
        String month = String.format("%2d", fakerEN.number().numberBetween(1, 12)).replace(" ", "0");
        return month;
    }

    private static String getRandomYear() {
        int numberYear = Calendar.getInstance().get(Calendar.YEAR);
        String year = Integer.toString(fakerEN.number().numberBetween(numberYear + 1, numberYear + 2)).substring(2);
        return year;
    }

    private static String getRandomHolder() {
        String holder = fakerEN.name().firstName() + " " + fakerEN.name().lastName();
        return holder;
    }

    private static String getRandomCVC() {
        String cvc = fakerEN.numerify("###");
        return cvc;
    }

    // Данные валидной карты
    public static CardData getApprovedNumber() {
        return getCardDataEn(approvedNumber);
    }

    // Данные невалидной карты
    public static CardData getDeclinedNumber() {
        return getCardDataEn(declinedNumber);
    }

    // Пустое поле "Номер карты"
    public static CardData getEmptyNumber() {
        return getCardDataEn("");
    }

    // Ввод в поле "Номер карты" карты с недостаточным количеством цифр (минимальное количество цифр в карте 13)
    public static CardData getNumberIfFewDigits() {
        return getCardDataEn("card" + approvedNumber.substring(3));
    }

    // Ввод буквенных символов в поле "Номер карты"
    public static CardData getLetters() {
        return getCardDataEn("ABCDI");
    }

    // Несуществующий номер карты
    public static CardData getNonExistentCardNumber() {
        return getCardDataEn(unrealCardNumber);
    }

    // Ввод нулей в поле "Номер карты"
    public static CardData getEnteringZeros() {
        return getCardDataEn("0000_0000_0000_0000");
    }

    // Несуществующий в БД номер карты
    public static CardData getNumberIfNotExistInBase() {
        return getCardDataEn(notExistNumber);
    }

    // Генерация разных типов карт
    public static CardData getNumberFaker() {
        return getCardDataEn(fakerNumber);
    }


    // Пустое поле Месяц
    public static CardData getEmptyMonth() {
        return new CardData(approvedNumber, "", getApprovedNumber().year, getApprovedNumber().holder, getApprovedNumber().cvc);
    }

    // Ввод в поле Месяц нулевых значений
    public static CardData getMonthWithZero() {
        return new CardData(approvedNumber, "00", getApprovedNumber().year, getApprovedNumber().holder, getApprovedNumber().cvc);
    }

    // Ввод в поле Месяц значения больше 12
    public static CardData getMonthMore12() {
        return new CardData(approvedNumber, Integer.toString(fakerEN.number().numberBetween(13, 99)), getApprovedNumber().year, getApprovedNumber().holder, getApprovedNumber().cvc);
    }

    // Ввод в поле Месяц одной цифры
    public static CardData getMonthWithOneDigit() {
        return new CardData(approvedNumber, Integer.toString(fakerEN.number().numberBetween(1, 9)), getApprovedNumber().year, getApprovedNumber().holder, getApprovedNumber().cvc);
    }

    // Ввод буквенных символов в поле Месяц
    public static CardData getLettersMonth() {
        return new CardData(approvedNumber, "AB", getApprovedNumber().year, getApprovedNumber().holder, getApprovedNumber().cvc);
    }


    // Пустое поле Год
    public static CardData getEmptyYear() {
        return new CardData(approvedNumber, getApprovedNumber().month, "", getApprovedNumber().holder, getApprovedNumber().cvc);
    }

    // Истек срок действия карты
    public static CardData getExpiredCard() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -0);
        int numberYear = calendar.get(Calendar.YEAR) % 1000;
        int numberMonth = calendar.get(Calendar.MONTH);
        return new CardData(approvedNumber, String.format("%2d", numberMonth).replace(" ", "0"), Integer.toString(numberYear), getApprovedNumber().holder,getApprovedNumber().cvc);
    }

    // Ввод в Поле Год нулевых значений
    public static CardData getYearWithZero() {
        return new CardData(approvedNumber, getApprovedNumber().month, "00", getApprovedNumber().holder, getApprovedNumber().cvc);
    }

    // Ввод в поле Год значения намного позднее текущего года
    public static CardData getInvalidYearIfInTheFarFuture() {
        int numberYear = Calendar.getInstance().get(Calendar.YEAR) % 1000;
        return new CardData(approvedNumber, getApprovedNumber().month, Integer.toString(fakerEN.number().numberBetween(numberYear + 6, 99)), getApprovedNumber().holder, getApprovedNumber().cvc);
    }

    // Ввод в поле Год 1 цифры
    public static CardData getYearWithOneDigit() {
        return new CardData(approvedNumber, getApprovedNumber().month, Integer.toString(fakerEN.number().numberBetween(1, 9)), getApprovedNumber().holder, getApprovedNumber().cvc);
    }

    // Ввод буквенных символов в поле Год
    public static CardData getYearLetters() {
        return new CardData(approvedNumber, getApprovedNumber().month, "AB", getApprovedNumber().holder, getApprovedNumber().cvc);
    }


    // Пустое поле Владелец
    public static CardData getEmptyHolder() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, "", getApprovedNumber().cvc);
    }

    // Ввод в поле Владелец только фамилии
    public static CardData getHolderWithoutName() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, fakerEN.name().lastName(), getApprovedNumber().cvc);
    }

    // Направление заявки с указанием в поле Владелец фамилии и имени на кириллице
    public static CardData getRussianHolder() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, fakerRU.name().firstName() + " " + fakerRU.name().lastName(), getApprovedNumber().cvc);
    }

    // Ввод в поле Владелец цифр
    public static CardData getDigitsInHolder() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, fakerEN.numerify("####_####_####_####"), getApprovedNumber().cvc);
    }

    // Ввод в поле Владелец спецсимволов
    public static CardData getSpecialCharactersInHolder() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-US"), new RandomService());
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, fakeValuesService.regexify("[\\-\\=\\+\\<\\>\\!\\@\\#\\$\\%\\^\\{\\}]{1,10}"), getApprovedNumber().cvc);
    }

    // Ввод в поле Владелец большого количества пробелов между фамилией и именем
    public static CardData getHolderWithManySpaces() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, fakerEN.name().firstName() + "        " + fakerEN.name().lastName(), getApprovedNumber().cvc);
    }

    // Направление заявки с указанием в поле Владелец большого количества букв
    public static CardData getHolderWithManyLetters() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, "AAAAAAAAAAABBBABBBBBBBBCCCCCCCCCCCCDDDDDDDDDDDDDDDDDDDDDDDDDDDD", getApprovedNumber().cvc);
    }

    // Ввод в поле Владелец фамилии через дефис
    public static CardData getHolderSurnameWithDash() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, fakerEN.name().firstName() + " " + fakerEN.name().lastName() + "-" + fakerEN.name().lastName(), getApprovedNumber().cvc);
    }

    // Ввод в поле Владелец имени через дефис
    public static CardData getHolderNameWithDash() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, fakerEN.name().firstName() + "-" + fakerEN.name().firstName() + " " + fakerEN.name().lastName(), getApprovedNumber().cvc);
    }

    // Пустое поле CVC-код
    public static CardData getEmptyCvcCode() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, getApprovedNumber().holder, "");
    }

    // Поле CVC-код с двумя цифрами
    public static CardData getCvcCodeWithTwoDigits() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, getApprovedNumber().holder, fakerEN.number().digits(2) + "w");
    }

    // Ввод буквенных символов в поле CVC-код
    public static CardData getCvcCodeLetters() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, getApprovedNumber().holder, "ABC");
    }

    // Поле CVC-код с нулевыми значениями
    public static CardData getCvcCodeWithZero() {
        return new CardData(approvedNumber, getApprovedNumber().month, getApprovedNumber().year, getApprovedNumber().holder, "000");
    }

    // Все поля формы пустые
    public static CardData getCardDataIfEmptyAllFields(){
        return new CardData("", "", "", "", "");
    }


}
