package Utilities;

public class StringUtils {
    /**
     * The resulting number from a string.
     * @param s the string to split the number from
     * @return the number
     */
    public static int SplitNumber(String s) {
        if(isValidCommand(s)) {
           String command = getCommand(s);
           String[] newS = command.split("[!A-Za-z ]+");
           try{
               return Integer.parseInt(newS[1]);
           }
           catch(NumberFormatException | IndexOutOfBoundsException e) {
               Notify.NotifyAdmin(e.toString(), Bot.getAdmin());
               return -1;
           }
        }
        return -1;
    }

    protected static boolean isValidCommand(String s) {
        for(CommandNames c : CommandNames.values()) {
            if(s.contains(c.toString().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String getCommand(String s) {
        String newS = s.substring(1);
        return newS;
    }
}
