package ru.netology.sqlUtils;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;

public class SQLutils {

    public static void cleanDB() throws SQLException {
        SQLutils.getConnection();
        val cleanCreditRequest = "DELETE FROM credit_request_entity;";
        val cleanPayment = "DELETE FROM payment_entity;";
        val cleanOrder = "DELETE FROM order_entity;";
        val runner = new QueryRunner();
        try (val conn = getConnection()) {
            val cleanCreditRequestEntity = runner.execute(conn, cleanCreditRequest);
            val cleanPaymentEntity = runner.execute(conn, cleanPayment);
            val cleanOrderEntity = runner.execute(conn, cleanOrder);
        }
    }

    public static String getCreditCardStatus() throws SQLException {

        val selectStatus = "SELECT * FROM credit_request_entity";
        val runner = new QueryRunner();
        try (val conn = getConnection()) {
            val creditCardStatus = runner.query(conn, selectStatus, new BeanHandler<>(CreditRequestEntity.class));
            return creditCardStatus.getStatus();
        }
    }

    public static String getDebitCardStatus() throws SQLException {

        val selectStatus = "SELECT * FROM payment_entity";
        val runner = new QueryRunner();
        try (val conn = getConnection()) {
            val debitCardStatus = runner.query(conn, selectStatus, new BeanHandler<>(PaymentEntity.class));
            return debitCardStatus.getStatus();
        }
    }

    public static String getPaymentEntityId(String status) throws SQLException {

        val paymentEntity = "SELECT * FROM payment_entity WHERE status='" + status + "';";
        val runner = new QueryRunner();
        try (val conn = getConnection()) {
            val paymentBlock = runner.query(conn, paymentEntity, new BeanHandler<>(PaymentEntity.class));
            return paymentBlock.getTransaction_id();
        }
    }

    public static String getCreditRequestEntityId(String status) throws SQLException {
        val creditRequest = "SELECT * FROM credit_request_entity WHERE status='" + status + "';";
        val runner = new QueryRunner();
        try (val conn = getConnection()) {
            val creditRequestBlock = runner.query(conn, creditRequest, new BeanHandler<>(CreditRequestEntity.class));
            return creditRequestBlock.getBank_id();
        }
    }

    public static String getOrderEntityId(String paymentEntityId) throws SQLException {
        val orderEntity = "SELECT * FROM order_entity WHERE payment_id='" + paymentEntityId + "';";
        val runner = new QueryRunner();
        try (val conn = getConnection()) {
            val orderBlock = runner.query(conn, orderEntity, new BeanHandler<>(OrderEntity.class));
            return orderBlock.getId();
        }
    }

    public static void checkEmptyPaymentEntity() throws SQLException {
        val paymentRequest = "SELECT * FROM payment_entity;";
        val runner = new QueryRunner();
        try (val conn = getConnection()) {
            val paymentBlock = runner.query(conn, paymentRequest, new BeanHandler<>(PaymentEntity.class));
            assertNull(paymentBlock);
        }
    }

    public static void checkEmptyCreditEntity() throws SQLException {
        val creditRequest = "SELECT * FROM credit_request_entity;";
        val runner = new QueryRunner();
        try (val conn = getConnection()) {
            val creditRequestBlock = runner.query(conn, creditRequest, new BeanHandler<>(CreditRequestEntity.class));
            assertNull(creditRequestBlock);
        }
    }

    public static void checkEmptyOrderEntity() throws SQLException {
        val orderRequest = "SELECT * FROM order_entity;";
        val runner = new QueryRunner();
        try (val conn = getConnection()) {
            val orderBlock = runner.query(conn, orderRequest, new BeanHandler<>(OrderEntity.class));
            assertNull(orderBlock);
        }
    }

    private static Connection getConnection() {

        String url = System.getProperty("test.dburl");
        String username = System.getProperty("test.dblogin");
        String password = System.getProperty("test.dbpassword");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.print("Connection success!");}
        catch (SQLException e) {
            System.out.print("Connection failed");
        }
        return conn;
    }
}