package com.studentmanagement.utils;

import java.sql.*;

public class DbConnect {
    private static DbConnect instance;

    private final static String jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=student_management;encrypt=true;trustServerCertificate=true;";
    private final static String username = "sa";
    private final static String password = "Thinh@123";

    private Connection connection;

    private DbConnect(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public static DbConnect getInstance(){
        if (instance == null){
            instance = new DbConnect();
        }
        return instance;
    }

    public ResultSet executeQuery(String sql, Object... parameters) {
        try {
            PreparedStatement preparedStatement = createPreparedStatement(sql, parameters);
            return preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public int executeUpdate(String sql, Object... parameters){
        try{
            PreparedStatement preparedStatement = createPreparedStatement(sql, parameters);
            return preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    private PreparedStatement createPreparedStatement(String sql, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }

        return preparedStatement;
    }
}
