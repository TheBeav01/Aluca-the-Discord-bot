package settings;

import Utilities.Bot;
import Utilities.EmbedBuilderHelper;
import Utilities.Notify;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class Names extends ListenerAdapter{
	public static final String MESSAGE = "Enter nickname. Type 'Clear' to reset the nickname";
	public static final String RESET = "Clear";
	public static final String BOT_ID = Bot.getBotID();
	private static User compareTo;
	private static boolean isActive;
	private static GuildController gc;
	private MessageReceivedEvent ev;
	private static EmbedBuilderHelper ebh;

	public Names(EmbedBuilderHelper ebh) {
		Names.ebh = ebh;

	}
	public void Init(MessageReceivedEvent event, EmbedBuilderHelper ebh) {
		Names.ebh = ebh;
		compareTo = event.getAuthor();
		ebh.ClearText();
		ebh.addText(MESSAGE, "");
		ebh.send();
		isActive = true;
	}

	public static boolean isActive() {
		return isActive;
	}

	public static void setIsActive(boolean isActive) {
		Names.isActive = isActive;
	}

	public static void execute(String newName, MessageReceivedEvent ev) {
		if(ev.getAuthor().getIdLong() == compareTo.getIdLong()) {
			System.out.println("Executing names ");
			gc = new GuildController(ev.getGuild());
			Member m = ev.getGuild().getMemberById(BOT_ID);
			try {
				gc.setNickname(m, newName).queue();
				Names.ebh.SendAsText("Name changed successfully", true);
			}
			catch (Exception e) {
				Notify.NotifyAdmin(e.toString());
				Names.ebh.SendAsText("Name changed unsuccessfully. Error code: 2", true);

			}
		}
		else {
			System.out.println("New ID = " + ev.getAuthor().getIdLong() + "OLD ID = " + compareTo.getIdLong());
		}
	}
}
