package settings;

import Utilities.Bot;
import Utilities.EmbedBuilderHelper;
import Utilities.MessageHandler;
import Utilities.Notify;
import modules.Main;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.RoleManager;

import java.awt.*;

public class RoleColor extends ListenerAdapter {

	static String id = "";
	private String color = "";
	private static boolean isActive = false;
	private static MessageHandler mh = Main.getMessageHandler();
	private static String MESSAGE = "Enter a hex code (#xxxxxx). Cannot be 0!";
	MessageReceivedEvent holder;
	private static EmbedBuilderHelper ebh;
	public RoleColor() {
	}

	public static void execute(String messageText, MessageReceivedEvent event) {
		if(isvalid(event)){
			AssignColor(event.getGuild().getMemberById(Bot.getBotID()),messageText);
		}
	}

	public static void AssignColor(Member m, String s) {
		Color newC = Color.decode(s);
		Role role;
		int size = 0;
		GuildController gc;
		if(s.equals("#000000")) {
			RoleColor.ebh.SendAsText("Ya goof, I said it shouldn't be zero.", true);
			return;
		}
		try {
			role = m.getRoles().get(0);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			gc = new GuildController(m.getGuild());
			gc.createRole().queue();
			size = m.getGuild().getRoles().size();
			role = m.getGuild().getRoles().get(size-1);
			gc.addSingleRoleToMember(m, role).queue();
			Notify.NotifyAdmin(e.toString() + "Size: " + size);
			return;
		}
		//If no other roles exist on the bot, nothing can be done. Create a new role and add it to the bot.
		if(m.getRoles().size() == 1) {
			gc = new GuildController(m.getGuild());
			gc.createRole().queue();
			size = m.getGuild().getRoles().size();
			role = m.getGuild().getRoles().get(size-2);
			gc.addSingleRoleToMember(m, role).queue();
			RoleManager manager = role.getManager();
			manager.setName("Bot Color").queue();
			manager.setColor(newC).queue();
		}
		else {
			role = m.getRoles().get(1);
			RoleManager manager = role.getManager();
			manager.setColor(newC).queue();
		}
		
	}

	public void Init(EmbedBuilderHelper ebh) {
		ebh.ClearText();
		ebh.addText(MESSAGE, "");
		ebh.send();
		isActive = true;
		RoleColor.ebh = ebh;
		id = Bot.getBotID();
	}

	public static boolean isActive() {
		return isActive;
	}
	public static void setIsActive(boolean isActive) {
		RoleColor.isActive = isActive;
	}
	public static boolean isvalid(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		String text = event.getMessage().getStrippedContent();
		return text.startsWith("#") && text.length() == 7;
	}

}
