package modules;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
public class Settings extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) {
			return;
		}
		String content = event.getMessage().getRawContent();
		if(content.equals("!s")) {
			event.getChannel().sendMessage("```Current options: \n [1] Set Nickname \n [2] Set Role Color```").queue();
			FlowHelper();
		}
	}
	
	public void FlowHelper() {
		//TODO: Add cases here.
	}
}
