# Task 020: Correção Arquitetural do Fluxo de Edição e Exclusão de Lançamentos

## Contexto
Corrigir os desvios arquiteturais nas operações de Edição e Exclusão de lançamentos. Garantir que o ViewModel utilize obrigatoriamente os Casos de Uso (UseCases) da camada Domain em vez de acessar o Repositório diretamente, viabilizando o estorno correto de saldos.

## Especificação Técnica e Correções

### 1. Correção do Fluxo de Exclusão (Recálculo de Saldo)
- **Ajuste no `FluxoViewModel.kt`:** Identificar a função `deleteTransaction`. Remover a chamada direta ao `transactionRepository.deleteTransaction`.
- **Injeção do UseCase:** Injetar o `DeleteTransactionUseCase` no construtor do `FluxoViewModel`.
- **Conexão:** Fazer a função `deleteTransaction` invocar o método `invoke(transaction)` do `DeleteTransactionUseCase`, garantindo que a lógica de transação e o método `revertBalance` contidos no UseCase sejam executados antes da exclusão física.

### 2. Correção do Fluxo de Edição (Abertura do Formulário e Persistência)
- **Ajuste no `FluxoScreen.kt`:** Ao clicar na opção "Editar" do menu de um lançamento, garantir que o objeto `FinancialTransaction` correspondente seja capturado e que o estado de exibição do formulário (`LancamentoForm`) seja ativado.
- **Passagem de Parâmetro:** O componente `LancamentoForm` deve receber este lançamento selecionado como parâmetro (ex: `lancamento = transactionToEdit`).
- **Pré-preenchimento e Salvamento:** No formulário, se um lançamento for recebido para edição, os campos de estado (Valor, Descrição, Conta, Categoria) devem ser pré-preenchidos. Ao salvar, se o lançamento contiver um ID válido (diferente de zero), o sistema deve atualizar o registro existente (executar `UPDATE` ou UseCase de edição correspondente) em vez de gerar um novo ID (executar `INSERT`).

## Critérios de Aceitação
- [X] O `FluxoViewModel` delega a exclusão exclusivamente ao `DeleteTransactionUseCase`.
- [X] Excluir qualquer tipo de lançamento (Receita, Despesa ou Transferência) atualiza reativamente o saldo da conta associada na interface através do estorno matemático.
- [X] Clicar em "Editar" na listagem abre com sucesso a folha do formulário com os dados preenchidos.
- [X] Salvar um lançamento editado atualiza o registro original no banco de dados Room sem duplicá-lo na lista.
