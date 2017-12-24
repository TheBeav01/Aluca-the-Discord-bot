package Utilities;

import modules.Main;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
public class Bot {

	public static String nickName;
	
	private static String displayName, roleColor;
	private static final String DISCRIMINATOR = "0544";
	private static final String ID = "367892259028598784";
	private static final String OWNER_ID = "192705186324676608";
	private static String PREFIX = "!";
	private Game game;
	private JDA jda;
	private static Member member;
	private static User user;
	public Bot() {
		nickName = "Aluca, the Roboderg";
		displayName = "Aluca, the Roboderg";
		this.game = Game.of("with her cute mate");
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
	public static String getPREFIX() {
		return PREFIX;
	}
	public static void setMember(Member m) {
		Bot.member = m;
		System.out.println(m.getNickname());
//		Bot.user = m.getUser();
	}
	public void setGame(String s) {
		this.game = Game.of(s);
		jda.getPresence().setGame(this.game);
	}
	public static void setNickname(String name) {
		displayName = name;
	}
	
	public void setJDA(JDA jda) {
		this.jda = jda;
	}
	
	public void setRoleColor(String roleColor) {
		Bot.roleColor = roleColor;
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
	public static String getDisplayName() {
		return displayName;
	}
	public static User getUser() {
		return user;
	}
	public static void setUser(String ID) {
		User u = Main.getJDA().getUserById(ID);
		Bot.user = Main.getJDA().getUserById(ID);
		try {
			Thread.sleep(10);
			System.out.println("Boop");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
