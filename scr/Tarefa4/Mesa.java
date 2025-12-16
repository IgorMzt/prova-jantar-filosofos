package scr.Tarefa4;
import java.util.LinkedList;
import java.util.Queue;

public class Mesa {
    private final int numFilosofos;
    private final boolean[] garfosDisponiveis;
    private final Queue<Integer> filaEspera;
    private final long[] tempoUltimaRefeicao;
    private final Logger logger;
    
    public Mesa(int numFilosofos, Logger logger) {
        this.numFilosofos = numFilosofos;
        this.garfosDisponiveis = new boolean[numFilosofos];
        this.filaEspera = new LinkedList<>();
        this.tempoUltimaRefeicao = new long[numFilosofos];
        this.logger = logger;
        
        for (int i = 0; i < numFilosofos; i++) {
            garfosDisponiveis[i] = true;
            tempoUltimaRefeicao[i] = System.currentTimeMillis();
        }
    }
    
    public synchronized void pegarGarfos(int idFilosofo) throws InterruptedException {
        int garfoEsquerdo = idFilosofo;
        int garfoDireito = (idFilosofo + 1) % numFilosofos;
        
        filaEspera.add(idFilosofo);
        logger.log("Filósofo " + idFilosofo + " entrou na FILA DE ESPERA (posição: " + filaEspera.size() + ")");
        
        while (!podeEPrimeiroNaFila(idFilosofo, garfoEsquerdo, garfoDireito)) {
            logger.log("Filósofo " + idFilosofo + " está AGUARDANDO (garfos ou vez na fila)");
            wait();
        }
        
        filaEspera.remove(idFilosofo);
        garfosDisponiveis[garfoEsquerdo] = false;
        garfosDisponiveis[garfoDireito] = false;
        
        logger.log("Filósofo " + idFilosofo + " PEGOU garfos " + garfoEsquerdo + " e " + garfoDireito);
        logger.log("Filósofo " + idFilosofo + " saiu da fila. Fila restante: " + filaEspera.size() + " filósofos");
    }
    
    private boolean podeEPrimeiroNaFila(int idFilosofo, int garfoEsq, int garfoDir) {
        if (filaEspera.isEmpty() || filaEspera.peek() != idFilosofo) {
            return false;
        }
        
        return garfosDisponiveis[garfoEsq] && garfosDisponiveis[garfoDir];
    }
    
    public synchronized void soltarGarfos(int idFilosofo) {
        int garfoEsquerdo = idFilosofo;
        int garfoDireito = (idFilosofo + 1) % numFilosofos;
        
        garfosDisponiveis[garfoEsquerdo] = true;
        garfosDisponiveis[garfoDireito] = true;
        tempoUltimaRefeicao[idFilosofo] = System.currentTimeMillis();
        
        logger.log("Filósofo " + idFilosofo + " SOLTOU garfos " + garfoEsquerdo + " e " + garfoDireito);
        
        notifyAll();
    }
    
    public synchronized String getStatusGarfos() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numFilosofos; i++) {
            sb.append("Garfo ").append(i).append(": ")
              .append(garfosDisponiveis[i] ? "LIVRE" : "EM USO")
              .append(" | ");
        }
        return sb.toString();
    }
    
    public synchronized int getTamanhoFila() {
        return filaEspera.size();
    }
    
    public synchronized long[] getTemposUltimaRefeicao() {
        return tempoUltimaRefeicao.clone();
    }
    
    public synchronized String getFilaEspera() {
        if (filaEspera.isEmpty()) {
            return "vazia";
        }
        return filaEspera.toString();
    }
}