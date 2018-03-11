package Utilities;

import modules.Main;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class EmbedBuilderHelper {
    private MessageHandler mh;
    private EmbedBuilder ebMain;

    /**
     * Constructs a helper method for an embed to (somewhat easily) build, edit, and send Discord embeds.
     * @param target A target embed builder that this method operates on
     * @param title The title of the embed
     * @param footer The footer of the embed
     * @param imageURL The image that the embed uses
     */
    public EmbedBuilderHelper(EmbedBuilder target, String title, String footer, String imageURL) {
        this.ebMain = target;
        mh = Main.getMessageHandler();
        ebMain.setTitle(title);
        ebMain.setFooter(footer, imageURL);
        ebMain.setTimestamp(java.time.Instant.now());
    }

    /**
     * Constructs a helper method for an embed to (somewhat easily) build, edit, and send Discord embeds. This methhod
     * is used if you want to use the default bot image. Hardocded in Bot. Will likely move it into a .cfg

     * @param target A target embed builder that this method operates on
     * @param title The title of the embed
     * @param footer The footer of the embed
     */
    public EmbedBuilderHelper(EmbedBuilder target, String title, String footer) {
        this.ebMain = target;
        mh = Main.getMessageHandler();
        ebMain.setTitle(title);
        ebMain.setFooter(footer, Bot.DEFAULT_IM_URL);
        ebMain.setTimestamp(java.time.Instant.now());
    }
    public void helloWorld() {
        addText("Message from me", "Hello World, but with an adjusted class!");
        send();
    }
    public void addText(String fieldTitle, String fieldText) {
        ebMain.addField(fieldTitle, fieldText, false);
    }
    public void addTextAsInline(String fieldName, String fieldText) {
        ebMain.addField(fieldName, fieldText, true);
    }
    public void setColor(int red, int green, int blue) {
        ebMain.setColor(new Color(red, green, blue));
    }
    public void send() {
        mh.sendEmbed(ebMain.build());
    }
    public void ClearText() {
        ebMain.clearFields();
    }
    public void SendAsText(String message, boolean willClear) {
        if(willClear) {
            ebMain.clearFields();
        }
        addText("Command message: ",message);
        send();
    }
}
