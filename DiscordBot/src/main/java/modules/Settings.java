package modules;
import Utilities.Bot;
import Utilities.Notify;
import Utilities.OwnerInfo;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import settings.Names;
import settings.RoleColor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Settings extends ListenerAdapter {
	private static boolean init = false;	
	MessageReceivedEvent lastSuccessful;
	static String id;
	private String settingsInit(MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();
		Scanner sc = null;
		File f = new File(Main.conf.getString("settings-Path"));
		try {
			sc = new Scanner(f);
			while(sc.hasNext()) {
				sb.append(sc.nextLine());
				sb.append(System.lineSeparator());
			}
		} catch (FileNotFoundException e) {
			Notify.NotifyAdmin(e.getMessage() + f.getAbsolutePath(), Main.getJDA().getUserById(OwnerInfo.sId));
			sb.append("Whoops. Something went wrong! Contact " + Main.getJDA().getUserById(OwnerInfo.sId).getAsMention());
			
		}
//		sc.close();
		init = true;
		return sb.toString();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		lastSuccessful = event;
		if(!RoleColor.isActive()) {
			if(event.getAuthor().isBot()) {
				Bot.setMember(event.getMember());
				return;
			}
			String content = event.getMessage().getRawContent();
			if(content.equals("!s")) {
				lastSuccessful = event;
				event.getChannel().sendMessage(settingsInit(event)).queue();
			}
			
			else if(content.equals("1") && init) {
				Names.Init(event);
			}
			
			else if(content.equals("2") && init) {
				event.getChannel().sendMessage("`Enter desired role color`").queue();
				RoleColor.Init(event);
			}
			else if(content.equals("3") && init) {
				event.getChannel().sendMessage("`Enable VC join/leave in this channel? Y/N`").queue();
			}
			else if(content.equals("exit") && init) {
				init = false;
				event.getChannel().sendMessage("Exiting").queue();
			}
		}
	}
	
	public static boolean getInitialized() {
		return init;
	}
	public static void setInitialized(boolean setInit) {
		init = setInit;
	}

}
