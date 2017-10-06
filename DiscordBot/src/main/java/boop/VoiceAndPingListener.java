package boop;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class VoiceAndPingListener extends ListenerAdapter {

	private MessageChannel m;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;
		Message message = event.getMessage();
		m = message.getChannel();
		String content = message.getRawContent();
		MessageChannel channel = event.getChannel();
		// getRawContent() is an atomic getter
		// getContent() is a lazy getter which modifies the content for e.g. console
		// view (strip discord formatting)
		if (content.equals("!ping") && message.getMember().isOwner()) {
			channel.sendMessage("Pong! Also sample text!").queue(); // Important to call .queue() on the RestAction returned by
													// sendMessage(...)
		}
		if(content.equals("!kill") && message.getMember().isOwner()) {
			channel.sendMessage("Aborting the bot...").queue();
			System.exit(0);
		}
	}
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		
		Member member = event.getMember();
		if(m.getId().equals("365653964613615618")) {
			m.sendMessage(member.getEffectiveName() + " Has joined: " + event.getChannelJoined().getName()).queue();
		}
		else {
			m.sendMessage("Originally intended to send to " + m.getName() + "And the ID is" + m.getId()).queue();
		}
	}

}
