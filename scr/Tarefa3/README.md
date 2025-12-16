# Jantar dos Filósofos - Solução com SEMÁFOROS (Tarefa 3)

## 📋 Descrição

Implementação do problema do Jantar dos Filósofos usando **Semaphore do Java** para limitar o número de filósofos que podem tentar pegar garfos simultaneamente, prevenindo deadlock de forma elegante.

## 🆕 Nova Abordagem: Semáforo como Controlador de Mesa

### Conceito

Em vez de modificar a ordem de aquisição dos garfos (Tarefa 2), esta solução usa um **semáforo contador** que:
- Permite no **máximo 4 filósofos** tentarem comer simultaneamente
- O 5º filósofo deve aguardar que alguém termine
- Garante que sempre existe pelo menos um garfo disponível

### Metáfora

Imagine a mesa do jantar com apenas **4 cadeiras disponíveis**:
- 5 filósofos querem jantar
- Apenas 4 podem sentar de cada vez
- O 5º deve aguardar uma cadeira vagar
- Com 4 filósofos e 5 garfos, **sempre sobra pelo menos 1 garfo livre**

## 🔧 Modificações Implementadas

### Arquivo: Filosofo.java

**Adicionado:**

1. **Campo Semaphore**: Referência ao semáforo compartilhado
```java
   private final Semaphore semaforoMesa;
```

2. **Método comer() modificado**:
```java
   private void comer() throws InterruptedException {
       semaforoMesa.acquire();  // Pede permissão para entrar na mesa
       try {
           // Pega garfos, come, solta garfos
       } finally {
           semaforoMesa.release();  // Libera lugar na mesa
       }
   }
```

### Arquivo: JantarDosFilosofos.java

**Adicionado:**

1. **Criação do Semáforo**:
```java
   Semaphore semaforoMesa = new Semaphore(4);  // 4 permissões
```

2. **Passagem do Semáforo**:
```java
   filosofos[i] = new Filosofo(i, garfoEsq, garfoDir, logger, semaforoMesa);
```

3. **Estatísticas Avançadas**:
   - Desvio padrão
   - Coeficiente de variação
   - Throughput (refeições/segundo)
   - Monitoramento de permissões disponíveis

## 🎯 Como a Solução Funciona

### Fluxo de Execução

1. **Filósofo quer comer**:
```
   Filosofo 0: "Quero comer!"
   Semáforo: "Há vaga? Sim! Permissões: 4→3"
   Filosofo 0: Entra na mesa
```

2. **Com 4 filósofos na mesa**:
```
   Filosofos 0,1,2,3: Na mesa (pegando garfos)
   Semáforo: Permissões = 0
   Filosofo 4: "Quero comer!"
   Semáforo: "AGUARDE! Mesa cheia"
   Filosofo 4: Bloqueado esperando
```

3. **Alguém termina**:
```
   Filosofo 2: Terminou! Libera semáforo
   Semáforo: Permissões: 0→1
   Filosofo 4: Desbloqueado! Entra na mesa
```

### Diagrama de Estados
```
┌─────────────┐
│  PENSANDO   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ AGUARDANDO  │◄──── Semáforo cheio (4 filósofos na mesa)
│  SEMÁFORO   │
└──────┬──────┘
       │ acquire() bem-sucedido
       ▼
┌─────────────┐
│  NA MESA    │
│(pegando     │
│ garfos)     │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   COMENDO   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  LIBERANDO  │
│  SEMÁFORO   │◄──── release()
└──────┬──────┘
       │
       └────► Volta para PENSANDO
```

## 🚨 Por Que Esta Solução Previne Deadlock?

### Análise das 4 Condições de Coffman

1. **Exclusão Mútua**: ✅ Mantém (garfos são exclusivos)
2. **Posse e Espera**: ✅ Mantém (segura garfo enquanto espera outro)
3. **Não Preempção**: ✅ Mantém (garfos não são tomados à força)
4. **Espera Circular**: ❌ **QUEBRADA PELO SEMÁFORO!**

### Como o Semáforo Quebra a Espera Circular

**Cenário sem semáforo (Tarefa 1):**
```
5 filósofos tentam comer → 5 pegam garfo esquerdo → DEADLOCK
```

**Cenário com semáforo (Tarefa 3):**
```
4 filósofos na mesa → máximo 4 garfos segurados
5 garfos disponíveis → SEMPRE sobra 1 garfo livre!
```

