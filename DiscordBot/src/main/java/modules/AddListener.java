package modules;

import net.dv8tion.jda.core.JDA;

public class AddListener {

	public AddListener() {
		
	}
	public AddListener(JDA api, Object o) {
		api.addEventListener(o);
	}
	
	public static void AddVL(JDA api) {
		api.addEventListener(new VoiceAndPingListener());
	}
	
	public static void AddSL(JDA api) {
		api.addEventListener(new Settings());
	}

}
