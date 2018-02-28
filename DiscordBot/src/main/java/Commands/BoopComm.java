package Commands;

import Utilities.MessageHandler;
import modules.Main;

public class BoopComm extends CommandBase {
    MessageHandler mh;
    public BoopComm() {
        init();
    }
    @Override
    public void execute() {
    }

    @Override
    public void init() {
       mh = Main.getMessageHandler();
    }
}
