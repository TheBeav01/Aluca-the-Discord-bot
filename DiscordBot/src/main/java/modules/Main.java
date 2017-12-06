package modules;

import Utilities.Bot;
import Utilities.DBUtils;
import Utilities.Logging;
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
import java.io.IOException;
import java.util.logging.Level;

public class Main {
	public static Config conf;
	static JDA api;
	static Bot b;
	static Logging logger;

	public static void main(String[] args) throws FileNotFoundException {
		initMain();
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

	private static void initMain() {
		logger = new Logging();
		String filePath = "Resources/application.json";
		conf = ConfigFactory.parseFile(new File(filePath));
		System.out.println(conf.entrySet());
		b = new Bot();

		DBUtils.Connect();
		try {
			logger.CreateLog(logger.buildFileName());
			logger.log("Log file created!", Level.INFO, "Init");
			logger.log("Config file loaded from: " + filePath, Level.INFO, "Init");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
