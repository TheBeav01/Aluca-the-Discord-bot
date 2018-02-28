package modules;

import Utilities.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
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
    private static boolean isActive = false;
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
        isActive = true;
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
                execute(Integer.parseInt(stripped));
                init = false;
            }
        }
    }
    public void execute(int code) {
        mh.deleteLastMessage();
        switch (code) {
            case 0:
                Names.Init(mh.getEvent(), ebh);
                isActive = false;
                break;
            case 1:
                RoleColor.Init(mh.getEvent(), ebh);
                isActive = false;
                break;
            case 2:
                ebh.ClearText();
                ebh.addText("Enable voice join/leave?", "Y/N");
                ebh.send();
                break;
            case -1:
                ebh.ClearText();
                ebh.addText("Leaving Settings", "");
                isActive = false;
                break;
            default:
                Notify.NotifyAdmin("SettingsRedux => Execute error");
                isActive = false;
                break;
        }
    }
    public static boolean getInitialized() {
        return init;
    }
    public static void setInitialized(boolean setInit) {
        init = setInit;
    }
    public static boolean isActive() {
        return isActive;
    }
    public static void setActive(boolean setAct) {
        isActive = setAct;
    }
    public static void addWhiteList(MessageChannel channel, Guild guild) {
        int isInDB = DBUtils.setWhitelist(channel.getId(), guild.getId());
        MessageHandler mh = Main.getMessageHandler();
        if (isInDB == -1) {
            throw new IllegalArgumentException("Was not found");
        } else if (isInDB == 0) {
            mh.sendMessage("Added channel " + channel.getName() + "to the list of approved channels");
            Main.logger.log("Added channel ID: " + channel.getId() + " to database",Level.INFO,"DB");
            isActive = false;
            return;
        } else {
            mh.sendMessage("Channel has been adjusted.");
            Main.logger.log("Changed channels from old to " + channel.getId(),Level.INFO,"DB");
            setInitialized(false);
            isActive = false;
            return;
        }

    }

}
