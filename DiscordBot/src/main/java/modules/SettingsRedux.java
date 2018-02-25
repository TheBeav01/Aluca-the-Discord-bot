package modules;

import Utilities.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import settings.Names;
import settings.RoleColor;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;

public class SettingsRedux extends MessageHandler {
    private static boolean init = false;
    private MessageReceivedEvent holder, lastSuccessful;
    private static String id;
    private EmbedBuilderHelper ebh;
    private MessageHandler mh;
    public SettingsRedux(MessageReceivedEvent event) {
        this.holder = event;
        mh = Main.getMessageHandler();
        init();
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
                ebh.addText(Integer.toString(iter) + ". " + splitted[0].trim(), splitted[1].trim());
                iter++;
            }
            sc.close();
        } catch (Exception e) {
            Notify.NotifyAdmin(e.getMessage() + f.getAbsolutePath());
            sb.append("Whoops. Something went wrong! Contact " + Main.getJDA().getUserById(OwnerInfo.sId).getAsMention());

        }
        Main.logger.log("Constructed settings embed");
        init = true;
        ebh.send();
    }

    public void checkValidity(MessageReceivedEvent toCheck) {
        System.out.println("HAAAAAAA");
        User Author = toCheck.getAuthor();
        MessageChannel ch = toCheck.getChannel();
        String stripped = toCheck.getMessage().getStrippedContent();
        if(stripped.equalsIgnoreCase("exit")) {
            init = false;
            execute(-1);
            return;
        }
        if(stripped.length() > 1) {
            return;
        }
        if((Author.getIdLong() == holder.getAuthor().getIdLong()) &&
                (ch.getIdLong() == holder.getChannel().getIdLong())) {
            if(stripped.matches("[0-9]+") && Integer.parseInt(stripped) <= Bot.MAX_SETTING_RANGE) {
                init = false;
                execute(Integer.parseInt(stripped));
            }
        }
    }
    public void execute(int code) {
        switch(code) {
            case 0:
                Names.Init(mh.getEvent(), ebh);
                break;
            case 1:
                RoleColor.Init(mh.getEvent(), ebh);
                break;
            case 2:
                ebh.ClearText();
                ebh.addText("Enable voice join/leave?", "Y/N");
                ebh.send();
                break;
            case -1:
                ebh.ClearText();
                ebh.addText("Leaving Settings", "");
                break;
            default:
                Notify.NotifyAdmin("SettingsRedux => Execute error");
                break;
        }
//        if(!RoleColor.isActive()) {
//            if(holder.getAuthor().isBot()) {
//                Bot.setMember(holder.getMember());
//                return;
//            }
//            String content = super.getMessageText();
//            if(content.equals("!s")) {
//                lastSuccessful = holder;
//            }
//
//            else if(content.equals("1") && init) {
//                Names.Init(lastSuccessful);
//            }
//
//            else if(content.equals("2") && init) {
//                lastSuccessful.getChannel().sendMessage("`Enter desired role color`").queue();
//                RoleColor.Init(lastSuccessful);
//            }
//            else if(content.equals("3") && init) {
//                lastSuccessful.getChannel().sendMessage("`Enable VC join/leave in this channel? Y/N`").queue();
//            }
//            else if(content.equals("exit") && init) {
//                init = false;
//                lastSuccessful.getChannel().sendMessage("Exiting").queue();
//            }
//        }
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
