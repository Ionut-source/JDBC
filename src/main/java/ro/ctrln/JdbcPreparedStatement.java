package ro.ctrln;

import java.sql.*;

public class JdbcPreparedStatement {

    public static void main(String[] args) {

        String dbUrl = "jdbc:sqlite:C:\\Users\\cumpa\\CtrlN-code\\AplicatieJava\\JDBC\\sql\\sqlite.db";
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            connection = DriverManager.getConnection(dbUrl);

            runSelect(connection);

            runStatement(connection, insertStatement());
            runSelect(connection);

            runPreparedStatement(connection, updateStatement(),1,1);
            runSelect(connection);

            runPreparedStatement(connection, deleteStatement(),1,5);
            runSelect(connection);

            runPreparedStatement(connection, initialStateStatement(),1,1);
            runSelect(connection);

            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void runPreparedStatement(Connection connection, String sqlStament, int parameterIndex, int value) throws SQLException {
        PreparedStatement statement = getStatement(connection, sqlStament);
        statement.setInt(parameterIndex, value);
        statement.execute();
        statement.close();

    }

    private static void runDeleteStatement(Connection connection, String sqlStament) throws SQLException {
        PreparedStatement statement = getStatement(connection, sqlStament);
        statement.setInt(1,5);
        statement.execute();
        statement.close();
    }

    private static void runUpdateStatement(Connection connection, String sqlStament) throws SQLException {
        PreparedStatement statement = getStatement(connection, sqlStament);
        statement.setInt(1,1);
        statement.execute();
        statement.close();

    }

    private static String initialStateStatement() {
        return "update person set last_name =  'Snow' where id = ?";
    }

    private static String deleteStatement() {
        return "delete from person where id = ?";
    }

    private static String updateStatement() {
        return "update person set last_name =  'Bastard ' where id = ?";
    }

    private static void runStatement(Connection connection, String sqlStament) throws SQLException {
        PreparedStatement statement = getStatement(connection, sqlStament);
        statement.execute();
        statement.close();
    }

    private static String insertStatement() {
        return "insert into person values(5, 'Gulliver', 'The Giant', 34, 'Tourist')";
    }

    private static void runSelect(Connection connection) throws SQLException {
        PreparedStatement statement = getStatement(connection, "select * from person");
        ResultSet resultSet = statement.executeQuery();
        parseResult(resultSet);
        statement.close();
    }

    private static PreparedStatement getStatement(Connection connection, String sqlQuery) throws SQLException {
        return connection.prepareStatement(sqlQuery);
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
