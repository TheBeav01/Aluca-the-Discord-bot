package Utilities;

import modules.PassiveListeners;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
        test = "lmao";
        recievedMessage = messageEvent;
        message = messageEvent.getMessage();
        messageText = recievedMessage.getMessage().getStrippedContent();
        user = messageEvent.getAuthor();
        guildMember = messageEvent.getMember();
        guild = messageEvent.getGuild();
        channel = messageEvent.getChannel();
        if(message.getAuthor().isBot()) {
            return;
        }
        if(StringUtils.isValidCommand(messageText)) {
            commandHandler(StringUtils.getCommand(messageText));


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
        else {
            System.out.println("Branched to main listener");
            PassiveListeners p = new PassiveListeners(command);
            p.executeCommands(command, user, guildMember, guild, channel);
        }
    }
}
