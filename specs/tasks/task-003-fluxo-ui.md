# Task 003: Layout da Tela de Fluxo de Caixa

## Contexto
Desenvolver o layout visual da tela de Fluxo de Caixa utilizando dados mockados no ViewModel, conforme as diretrizes de UI estática da Fase 1.

## Desenho da Tela (UI)
1. **Topo (Seletor de Mês):**
   - Exibir um seletor de mês/ano no topo da tela (ex: "Janeiro 2026").
   - Adicionar duas setas (ícones) ao lado do texto: uma para a esquerda (mês anterior) e uma para a direita (próximo mês).
   - *Nota de UI:* Como estamos na fase estática, clicar nas setas pode apenas simular a mudança visual ou atualizar o estado do texto para demonstrar a interação.
2. **Corpo (Listagem de Lançamentos):**
   - Uma lista vertical (`LazyColumn`) exibindo os lançamentos do mês selecionado.
   - Cada item da lista (card do lançamento) deve conter:
     - Título/Descrição do lançamento (ex: "Supermercado", "Salário").
     - Categoria (ex: "Alimentação", "Trabalho").
     - Valor formatado em R$.
     - Indicador visual do tipo: Valores de **Despesa** devem ser exibidos em vermelho (ou com sinal negativo) e valores de **Receita** em verde (ou com sinal positivo).

## Critérios de Aceitação
- [X] Criar o `FluxoViewModel.kt` contendo uma lista mockada de transações (incluindo receitas e despesas) e o estado do mês atual.
- [X] Ao abrir a tela, o seletor do topo deve exibir o mês atual por padrão.
- [X] Criar a `FluxoScreen.kt` consumindo os dados desse ViewModel.
- [X] A listagem diferencia visualmente e de forma clara o que é Receita e o que é Despesa (através de cores ou sinais).
- [X] A tela de Fluxo de Caixa está corretamente integrada ao NavHost construído na Task 001.
