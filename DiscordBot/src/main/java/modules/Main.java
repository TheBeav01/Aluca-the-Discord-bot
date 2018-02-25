package modules;

import Utilities.Bot;
import Utilities.DBUtils;
import Utilities.Logging;
import Utilities.MessageHandler;
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

/**
 * Main program that runs when the bot does. This hands off control to the listeners and handlers that actually do the
 * responses.
 */
public class Main {
	public static Config conf;
	static JDA api;
	static Bot b;
	public static Logging logger;
	static MessageHandler mh;
	static PassiveListeners VAPLR;
	public static void main(String[] args) throws FileNotFoundException {
		initMain();
		try {
		 api = new JDABuilder(AccountType.BOT)
					.setToken(conf.getString("token")).buildAsync();
		} catch (LoginException | IllegalArgumentException | RateLimitedException e) {
			e.printStackTrace();
		}
		b.setJDA(api);
		mh = new MessageHandler();
		api.addEventListener(mh, new RoleColor(), new Names()); //
		api.getPresence().setGame(b.getGame());
	}

	/**
	 * Gets the currently running JDA
	 * @return the running JDA API.
	 */
	public static JDA getJDA() {
		return api;
	}

	/**
	 *
	 * @return This bot entity
	 */
	public static Bot getBot() {
		return b;
	}

	/**
	 * A getter for the passive listener
	 * @return The passive listener
	 */
	public static PassiveListeners getVAPLR() {
		return VAPLR;
	}

	/**
	 *
	 * @return A message handler the bot uses.
	 */
	public static MessageHandler getMessageHandler() {
		return mh;
	}

	/**
	 * This cleans up main so the size wouldn't be too massive. This initializes the logging, and the config file.
	 */
	private static void initMain() {
		logger = new Logging();
		String filePath = "Resources/application.json";
		conf = ConfigFactory.parseFile(new File(filePath));
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
