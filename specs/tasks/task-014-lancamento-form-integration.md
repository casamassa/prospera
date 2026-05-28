# Task 014: Integração do Formulário de Lançamento com Gravação Real

## Contexto
Conectar a `ModalBottomSheet` do formulário de novos lançamentos ao caso de uso de inserção. Ao salvar um formulário, os dados digitados pelo usuário devem ser validados, convertidos e persistidos no banco de dados, impactando reativamente o saldo da conta e os extratos.

## Especificação Técnica (UI & ViewModel)
1. **Refatoração do `LancamentoFormViewModel.kt` (ou similar):**
    - Injetar o `InsertTransactionUseCase` para realizar a gravação estável.
    - Injetar o `AccountRepository` e `CategoryRepository` para carregar dinamicamente as opções que aparecem nos campos Dropdown de seleção de Conta e Categoria.
    - Criar uma função `salvarLancamento()` que capture o Valor, Descrição, Tipo (Receita/Despesa), Data (convertida para Unix Timestamp), ID da Conta e ID da Categoria selecionados, e dispare o UseCase.
2. **Ajustes no Layout do Formulário (`LancamentoForm.kt`):**
    - Substituir os dados fixos dos Dropdowns pelas listas de contas e categorias reais carregadas do banco.
    - Tratar a conversão de texto para `Double` no campo de valor monetário com segurança para evitar quebras por digitação inválida.
    - Garantir que, após o sucesso da execução do UseCase, a `ModalBottomSheet` seja fechada e os campos limpos.

## Critérios de Aceitação
- [X] Os campos suspensos (Dropdowns) de Conta e Categoria exibem as opções reais cadastradas no banco de dados.
- [X] Ao preencher os campos e clicar em "Salvar", os dados são persistidos na tabela `lancamentos`.
- [X] A inserção executa com sucesso a lógica de atualização automática do saldo da conta correspondente via UseCase.
- [X] Após salvar com sucesso, a folha do formulário fecha e a interface reflete os novos dados imediatamente na tela de fundo.
- [X] O projeto compila e roda sem vazamentos de API superiores ao `minSdkVersion 24`.

## Ajustes e Correções de Bugs (Refinamento de Teste)
- [X] **Carga Inicial de Categorias (Data Seeding):** Para viabilizar os testes de inserção de lançamentos, configure a inicialização do banco de dados (`AppDatabase.kt` ou a classe de injeção/inicialização correspondente) para inserir uma lista de categorias padrão de teste caso a tabela de categorias esteja vazia. Insira categorias como "Alimentação" (DESPESA), "Transporte" (DESPESA) e "Salário" (RECEITA).
