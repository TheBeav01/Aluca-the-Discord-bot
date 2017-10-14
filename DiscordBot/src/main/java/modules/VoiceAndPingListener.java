package modules;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.guild.voice.*;
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
		if (content.equals("!ping") && message.getMember().isOwner()) {
			channel.sendMessage("Pong :ping_pong: \n Time taken (in ms.): " + channel.getJDA().getPing()).queue(); // Important to call .queue() on the RestAction returned by
													// sendMessage(...)
		}
		if(content.equals("!kill") && message.getMember().isOwner()) {
			channel.sendMessage("Shutting down.").queue();
			System.exit(0);
		}
		
		
	}
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		
		Member member = event.getMember();
		if(m.getId().equals("365653964613615618")) {
			m.sendMessage(member.getEffectiveName() + " has joined: " + event.getChannelJoined().getName()).queue();
		}
		else {
			m.sendMessage("Originally intended to send to " + m.getName() + "And the ID is" + m.getId()).queue();
		}
	}
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		Member member = event.getMember();
		if(m.getId().equals("365653964613615618")) {
			m.sendMessage(member.getEffectiveName() + " Has left: " + event.getChannelLeft().getName()).queue();
		}
		else {
			m.sendMessage("Originally intended to send to " + m.getName() + "And the ID is" + m.getId()).queue();
		}
	}

}
