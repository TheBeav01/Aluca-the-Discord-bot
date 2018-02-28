package Commands;

import Utilities.MessageHandler;
import modules.Main;

import java.util.logging.Level;

public class KillComm extends CommandBase {
    MessageHandler mh;
    public KillComm() {
        init();
    }
    @Override
    public void execute() {
        mh.sendMessage("Shutting down.");
        Main.logger.log("END OF LOG FILE \n ------------------------------------------------", Level.INFO,
                "Shutdown");
        Main.logger.close();
        Main.getJDA().shutdown();
        System.exit(0);
    }

    @Override
    public void init() {
        this.mh = Main.getMessageHandler();
    }
}
