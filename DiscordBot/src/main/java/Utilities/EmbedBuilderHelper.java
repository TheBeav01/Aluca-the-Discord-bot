package Utilities;

import modules.Main;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class EmbedBuilderHelper {
    private MessageHandler mh;
    private EmbedBuilder ebMain;
    public EmbedBuilderHelper(EmbedBuilder target, String title, String footer, String imageURL) {
        this.ebMain = target;
        mh = Main.getMessageHandler();
        ebMain.setTitle(title);
        ebMain.setFooter(footer, imageURL);
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
    public void addColor(int red, int green, int blue) {
        ebMain.setColor(new Color(red, green, blue));
    }
    public void send() {
        mh.sendEmbed(ebMain.build());
    }
}
