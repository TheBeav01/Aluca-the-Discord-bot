package settings;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Execute {
	Object getSettingValue();
	boolean isvalid(MessageReceivedEvent event);

}
