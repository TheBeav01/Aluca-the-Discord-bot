package modules;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Utilities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
public class Settings extends ListenerAdapter {
	private boolean init = false;
	
	private String token = "MzY3ODkyMjU5MDI4NTk4Nzg0.DMCB5Q.umIX4ORVdVCXa7xsBoB4ookBd5w";
	MessageReceivedEvent lastSuccessful;
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		lastSuccessful = event;
		if(event.getAuthor().isBot()) {
			return;
		}
		String content = event.getMessage().getRawContent();
		if(content.equals("!s")) {
			lastSuccessful = event;
			event.getChannel().sendMessage(settingsInit(event)).queue();
		}
		
		else if(content.equals("1") && init) {
			event.getChannel().sendMessage("`Enter desired role color`").queue();
		}
		
		else if(content.equals("2") && init) {
			event.getChannel().sendMessage("`Enter desired nickname`").queue();
		}
		else if(content.equals("exit") && init) {
			init = false;
			event.getChannel().sendMessage("Exiting").queue();
		}
	}
	

	
	protected String getToken() {
		return token;
	}
	
	private String settingsInit(MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();
		Scanner sc = null;
		File f = new File("Resources/Settings List.tt");
		try {
			sc = new Scanner(f);
			while(sc.hasNext()) {
				sb.append(sc.nextLine());
				sb.append(System.lineSeparator());
			}
		} catch (FileNotFoundException e) {
			event.getJDA().getUserById(OwnerInfo.sId);
			Notify.NotifyAdmin(e.getMessage(), event.getJDA().getUserById(OwnerInfo.sId));
			
		}
//		sc.close();
		init = true;
		return sb.toString();
	}

}
