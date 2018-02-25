package Utilities;

import modules.Main;
import modules.PassiveListeners;
import modules.SettingsRedux;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.logging.Level;

/**
 * This getter class pulls useful information gathered from {@link net.dv8tion.jda.core.hooks.ListenerAdapter the listener}.
 *
 */
public class MessageHandler extends ListenerAdapter {
    public Message message;
    private User user;
    private String messageText;
    private Member guildMember;
    private Guild guild;
    private MessageReceivedEvent recievedMessage;
    private MessageChannel channel;
    public char PREFIX = '!';
    public boolean executableCommand;
    private String test;
    private SettingsRedux sr;
    /**
     * In this class, it's the setter for each variable. This checks if the author is a bot, and if not it checks if
     * there's a valid command within the message itself using isValidCommand.
     * @param messageEvent
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent messageEvent) {
        recievedMessage = messageEvent;
        message = messageEvent.getMessage();
        messageText = recievedMessage.getMessage().getStrippedContent();

        user = messageEvent.getAuthor();
        guildMember = messageEvent.getMember();
        guild = messageEvent.getGuild();
        channel = messageEvent.getChannel();
        System.out.println(isMentioned());
        if(message.getAuthor().isBot()) {
            return;
        }
        if(SettingsRedux.getInitialized()) {
            sr.checkValidity(recievedMessage);
        }
        else if (StringUtils.isValidCommand(messageText) || SettingsRedux.getInitialized() || isMentioned()) {
            if(isMentioned()) {
                Main.logger.log("Received mention", Level.INFO,"MessageEvent");
                commandHandler("Mention");
            }
            else {
                commandHandler(StringUtils.getCommand(messageText));
            }
        }
    }



    /**
     *Sends a message to the channel the calling command was initiated in.
     * @param messageText
     */
    public void sendMessage(String messageText) {
        channel.sendMessage(messageText).queue();
    }

    /**
     * Passback from PassiveListeners.java. This does what one expects it to: Set all sorts of things to be used in PassiveListeners.
     * @param message
     * @param user
     * @param member
     * @param guild
     * @param channel
     */
    public void setFields(String message, User user, Member member, Guild guild, MessageChannel channel, SettingsRedux settingsRedux) {
        this.messageText = message;
        this.user = user;
        this.guildMember = member;
        this.guild = guild;
        this.channel = channel;
        this.sr = settingsRedux;
    }

    /**
     * The only method that should call this is onMessageRecieved(0 as that contains the logic for what is a command and
     * what isn't. Will handle the branching between different modules.
     * @param command The prefix stripped command.
     */
    private void commandHandler(String command) {
        if(command.equalsIgnoreCase("s")) {
            System.out.println("Branched to settings");
            sr = new SettingsRedux(recievedMessage);
        }
        else if(command.startsWith("help")) {
            System.out.println("Branched to Help");
        }
        else if(command.startsWith("Mention")) {
            sendMessage(":ping_pong:");
        }
        else {
            System.out.println("Branched to main listener");
            PassiveListeners p = new PassiveListeners(command);
            p.executeCommands(command, messageText, user, guildMember, guild, channel, sr);
        }
    }

    /**
     * Simple method that determines if the previous message mentioned the bot.
     * @return True if the bot has been mentioned. False otherwise.
     */
    public boolean isMentioned() {
        System.out.println(message.getContent());
        System.out.println(guild.getMember(Main.getJDA().getUserById(Bot.getBotID())).getEffectiveName());
        return messageText.contains(guild.getMember(Main.getJDA().getUserById(Bot.getBotID())).getEffectiveName());
    }
    public boolean BotOwnerHasMessaged() {
        System.out.println("Foo: " + user.getId().equals(Bot.getAdmin().getId()));
        return user.getId().equals(Bot.getAdmin().getId());
    }

    public void SendMessage(String s) {
        channel.sendMessage(s).queue();
    }
    public void sendEmbed(MessageEmbed me) {
        channel.sendMessage(me).queue();
    }

    public MessageReceivedEvent getEvent() {
        return recievedMessage;
    }
    public String getMessageText() {
        return messageText;
    }
}
