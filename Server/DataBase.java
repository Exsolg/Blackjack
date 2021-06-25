package Server;

import Engine.Player;

import java.sql.*;

public class DataBase {
    private Connection connection;
    private Statement statement;
    private static int playersCount;

    public DataBase() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XEPDB1", "system", "12358");
        statement = connection.createStatement();

        ResultSet checkTable = statement.executeQuery("SELECT COUNT(*)\n" +
                      "FROM ALL_TABLES\n" +
                      "WHERE TABLE_NAME = 'LOGIN'");

        checkTable.next();
        if (checkTable.getString(1).equals("0")) {
            statement.executeUpdate("CREATE TABLE LOGIN (\n" +
                    "  ID NUMBER(10) PRIMARY KEY,\n" +
                    "  NAME VARCHAR2(40) NOT NULL UNIQUE)");
        }

        statement.executeUpdate("TRUNCATE TABLE LOGIN");
    }

    public boolean isPlayerExists(Player player) throws SQLException {
        String playerName = player.getName();

        ResultSet setOfLogins = statement.executeQuery("SELECT * FROM LOGIN");
        while (setOfLogins.next()){
            if (playerName.equals(setOfLogins.getString(2))){
                return true;
            }
        }
        return false;
    }

    public void addPlayer(Player player) throws SQLException {
        if(!isPlayerExists(player)) {
            playersCount++;

            String playerName = player.getName();
            System.out.println(playersCount);

            statement.executeUpdate("INSERT INTO LOGIN " +
                    "VALUES (" + (playersCount + 1) + ", '" + playerName + "')");
        }
    }

    public void deletePlayer(Player player) throws SQLException {
        if(isPlayerExists(player)) {
            String playerName = player.getName();
            statement.executeUpdate("DELETE FROM LOGIN WHERE NAME='" + playerName + "'");
        }
    }
}
