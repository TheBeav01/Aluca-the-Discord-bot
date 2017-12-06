package Utilities;

import modules.Main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class Logging {
    private PrintWriter pw;
    private String fileName;
    private DateFormat df;
    public boolean CreateLog(String pathname) throws IOException {
        try {
            pw = new PrintWriter(new FileWriter(pathname, true));
        }
        catch (IOException e) {
            Notify.NotifyAdmin(e.toString(),Bot.getAdmin());
            return false;
        }
        return true;
    }

    /**
     *
     * @return The string for the fileName
     */
    public String buildFileName() {
        df = new SimpleDateFormat("MM/dd");
        Date d = new Date();
        String format = df.format(d).replace('/','_');
        format = format.replace(':', '_');
        System.out.println(Main.conf.isEmpty());
        fileName = Main.conf.getString("log-base-path") + format + ".txt";
        return fileName;
    }
    private String buildString(String s, Level l, String function) {
        String result = getDate() + " [" + l.toString() + "]" + " - " + function.toUpperCase() + " -> " + s;
        return result;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void log(String s) {
        pw.println(getDate() + ": " + s);
        pw.flush();
    }
    public void log(String s, Level l, String function) {
        String mainBody = buildString(s, l, function);
        pw.println(mainBody);
        pw.flush();
    }
    public void close() {
        pw.close();
    }
    public String getDate() {
        return new SimpleDateFormat("hh:mm:ss").format(new Date());
    }
}
