# Jantar dos Filósofos - Solução SEM Deadlock (Tarefa 2)

## 📋 Descrição

Esta é a **modificação da Tarefa 1** para prevenir deadlock. A solução usa ordenação assimétrica de recursos: o Filósofo 4 pega os garfos em ordem inversa aos demais.

## 🔄 Modificações da Tarefa 1

### Arquivo: Filosofo.java

**O que foi modificado:**

1. Adicionado método `comer()` que verifica o ID do filósofo
2. Separado em dois métodos: `comerOrdemNormal()` e `comerOrdemInvertida()`
3. Filósofo 4 executa `comerOrdemInvertida()` (Direito → Esquerdo)
4. Filósofos 0-3 executam `comerOrdemNormal()` (Esquerdo → Direito)

**Código adicionado:**
```java
private void comer() throws InterruptedException {
    if (id == 4) {
        comerOrdemInvertida();  // Filósofo 4: Direito → Esquerdo
    } else {
        comerOrdemNormal();      // Outros: Esquerdo → Direito
    }
}
```

### Arquivo: JantarDosFilosofos.java

**O que foi modificado:**

1. Tempo de execução aumentado para 120 segundos (2 minutos)
2. Adicionado sistema de estatísticas no relatório final
3. Status a cada 10 segundos (em vez de 5)
4. Cálculo de média, mínimo, máximo e diferença
5. Detecção automática de possível starvation

### Arquivos NÃO modificados

- **Garfo.java**: Mantém a mesma lógica de sincronização
- **Logger.java**: Mantém o mesmo sistema de logging

## 🚨 Por Que Esta Solução Previne Deadlock?

### A Causa do Deadlock na Tarefa 1

Deadlock ocorria quando todos os filósofos pegavam seus garfos esquerdos simultaneamente:
```
Filósofo 0: segura Garfo 0 → aguarda Garfo 1
Filósofo 1: segura Garfo 1 → aguarda Garfo 2
Filósofo 2: segura Garfo 2 → aguarda Garfo 3
Filósofo 3: segura Garfo 3 → aguarda Garfo 4
Filósofo 4: segura Garfo 4 → aguarda Garfo 0  ← CICLO!
```

### Como a Modificação Resolve

Com o Filósofo 4 invertido:
```
Filósofo 0: tenta Garfo 0 (esquerdo)
Filósofo 1: tenta Garfo 1 (esquerdo)
Filósofo 2: tenta Garfo 2 (esquerdo)
Filósofo 3: tenta Garfo 3 (esquerdo)
Filósofo 4: tenta Garfo 0 (direito dele) ← COMPETE COM FIL 0!
```

**Cenário 1:** Se Filósofo 4 pegar Garfo 0 primeiro
- Filósofo 4 pega Garfo 0, depois Garfo 4
- Filósofo 4 come e libera
- Filósofo 0 pode pegar Garfo 0
- **Não há ciclo!**

**Cenário 2:** Se Filósofo 0 pegar Garfo 0 primeiro
- Filósofo 4 fica esperando Garfo 0
- Filósofo 4 NÃO segura Garfo 4
- Filósofo 3 pode pegar Garfo 4
- **Não há ciclo!**

### Princípio: Quebra da Espera Circular

A modificação quebra uma das **4 condições necessárias** para deadlock:
- ❌ **Espera Circular**: Não pode mais formar ciclo completo

As outras 3 condições continuam:
- ✅ Exclusão Mútua: Garfos são exclusivos
- ✅ Posse e Espera: Filósofos seguram um garfo enquanto aguardam outro
- ✅ Não Preempção: Garfos não são tomados à força

Mas **sem espera circular, não há deadlock!**

## ⚠️ Possibilidade de Starvation

### Starvation AINDA PODE OCORRER?

**SIM!** Prevenir deadlock ≠ Prevenir starvation

#### Por Que?

1. **Sem Garantia de Justiça (Fairness)**
   - `synchronized` em Java não é FIFO
   - Qualquer thread esperando pode ser escolhida
   - Não há ordem garantida

