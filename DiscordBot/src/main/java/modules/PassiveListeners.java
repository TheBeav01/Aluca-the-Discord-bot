package modules;

import Commands.DeleteMessageComm;
import Commands.KillComm;
import Commands.PingComm;
import Commands.RollComm;
import Utilities.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;

import java.util.List;
import java.util.logging.Level;

public class PassiveListeners extends MessageHandler {
	private String message;
	private MessageHandler mh;
	private EmbedBuilderHelper ebh;
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
	 *  @param strippedMessage The command stripped message.
	 * @param rawMessage      The message with the command.
	 * @param user            The User object that posted the message.
	 * @param member          The Member object that posted the message.
	 * @param guild           The guild the message originated in.
	 * @param channel         The guild channel the message originated in.
	 * @param settingsRedux
	 */

	//TODO: Switch statements to clean up the code?
	public void executeCommands(String strippedMessage, String rawMessage, User user,
								Member member, Guild guild, MessageChannel channel, SettingsRedux settingsRedux) {
		setFields(strippedMessage, user, member, guild, channel, settingsRedux);

		if (mh == null) {
			Main.logger.log("MessageHandler has not been initialized!", Level.WARNING, "Gen Logging");
			return;
		}
		try {
			if (strippedMessage.contains("ping")) {
				PingComm pc = new PingComm();
				pc.execute();
				return;
			}
			if(strippedMessage.startsWith("r ")) {

				RollComm rc = new RollComm(StringUtils.split(strippedMessage," "));
				rc.execute();
				return;
			}
			if (strippedMessage.contains("kill") && (member.isOwner() || mh.BotOwnerHasMessaged())) {
				KillComm kc = new KillComm();
				kc.execute();
				return;
			}
			if (strippedMessage.contains("del") && (member.isOwner() || mh.BotOwnerHasMessaged())) {
				DeleteMessageComm dmc = new DeleteMessageComm(strippedMessage, channel);
				dmc.execute();
				return;
			}
			if (rawMessage.equalsIgnoreCase("y") && (member.isOwner() || mh.BotOwnerHasMessaged())
					&& SettingsRedux.isActive()) {
				ebh = new EmbedBuilderHelper(new EmbedBuilder(), "DB Shennanigans", "");
				if (!DBUtils.getWhitelistID(channel.getId())) {
					System.out.println("layer 2");
					SettingsRedux.addWhiteList(channel, guild);
				}
				else {
					mh.deleteLastMessage();
					ebh.SendAsText("ID already found!", false);
				}
				return;
			}

			if (rawMessage.equalsIgnoreCase("n") && (member.isOwner() || mh.BotOwnerHasMessaged())
					&& SettingsRedux.isActive()) {
				mh.deleteLastMessage();
				ebh = new EmbedBuilderHelper(new EmbedBuilder(), "DB Shennanigans", "");
				SettingsRedux.setInitialized(false);
				SettingsRedux.setActive(false);
				ebh.SendAsText("Exiting from the settings menu.", true);
				return;
			}
			if(strippedMessage.equalsIgnoreCase("boop")) {
				Main.logger.log("Sent boop to " + channel.getId(),Level.INFO,"General Logging");

				if(user.getId().matches("145399954574147584")) { //Rax <3
					mh.sendMessage("*blinks at the sudden boop, then hugtackles and wrapping both wings around " +
							" you, purring happily*");
				}
				else if(user.getId().matches("370045256902639647")) {
					mh.sendMessage("*hugtackles the dragon, purring and licking at its face*");
				}
				else if(mh.BotOwnerHasMessaged()) {
					mh.sendMessage("*purrs and nuzzles finger happily*");
				}
				else {
					mh.sendMessage("*tilts head and looks up at you with a confused gaze*");
				}
				return;
			}
			if(strippedMessage.contains("echo")) {
				String s = StringUtils.split(strippedMessage, " ");
				mh.deleteLastMessage();
				mh.sendMessage(s);
				return;
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
			m.sendMessage(member.getEffectiveName() + " has joined " + event.getChannelJoined().getName()).queue();
			Main.logger.log("VC join detected in " + m.getId(),Level.INFO,"General Logging");
		} catch (Exception e) {
			Notify.NotifyAdmin(e.getMessage());
		}
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
			m.sendMessage(member.getEffectiveName() + " has left " + event.getChannelLeft().getName()).queue();
			Main.logger.log("VC leave detected " + m.getId(),Level.INFO,"General Logging");
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
				Main.logger.log("Join detected in " + event.getGuild().getId(),Level.INFO,"General Logging");
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
				Main.logger.log("Leave detected in " + event.getGuild().getId(),Level.INFO,"General Logging");

			}
		}
		catch (Exception e) {
			Notify.NotifyAdmin(e.toString() + " " + e.getCause().toString());
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
		for (TextChannel aGuildList : guildList) {
			if (DBUtils.getWhitelistID(aGuildList.getId())) {
				return aGuildList;
			}
		}
		return null;
	}
}
