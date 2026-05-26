# Task 002: Layout da Tela Home

## Contexto
Desenvolver o layout visual da tela Home utilizando dados mockados no ViewModel, conforme a arquitetura especificada.

## Desenho da Tela (UI)
1. **Topo:** Card ou texto destacado exibindo o "Saldo Atual Geral" (Somatória de todas as contas) formatado em R$.
2. **Corpo:** Uma lista vertical (`LazyColumn`) exibindo as contas cadastradas. Cada item da lista deve conter:
   - Nome da conta (ex: Carteira, Banco X, Poupança)
   - Saldo específico daquela conta formatado em R$.

## Critérios de Aceitação
- [X] Criar `HomeViewModel.kt` com uma lista de contas estáticas mockadas.
- [X] Criar `HomeScreen.kt` consumindo os dados do ViewModel.
- [X] O layout exibe o saldo geral em destaque no topo.
- [X] A listagem de contas renderiza corretamente abaixo do saldo geral.
