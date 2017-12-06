package Utilities;

import java.sql.*;

public class DBUtils {
    public static Connection DBCon;
    private static String statement;
    private static String driver;
    private static String URL = DBConstants.DB_URL;
    private static ResultSet r;
    public static int joined, left;
    public static boolean Connect() {
        driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver).newInstance();
            DBCon = DriverManager.getConnection(URL, DBConstants.DB_USER, DBConstants.DB_PASS);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            Notify.NotifyAdmin("getWhitelistID caught an exception: " + e.getMessage(), Bot.getAdmin());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param ID
     * @return true if the given ID exists. False otherwise
     */
    public static boolean getWhitelistID(String ID) {
        statement = "SELECT ChannelID FROM `aluca-the-dergbot`.`VC-whitelist` WHERE ChannelID = ?";
        try {
            PreparedStatement ps = DBCon.prepareStatement(statement);
            ps.setString(1, ID);
            r = ps.executeQuery();
            if (!r.first()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Notify.NotifyAdmin("getWhitelistID caught an exception: " + e.getMessage() + " " + e.getErrorCode(), Bot.getAdmin());
            return false;
        }
        return true;
    }

    public static boolean setWhitelist(String ID) {
        statement = "INSERT INTO `aluca-the-dergbot`.`VC-whitelist`(ChannelID) VALUES (?)";
        try {
            PreparedStatement ps = DBCon.prepareStatement(statement);
            ps.setString(1, ID);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Notify.NotifyAdmin("setWhitelist caught an exception: " + e.getMessage() + " " + e.getErrorCode(), Bot.getAdmin());
            return false;
        }
    }

    public static boolean setGuildUser(String guildName, String guildID, String userID, boolean hasJoined) {
        statement = "SELECT userID FROM `aluca-the-dergbot`.`guildlist` WHERE userID = ?";
        try {
            PreparedStatement ps = DBCon.prepareStatement(statement);
            ps.setString(1, userID);
            r = ps.executeQuery();
            if (!r.first()) {
                String insert = "INSERT INTO `aluca-the-dergbot`.`guildlist` " +
                        "(guildName, guildID, userID, timesJoined, timesLeft) VALUES (?,?,?,?,?);";
                ps = DBCon.prepareStatement(insert);
                ps.setString(1,guildName);
                ps.setString(2,guildID);
                ps.setString(3, userID);
                ps.setInt(4,0);
                ps.setInt(5,0);
                ps.execute();
                return true;
            }
            else {
                if(hasJoined) {
                   joined =  r.getInt(4);
                   joined++;
                   String update = "UPDATE `aluca-the-dergbot`.`guildlist` SET timesJoined = ? WHERE userID = ?";
                   ps = DBCon.prepareStatement(update);
                   ps.setInt(4,joined);
                }
                else {
                    left =  r.getInt(4);
                    left++;
                    String update = "UPDATE `aluca-the-dergbot`.`guildlist` SET timesLeft = ? WHERE userID = ?";
                    ps = DBCon.prepareStatement(update);
                    ps.setInt(5,left);
                }
            }
        } catch (SQLException e) {
            Notify.NotifyAdmin("An error occurred!" + e.toString(), Bot.getAdmin());
        }
        return false;
    }
}
