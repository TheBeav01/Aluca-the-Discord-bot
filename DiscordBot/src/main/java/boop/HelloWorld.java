package boop;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class HelloWorld {

	static JDA api;


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		 api = new JDABuilder(AccountType.BOT)
					.setToken("MzY1NjcxMzUxNDE3MzA3MTM3.DLhu-g.zTnjuiI5p_u-QAk1RqVvqxGE2C8").buildAsync();
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RateLimitedException e) {
			e.printStackTrace();
		}
		
		AddListener.AddVL(api);
		
	}

}
