package online.thinhtran.demo.utils;

import online.thinhtran.demo.models.Account;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;
import java.util.List;

public class DbConnect {
    private static DbConnect instance;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test1";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Thinh@123";
    private Connection connection;
    private QueryRunner queryRunner;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private DbConnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            queryRunner = new QueryRunner();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DbConnect getInstance() {
        if (instance == null) {
            instance = new DbConnect();
        }

        return instance;
    }

    public <T> List<T> executeReturnQueryOriginal(String sql, Class<T> type, Object... parameters) {
        ColumnListHandler<T> handler = new ColumnListHandler<>();
        try {
            return queryRunner.query(connection, sql, handler, parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // select * from assignments where id = 1;
    public <T> List<T> executeReturnQuery(String sql, Class<T> type, Object... parameters) {
        BeanListHandler<T> handler = new BeanListHandler<>(type);
        try {
            return queryRunner.query(connection, sql, handler, parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T executeScalarQuery(String sql, Class<T> type, Object... parameters) {
        ScalarHandler<T> handler = new ScalarHandler<>();
        try {
            return queryRunner.query(connection, sql, handler, parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int executeVoidQuery(String sql, Object... parameters) {
        try {
            return queryRunner.update(connection, sql, parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        DbConnect dbConnect = DbConnect.getInstance();
        List<Account> count = dbConnect.executeReturnQuery("select * from accounts", Account.class);
        System.out.println(count);
    }
}
