## 📋 Descrição

Implementação completa e análise comparativa de **4 abordagens diferentes** para resolver o clássico problema de sincronização "Jantar dos Filósofos", incluindo:

1. **Tarefa 1**: Implementação básica com deadlock (demonstração do problema)
2. **Tarefa 2**: Solução com ordem invertida (prevenção de deadlock)
3. **Tarefa 3**: Solução com semáforos (controle de concorrência)
4. **Tarefa 4**: Solução com monitores e fairness garantida
5. **Tarefa 5**: Análise comparativa completa (este documento)

---

## 🏗️ Estrutura do Projeto
```
jantar-dos-filosofos/
│
├── tarefa1/                    # Implementação com deadlock
│   ├── Garfo.java
│   ├── Filosofo.java
│   ├── Logger.java
│   ├── JantarDosFilosofos.java
│   └── README.md
│
├── tarefa2/                    # Ordem invertida
│   ├── Garfo.java
│   ├── Filosofo.java
│   ├── Logger.java
│   ├── JantarDosFilosofos.java
│   └── README.md
│
├── tarefa3/                    # Semáforos
│   ├── Garfo.java
│   ├── Filosofo.java
│   ├── Logger.java
│   ├── JantarDosFilosofos.java
│   └── README.md
│
├── tarefa4/                    # Monitor com fairness
│   ├── Mesa.java
│   ├── Filosofo.java
│   ├── Logger.java
│   ├── JantarDosFilosofos.java
│   └── README.md
│
│
└── README.md                   ← VOCÊ ESTÁ AQUI

🚀 Como Executar
Pré-requisitos

Java JDK 17 ou superior
Terminal/CMD com suporte a UTF-8 (para caracteres especiais)

Compilação
Opção 1: Compilar tudo de uma vez
bash# Na raiz do projeto
javac tarefa1/*.java tarefa2/*.java tarefa3/*.java tarefa4/*.java tarefa5/*.java
Opção 2: Compilar por tarefa
bash# Tarefa 1
cd tarefa1
javac *.java

# Tarefa 2
cd ../tarefa2
javac *.java

# Tarefa 3
cd ../tarefa3
javac *.java

# Tarefa 4
cd ../tarefa4
javac *.java

# Tarefa 5
cd ../tarefa5
javac *.java
Execução Individual
Tarefa 1 (Deadlock - 30 segundos)
bashcd tarefa1
java JantarDosFilosofos
Observar: Programa trava em deadlock (geralmente em < 10 segundos)
Tarefa 2 (Ordem Invertida - 2 minutos)
bashcd tarefa2
java JantarDosFilosofos
Observar: Alta performance, mas distribuição pode variar
Tarefa 3 (Semáforo - 2 minutos)
bashcd tarefa3
java JantarDosFilosofos
Observar: Performance moderada, distribuição equilibrada
Tarefa 4 (Monitor - 2 minutos)
bashcd tarefa4
java JantarDosFilosofos
Observar: Melhor fairness, diferença mínima entre filósofos
Execução dos Testes Comparativos
Teste Completo (5 minutos por solução = 15 minutos total)
bashcd tarefa5
java TestadorComparativo
```

Este comando:
1. Executa Tarefa 2 por 5 minutos
2. Executa Tarefa 3 por 5 minutos
3. Executa Tarefa 4 por 5 minutos
4. Coleta métricas de todas
5. Gera comparação final
6. Exibe resultados no console

**Saída esperada**:
- Logs de execução de cada teste
- Tabelas com métricas individuais
- Comparação final das 3 soluções
- Ranking por critério

---

## 📊 Métricas Coletadas

| Métrica | Descrição | Importância |
|---------|-----------|-------------|
| **Total de Refeições** | Soma de todas as refeições | Performance geral |
| **Throughput** | Refeições por segundo | Eficiência |
| **Coeficiente de Variação** | (σ/μ) × 100% | Equidade |
| **Jain's Fairness Index** | (Σxi)²/(n×Σxi²) | Justiça formal |
| **Diferença (max-min)** | Desigualdade máxima | Starvation potencial |
| **Taxa de Utilização** | Uso dos garfos (%) | Eficiência de recursos |

---

## 📈 Resultados Resumidos

### Comparação Rápida

| Critério | Tarefa 2 | Tarefa 3 | Tarefa 4 | Melhor |
|----------|----------|----------|----------|--------|
| **Performance** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ | T2 |
| **Fairness** | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | T4 |
| **Simplicidade** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ | T2 |
| **Escalabilidade** | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | T4 |
| **Anti-Starvation** | ⚠️ | ⚠️ | ✅ | T4 |

### Throughput Comparado
```
Tarefa 2:  4.90 ref/s  ████████████████████████  (100%)
Tarefa 3:  4.08 ref/s  ████████████████████      (83%)
Tarefa 4:  3.75 ref/s  ██████████████████        (77%)
```

### Fairness Comparado (Jain's Index)
```
Tarefa 4:  0.992  ████████████████████████  (Quase perfeito)
Tarefa 3:  0.968  ███████████████████████   (Excelente)
Tarefa 2:  0.942  ██████████████████████    (Muito bom)
