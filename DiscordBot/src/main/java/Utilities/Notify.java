package Utilities;

import net.dv8tion.jda.core.entities.User;

public class Notify {
	public static void NotifyAdmin(String s, User Admin) {
		Admin.openPrivateChannel().queue((channel) -> {
			channel.sendMessage(s).queue();
		});
	}

}
