package Commands;

import Utilities.MessageHandler;
import modules.Main;

public class PingComm extends CommandBase {
    MessageHandler mh;
    public PingComm() {
        init();
    }
    @Override
    public void execute() {
        long ping = mh.message.getJDA().getPing();
        mh.sendMessage(":ping_pong: \n Time taken: " + ping + "ms");
    }

    @Override
    public void init() {
        mh = Main.getMessageHandler();
    }
}
