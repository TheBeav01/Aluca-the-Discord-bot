package settings;

import Utilities.Bot;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.*;

public class Names extends ListenerAdapter implements Execute {
	public static final String MESSAGE = "Enter nickname. Type 'Clear' to reset the nickname";
	public static final String RESET = "Clear";
	private boolean isActive;
	private GuildController gc;
	private long messageID;
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() && event.getMessage().getRawContent().matches(MESSAGE)) {
			Bot.setMember(event.getMember());
			isActive = true;
		} else if (!event.getAuthor().isBot() && isActive && event.getAuthor().getId().matches(Bot.getOwnerID())) {
			messageID = event.getMessageIdLong();
			gc = new GuildController(event.getGuild());
			isActive = false;
			
			String name = event.getMessage().getRawContent();
			try {
				if (name.matches(RESET)) {
					gc.setNickname(Bot.getMember(), null).queue();
					event.getChannel().deleteMessageById(messageID).queue();
					event.getChannel().sendMessage("Name reset").queue();
					Bot.setNickname(Bot.nickName);
				} else {
					gc.setNickname(Bot.getMember(), name).queue();
					event.getChannel().deleteMessageById(messageID).queue();
					event.getChannel().sendMessage("Name set to " + name).queue();
					Bot.setNickname(name);
				}
			}
			catch(Exception e) {
				event.getChannel().sendMessage("Something went wrong.").queue();
				e.printStackTrace();
			}
		}
	}

	public static void Init(MessageReceivedEvent event) {
		event.getTextChannel().sendMessage(MESSAGE).queue();
	}

	@Override
	public Object getSettingValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isvalid(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
