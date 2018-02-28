package Commands;

import Utilities.MessageHandler;
import Utilities.StringUtils;
import modules.Main;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.logging.Level;

public class DeleteMessageComm extends CommandBase {
    MessageHandler mh;
    String message;
    MessageChannel ch;

    public DeleteMessageComm(String message, MessageChannel ch) {
        init();
        this.message = message;
        this.ch = ch;
    }
    @Override
    public void execute() {
        int x = StringUtils.SplitNumber(message);
        int i = 0;
        for (Message messages : ch.getIterableHistory()) {
            if (i > x) {
                break;
            } else {
                Main.logger.log("From: " + messages.getAuthor().getName() + ": " + messages.getRawContent(),
                        Level.INFO, "delete");
                messages.delete().queue();
                i++;
            }
        }
    }

    @Override
    public void init() {
        this.mh = Main.getMessageHandler();
    }
}