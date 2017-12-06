package modules;

import Utilities.Bot;
import Utilities.DBUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import settings.Names;
import settings.RoleColor;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
	static Config conf;
	static JDA api;
	static Bot b;


	public static void main(String[] args) throws FileNotFoundException {
		conf = ConfigFactory.parseFile(new File("C:/Users/Alex Beaver/Documents/git/Personal-fun-stuff/DiscordBot/Resources/application.json"));
		System.out.println(conf.entrySet());
		b = new Bot();
		DBUtils.Connect();
		try {
		 api = new JDABuilder(AccountType.BOT)
					.setToken(conf.getString("token")).buildAsync();
		} catch (LoginException | IllegalArgumentException | RateLimitedException e) {
			e.printStackTrace();
		}
		b.setJDA(api);
		api.addEventListener(new VoiceAndPingListener(), new Settings(), new RoleColor(), new Names());
		api.getPresence().setGame(b.getGame());
	}
	
	public static JDA getJDA() {
		return api;
	}
	
	public static Bot getBot() {
		return b;
	}

}