### Prova Matemática

- **N** = 5 filósofos
- **M** = 5 garfos  
- **K** = 4 filósofos permitidos simultaneamente

**Pior caso**: 4 filósofos pegam seus garfos esquerdos
- Garfos segurados: 4
- Garfos livres: 5 - 4 = **1 garfo livre**
- Pelo menos 1 dos 4 filósofos conseguirá pegar seu segundo garfo
- Esse filósofo come e libera 2 garfos
- **Não pode formar ciclo completo!**

**Fórmula geral**: Para N filósofos e N garfos, permitir (N-1) simultaneamente garante ausência de deadlock.

### Comparação Visual

**Tarefa 1 (Deadlock):**
```
Fil 0: [Garfo 0] → aguarda Garfo 1
Fil 1: [Garfo 1] → aguarda Garfo 2
Fil 2: [Garfo 2] → aguarda Garfo 3
Fil 3: [Garfo 3] → aguarda Garfo 4
Fil 4: [Garfo 4] → aguarda Garfo 0  ← CICLO!
```

**Tarefa 3 (Sem Deadlock):**
```
Fil 0: [Garfo 0] → aguarda Garfo 1
Fil 1: [Garfo 1] → aguarda Garfo 2
Fil 2: [Garfo 2] → aguarda Garfo 3
Fil 3: [Garfo 3] → aguarda Garfo 4
Fil 4: BLOQUEADO pelo semáforo (não segura nada)
Garfo 4: LIVRE! ← Fil 3 pode pegar e comer
```

## 📊 Comparação com Tarefa 2

### Tabela Comparativa

| Aspecto | Tarefa 2 (Ordem Invertida) | Tarefa 3 (Semáforo) |
|---------|---------------------------|---------------------|
| **Mecanismo** | Ordem assimétrica | Limite de concorrência |
| **Modificação** | 1 filósofo diferente | Todos usam semáforo |
| **Complexidade** | Baixa | Média |
| **Elegância** | Menos elegante | Mais elegante |
| **Simetria** | Assimétrica | Simétrica |
| **Overhead** | Mínimo | Baixo (acquire/release) |
| **Throughput** | Alto (~60 ref/fil) | Médio (~50 ref/fil) |
| **Equidade** | Depende do SO | Melhor (FIFO no semáforo) |
| **Escalabilidade** | Ruim (difícil generalizar) | Boa (fórmula N-1) |
| **Starvation** | Possível | Possível (mas menos provável) |

### Resultados Típicos (120 segundos)

**Tarefa 2:**
```
Total: 294 refeições
Média: 58.8 ref/filósofo
Diferença: 7
Throughput: 2.45 ref/s
```

**Tarefa 3:**
```
Total: 245 refeições
Média: 49.0 ref/filósofo
Desvio padrão: 2.3
Diferença: 5
Throughput: 2.04 ref/s
Coeficiente variação: 4.7%
```

### Análise de Performance

**Tarefa 2 é ~20% mais rápida** porque:
- Sem overhead do semáforo
- 5 filósofos podem tentar simultaneamente
- Apenas 1 filósofo tem ordem diferente

**Tarefa 3 tem throughput menor** porque:
- Apenas 4 filósofos ativos por vez
- Overhead de acquire/release
- Serialização adicional

**MAS Tarefa 3 é mais equilibrada**:
- Menor desvio padrão
- Coeficiente de variação menor
- Distribuição mais justa

## ✅ Vantagens da Abordagem com Semáforos

### 1. **Elegância e Clareza**
- Solução conceitualmente simples
- Fácil de entender: "4 cadeiras para 5 pessoas"
- Código mais legível

### 2. **Simetria**
- Todos os filósofos têm código idêntico
- Não há casos especiais
- Filosoficamente mais justo

### 3. **Escalabilidade**
- Fórmula geral: `Semaphore(N-1)` para N filósofos
- Fácil adaptar para 10, 20, 100 filósofos
- Não precisa modificar lógica individual

### 4. **Controle Fino**
- Pode ajustar o limite facilmente: `Semaphore(3)` ou `Semaphore(2)`
- Trade-off configurável entre throughput e garantias

### 5. **Melhor Equidade (potencial)**
- `Semaphore` pode ser criado com fairness: `new Semaphore(4, true)`
- Garante FIFO na fila de espera
- Reduz chance de starvation

