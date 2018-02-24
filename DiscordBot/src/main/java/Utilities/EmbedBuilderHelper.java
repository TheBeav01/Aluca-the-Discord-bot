package Utilities;

import modules.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class EmbedBuilderHelper {
    MessageEmbed result;
    MessageHandler mh;
    public EmbedBuilderHelper() {
        mh = Main.getMessageHandler();
    }
    public EmbedBuilder build() {
        return null;
    }
    public void helloWorld() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("Message from me:","Hello world!", false);
        result = eb.build();
        send(result);
    }
    private void send(MessageEmbed eb) {
        mh.sendEmbed(eb);
    }
}
