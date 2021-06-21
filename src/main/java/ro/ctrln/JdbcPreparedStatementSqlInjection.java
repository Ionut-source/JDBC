package ro.ctrln;

import ro.ctrln.exceptions.SqlInjectionException;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class JdbcPreparedStatementSqlInjection {

    public static void main(String[] args) {

        String dbUrl = "Jdbc:sqlite:C:\\Users\\cumpa_000\\IdeaProjects\\CtrlN-code\\JDBC\\sql\\sqlite.db";
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            connection = DriverManager.getConnection(dbUrl);

            runSelect(connection);

            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String insertStatement() {
        return "insert into person values(5, 'Gulliver', 'The Giant', 34, 'Tourist')";
    }

    private static void runSelect(Connection connection) throws SQLException, SqlInjectionException {
        PreparedStatement statement = getStatement(connection, selectStatement());
        statement.setString(1,"1 or 1 = 1");
        ResultSet resultSet = statement.executeQuery();
        parseResult(resultSet);
        statement.close();
    }

    private static void runSelectSqlInjection(Connection connection) throws SQLException, SqlInjectionException {
        PreparedStatement statement = getStatement(connection, selectStatement("1 or 1=1"));
        ResultSet resultSet = statement.executeQuery();
        parseResult(resultSet);
        statement.close();
    }

    // Aceasta metoda permite SQL Injection
    private static String selectStatement(String id) throws SqlInjectionException {
        if (sqlInjection(id)) {
            throw new SqlInjectionException("Cineva incearca afisarea de date fara permisiune!");
        }
        return "select * from person where id = " + id;
    }

    private static String selectStatement() throws SqlInjectionException {
        return "select * from person where id = ?";
    }

    private static boolean sqlInjection(String id) {
        List<String> sqlKeywords = Arrays.asList("=", "INSERT", "UPDATE", "SELECT", "DELETE", "--");
        for (String sqlKeyword : sqlKeywords) {
            return id.contains(sqlKeyword.toLowerCase()) || id.contains(sqlKeyword);
        }
        return false;
    }

    private static PreparedStatement getStatement(Connection connection, String sqlStatement) throws SQLException {
        return connection.prepareStatement(sqlStatement);
    }

    private static void parseResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int numberOfColums = resultSetMetaData.getColumnCount();

        for (int i = 1; i <= numberOfColums; i++) {
            System.out.print(resultSetMetaData.getColumnName(i) + "\t\t");
        }
        System.out.println("\n");

        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            int age = resultSet.getInt(4);
            String job = resultSet.getString(5);
            System.out.print(id + "\t\t" + firstName + "\t\t\t\t" + lastName + "\t\t" + age + "\t\t" + job + "\n");
        }
        System.out.println("\n");

        resultSet.close();
    }
}
