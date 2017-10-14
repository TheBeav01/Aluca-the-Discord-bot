package Utilities;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Notify {
	public static void NotifyAdmin(String s, User Admin) {
		Admin.openPrivateChannel().queue((channel) -> {
			channel.sendMessage(s).queue();
		});
	}

}
