package Utilities;

//import com.mysql.jdbc.*;
import java.sql.*;

public class DBUtils {
	public static Connection DBCon;
	private static String statement;
	private static String driver;
	private static String URL = DBConstants.DB_URL;
	private static ResultSet r;
	public static boolean Connect() {
		driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver).newInstance();
			DBCon = DriverManager.getConnection(URL, DBConstants.DB_USER, DBConstants.DB_PASS);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			Notify.NotifyAdmin("getID caught an exception: " + e.getMessage(), Bot.getAdmin());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param ID
	 * @return true if the given ID exists. False otherwise
	 */
	public static boolean getID(String ID) {
		statement = "SELECT ChannelID FROM `aluca-the-dergbot`.`VC-whitelist` WHERE ChannelID = ?";
		try {
			PreparedStatement ps = DBCon.prepareStatement(statement);
			ps.setString(1, ID);
			r = ps.executeQuery();
			if(!r.first()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Notify.NotifyAdmin("getID caught an exception: " + e.getMessage() + " " + e.getErrorCode(), Bot.getAdmin());
			return false;
		}
		return true;
	}
	
	public static boolean setID(String ID) {
		statement = "INSERT INTO `aluca-the-dergbot`.`VC-whitelist`(ChannelID) VALUES (?)";
			try {
				PreparedStatement ps = DBCon.prepareStatement(statement);
				ps.setString(1, ID);
				ps.execute();
				return true;
			}catch(SQLException e) {
				e.printStackTrace();
				Notify.NotifyAdmin("setID caught an exception: " + e.getMessage() + " " + e.getErrorCode(), Bot.getAdmin());
				return false;
			}
	}
}
