package modules;

import Utilities.Notify;
import Utilities.OwnerInfo;
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
		if (event.getAuthor().isBot()) {
			
			return;
		}
			
		Message message = event.getMessage();
		m = message.getChannel();
		String content = message.getRawContent();
		MessageChannel channel = event.getChannel();
		try {
		if (content.equals("!ping") && message.getMember().isOwner()) {
			channel.sendMessage("Pong :ping_pong: \n Time taken (in ms.): " + channel.getJDA().getPing()).queue();
		}
		if (content.equals("!kill") && message.getMember().isOwner()) {
			channel.sendMessage("Shutting down.").queue();
			Main.getJDA().shutdown();
			System.exit(0);
		}
		}
		catch(Exception e) {
			Notify.NotifyAdmin(e.getMessage(), Main.getJDA().getUserById(OwnerInfo.sId));
		}

	}

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {

		Member member = event.getMember();
		try {
		event.getGuild().getDefaultChannel()
				.sendMessage(member.getEffectiveName() + " Has left: " + event.getChannelJoined().getName()).queue();
		}
		catch(Exception e) {
			Notify.NotifyAdmin(e.getMessage(), Main.getJDA().getUserById(OwnerInfo.sId));
		}
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		Member member = event.getMember();
		try {
		event.getGuild().getDefaultChannel()
				.sendMessage(member.getEffectiveName() + " Has left: " + event.getChannelLeft().getName()).queue();
		}
		catch(Exception e) {
			Notify.NotifyAdmin(e.getMessage(), Main.getJDA().getUserById(OwnerInfo.sId));
		}
	}

}
