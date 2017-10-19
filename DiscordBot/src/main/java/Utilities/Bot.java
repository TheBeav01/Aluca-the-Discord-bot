package Utilities;

import modules.Main;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
public class Bot {

	private String nickName, roleColor;
	private static final String DISCRIMINATOR = "0544";
	private static final String ID = "367892259028598784";
	private static final String OWNER_ID = "192705186324676608";

	private Game game;
	private JDA jda;
	private static Member member;
	public Bot() {
		this.nickName = ("Aluca, the Roboderg");
		this.game = Game.of("with her master");
		this.jda = null;
		Bot.member = null;
	}
	
	public String getDiscriminator() {
		return DISCRIMINATOR;
	}
	
	public String getNickname() {
		return nickName;
	}
	
	public Game getGame() {
		return game;
	}
	public String getRoleColor() {
		return roleColor;
	}
	
	public static void setMember(Member m) {
		Bot.member = m;
	}
	public void setNickname(String name) {
		this.nickName = name;
	}
	
	public void setJDA(JDA jda) {
		this.jda = jda;
	}
	
	public void setRoleColor(String roleColor) {
		this.roleColor = roleColor;
	}

	public JDA getJDA() {
		return jda;
	}
	
	public static String getBotID() {
		return ID;
	}
	
	public static Member getMember() {
		return member;
	}
	public static String getOwnerID() {
		return OWNER_ID;
	}
	public static User getAdmin() {
		return Main.getJDA().getUserById(Bot.getOwnerID());
	}
	
}
