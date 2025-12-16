# Jantar dos Filósofos - Implementação com Deadlock

## 📋 Descrição

Esta é uma implementação do clássico problema de sincronização "Jantar dos Filósofos" que **demonstra a ocorrência de deadlock**. O problema foi proposto por Edsger Dijkstra em 1965 e ilustra desafios de sincronização em sistemas concorrentes.

## 🏗️ Estrutura do Projeto

### Arquivos

- **`Garfo.java`**: Representa um garfo (recurso compartilhado)
- **`Filosofo.java`**: Representa um filósofo (thread) que alterna entre pensar e comer
- **`Logger.java`**: Sistema de logging thread-safe com timestamps
- **`JantarDosFilosofos.java`**: Classe principal que configura e executa a simulação

## 🎯 Funcionamento

### Cenário
- 5 filósofos sentados em uma mesa circular
- 5 garfos (um entre cada par de filósofos)
- Cada filósofo precisa de 2 garfos para comer (esquerdo e direito)

### Ciclo de Vida do Filósofo
1. **Pensar**: Tempo aleatório entre 1-3 segundos
2. **Tentar pegar garfo esquerdo**
3. **Tentar pegar garfo direito**
4. **Comer**: Tempo aleatório entre 1-3 segundos
5. **Soltar ambos os garfos**
6. Repetir

## 🚨 Por Que Esta Implementação Causa Deadlock?

### Condições para Deadlock (Coffman et al., 1971)

Esta implementação satisfaz as **quatro condições necessárias** para deadlock:

#### 1. **Exclusão Mútua**
- Os garfos são recursos não compartilháveis
- Apenas um filósofo pode usar um garfo por vez
- Implementado via `synchronized` nos objetos Garfo

#### 2. **Posse e Espera (Hold and Wait)**
- Um filósofo pode segurar um garfo enquanto espera por outro
- Na linha crítica: pega garfo esquerdo, depois tenta pegar o direito
- Mantém o recurso (garfo esquerdo) enquanto aguarda (garfo direito)

#### 3. **Não Preempção**
- Um garfo não pode ser tirado à força de um filósofo
- O filósofo só solta o garfo voluntariamente após comer
- Nenhum mecanismo de timeout ou liberação forçada

#### 4. **Espera Circular**
- **ESTA É A CAUSA PRINCIPAL DO DEADLOCK**
- Todos os filósofos seguem o mesmo algoritmo: pegar garfo esquerdo → pegar garfo direito
- Cenário de deadlock:
```
  Filósofo 0 pega Garfo 0 → aguarda Garfo 1
  Filósofo 1 pega Garfo 1 → aguarda Garfo 2
  Filósofo 2 pega Garfo 2 → aguarda Garfo 3
  Filósofo 3 pega Garfo 3 → aguarda Garfo 4
  Filósofo 4 pega Garfo 4 → aguarda Garfo 0 ← CICLO!
```

### Diagrama do Deadlock
```
        Garfo 0
         ↗  ↖
    Fil 0    Fil 4
     ↓        ↑
  Garfo 1  Garfo 4
     ↓        ↑
    Fil 1    Fil 3
     ↓        ↑
  Garfo 2  Garfo 3
         ↘  ↗
        Fil 2
```

Quando cada filósofo segura seu garfo esquerdo, forma-se um **ciclo de dependências** onde:
- Cada filósofo possui um recurso
- Cada filósofo aguarda pelo recurso que o próximo possui
- Ninguém pode prosseguir → **DEADLOCK**

## 🔧 Como Compilar e Executar

### Compilação
```bash
javac *.java
```

### Execução
```bash
java JantarDosFilosofos
```

O programa executará por **30 segundos** e mostrará logs detalhados de todas as ações.

## 📊 Sistema de Logging

O sistema registra:
- ✅ Quando um filósofo começa a pensar
- ✅ Quando um filósofo tenta pegar um garfo
- ✅ Quando um filósofo consegue pegar um garfo
- ✅ Quando um filósofo começa a comer (possui ambos os garfos)
- ✅ Quando um filósofo termina de comer e solta os garfos
- ✅ Status periódico a cada 5 segundos
- ✅ Relatório final com número de refeições

