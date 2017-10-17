package settings;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Names extends ListenerAdapter implements Execute {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

	}

	public Names() {
		// TODO Auto-generated constructor stub
	}

	public static void Init() {

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
	
