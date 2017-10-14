package Utilities;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

public class OwnerInfo {
	public static String sId = "192705186324676608";
	static User Admin;

	public String getIDString() {
		return sId;
	}
	
	public String getUsername() {
		return Admin.getJDA().getUserById(sId).getName(); 
	}
	
	public String getDiscrim() {
		return Admin.getJDA().getUserById(sId).getDiscriminator();
	}

}
