# Task 012: Integração de Dados Reais na Tela Home

## Contexto
Conectar a tela Home e o seu respectivo ViewModel à camada de dados persistidos. O app deve passar a ler o saldo geral e a listagem de contas diretamente do banco de dados Room através do repositório, removendo os dados mockados anteriores.

## Especificação Técnica (UI & ViewModel)
1. **Refatoração do `HomeViewModel.kt`:**
    - Injetar a dependência do `AccountRepository` (ou o Use Case correspondente, caso tenha sido criado) no construtor do ViewModel.
    - Expor o estado da tela utilizando um `StateFlow` ou `mutableStateOf` que observe de forma reativa a lista de contas ativas do banco de dados.
    - Criar uma propriedade calculada (ou reativa via `map`) que faça a somatória em tempo real do saldo atual de todas as contas para gerar o "Saldo Atual Geral".
2. **Ajustes na `HomeScreen.kt`:**
    - Garantir que a UI consuma o novo estado reativo vindo do `HomeViewModel`.
    - Manter os padrões visuais e formatações de moeda (R$) estabelecidos na Fase 1.
3. **Inicialização:**
    - Como ainda não criamos a tela para inserir contas reais, garanta que o ViewModel ou a inicialização do banco insira ao menos uma ou duas contas de teste (ex: "Carteira" com saldo R$ 0,00) caso o banco de dados esteja totalmente vazio, permitindo que a tela não abra em branco.

## Critérios de Aceitação
- [X] O `HomeViewModel` não possui mais nenhuma lista estática codificada (hardcoded) para exibição.
- [X] O saldo geral exibido no topo da tela é o reflexo exato da soma matemática das contas trazidas do banco de dados Room.
- [X] A listagem de contas renderiza dinamicamente as contas vindas do repositório de forma reativa.
- [X] O aplicativo compila sem falhas e roda no dispositivo exibindo as informações iniciais do banco de dados.
