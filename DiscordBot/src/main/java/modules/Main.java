package modules;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Main {

	static JDA api;
	
	private static Settings s = new Settings();


	public static void main(String[] args) {
		try {
		 api = new JDABuilder(AccountType.BOT)
					.setToken(s.getToken()).buildAsync();
		} catch (LoginException | IllegalArgumentException | RateLimitedException e) {
			e.printStackTrace();
		}
		api.addEventListener(new VoiceAndPingListener(), new Settings());
	}
	
	public JDA getJDA() {
		return api;
	}

}
