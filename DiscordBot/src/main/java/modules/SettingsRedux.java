package modules;

import Utilities.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import settings.Names;
import settings.RoleColor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;

public class SettingsRedux extends MessageHandler {
    private static boolean init = false;
    private MessageReceivedEvent holder, lastSuccessful;
    private static String id;
    private EmbedBuilderHelper ebh;
    public SettingsRedux(MessageReceivedEvent event) {
        this.holder = event;
        init();
    }
    public void executeCommands() {

    }

    private void init() {
        ebh = new EmbedBuilderHelper(new EmbedBuilder(), "Settings Module","'exit' to exit", Bot.DEFAULT_IM_URL);
        ebh.addColor(184,16,16);
        StringBuilder sb = new StringBuilder();
        Main.logger.log("Branched over to new settings", Level.INFO, "Settings");
        File f = new File(Main.conf.getString("settings-Path"));
        try {
           Scanner sc = new Scanner(f);
           int iter = 0;
            while(sc.hasNext()) {
                String temp = sc.nextLine();
                String[] splitted = temp.split(",");
                System.out.println(splitted[1]);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            Notify.NotifyAdmin(e.getMessage() + f.getAbsolutePath());
            sb.append("Whoops. Something went wrong! Contact " + Main.getJDA().getUserById(OwnerInfo.sId).getAsMention());

        }
        init = true;
        ebh.send();
    }

    public void execute(int code) {
        holder = super.getEvent();
        if(!RoleColor.isActive()) {
            if(holder.getAuthor().isBot()) {
                Bot.setMember(holder.getMember());
                return;
            }
            String content = super.getMessageText();
            if(content.equals("!s")) {
                lastSuccessful = holder;
            }

            else if(content.equals("1") && init) {
                Names.Init(lastSuccessful);
            }

            else if(content.equals("2") && init) {
                lastSuccessful.getChannel().sendMessage("`Enter desired role color`").queue();
                RoleColor.Init(lastSuccessful);
            }
            else if(content.equals("3") && init) {
                lastSuccessful.getChannel().sendMessage("`Enable VC join/leave in this channel? Y/N`").queue();
            }
            else if(content.equals("exit") && init) {
                init = false;
                lastSuccessful.getChannel().sendMessage("Exiting").queue();
            }
        }
    }

//    @Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//        lastSuccessful = event;
//        if(!RoleColor.isActive()) {
//            if(event.getAuthor().isBot()) {
//                Bot.setMember(event.getMember());
//                return;
//            }
//            String content = event.getMessage().getRawContent();
//            if(content.equals("!s")) {
//                lastSuccessful = event;
//            }
//
//            else if(content.equals("1") && init) {
//                Names.Init(event);
//            }
//
//            else if(content.equals("2") && init) {
//                event.getChannel().sendMessage("`Enter desired role color`").queue();
//                RoleColor.Init(event);
//            }
//            else if(content.equals("3") && init) {
//                event.getChannel().sendMessage("`Enable VC join/leave in this channel? Y/N`").queue();
//            }
//            else if(content.equals("exit") && init) {
//                init = false;
//                event.getChannel().sendMessage("Exiting").queue();
//            }
//        }
//    }

    public static boolean getInitialized() {
        return init;
    }
    public static void setInitialized(boolean setInit) {
        init = setInit;
    }

}
