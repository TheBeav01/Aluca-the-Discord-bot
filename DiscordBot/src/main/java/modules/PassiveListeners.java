package modules;

import Utilities.*;
import net.dv8tion.jda.core.entities.*;

import java.util.logging.Level;

public class PassiveListeners extends MessageHandler {
	private String message;
	private User user;
	private Member member;
	private Guild guild;
	private MessageChannel channel;

	public PassiveListeners() {
		super();
		System.out.println("Passive listener constructor");
	}

	public PassiveListeners(String s) {
		message = s;
		System.out.println("Secondary constructor");
	}

	/**
	 * The main part of the bot that handles the response to most commands.
	 * @param strippedMessage The command stripped message.
	 * @param rawMessage The message with the command.
	 * @param user The User object that posted the message.
	 * @param member The Member object that posted the message.
	 * @param guild The guild the message originated in.
	 * @param channel The guild channel the message originated in.
	 */
	public void executeCommands(String strippedMessage, String rawMessage, User user, Member member, Guild guild, MessageChannel channel) {
		setFields(strippedMessage, user, member, guild, channel);

		try {
			if (strippedMessage.contains("ping")) {
				long ping = channel.getJDA().getPing();
				sendMessage(":ping_pong: \n Time taken: " + ping + "ms");
			}
			if (strippedMessage.contains("kill") && (member.isOwner() || botOwnerHasPosted(user.getIdLong()))) {
				sendMessage("Shutting down.");
				Main.logger.log("END OF LOG FILE \n ------------------------------------------------", Level.INFO, "Shutdown");
				Main.logger.close();
				Main.getJDA().shutdown();
				System.exit(0);
			}
			if (strippedMessage.contains("del")) {

				int x = StringUtils.SplitNumber(strippedMessage);
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
			}
				if (rawMessage.equals("Y") && member.isOwner() && Settings.getInitialized()) {
					if (!DBUtils.getWhitelistID(channel.getId())) {
						int isInDB = DBUtils.setWhitelist(channel.getId(), guild.getId());
						if (isInDB == -1) {
							throw new IllegalArgumentException("Was not found");
						} else if (isInDB == 0) {
							sendMessage("Added channel " + channel.getName() + "to the list of approved channels");
							return;
						} else {
							sendMessage("Channel has been adjusted.");
							return;
						}
					}
				}

				if (rawMessage.equals("N") && member.isOwner() && Settings.getInitialized()) {
					Settings.setInitialized(false);
					sendMessage("Exiting from the settings menu.");
					return;
				}
		} catch (Exception e) {
			Notify.NotifyAdmin(e.toString());
		}
	}
	private boolean botOwnerHasPosted(long COMP) {
		return (Bot.getAdmin().getIdLong() == COMP);
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
	}
