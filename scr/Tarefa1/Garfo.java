package scr.Tarefa1;
public class Garfo {
    private final int id;
    private boolean emUso;
    
    public Garfo(int id) {
        this.id = id;
        this.emUso = false;
    }
    
    public int getId() {
        return id;
    }
    
    public synchronized void pegar() throws InterruptedException {
        while (emUso) {
            wait();
        }
        emUso = true;
    }
    
    public synchronized void soltar() {
        emUso = false;
        notifyAll();
    }
}