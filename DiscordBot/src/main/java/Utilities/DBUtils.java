package Utilities;

import modules.Main;

import java.sql.*;
import java.util.logging.Level;

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
                System.out.println("Search returned false");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Notify.NotifyAdmin("getWhitelistID caught an exception: " + e.getMessage() + " " + e.getErrorCode(), Bot.getAdmin());
            return false;
        }
        return true;
    }

    /**
     *
     * @param ChID the channel ID
     * @param GID the guild ID
     * @return
     */
    public static int setWhitelist(String ChID, String GID) {
        statement = "SELECT GuildID FROM `aluca-the-dergbot`.`vc-whitelist` WHERE GuildID = ?";
        try {
            PreparedStatement ps = DBCon.prepareStatement(statement);
            ps.setString(1,GID);
            r = ps.executeQuery();
            if(!r.first()) {
                String insert = "INSERT INTO `aluca-the-dergbot`.`vc-whitelist`" + "(ChannelID, GuildID) VALUES(?,?)";
                ps = DBCon.prepareStatement(insert);
                ps.setString(1,ChID);
                ps.setString(2,GID);
                ps.execute();
                Main.logger.log("Insert preformed", Level.INFO,"DB");
                return 0;
            }
            else {
                String update = "UPDATE `aluca-the-dergbot`.`vc-whitelist` SET ChannelID = ? WHERE GuildID = ?";
                ps = DBCon.prepareStatement(update);
                ps.setString(1, ChID);
                ps.setString(2,GID);
                ps.execute();
                Main.logger.log("Update preformed", Level.INFO,"DB");
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Notify.NotifyAdmin("setWhitelist caught an exception: " + e.getMessage() + " " + e.getErrorCode(), Bot.getAdmin());
            return -1;
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
                Main.logger.log("Guild user set", Level.INFO,"DB");
                return true;
            }
            else {
                if(hasJoined) {
                   joined =  r.getInt(4);
                   joined++;
                   String update = "UPDATE `aluca-the-dergbot`.`guildlist` SET timesJoined = ? WHERE userID = ?";
                   ps = DBCon.prepareStatement(update);
                   ps.setInt(1,joined);
                   ps.setString(2,userID);
                   ps.execute();
                    Main.logger.log("Joined count updated", Level.INFO,"DB");
                }
                else {
                    left =  r.getInt(4);
                    left++;
                    String update = "UPDATE `aluca-the-dergbot`.`guildlist` SET timesLeft = ? WHERE userID = ?";
                    ps = DBCon.prepareStatement(update);
                    ps.setInt(1,left);
                    ps.setString(2,userID);
                    ps.execute();
                    Main.logger.log("Insert preformed", Level.INFO,"DB");
                }
            }
        } catch (SQLException e) {
            Notify.NotifyAdmin("An error occurred!" + e.toString(), Bot.getAdmin());
        }
        return false;
    }
}
