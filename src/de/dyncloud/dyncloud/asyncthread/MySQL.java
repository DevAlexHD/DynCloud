package de.dyncloud.dyncloud.asyncthread;

/**
 * Created by Alexander on 14.04.2017.
 */
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.*;
import java.sql.*;

public class MySQL {

    public static String username = "devalex";
    public static String password = "!pfYr051";
    public static String database = "devalex";
    public static String host = "h2169535.stratoserver.net";
    public static String port = "3306";
    public static Connection con;

    public static void connect() {
        if (!isConnected()) {
            try {
                MySQL.con = DriverManager.getConnection("jdbc:mysql://" + MySQL.host + ":" + MySQL.port + "/" + MySQL.database, MySQL.username, MySQL.password);
                ProxyServer.getInstance().getConsole().sendMessage((BaseComponent)new TextComponent("§aMySQL-Verbindung wurde hergestellt."));
            }
            catch (SQLException e) {
                e.printStackTrace();
                ProxyServer.getInstance().getConsole().sendMessage((BaseComponent)new TextComponent("§cError beim herstellen der Verbindung"));
            }
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                MySQL.con.close();
                ProxyServer.getInstance().getConsole().sendMessage((BaseComponent)new TextComponent("§Verbindung geschlossen"));
            }
            catch (SQLException e) {
                e.printStackTrace();
                ProxyServer.getInstance().getConsole().sendMessage((BaseComponent)new TextComponent("§cError beim schliessen der Verbindung"));
            }
        }
    }

    public static boolean isConnected() {
        return MySQL.con != null;
    }

    public static void update(final String qry) {
        if (isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate(qry);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createTable() {
        if (isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Bans (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100), Dauer VARCHAR(100), Banner VARCHAR(100) , BanID VARCHAR(100))");
                MySQL.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS History (Typ VARCHAR(100), UUID VARCHAR(100), Grund VARCHAR(100), Von VARCHAR(100), Dauer VARCHAR(100), Datum VARCHAR(100), Name VARCHAR(100))");
                MySQL.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Mutes (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100), Dauer VARCHAR(100), Muter VARCHAR(100))");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet query(final String qry) {
        if (isConnected()) {
            try {
                return MySQL.con.createStatement().executeQuery(qry);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Connection getConnection()
    {
        return con;
    }

    public PreparedStatement prepare(final String query) {
        try {
            return this.getConnection().prepareStatement(query);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
