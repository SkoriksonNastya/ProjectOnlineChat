package logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static Logger log;
    private static final String PATH = "./src/logger/log.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Logger() {

    }

    public static Logger getLog() {
        if (log == null) {
            log = new Logger();
        }
        return log;
    }

    public boolean log (String msg) {
        try {
            PrintWriter wr = new PrintWriter(new FileWriter(PATH, true));
            wr.write(LocalDateTime.now().format(formatter)+ " " + msg + "\n");
            wr.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