### 6. **Extensível**
- Fácil adicionar outras políticas
- Pode combinar com outras técnicas
- Base para algoritmos mais complexos

## ❌ Desvantagens da Abordagem com Semáforos

### 1. **Throughput Reduzido**
- Apenas N-1 filósofos ativos
- ~20% menos refeições que Tarefa 2
- Serialização artificial

### 2. **Overhead de Sincronização**
- Custo de acquire/release
- Gerenciamento interno do semáforo
- Context switches adicionais

### 3. **Subutilização de Recursos**
- Sempre 1 filósofo bloqueado
- Mesmo que recursos estejam livres
- CPU ociosa desnecessariamente

### 4. **Não Elimina Starvation**
- Mesmo com fairness=true
- Ainda possível (embora menos provável)
- Precisa mecanismos adicionais

### 5. **Dependência de Biblioteca**
- Requer `java.util.concurrent`
- Mais complexo que synchronized puro
- Curva de aprendizado

### 6. **Granularidade Grossa**
- Bloqueia entrada na mesa inteira
- Não distingue quais garfos estão livres
- Menos otimizado que soluções específicas

## 🎓 Análise Crítica Geral

### Quando Usar Cada Solução?

**Use Tarefa 2 (Ordem Invertida) quando:**
- ✅ Performance máxima é crítica
- ✅ Sistema simples (poucos filósofos)
- ✅ Starvation é aceitável
- ✅ Simplicidade de código é prioridade

**Use Tarefa 3 (Semáforo) quando:**
- ✅ Escalabilidade é importante
- ✅ Equidade é desejada
- ✅ Código deve ser simétrico/elegante
- ✅ Fácil manutenção é prioridade
- ✅ Sistema pode crescer no futuro

### Trade-offs Fundamentais
```
Performance ←→ Equidade
Simplicidade ←→ Elegância
Throughput ←→ Justiça
```

A Tarefa 3 sacrifica um pouco de performance em troca de:
- Melhor design
- Maior escalabilidade
- Potencial para equidade

### Melhorias Possíveis

Para sistema de produção, considere:

1. **Semáforo com Fairness**:
```java
   new Semaphore(4, true);  // FIFO
```

2. **Combinação de Técnicas**:
   - Semáforo + Ordem invertida
   - Melhor performance + Garantias

3. **Timeout**:
```java
   if (!semaforo.tryAcquire(5, TimeUnit.SECONDS)) {
       // Desiste e tenta depois
   }
```

4. **Prioridades**:
   - Filósofos que comeram menos têm prioridade
   - Previne starvation ativamente

## 🔧 Como Executar

### Compilação
```bash
javac *.java
```

### Execução
```bash
java JantarDosFilosofos
```

Observe nos logs:
- "ENTROU NA MESA" quando acquire() sucede
- "AGUARDANDO semáforo" quando bloqueado
- "SAIU DA MESA" quando release() é chamado
- Permissões disponíveis no status periódico

## 📈 Estatísticas Avançadas

Esta implementação calcula:

1. **Desvio Padrão**: Dispersão dos dados
2. **Coeficiente de Variação**: (σ/μ) × 100%
   - < 10%: Distribuição muito uniforme
   - 10-20%: Razoável
   - > 20%: Alta variabilidade

3. **Throughput**: Refeições por segundo
4. **Eficiência**: Uso do semáforo

## 🎯 Conclusão

A solução com semáforos demonstra um princípio importante de sistemas concorrentes:

> **Limitar concorrência pode prevenir problemas de sincronização**

Ao forçar N-1 participantes ativos, garantimos que sempre existe recurso livre, eliminando a possibilidade de espera circular.

É uma solução **mais elegante** que a Tarefa 2, embora com **custo de performance**. A escolha entre ambas depende dos requisitos específicos do sistema.

### Lições Aprendidas

1. **Deadlock ≠ Problema de performance**: Às vezes menos concorrência é melhor
2. **Elegância tem custo**: Soluções bonitas podem ser mais lentas
3. **Semáforos são poderosos**: Ferramenta versátil para sincronização
4. **Trade-offs são inevitáveis**: Não existe solução perfeita

---

**Implementação**: Tarefa 3 - Solução com Semáforos
**Mecanismo**: Semaphore(4) limitando acesso à mesa
**Resultado**: Deadlock impossível, throughput reduzido, distribuição mais equilibrada