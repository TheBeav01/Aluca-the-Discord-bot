package modules;

import Utilities.Bot;
import Utilities.DBUtils;
import Utilities.Notify;
import Utilities.StringUtils;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;
import java.util.logging.Level;

public class VoiceAndPingListener extends ListenerAdapter {


	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}

		Message message = event.getMessage();
		String content = message.getRawContent();
		MessageChannel channel = event.getChannel();
		if(Main.conf.getInt("log-messages") == 1) {
			Main.logger.log("From :" + event.getAuthor() + " in: " + event.getGuild().getName() +
							"(Channel: " + event.getChannel().getName() + ")" +
					" - " + content,Level.INFO,"Message");
		}
		try {
			if (content.startsWith("!ping")) {
				long ping = channel.getJDA().getPing();
				channel.sendMessage(":ping_pong: \n Time taken: " + ping + "ms").queue();
			}
			if (content.startsWith("!kill") && message.getMember().isOwner()) {
				channel.sendMessage("Shutting down.").queue();
				Main.logger.log("END OF LOG FILE \n ------------------------------------------------",Level.INFO,"Shutdown");
				Main.logger.close();
				Main.getJDA().shutdown();
				System.exit(0);
			}

			if (content.equals("Y") && message.getMember().isOwner() && Settings.getInitialized()) {
				if (!DBUtils.getWhitelistID(channel.getId())) {
					if (!DBUtils.setWhitelist(channel.getId())) {
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

			if (content.startsWith("!del")) {

				int x = StringUtils.SplitNumber(content);
				int i = 0;
				for (Message messages : event.getChannel().getIterableHistory()) {
					if (i > x) {
						break;
					} else {
						Main.logger.log("From: " + messages.getAuthor().getName() + ": " + messages.getRawContent(),
								Level.INFO, "delete");
						messages.delete().queue();
						i++;
					}
				}

			}
		}
		catch (Exception e) {
			Notify.NotifyAdmin(e.toString(), Bot.getAdmin());
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
		boolean joined = DBUtils.setGuildUser(event.getGuild().getName(), event.getGuild().getId(), id, true);
		StringBuilder sb = new StringBuilder();
		if(joined) {
			sb.append("User: " + m.getEffectiveName() + "Has joined \n" + event.getGuild().getName() +
					"User has joined: " + DBUtils.joined + "times");

		}
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		Member m = event.getMember();
		String id = m.getUser().getId();

	}

}
