package Utilities;

import java.util.ArrayList;
import java.util.Arrays;

public class StringUtils {
    /**
     * The resulting number from a string.
     * @param s the string to split the number from
     * @return the number
     */
    public static int SplitNumber(String s) {
           String command = getCommand(s);
           String[] newS = command.split("[!A-Za-z ]+");
           try{
               return Integer.parseInt(newS[1]);
           }
           catch(NumberFormatException | IndexOutOfBoundsException e) {
               Notify.NotifyAdmin(e.toString());
               return -1;
           }
    }

    public static String split(String src, String s) {
        String[] newS = src.split(s);
        StringBuilder sb = new StringBuilder();
        ArrayList<String> args = new ArrayList<>(Arrays.asList(newS));
        args.remove(0);
        for (String arg : args) {
          sb.append(arg);
        }
        return sb.toString();
    }

    /**
     *
     * @param s THe string with the command to be parsed.
     * @return True if a valid command in the enum is found. False otherwise.
     */
    protected static boolean isValidCommand(String s) {
        for(CommandNames c : CommandNames.values()) {
            if(s.contains(Bot.getPREFIX() + c.toString().toLowerCase())) {
                System.out.println("Command found");
                return true;
            }
        }
        return false;
    }

    /**
     * Chops off the prefix from the command string, leaving the command name and all its values.
     * @param s Command string of length x
     * @return  Command string of length x-1
     */
    public static String getCommand(String s) {
        String newS = s.substring(1);
        return newS;
    }
}
