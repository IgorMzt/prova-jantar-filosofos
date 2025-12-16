package scr.Tarefa4;

import java.util.Random;

public class Filosofo extends Thread {
    private final int id;
    private final Mesa mesa;
    private final Random random;
    private final Logger logger;
    private int vezesQueComeu;
    
    public Filosofo(int id, Mesa mesa, Logger logger) {
        this.id = id;
        this.mesa = mesa;
        this.random = new Random();
        this.logger = logger;
        this.vezesQueComeu = 0;
    }
    
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                pensar();
                comer();
            }
        } catch (InterruptedException e) {
            logger.log("Filósofo " + id + " foi interrompido. Comeu " + vezesQueComeu + " vezes.");
            Thread.currentThread().interrupt();
        }
    }
    
    private void pensar() throws InterruptedException {
        logger.log("Filósofo " + id + " está PENSANDO");
        Thread.sleep(1000 + random.nextInt(2000));
    }
    
    private void comer() throws InterruptedException {
        logger.log("Filósofo " + id + " quer COMER");
        
        mesa.pegarGarfos(id);
        
        try {
            logger.log("Filósofo " + id + " está COMENDO (vez #" + (++vezesQueComeu) + ")");
            Thread.sleep(1000 + random.nextInt(2000));
            logger.log("Filósofo " + id + " TERMINOU de comer");
        } finally {
            mesa.soltarGarfos(id);
        }
    }
    
    public int getVezesQueComeu() {
        return vezesQueComeu;
    }
}