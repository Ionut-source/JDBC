package ro.ctrln;

import java.sql.*;

public class Jdbc {

    public static void main(String[] args) {

        String dbUrl = "Jdbc:sqlite:C:\\Users\\cumpa_000\\IdeaProjects\\CtrlN-code\\JDBC\\sql\\sqlite.db";
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            connection = DriverManager.getConnection(dbUrl);

            runSelect(connection);

            runStatement(connection, insertStatement());
            runSelect(connection);

            runStatement(connection, updateStatement());
            runSelect(connection);

            runStatement(connection, deleteStatement());
            runSelect(connection);

            runStatement(connection, initialStateStatement());
            runSelect(connection);

            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String initialStateStatement() {
        return "update person set last_name =  'Snow' where id = 1";
    }

    private static String deleteStatement() {
        return "delete from person where id = 5";
    }

    private static String updateStatement() {
        return "update person set last_name =  'Bastard ' where id = 1";
    }

    private static void runStatement(Connection connection, String sqlStatement) throws SQLException {
        Statement statement = getStatement(connection);
        statement.execute(sqlStatement);
        statement.close();
    }

    private static String insertStatement() {
        return "insert into person values(5, 'Gulliver', 'The Giant', 34, 'Tourist')";
    }

    private static void runSelect(Connection connection) throws SQLException {
        Statement statement = getStatement(connection);
        ResultSet resultSet = statement.executeQuery("select * from person");
        parseResult(resultSet);
        statement.close();
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