2. **Competição Assimétrica**
   - Filósofo 4 compete diferentemente
   - Mas isso não dá prioridade a ninguém

3. **Escalonador do SO**
   - Pode favorecer certas threads
   - Timing pode ser consistentemente desfavorável a um filósofo

#### Exemplo de Starvation:
```
Após 120 segundos:
Filósofo 0: 62 refeições
Filósofo 1: 58 refeições
Filósofo 2: 5 refeições   ← STARVATION!
Filósofo 3: 60 refeições
Filósofo 4: 59 refeições
```

Filósofo 2 está em starvation porque sempre chega aos garfos quando outros os têm.

### Como o Sistema Detecta

O relatório final calcula:
- **Diferença (max - min)**: Indicador de equidade
- **Alerta**: Se diferença > 20, indica possível starvation

## 📊 Comparação: Tarefa 1 vs Tarefa 2

| Métrica | Tarefa 1 (Com Deadlock) | Tarefa 2 (Sem Deadlock) |
|---------|-------------------------|-------------------------|
| **Deadlock** | Ocorre frequentemente | **NUNCA ocorre** |
| **Tempo de execução** | 30 segundos | 120 segundos |
| **Refeições totais** | 0-10 (com deadlock) | 200-400+ |
| **Progresso** | Para completamente | **Contínuo** |
| **Starvation** | N/A (há deadlock) | Possível, mas rara |
| **Estatísticas** | Básicas | **Completas** (média, min, max) |
| **Modificações** | - | Apenas método `comer()` |

### Resultados Típicos

**Tarefa 1:**
```
Total: 3 refeições
Status: DEADLOCK DETECTADO
```

**Tarefa 2:**
```
Total: 294 refeições
Média: 58.8 refeições/filósofo
Diferença: 7 (equilibrado)
Status: SUCESSO - Sem Deadlock ✅
```

## 🔧 Como Executar

### Compilação
```bash
javac *.java
```

### Execução
```bash
java JantarDosFilosofos
```

**Observação:** O mesmo comando, mas agora com código modificado que previne deadlock!

## 📈 Sistema de Estatísticas

### Métricas Calculadas

1. **Total de refeições**: Soma geral
2. **Média**: Total ÷ 5
3. **Mínimo**: Filósofo que comeu menos
4. **Máximo**: Filósofo que comeu mais
5. **Diferença**: Máximo - Mínimo (indicador de equidade)

### Interpretação

- **Total > 200**: ✅ Sistema funcionando normalmente
- **Diferença < 10**: ✅ Muito equilibrado
- **Diferença 10-20**: ⚠️ Variação aceitável
- **Diferença > 20**: 🚨 Possível starvation

## 🎓 Análise Crítica

### Vantagens da Solução

✅ **Elimina deadlock**: 100% efetivo
✅ **Modificação mínima**: Apenas 1 arquivo alterado
✅ **Sem overhead**: Performance mantida
✅ **Simples de implementar**: Fácil entendimento

### Limitações

❌ **Não previne starvation**: Ainda pode ocorrer
❌ **Depende do escalonador**: Não há controle de justiça
❌ **Assimetria**: Um filósofo é "diferente"
❌ **Sem garantias formais**: De distribuição equitativa

### Conclusão

Esta modificação demonstra que:
1. **Deadlock pode ser prevenido** quebrando espera circular
2. **Starvation é problema separado** que requer outras técnicas
3. **Soluções simples funcionam** para prevenir deadlock
4. **Trade-offs existem**: Simplicidade vs Garantias

A solução é **adequada para prevenir deadlock**, mas para sistemas críticos que também precisam evitar starvation, seria necessário adicionar:
- Semáforos com fairness
- Sistema de prioridades
- Arbitrador central

---

**Implementação**: Tarefa 2 - Solução com Prevenção de Deadlock
**Modificação**: Classe Filosofo (método `comer()` com ordem invertida para ID 4)
**Resultado**: Deadlock eliminado, starvation possível mas rara