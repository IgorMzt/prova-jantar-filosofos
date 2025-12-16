package scr.Tarefa3;

import java.util.concurrent.Semaphore;

public class JantarDosFilosofos {
    private static final int NUM_FILOSOFOS = 5;
    private static final int TEMPO_EXECUCAO_MS = 120000;
    private static final int MAX_FILOSOFOS_MESA = 4;
    
    public static void main(String[] args) {
        Logger logger = new Logger();
        
        logger.log("========================================");
        logger.log("JANTAR DOS FILÓSOFOS - Versão com SEMÁFOROS");
        logger.log("========================================");
        logger.log("Iniciando simulação com " + NUM_FILOSOFOS + " filósofos");
        logger.log("Tempo de execução: " + (TEMPO_EXECUCAO_MS / 1000) + " segundos");
        logger.log("Estratégia: Semáforo limitando a " + MAX_FILOSOFOS_MESA + " filósofos na mesa");
        logger.log("========================================\n");
        
        Semaphore semaforoMesa = new Semaphore(MAX_FILOSOFOS_MESA);
        logger.log("Semáforo criado: permite " + MAX_FILOSOFOS_MESA + " filósofos simultâneos\n");
        
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
            filosofos[i] = new Filosofo(i, garfoEsquerdo, garfoDireito, logger, semaforoMesa);
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
                    logger.log("Permissões disponíveis no semáforo: " + semaforoMesa.availablePermits());
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
        double desvioPadrao = calcularDesvioPadrao(filosofos, media);
        
        logger.log("========================================");
        logger.log("Total de refeições: " + totalRefeicoes);
        logger.log("Média por filósofo: " + String.format("%.2f", media));
        logger.log("Desvio padrão: " + String.format("%.2f", desvioPadrao));
        logger.log("Mínimo: " + minRefeicoes + " refeições");
        logger.log("Máximo: " + maxRefeicoes + " refeições");
        logger.log("Diferença (max-min): " + (maxRefeicoes - minRefeicoes));
        logger.log("Coeficiente de variação: " + String.format("%.2f%%", (desvioPadrao / media) * 100));
        logger.log("========================================");
        
        if (totalRefeicoes > 50) {
            logger.log("\n✅ SUCESSO - Sem Deadlock!");
            logger.log("O sistema funcionou continuamente durante " + (TEMPO_EXECUCAO_MS/1000) + " segundos.");
            logger.log("Total de " + totalRefeicoes + " refeições demonstra operação normal.");
        }
        
        if (maxRefeicoes - minRefeicoes > 20) {
            logger.log("\n⚠️  ALERTA DE STARVATION!");
            logger.log("Diferença significativa entre filósofos pode indicar starvation.");
        } else {
            logger.log("\n✅ Distribuição equilibrada de recursos.");
        }
        
        logger.log("\n📊 ANÁLISE DE PERFORMANCE:");
        logger.log("Throughput: " + String.format("%.2f", totalRefeicoes / (TEMPO_EXECUCAO_MS / 1000.0)) + " refeições/segundo");
        logger.log("Eficiência do semáforo: " + MAX_FILOSOFOS_MESA + " de " + NUM_FILOSOFOS + " filósofos simultâneos");
    }
    
    private static double calcularDesvioPadrao(Filosofo[] filosofos, double media) {
        double soma = 0;
        for (Filosofo filosofo : filosofos) {
            double diff = filosofo.getVezesQueComeu() - media;
            soma += diff * diff;
        }
        return Math.sqrt(soma / filosofos.length);
    }
}