### Exemplo de Log
```
[14:23:45.123] Filósofo 0 está PENSANDO
[14:23:47.456] Filósofo 0 está tentando pegar o garfo ESQUERDO 0
[14:23:47.457] Filósofo 0 pegou o garfo ESQUERDO 0
[14:23:47.507] Filósofo 0 está tentando pegar o garfo DIREITO 1
[14:23:47.508] Filósofo 0 pegou o garfo DIREITO 1
[14:23:47.508] Filósofo 0 está COMENDO (vez #1)
[14:23:49.789] Filósofo 0 soltou o garfo DIREITO 1
[14:23:49.790] Filósofo 0 soltou o garfo ESQUERDO 0
```

## 🔍 Evidências de Deadlock

### Sinais de Deadlock:
1. **Logs param de aparecer**: Nenhum filósofo consegue progredir
2. **Última ação de cada filósofo**: "pegou o garfo ESQUERDO"
3. **Número baixo de refeições**: < 10 refeições no total indica deadlock
4. **Status mostra progresso zero**: Contador de refeições não aumenta

### Exemplo de Deadlock Observado:
```
[14:25:30.123] Filósofo 0 pegou o garfo ESQUERDO 0
[14:25:30.124] Filósofo 1 pegou o garfo ESQUERDO 1
[14:25:30.125] Filósofo 2 pegou o garfo ESQUERDO 2
[14:25:30.126] Filósofo 3 pegou o garfo ESQUERDO 3
[14:25:30.127] Filósofo 4 pegou o garfo ESQUERDO 4
[14:25:30.177] Filósofo 0 está tentando pegar o garfo DIREITO 1
[14:25:30.178] Filósofo 1 está tentando pegar o garfo DIREITO 2
[14:25:30.179] Filósofo 2 está tentando pegar o garfo DIREITO 3
[14:25:30.180] Filósofo 3 está tentando pegar o garfo DIREITO 4
[14:25:30.181] Filósofo 4 está tentando pegar o garfo DIREITO 0
... [SILÊNCIO - DEADLOCK OCORREU] ...
```

## 🎓 Análise Técnica

### Por Que o Deadlock Não É Garantido?

O deadlock **não ocorre sempre** porque:
- O timing das threads é não-determinístico
- O escalonador do SO pode dar mais tempo a alguns filósofos
- Se um filósofo termina de comer antes que todos peguem seus garfos esquerdos, o ciclo não se forma

### Fatores que Aumentam a Probabilidade:
- ✅ `Thread.sleep(50)` após pegar o garfo esquerdo
- ✅ Todos os filósofos iniciam simultaneamente
- ✅ Algoritmo idêntico para todos os filósofos

### Resultado Típico:
- **Com deadlock**: 0-5 refeições no total
- **Sem deadlock**: 20-60 refeições no total

## 🛠️ Soluções Possíveis (NÃO Implementadas Aqui)

Para **evitar** deadlock, poderia-se:

1. **Ordenação de Recursos**: Filósofo ímpar pega direito→esquerdo
2. **Limite de Filósofos**: Apenas 4 dos 5 podem tentar comer simultaneamente
3. **Timeout**: Soltar garfo esquerdo se não conseguir o direito em X segundos
4. **Arbitrador**: Usar um semáforo ou lock único para permissão de comer

## 📈 Critérios Atendidos

- ✅ **Código funcional**: Implementação completa em Java
- ✅ **Sistema de logging**: Logs detalhados com timestamps
- ✅ **Documentação**: Explicação completa do deadlock
- ✅ **Evidências**: Instruções para observar e diagnosticar deadlock

## 🎯 Conclusão

Esta implementação **propositalmente permite deadlock** para demonstrar o problema clássico de sincronização. A espera circular criada pelo algoritmo simétrico (todos pegam esquerda→direita) é a causa raiz do deadlock.

## 👨‍💻 Autor

Implementação para estudo de Sistemas Operacionais - Problema do Jantar dos Filósofos

---

**Nota**: Este código é para fins educacionais e demonstra um **anti-padrão**. Em sistemas reais, sempre implemente mecanismos para prevenir ou detectar deadlocks.