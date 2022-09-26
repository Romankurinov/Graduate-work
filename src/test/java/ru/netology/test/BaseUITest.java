package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.netology.data.SQLHelper;
import ru.netology.pages.PaymentChoosePage;

import static com.codeborne.selenide.Selenide.open;

public class BaseUITest {
    protected PaymentChoosePage paymentChoosePage;

    @BeforeEach
    void setUpSutUrl() {
        paymentChoosePage = open(System.getProperty("sut.url"), PaymentChoosePage.class);
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");}

    @AfterEach
    void cleanDataBases() {
        SQLHelper.dropDataBase();
    }


}
