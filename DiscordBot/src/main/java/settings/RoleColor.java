package settings;

import java.awt.Color;

import Utilities.Bot;
import Utilities.Notify;
import Utilities.OwnerInfo;
import modules.Main;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.RoleManager;

public class RoleColor extends ListenerAdapter implements Execute {

	static String id = "";
	private String color = "";
	private static boolean isActive = false;
	private static String MESSAGE = "Enter a hex code (#xxxxxx). Cannot be 0!";
	MessageReceivedEvent holder;

	public RoleColor() {

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			if (event.getMessage().getRawContent().equals(MESSAGE)) {
				isActive = true;
				Bot.setMember(event.getMember());
			}
			return;
		}
		if (!event.getAuthor().isBot() && isActive && event.getAuthor().getId().matches(Bot.getOwnerID())) {
			if (event.getMessage().getRawContent().length() == 7
					&& event.getMessage().getRawContent().startsWith("#")) {
				color = event.getMessage().getRawContent();
				AssignColor(Bot.getMember(), color);
				event.getChannel().sendMessage("Color set to: " + color).queue();
				isActive = false;
			} else {
				event.getChannel().sendMessage("Wrong format! Got " + event.getMessage().getRawContent()).queue();
			}
		}
	}

	private void AssignColor(Member m, String s) {
		Color newC = Color.decode(s);
		Role role = null;
		int size = 0;
		GuildController gc;
		if(s.equals("#000000")) {
			holder.getChannel().sendMessage("Ya goof, I said it shouldn't be zero.");
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
			gc.addSingleRoleToMember(m, role);
			Notify.NotifyAdmin(e.toString() + "Size: " + size, Main.getJDA().getUserById(OwnerInfo.sId));
			return;
		}
		catch (Exception e) {
			Notify.NotifyAdmin(e.toString(), Main.getJDA().getUserById(OwnerInfo.sId));
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

	@Override
	public String getSettingValue() {
		return color;
	}

	public static void Init(MessageReceivedEvent event) {
		event.getChannel().sendMessage(MESSAGE).queue();
		id = Bot.getBotID();
	}

	public static boolean isActive() {
		return isActive;
	}

	@Override
	public boolean isvalid(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
