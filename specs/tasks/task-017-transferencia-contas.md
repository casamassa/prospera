# Task 017: Funcionalidade de Transferência Entre Contas

## Contexto
Implementar a regra de negócio e a interface visual para permitir que o usuário transfira dinheiro entre duas contas cadastradas. Essa ação gerará um lançamento de débito na conta de origem e atualizará o saldo de ambas as contas de maneira atômica no banco de dados.

## Especificação Técnica (Lógica & UI)
1. **Ajuste no Formulário de Lançamentos (`LancamentoForm.kt`):**
    - No componente de botões segmentados (Segmented Buttons), adicionar uma terceira opção chamada **Transferência** ao lado de Receita e Despesa.
    - **Comportamento Dinâmico:** Se o usuário selecionar "Transferência", o campo de "Categoria" deve ser ocultado da tela, e um novo campo Dropdown chamado **Conta de Destino** deve aparecer logo abaixo do campo "Conta de Origem".
    - O Dropdown de "Conta de Destino" deve listar todas as contas ativas, exceto a conta que já foi selecionada como origem (para evitar transferir para si mesma).
2. **Caso de Uso (`InsertTransactionUseCase.kt` ou similar):**
    - Atualizar a lógica para identificar quando um lançamento possui o `transfer_target_account_id` preenchido.
    - **Regra de Saldo:** Subtrair o valor do saldo da Conta de Origem e Somar o valor do saldo da Conta de Destino. Ambas as atualizações de saldo e a inserção do registro de transferência na tabela `lancamentos` devem ocorrer dentro da mesma transação do Room.
3. **Exibição no Extrato (`FluxoScreen.kt`):**
    - No histórico de lançamentos, se o registro for uma transferência, o indicador visual não deve ser nem verde nem vermelho, mas sim uma cor neutra (ex: azul ou cinza de sistema), exibindo um ícone de transferência (setas bidirecionais).

## Critérios de Aceitação
- [X] O formulário de lançamentos exibe a opção "Transferência" e alterna dinamicamente os campos (exibe Conta Destino, esconde Categoria).
- [X] O Dropdown de conta destino impede a seleção da mesma conta de origem.
- [X] Salvar uma transferência abate o valor na conta de origem e incrementa na conta de destino com sucesso no Room.
- [X] A operação é atômica (se uma conta falhar ao atualizar, nenhuma alteração é persistida).
- [X] O projeto compila e roda sem quebras, mantendo compatibilidade com o `minSdkVersion 24`.
