package Commands;

import Utilities.EmbedBuilderHelper;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Random;

public class RollComm extends CommandBase {
    private int dice, upperBound, addNum;
    private String diceCommand;
    private Random rand;
    public RollComm(String diceComm) {
        addNum = 0;
        diceCommand = diceComm;
        if(diceComm.contains("+")) {
            addNum = Integer.parseInt(diceComm.substring(diceComm.indexOf("+"), diceComm.length()));
            diceCommand = diceComm.substring(0,diceComm.indexOf('+'));
        }
        this.dice = Integer.parseInt(diceCommand.split("d")[0]);
        this.upperBound = Integer.parseInt(diceCommand.split("d")[1]);
        init();
    }
    @Override
    public void execute() {
        int result = 0;
        StringBuilder sb = new StringBuilder("Result: ");
        for (int i = 0; i < dice; i++) {
            int toAdd = rand.nextInt(upperBound) + 1;
            sb.append(toAdd).append(" + ");
            result += toAdd;
        }
        sb.append(addNum).append(" = ").append(result);
        EmbedBuilderHelper ebh = new EmbedBuilderHelper(new EmbedBuilder(), "Dice Roller","Aluca");
        ebh.setColor(0,255,0);
        ebh.addText("Result message ( " + diceCommand + " ):", sb.toString());
        ebh.send();

    }

    @Override
    public void init() {
        rand = new Random();
    }
}
