package ro.ctrln;

import ro.ctrln.exceptions.SqlInjectionException;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class JdbcSqlInjection {

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
        Statement statement = getStatement(connection);
        ResultSet resultSet = statement.executeQuery(selectStatement("1 or 1=1"));
//        ResultSet resultSet = statement.executeQuery(selectStatement("3"));
        parseResult(resultSet);
        statement.close();
    }

    private static String selectStatement(String id) throws SqlInjectionException {
        if (sqlInjection(id)) {
            throw new SqlInjectionException("Cineva incearca afisarea de date fara permisiune!");
        }
        return "select * from person where id = " + id;
    }

    private static boolean sqlInjection(String id) {
        List<String> sqlKeywords = Arrays.asList("=", "INSERT", "UPDATE", "SELECT", "DELETE", "--");
        for (String sqlKeyword : sqlKeywords) {
            return id.contains(sqlKeyword.toLowerCase()) || id.contains(sqlKeyword);
        }
        return false;
    }

    private static Statement getStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    private static void parseResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int numberOfColums = resultSetMetaData.getColumnCount();

        for (int i = 1; i <= numberOfColums; i++) {
            System.out.print(resultSetMetaData.getColumnName(i) + "\t\t");
        }
        System.out.println("\n");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            int age = resultSet.getInt("age");
            String job = resultSet.getString("job");
            System.out.print(id + "\t\t" + firstName + "\t\t\t\t" + lastName + "\t\t" + age + "\t\t" + job + "\n");
        }
        System.out.println("\n");

        resultSet.close();
    }
}
