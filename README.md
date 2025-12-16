# Jantar dos Filósofos — Estudo Completo sobre Deadlock, Starvation e Fairness

## 📌 Visão Geral

Este projeto reúne **quatro implementações do clássico problema do Jantar dos Filósofos**, proposto por Edsger Dijkstra (1965), com o objetivo de **demonstrar, comparar e analisar** diferentes estratégias de sincronização em sistemas concorrentes.

As implementações evoluem progressivamente:

1. Solução ingênua (com deadlock)
2. Prevenção de deadlock por ordem assimétrica
3. Prevenção de deadlock com semáforos
4. Prevenção de deadlock **e starvation**, com garantia de fairness via monitores

O foco é **didático e acadêmico**, explorando trade-offs entre simplicidade, performance, escalabilidade e justiça.

---

## 🧠 Conceitos Trabalhados

* Programação concorrente em Java
* Threads e sincronização (`synchronized`)
* Deadlock (Condições de Coffman)
* Starvation
* Fairness (justiça)
* Semáforos (`Semaphore`)
* Monitores (`wait()` / `notifyAll()`)
* Análise estatística de concorrência

---

## 🗂️ Estrutura Geral do Projeto

* **Garfo.java** — Recurso compartilhado
* **Filosofo.java** — Thread que alterna entre pensar e comer
* **Logger.java** — Logging thread-safe com timestamps
* **JantarDosFilosofos.java** — Execução e relatórios (Tarefas 1–3)
* **Mesa.java** — Monitor centralizado (Tarefa 4)

---

## 🔴 Tarefa 1 — Implementação COM Deadlock (Solução Ingênua)

### Estratégia

Todos os filósofos seguem o mesmo algoritmo:

> pegar garfo esquerdo → pegar garfo direito

### Por que ocorre deadlock?

A implementação satisfaz **todas as 4 condições necessárias de Coffman**:

1. **Exclusão Mútua** — Garfos são exclusivos
2. **Posse e Espera** — Filósofo segura um garfo enquanto espera outro
3. **Não Preempção** — Garfos não podem ser retirados à força
4. **Espera Circular** — Forma-se um ciclo fechado entre os filósofos

### Resultado

* Sistema pode travar completamente
* Demonstra claramente o problema de deadlock

---

## 🟡 Tarefa 2 — Solução SEM Deadlock (Ordem Invertida)

### Estratégia

* O **Filósofo 4** pega os garfos em ordem inversa (direito → esquerdo)
* Os demais mantêm a ordem normal

### Princípio

🔑 Quebra da **espera circular**, eliminando o deadlock.

### Observação

* Deadlock eliminado
* **Starvation ainda é possível**, pois não há garantia de justiça

---

## 🟢 Tarefa 3 — Solução SEM Deadlock com Semáforos

### Estratégia

Uso de um **Semaphore(N-1)** para limitar quantos filósofos podem tentar comer simultaneamente.

* Para 5 filósofos: `Semaphore(4)`

### Por que funciona?

Sempre sobra pelo menos **1 garfo livre**, tornando a espera circular impossível.

### Vantagens

* Solução elegante e simétrica
* Escalável

### Limitações

* Throughput menor
* Starvation ainda pode ocorrer

---

## 🔵 Tarefa 4 — Solução com Monitores e Fairness Garantida

### Estratégia

Implementação de um **monitor centralizado (Mesa)** que:

* Controla todos os garfos
* Usa **fila FIFO**
* Permite apenas aquisição **atômica** dos dois garfos

### Garantias

* ❌ Deadlock: impossível
* ❌ Starvation: impossível
* ✅ Fairness: garantida

### Trade-offs

* Código mais complexo
* Overhead maior de sincronização

---

## 📊 Comparação Geral

| Aspecto      | Tarefa 1   | Tarefa 2     | Tarefa 3      | Tarefa 4     |
| ------------ | ---------- | ------------ | ------------- | ------------ |
| Deadlock     | ❌ Possível | ✅ Não ocorre | ✅ Não ocorre  | ✅ Não ocorre |
| Starvation   | ❌ Possível | ⚠️ Possível  | ⚠️ Improvável | ✅ Impossível |
| Fairness     | ❌ Nenhuma  | ❌ Nenhuma    | ⚠️ Parcial    | ✅ Garantida  |
| Performance  | 🟢 Alta    | 🟢 Alta      | 🟡 Média      | 🟡 Média     |
| Complexidade | 🟢 Baixa   | 🟡 Baixa     | 🟡 Média      | 🔴 Alta      |

---

## 🚀 Como Executar

### Compilação

```bash
javac *.java
```

### Execução (Tarefas 1–4)

```bash
java JantarDosFilosofos
```
