# Task 015: Integração do CRUD de Contas com Persistência Real

## Contexto
Conectar a tela "Minhas Contas" (`ContasScreen.kt`) e o seu respectivo diálogo de formulário à camada de dados do `AccountRepository`. Implementar as ações reais de Criar, Editar e Excluir contas utilizando as regras de integridade do banco de dados Room.

## Especificação Técnica (UI, ViewModel & Regras)
1. **Refatoração do `ContasViewModel.kt` (ou similar):**
    - Injetar o `AccountRepository` e o `TransactionRepository` (necessário para verificar dependências de lançamentos antes da exclusão).
    - Expor o estado reativo da lista contendo apenas as contas com `is_active = true`.
    - **Operação de Salvar (Criar/Editar):** Capturar os dados do diálogo, validar o valor do saldo e realizar o `insert` ou `update` no Room.
    - **Operação de Excluir (Lógica Híbrida):** Ao clicar no ícone de lixeira de uma conta:
        - Verificar se existem lançamentos vinculados ao ID dessa conta no banco de dados.
        - **Caso NÃO existam lançamentos:** Executar a exclusão física do registro da tabela (`delete`).
        - **Caso EXISTAM lançamentos:** Executar a exclusão lógica, atualizando o campo `is_active` para `false` para que a conta suma das telas mantendo o histórico de saldo estável.
2. **Ajustes na `ContasScreen.kt`:**
    - Alimentar a `LazyColumn` com o fluxo de contas reais vindo do ViewModel.
    - Conectar as ações de clique dos ícones de lápis (Editar) e lixeira (Excluir) para dispararem os respectivos comportamentos de dados.
    - Limpar os campos do diálogo (`AlertDialog`) após o fechamento ou sucesso da ação.

## Critérios de Aceitação
- [X] A listagem de contas exibe em tempo real as contas recuperadas do banco de dados Room.
- [X] O formulário de "Nova Conta" insere um registro válido no banco com o nome e saldo inicial especificados.
- [X] O botão de edição atualiza os dados da conta de forma cirúrgica no Room sem duplicar o registro.
- [X] Contas sem lançamentos associados são excluídas fisicamente do banco de dados ao clicar na lixeira.
- [X] Contas com lançamentos vinculados sofrem exclusão lógica (`is_active = false`) ao clicar na lixeira, sumindo da lista sem corromper o banco.
- [X] O projeto compila e executa localmente dentro da especificação de compatibilidade da `minSdkVersion 24`.

## Ajustes e Correções de Bugs (Refinamento Crítico)
- [X] **Correção do Botão Nova Conta:** No arquivo `ContasScreen.kt`, identifique o evento de clique do botão "Nova Conta" e garanta que ele altere corretamente a variável de estado (`showDialog` ou similar) para `true`, forçando a renderização da `AlertDialog`.
- [X] **Recuperação da Conta Inicial (Carteira):** Garanta que a conta padrão "Carteira" seja reinserida no banco de dados pela lógica de Carga Inicial (Data Seeding) caso o banco esteja vazio, para que ela volte a aparecer reativamente tanto na `HomeScreen` quanto na `ContasScreen`.
- [X] **Consistência do ViewModel:** Certifique-se de que o ViewModel da `ContasScreen` esteja observando o repositório utilizando um fluxo contínuo e reativo (`Flow` ou `StateFlow`) para que qualquer nova conta criada atualize a lista imediatamente.
