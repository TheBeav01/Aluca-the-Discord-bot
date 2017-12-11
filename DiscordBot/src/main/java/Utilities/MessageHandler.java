package Utilities;

import modules.Main;
import modules.PassiveListeners;
import modules.Settings;
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

    /**
     * In this class, it's the setter for each variable. Furthermore, this
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
        if(StringUtils.isValidCommand(messageText) || Settings.getInitialized() || isMentioned()) {
            if(isMentioned()) {
                Main.logger.log("Recieved mention", Level.INFO,"MessageEvent");
                commandHandler("Mention");
            }
            else {
                commandHandler(StringUtils.getCommand(messageText));
            }

        }
    }



    /**
     *Sends a message to the channel the command was initiated in.
     * @param messageText
     */
    public void sendMessage(String messageText) {
        channel.sendMessage(messageText).queue();
    }


    public void setFields(String message, User user, Member member, Guild guild, MessageChannel channel) {
        this.messageText = message;
        this.user = user;
        this.guildMember = member;
        this.guild = guild;
        this.channel = channel;
    }

    private void commandHandler(String command) {
        if(command.startsWith(PREFIX+"s")) {
            System.out.println("Branched to settings");

        }
        else if(command.startsWith("Mention")) {
            sendMessage(":ping_pong:");
        }
        else {
            System.out.println("Branched to main listener");
            PassiveListeners p = new PassiveListeners(command);
            p.executeCommands(command, messageText, user, guildMember, guild, channel);
        }
    }

    public boolean isMentioned() {
        System.out.println(message.getContent());
        System.out.println(guild.getMember(Main.getJDA().getUserById(Bot.getBotID())).getEffectiveName());
        return messageText.contains(guild.getMember(Main.getJDA().getUserById(Bot.getBotID())).getEffectiveName());
    }
}
