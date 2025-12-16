package scr.Tarefa1;
import java.util.Random;

public class Filosofo extends Thread {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private final Random random;
    private final Logger logger;
    private int vezesQueComeu;
    
    public Filosofo(int id, Garfo garfoEsquerdo, Garfo garfoDireito, Logger logger) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
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
        logger.log("Filósofo " + id + " está tentando pegar o garfo ESQUERDO " + garfoEsquerdo.getId());
        synchronized (garfoEsquerdo) {
            garfoEsquerdo.pegar();
            logger.log("Filósofo " + id + " pegou o garfo ESQUERDO " + garfoEsquerdo.getId());
            
            Thread.sleep(50);
            
            logger.log("Filósofo " + id + " está tentando pegar o garfo DIREITO " + garfoDireito.getId());
            synchronized (garfoDireito) {
                garfoDireito.pegar();
                logger.log("Filósofo " + id + " pegou o garfo DIREITO " + garfoDireito.getId());
                
                logger.log("Filósofo " + id + " está COMENDO (vez #" + (++vezesQueComeu) + ")");
                Thread.sleep(1000 + random.nextInt(2000));
                
                garfoDireito.soltar();
                logger.log("Filósofo " + id + " soltou o garfo DIREITO " + garfoDireito.getId());
            }
            
            garfoEsquerdo.soltar();
            logger.log("Filósofo " + id + " soltou o garfo ESQUERDO " + garfoEsquerdo.getId());
        }
        
        logger.log("Filósofo " + id + " TERMINOU de comer");
    }
    
    public int getVezesQueComeu() {
        return vezesQueComeu;
    }
}