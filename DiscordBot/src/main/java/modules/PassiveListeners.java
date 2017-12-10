package modules;

import Utilities.Bot;
import Utilities.MessageHandler;
import Utilities.Notify;
import Utilities.StringUtils;
import net.dv8tion.jda.core.entities.*;

import java.util.logging.Level;

public class PassiveListeners extends MessageHandler {
	private String test;
	private User u;
	private String defaultContent;
	public PassiveListeners() {
		super();
		test = "foo";
	}
	public void executeCommands(String message, User user, Member member, Guild guild, MessageChannel channel) {
		setFields(message, user, member, guild, channel);

		try {
			if (message.contains(PREFIX+"ping")) {
				long ping = channel.getJDA().getPing();
				sendMessage(":ping_pong: \n Time taken: " + ping + "ms");
			}
			if (message.contains(PREFIX+"kill") && member.isOwner()) {
				sendMessage("Shutting down.");
				Main.logger.log("END OF LOG FILE \n ------------------------------------------------", Level.INFO,"Shutdown");
				Main.logger.close();
				Main.getJDA().shutdown();
				System.exit(0);
			}
			if (message.contains(PREFIX+"del")) {

				int x = StringUtils.SplitNumber(message);
				int i = 0;
				for (Message messages : channel.getIterableHistory()) {
					if (i > x) {
						break;
					} else {
						Main.logger.log("From: " + messages.getAuthor().getName() + ": " + messages.getRawContent(),
								Level.INFO, "delete");
						messages.delete().queue();
						i++;
					}
				}
//			}
//			if(content.contains(event.getGuild().getMember(Main.getJDA().getUserById(Bot.getBotID())).getAsMention())) {
//				channel.sendMessage(":ping_pong:").queue();
//			}

//			if (content.equals("Y") && message.getMember().isOwner() && Settings.getInitialized()) {
//				if (!DBUtils.getWhitelistID(channel.getId())) {
//					if (DBUtils.setWhitelist(channel.getId(), event.getGuild().getId()) == -1) {
//						throw new IllegalArgumentException("Was not found");
//					} else if (DBUtils.setWhitelist(channel.getId(), event.getGuild().getId()) == 0) {
//						channel.sendMessage("Added channel " + channel.getName() + "to the list of approved channels")
//								.queue();
//						return;
//					} else {
//						channel.sendMessage("Channel has been adjusted.").queue();
//						return;
//					}
//				}
//			}
//
//			if (content.equals("N") && message.getMember().isOwner() && Settings.getInitialized()) {
//				Settings.setInitialized(false);
//				return;
			}
		}
		catch (Exception e) {
			Notify.NotifyAdmin(e.toString(), Bot.getAdmin());
		}
	}


//	@Override
//	public void onMessageReceived(MessageReceivedEvent event) {
//		if (event.getAuthor().isBot()) {
//			return;
//		}
//		Message message = event.getMessage();
//		String content = message.getRawContent();
//		MessageChannel channel = event.getChannel();
//
//	}
//
//	@Override
//	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
//		MessageChannel m = FindChannel(event.getGuild());
//		Member member = event.getMember();
//		if (m == null) {
//			Notify.NotifyAdmin("Channel was not defined for the server. Define it in the settings", Bot.getAdmin());
//			return;
//		}
//
//		try {
//			m.sendMessage(member.getEffectiveName() + " has joined " + event.getChannelJoined().getName()).queue();
//		} catch (Exception e) {
//			Notify.NotifyAdmin(e.getMessage(), Bot.getAdmin());
//		}
//	}
//
//	/**
//	 * Finds the allowed channel in the guild to post the message.
//	 *
//	 * @param guild
//	 * @return null if the channel doesn't exist.
//	 */
//	private MessageChannel FindChannel(Guild guild) {
//		List<TextChannel> guildList = guild.getTextChannels();
//		for(int i = 0; i < guildList.size(); i++) {
//			if(DBUtils.getWhitelistID(guildList.get(i).getId())) {
//				return guildList.get(i);
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
//		MessageChannel m = FindChannel(event.getGuild());
//		Member member = event.getMember();
//		if (m == null) {
//			Notify.NotifyAdmin("Channel was not defined for the server." + event.getGuild().toString()
//					+ "Define it in the settings", Bot.getAdmin());
//			return;
//		}
//
//		try {
//			m.sendMessage(member.getEffectiveName() + " has left " + event.getChannelLeft().getName()).queue();
//		} catch (Exception e) {
//			Notify.NotifyAdmin(e.getMessage(), Bot.getAdmin());
//		}
//	}
//
//	@Override
//	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
//		Member m = event.getMember();
//		String id = m.getUser().getId();
//		boolean joined = DBUtils.setGuildUser(event.getGuild().getName(), event.getGuild().getId(), id, true);
//		StringBuilder sb = new StringBuilder();
//		if(joined) {
//			sb.append("User: " + m.getEffectiveName() + "Has joined \n" + event.getGuild().getName() +
//					"User has joined: " + DBUtils.joined + "times");
//
//		}
//	}
//
//	@Override
//	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
//		Member m = event.getMember();
//		String id = m.getUser().getId();
//
//	}
}
