package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class CardNumber {
        private String cardNumber;
        private String status;
    }

    public static CardNumber approvedCardInfo() {

        return new CardNumber("4444 4444 4444 4441", "APPROVED");
    }

    public static CardNumber declinedCardInfo() {

        return new CardNumber("4444 4444 4444 4442", "DECLINED");
    }

    @Value
    public static class CardInfo {
        private String month;
        private String year;
        private String cvc;
        private String owner;
        private String pastMonth;
        private String pastYear;
        private String futureYear;
        private String ownerNameRus;
        private String todayYear;
        final String unrealCardNum = "8888 8888 8888 8888";
        final String symbolOwnerName = " !@$&^ ";
        final String cvcCode = "000";
    }

    public static CardInfo getCardInfo() {
        LocalDate today = LocalDate.now();
        String month = String.format("%tm", today.plusMonths(2));
        String year = getRandomYear();
        String todayYear = String.valueOf(today.getYear());
        String cvc = getRandomCVC();
        String owner = transliterate(generateOwnerName());
        String pastMonth = String.format("%tm", today.minusMonths(1));
        String pastYear = getRandomPastYear();
        String futureYear = getRandomFutureYear();
        String ownerNameRus = generateOwnerName();

        return new CardInfo(month, year, cvc, owner, pastMonth, pastYear, futureYear, ownerNameRus, todayYear);
    }

    public static String generateOwnerName() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String transliterate(String ownerName) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ownerName.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (ownerName.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }


    private static String getRandomCVC() {
        int a = 0;
        int b = 10;

        int num1 = a + (int) (Math.random() * b);
        int num2 = a + (int) (Math.random() * b);
        int num3 = a + (int) (Math.random() * b);
        String cvc = Integer.toString(num1) + num2 + num3;
        return cvc;
    }

    private static String getRandomYear() {
        String[] years = {"22", "23", "24" , "25"};
        Random random = new Random();
        int index = random.nextInt(years.length);
        return (years[index]);
    }

    private static String getRandomPastYear() {
        String[] years = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"};
        Random random = new Random();
        int index = random.nextInt(years.length);
        return (years[index]);
    }

    private static String getRandomFutureYear() {
        String[] years = {"27", "28", "29", "30", "31", "32", "33", "34", "35"};
        Random random = new Random();
        int index = random.nextInt(years.length);
        return (years[index]);
    }
}
