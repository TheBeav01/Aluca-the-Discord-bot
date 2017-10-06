package boop;

import net.dv8tion.jda.core.JDA;

public class AddListener {

	public AddListener() {
		// TODO Auto-generated constructor stub
	}
	
	public static void AddVL(JDA api) {
		api.addEventListener(new VoiceAndPingListener());
	}

}
