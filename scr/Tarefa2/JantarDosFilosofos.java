package scr.Tarefa2;

public class JantarDosFilosofos {
    private static final int NUM_FILOSOFOS = 5;
    private static final int TEMPO_EXECUCAO_MS = 120000;
    
    public static void main(String[] args) {
        Logger logger = new Logger();
        
        logger.log("========================================");
        logger.log("JANTAR DOS FILÓSOFOS - Versão SEM Deadlock");
        logger.log("========================================");
        logger.log("Iniciando simulação com " + NUM_FILOSOFOS + " filósofos");
        logger.log("Tempo de execução: " + (TEMPO_EXECUCAO_MS / 1000) + " segundos");
        logger.log("Estratégia: Filósofo 4 pega garfos em ordem inversa");
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
                    Thread.sleep(10000);
                    contador += 10;
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
        logger.log("RELATÓRIO FINAL - ESTATÍSTICAS");
        logger.log("========================================");
        int totalRefeicoes = 0;
        int minRefeicoes = Integer.MAX_VALUE;
        int maxRefeicoes = 0;
        
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            int vezes = filosofos[i].getVezesQueComeu();
            totalRefeicoes += vezes;
            minRefeicoes = Math.min(minRefeicoes, vezes);
            maxRefeicoes = Math.max(maxRefeicoes, vezes);
            logger.log("Filósofo " + i + ": " + vezes + " refeições");
        }
        
        double media = (double) totalRefeicoes / NUM_FILOSOFOS;
        
        logger.log("========================================");
        logger.log("Total de refeições: " + totalRefeicoes);
        logger.log("Média por filósofo: " + String.format("%.2f", media));
        logger.log("Mínimo: " + minRefeicoes + " refeições");
        logger.log("Máximo: " + maxRefeicoes + " refeições");
        logger.log("Diferença (max-min): " + (maxRefeicoes - minRefeicoes));
        logger.log("========================================");
        
        if (totalRefeicoes > 50) {
            logger.log("\n✅ SUCESSO - Sem Deadlock!");
            logger.log("O sistema funcionou continuamente durante " + (TEMPO_EXECUCAO_MS/1000) + " segundos.");
            logger.log("Total de " + totalRefeicoes + " refeições demonstra operação normal.");
        }
        
        if (maxRefeicoes - minRefeicoes > 20) {
            logger.log("\n⚠️  ALERTA DE STARVATION!");
            logger.log("Diferença significativa entre filósofos pode indicar starvation.");
            logger.log("Filósofos com menos refeições podem estar sendo prejudicados.");
        } else {
            logger.log("\n✅ Distribuição equilibrada de recursos.");
        }
    }
}