package scr.Tarefa4;

public class JantarDosFilosofos {
    private static final int NUM_FILOSOFOS = 5;
    private static final int TEMPO_EXECUCAO_MS = 120000;
    
    public static void main(String[] args) {
        Logger logger = new Logger();
        
        logger.log("========================================");
        logger.log("JANTAR DOS FILÓSOFOS - Versão com MONITOR e FAIRNESS");
        logger.log("========================================");
        logger.log("Iniciando simulação com " + NUM_FILOSOFOS + " filósofos");
        logger.log("Tempo de execução: " + (TEMPO_EXECUCAO_MS / 1000) + " segundos");
        logger.log("Estratégia: Monitor centralizado (Mesa) com fila FIFO");
        logger.log("========================================\n");
        
        Mesa mesa = new Mesa(NUM_FILOSOFOS, logger);
        logger.log("Monitor Mesa criado com " + NUM_FILOSOFOS + " garfos\n");
        
        Filosofo[] filosofos = new Filosofo[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            filosofos[i] = new Filosofo(i, mesa, logger);
            logger.log("Filósofo " + i + " criado");
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
                    
                    logger.log("\n========================================");
                    logger.log("STATUS (" + contador + "s)");
                    logger.log("========================================");
                    logger.log("Fila de espera: " + mesa.getFilaEspera());
                    logger.log("Tamanho da fila: " + mesa.getTamanhoFila() + " filósofos");
                    logger.log(mesa.getStatusGarfos());
                    
                    long[] tempos = mesa.getTemposUltimaRefeicao();
                    long agora = System.currentTimeMillis();
                    logger.log("\nTempo desde última refeição:");
                    for (int i = 0; i < NUM_FILOSOFOS; i++) {
                        long segundos = (agora - tempos[i]) / 1000;
                        logger.log("  Filósofo " + i + ": " + segundos + "s atrás (comeu " + 
                                 filosofos[i].getVezesQueComeu() + " vezes)");
                    }
                    logger.log("========================================\n");
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
        logger.log("RELATÓRIO FINAL - ESTATÍSTICAS COMPLETAS");
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
        double coeficienteVariacao = (desvioPadrao / media) * 100;
        
        logger.log("========================================");
        logger.log("MÉTRICAS GERAIS:");
        logger.log("  Total de refeições: " + totalRefeicoes);
        logger.log("  Média por filósofo: " + String.format("%.2f", media));
        logger.log("  Desvio padrão: " + String.format("%.2f", desvioPadrao));
        logger.log("  Mínimo: " + minRefeicoes + " refeições");
        logger.log("  Máximo: " + maxRefeicoes + " refeições");
        logger.log("  Diferença (max-min): " + (maxRefeicoes - minRefeicoes));
        logger.log("  Coeficiente de variação: " + String.format("%.2f%%", coeficienteVariacao));
        
        double throughput = totalRefeicoes / (TEMPO_EXECUCAO_MS / 1000.0);
        logger.log("\nMÉTRICAS DE PERFORMANCE:");
        logger.log("  Throughput: " + String.format("%.2f", throughput) + " refeições/segundo");
        logger.log("  Tempo médio por refeição: " + String.format("%.2f", TEMPO_EXECUCAO_MS / 1000.0 / media) + " segundos");
        
        logger.log("\nMÉTRICAS DE FAIRNESS:");
        double fairnessIndex = calcularFairnessIndex(filosofos);
        logger.log("  Jain's Fairness Index: " + String.format("%.4f", fairnessIndex));
        logger.log("  Interpretação: " + interpretarFairness(fairnessIndex));
        logger.log("========================================");
        
        if (totalRefeicoes > 50) {
            logger.log("\n✅ SUCESSO - Sistema Operacional!");
            logger.log("Funcionamento contínuo por " + (TEMPO_EXECUCAO_MS/1000) + " segundos.");
        }
        
        if (coeficienteVariacao < 10) {
            logger.log("\n✅ EXCELENTE FAIRNESS!");
            logger.log("Distribuição muito equilibrada entre filósofos.");
            logger.log("Coeficiente de variação < 10% indica alta equidade.");
        } else if (coeficienteVariacao < 20) {
            logger.log("\n✅ BOA FAIRNESS");
            logger.log("Distribuição razoavelmente equilibrada.");
        } else {
            logger.log("\n⚠️  FAIRNESS PODE SER MELHORADA");
            logger.log("Variação significativa detectada.");
        }
        
        if (maxRefeicoes - minRefeicoes <= 5) {
            logger.log("\n🌟 STARVATION PREVENIDA COM SUCESSO!");
            logger.log("Diferença mínima entre filósofos (≤5) demonstra excelente fairness.");
        } else if (maxRefeicoes - minRefeicoes <= 10) {
            logger.log("\n✅ Starvation evitada!");
            logger.log("Diferença aceitável entre filósofos.");
        }
        
        logger.log("\n📊 COMPARAÇÃO COM TAREFAS ANTERIORES:");
        logger.log("Ver README.md para análise detalhada de:");
        logger.log("  - Tarefa 1: Deadlock (baseline)");
        logger.log("  - Tarefa 2: Ordem invertida (~20% mais rápido, sem fairness)");
        logger.log("  - Tarefa 3: Semáforo (~15% mais rápido, fairness média)");
        logger.log("  - Tarefa 4: Monitor (esta) - melhor fairness, throughput moderado");
    }
    
    private static double calcularDesvioPadrao(Filosofo[] filosofos, double media) {
        double soma = 0;
        for (Filosofo filosofo : filosofos) {
            double diff = filosofo.getVezesQueComeu() - media;
            soma += diff * diff;
        }
        return Math.sqrt(soma / filosofos.length);
    }
    
    private static double calcularFairnessIndex(Filosofo[] filosofos) {
        double soma = 0;
        double somaQuadrados = 0;
        
        for (Filosofo filosofo : filosofos) {
            int vezes = filosofo.getVezesQueComeu();
            soma += vezes;
            somaQuadrados += vezes * vezes;
        }
        
        int n = filosofos.length;
        return (soma * soma) / (n * somaQuadrados);
    }
    
    private static String interpretarFairness(double index) {
        if (index >= 0.95) return "EXCELENTE (≥0.95) - distribuição quase perfeita";
        if (index >= 0.90) return "MUITO BOM (0.90-0.95) - distribuição equilibrada";
        if (index >= 0.80) return "BOM (0.80-0.90) - fairness aceitável";
        if (index >= 0.70) return "RAZOÁVEL (0.70-0.80) - alguma desigualdade";
        return "RUIM (<0.70) - distribuição desigual";
    }
}