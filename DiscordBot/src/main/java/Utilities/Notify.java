package Utilities;

import net.dv8tion.jda.core.entities.User;

public class Notify {
	public static void NotifyAdmin(String s) {
		User admin = Bot.getAdmin();
		admin.openPrivateChannel().queue((channel) -> {
			channel.sendMessage(s).queue();
		});
	}

}
