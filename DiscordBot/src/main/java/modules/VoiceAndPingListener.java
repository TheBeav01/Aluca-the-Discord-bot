package modules;

import Utilities.Bot;
import Utilities.DBUtils;
import Utilities.Notify;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

public class VoiceAndPingListener extends ListenerAdapter {


	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {

			return;
		}

		Message message = event.getMessage();
		String content = message.getRawContent();
		MessageChannel channel = event.getChannel();
		try {
			if (content.equals("!ping")) {
				long ping = channel.getJDA().getPing();
				channel.sendMessage(":ping_pong: \n Time taken: " + ping + "ms" ).queue();
			}
			if (content.equals("!kill") && message.getMember().isOwner()) {
				channel.sendMessage("Shutting down.").queue();
				Main.getJDA().shutdown();
				System.exit(0);
			}

			if (content.equals("Y") && message.getMember().isOwner() && Settings.getInitialized()) {
				if (!DBUtils.getWhitelistID(channel.getId())) {
					if(!DBUtils.setWhitelist(channel.getId())) {
						throw new IllegalArgumentException("Was not found");
					}
					channel.sendMessage("Added channel " + channel.getName() + "to the list of approved channels")
							.queue();
					return;
				} else {
					channel.sendMessage("Channel is already added!").queue();
					return;
				}
			}

			if (content.equals("N") && message.getMember().isOwner() && Settings.getInitialized()) {
				Settings.setInitialized(false);
				return;
			}
		} catch (Exception e) {
			Notify.NotifyAdmin(e.getMessage(), Bot.getAdmin());
		}

	}

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		MessageChannel m = FindChannel(event.getGuild());
		Member member = event.getMember();
		if (m == null) {
			Notify.NotifyAdmin("Channel was not defined for the server. Define it in the settings", Bot.getAdmin());
			return;
		}

		try {
			m.sendMessage(member.getEffectiveName() + " has joined " + event.getChannelJoined().getName()).queue();
		} catch (Exception e) {
			Notify.NotifyAdmin(e.getMessage(), Bot.getAdmin());
		}
	}

	/**
	 * Finds the allowed channel in the guild to post the message.
	 * 
	 * @param guild
	 * @return null if the channel doesn't exist.
	 */
	private MessageChannel FindChannel(Guild guild) {
		List<TextChannel> guildList = guild.getTextChannels();
		for(int i = 0; i < guildList.size(); i++) {
			if(DBUtils.getWhitelistID(guildList.get(i).getId())) {
				return guildList.get(i);
			}
		}
		return null;
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		MessageChannel m = FindChannel(event.getGuild());
		Member member = event.getMember();
		if (m == null) {
			Notify.NotifyAdmin("Channel was not defined for the server." + event.getGuild().toString()
					+ "Define it in the settings", Bot.getAdmin());
			return;
		}

		try {
			m.sendMessage(member.getEffectiveName() + " has left " + event.getChannelLeft().getName()).queue();
		} catch (Exception e) {
			Notify.NotifyAdmin(e.getMessage(), Bot.getAdmin());
		}
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Member m = event.getMember();
		String id = m.getUser().getId();

	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

	}

}
