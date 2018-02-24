package modules;

import Utilities.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;

import java.util.List;
import java.util.logging.Level;

public class PassiveListeners extends MessageHandler {
	private String message;
	private User user;
	private Member member;
	private Guild guild;
	private MessageChannel channel;
	private MessageHandler mh;
//	public PassiveListeners() {
//		super();
//		System.out.println("Passive listener constructor");
//	}

	public PassiveListeners(String s) {
		message = s;
		System.out.println("Secondary constructor");
		mh = Main.getMessageHandler();
	}

	/**
	 * The main part of the bot that handles the response to most commands.
	 *
	 * @param strippedMessage The command stripped message.
	 * @param rawMessage      The message with the command.
	 * @param user            The User object that posted the message.
	 * @param member          The Member object that posted the message.
	 * @param guild           The guild the message originated in.
	 * @param channel         The guild channel the message originated in.
	 */
	public void executeCommands(String strippedMessage, String rawMessage, User user, Member member, Guild guild, MessageChannel channel) {
		setFields(strippedMessage, user, member, guild, channel);
		if (mh == null) {
			Main.logger.log("MessageHandler has not been initialized!", Level.WARNING, "Gen Logging");
		}
		try {
			if (strippedMessage.contains("ping")) {
				long ping = channel.getJDA().getPing();
				mh.sendMessage(":ping_pong: \n Time taken: " + ping + "ms");
			}
			if (strippedMessage.contains("kill") && (member.isOwner() || mh.BotOwnerHasMessaged())) {
				mh.sendMessage("Shutting down.");
				Main.logger.log("END OF LOG FILE \n ------------------------------------------------", Level.INFO, "Shutdown");
				Main.logger.close();
				Main.getJDA().shutdown();
				System.exit(0);
			}
			if (strippedMessage.contains("del") && (member.isOwner() || mh.BotOwnerHasMessaged())) {

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
			if (rawMessage.equalsIgnoreCase("Y") && (member.isOwner() || mh.BotOwnerHasMessaged())
					&& Settings.getInitialized()) {
				if (!DBUtils.getWhitelistID(channel.getId())) {
					int isInDB = DBUtils.setWhitelist(channel.getId(), guild.getId());
					if (isInDB == -1) {
						throw new IllegalArgumentException("Was not found");
					} else if (isInDB == 0) {
						mh.sendMessage("Added channel " + channel.getName() + "to the list of approved channels");
						return;
					} else {
						mh.sendMessage("Channel has been adjusted.");
						return;
					}
				}
			}

			if (rawMessage.equalsIgnoreCase("N") && (member.isOwner() || mh.BotOwnerHasMessaged())
					&& Settings.getInitialized()) {
				Settings.setInitialized(false);
				mh.sendMessage("Exiting from the settings menu.");
				return;
			}
			if (strippedMessage.equalsIgnoreCase("ehw")) {
				EmbedBuilderHelper ebh = new EmbedBuilderHelper();
				Main.logger.log("Embedded Hello World", Level.INFO, "General Logging");
				ebh.helloWorld();
			}
			if(strippedMessage.equalsIgnoreCase("boop")) {
				if(user.getId() == "145399954574147584") { //Rax <3
					mh.sendMessage("*blinks at the sudden boop, then hugtackles and wrapping both wings around " +
							" you, purring happily*");
				}
				if(mh.BotOwnerHasMessaged()) {
					mh.sendMessage("*purrs and nuzzles finger happily*");
				}
				else {
					mh.sendMessage("*tilts head and looks up at you with a confused gaze*");
				}
			}
		} catch (Exception e) {
			Notify.NotifyAdmin(e.toString());
		}
	}


	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		MessageChannel m = FindChannel(event.getGuild());
		Member member = event.getMember();
		if (m == null) {
			Notify.NotifyAdmin("Channel was not defined for the server. Define it in the settings");
			return;
		}

		try {
			mh.sendMessage(member.getEffectiveName() + " has joined " + event.getChannelJoined().getName());
		} catch (Exception e) {
			Notify.NotifyAdmin(e.getMessage());
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
		for (int i = 0; i < guildList.size(); i++) {
			if (DBUtils.getWhitelistID(guildList.get(i).getId())) {
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
					+ "Define it in the settings");
			return;
		}

		try {
			mh.sendMessage(member.getEffectiveName() + " has left " + event.getChannelLeft().getName());
		} catch (Exception e) {
			Notify.NotifyAdmin(e.getMessage());
		}
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Member m = event.getMember();
		String id = m.getUser().getId();
		try {
			boolean joined = DBUtils.setGuildUser(event.getGuild().getName(), event.getGuild().getId(), id, true);
			if (joined) {
				mh.sendMessage("User: " + m.getEffectiveName() + "Has joined " + event.getGuild().getName() +
						"\n User has joined: " + DBUtils.joined + "times");

			}
		}
		catch (Exception e) {
			Notify.NotifyAdmin(e.toString());
		}
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		Member m = event.getMember();
		String id = m.getUser().getId();
		try {
			boolean left = DBUtils.setGuildUser(event.getGuild().getName(), event.getGuild().getId(), id, false);
			if (left) {
				mh.sendMessage("User: " + m.getEffectiveName() + "Has left " + event.getGuild().getName() +
						"\n User has left: " + DBUtils.joined + "times");

			}
		}
		catch (Exception e) {
			Notify.NotifyAdmin(e.toString());
		}
	}
}
