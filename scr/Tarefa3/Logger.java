package scr.Tarefa3;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final DateTimeFormatter formatter;
    
    public Logger() {
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    }
    
    public synchronized void log(String mensagem) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[" + timestamp + "] " + mensagem);
    }
}