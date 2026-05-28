# Task 013: Integração de Dados Reais no Fluxo de Caixa

## Contexto
Conectar a tela de Fluxo de Caixa à persistência de dados real. O aplicativo deve carregar dinamicamente a listagem de lançamentos mensais a partir do banco de dados Room, respondendo reativamente às alterações de mês feitas pelo usuário no seletor do topo.

## Especificação Técnica (UI & ViewModel)
1. **Refatoração do `FluxoViewModel.kt`:**
    - Injetar o `TransactionRepository` no construtor do ViewModel.
    - Manter o estado do mês e ano atualmente selecionados (utilizando `java.time.YearMonth` ou uma abordagem numérica compatível com a API 24).
    - Utilizar um fluxo reativo (como um `flatMapLatest` no StateFlow do mês atual) para disparar uma nova busca no repositório sempre que o usuário mudar o mês através das setas do topo.
    - Converter as datas do filtro para milissegundos (Unix Timestamp) para consultar os limites de início e fim do mês na query do Room.
2. **Ajustes na `FluxoScreen.kt`:**
    - Garantir que a lista vertical (`LazyColumn`) consuma o estado de transações reais vindo do ViewModel.
    - Manter os indicadores visuais de cores (Verde para Receitas, Vermelho para Despesas) consolidados na Fase 1.
    - Garantir que as setas de navegação de mês atualizem corretamente o estado no ViewModel, forçando a recarga automática dos dados na tela.

## Critérios de Aceitação
- [X] O `FluxoViewModel` não possui listas estáticas de transações codificadas.
- [X] Ao abrir a tela, o app busca no banco apenas as transações correspondentes ao mês atual.
- [X] Clicar nas setas para avançar ou retroceder o mês atualiza a interface e faz uma nova requisição automática ao banco de dados Room.
- [X] A listagem renderiza os dados reais respeitando os tipos (Receita/Despesa) e mantendo as cores corretas na UI.
- [X] O projeto compila e roda sem quebras, mantendo compatibilidade estrita com o `minSdkVersion 24`.
