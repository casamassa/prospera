# Task 004: Layout da Tela de Relatórios

## Contexto
Desenvolver o layout visual da tela de Relatórios utilizando dados mockados no ViewModel. Esta tela exibirá gráficos estáticos baseados no mês selecionado, seguindo as diretrizes de UI da Fase 1.

## Desenho da Tela (UI)
1. **Topo (Seletor de Mês):**
   - Reutilizar o mesmo padrão visual de seletor de mês/ano com setas laterais criado na tela de Fluxo de Caixa (ex: "Janeiro 2026").
2. **Corpo (Abas ou Rolagem Vertical para 3 Relatórios):**
   - O usuário deve conseguir visualizar três relatórios distintos organizados em uma lista rolável (`LazyColumn` ou `Column` com scroll):
   
   - **Relatório 1: Despesas por Categoria (Gráfico de Pizza/Donut)**
     - Representação visual em formato de círculo dividido em fatias coloridas para cada categoria de gasto (ex: Alimentação, Transporte).
     - Abaixo ou ao lado, uma legenda com quadradinhos coloridos indicando o nome da categoria e a porcentagem.
   
   - **Relatório 2: Receitas por Categoria (Gráfico de Pizza/Donut)**
     - Mesma estrutura do gráfico anterior, mas focado nas fontes de ganho (ex: Salário, Investimentos).
   
   - **Relatório 3: Despesa Total x Receita Total (Gráfico de Colunas/Barras)**
     - Exibir duas barras verticais ou horizontais comparativas lado a lado: uma barra Verde (Receitas) e uma barra Vermelha (Despesas), permitindo uma comparação visual rápida do balanço mensal.

*Nota de Implementação para UI:* O Gemini pode implementar esses gráficos usando o componente `Canvas` do Jetpack Compose para desenhar arcos (`drawArc`) para a pizza e retângulos (`drawRect`) para as colunas, utilizando dados fixos do ViewModel.

## Critérios de Aceitação
- [X] Criar o `RelatoriosViewModel.kt` contendo os dados mockados de distribuição por categoria e o estado do mês atual.
- [X] Criar a `RelatoriosScreen.kt` consumindo esses dados estruturados.
- [X] Renderizar com sucesso o gráfico de pizza para Despesas com sua respectiva legenda.
- [X] Renderizar com sucesso o gráfico de pizza para Receitas com sua respectiva legenda.
- [X] Renderizar com sucesso o gráfico de barras comparando o total de Receita vs Despesa.
- [X] A tela de Relatórios está corretamente vinculada ao ícone correspondente na BottomBar configurada na Task 001.
