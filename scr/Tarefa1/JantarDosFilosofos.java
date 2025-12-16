package scr.Tarefa1;

public class JantarDosFilosofos {
    private static final int NUM_FILOSOFOS = 5;
    private static final int TEMPO_EXECUCAO_MS = 30000;
    
    public static void main(String[] args) {
        Logger logger = new Logger();
        
        logger.log("========================================");
        logger.log("JANTAR DOS FILÓSOFOS - Versão com Deadlock");
        logger.log("========================================");
        logger.log("Iniciando simulação com " + NUM_FILOSOFOS + " filósofos");
        logger.log("Tempo de execução: " + (TEMPO_EXECUCAO_MS / 1000) + " segundos");
        logger.log("========================================\n");
        
        Garfo[] garfos = new Garfo[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Garfo(i);
            logger.log("Garfo " + i + " criado");
        }
        
        logger.log("");
        
        Filosofo[] filosofos = new Filosofo[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Garfo garfoEsquerdo = garfos[i];
            Garfo garfoDireito = garfos[(i + 1) % NUM_FILOSOFOS];
            filosofos[i] = new Filosofo(i, garfoEsquerdo, garfoDireito, logger);
            logger.log("Filósofo " + i + " criado (Garfo Esq: " + garfoEsquerdo.getId() + 
                      ", Garfo Dir: " + garfoDireito.getId() + ")");
        }
        
        logger.log("\n========================================");
        logger.log("Iniciando threads dos filósofos...");
        logger.log("========================================\n");
        
        long startTime = System.currentTimeMillis();
        for (Filosofo filosofo : filosofos) {
            filosofo.start();
        }
        
        Thread monitor = new Thread(() -> {
            try {
                int contador = 0;
                while (System.currentTimeMillis() - startTime < TEMPO_EXECUCAO_MS) {
                    Thread.sleep(5000);
                    contador += 5;
                    logger.log("\n--- STATUS (" + contador + "s) ---");
                    for (int i = 0; i < NUM_FILOSOFOS; i++) {
                        logger.log("Filósofo " + i + " comeu " + filosofos[i].getVezesQueComeu() + " vezes");
                    }
                    logger.log("--- FIM STATUS ---\n");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        monitor.start();
        
        try {
            Thread.sleep(TEMPO_EXECUCAO_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.log("\n========================================");
        logger.log("Encerrando simulação...");
        logger.log("========================================\n");
        
        for (Filosofo filosofo : filosofos) {
            filosofo.interrupt();
        }
        monitor.interrupt();
        
        try {
            for (Filosofo filosofo : filosofos) {
                filosofo.join(1000);
            }
            monitor.join(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.log("\n========================================");
        logger.log("RELATÓRIO FINAL");
        logger.log("========================================");
        int totalRefeicoes = 0;
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            int vezes = filosofos[i].getVezesQueComeu();
            totalRefeicoes += vezes;
            logger.log("Filósofo " + i + ": " + vezes + " refeições");
        }
        logger.log("Total de refeições: " + totalRefeicoes);
        logger.log("========================================");
        
        if (totalRefeicoes < 10) {
            logger.log("\n⚠️  DEADLOCK DETECTADO!");
            logger.log("O número muito baixo de refeições indica que houve deadlock.");
            logger.log("Os filósofos provavelmente ficaram bloqueados aguardando pelos garfos.");
        } else {
            logger.log("\nNenhum deadlock evidente foi detectado nesta execução.");
            logger.log("Execute novamente para aumentar a chance de observar o deadlock.");
        }
    }
}