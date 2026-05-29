# Task 020: Correção do Fluxo de Edição e Estorno de Saldo na Exclusão

## Contexto
Corrigir a comunicação entre a listagem de lançamentos e o formulário para viabilizar a edição, e implementar a regra de negócio para estornar o saldo da conta quando um lançamento for excluído.

## Especificação Técnica

### 1. Acionamento da Edição (UI & State)
- Na `FluxoScreen.kt`, ao clicar em "Editar", o estado que controla a exibição do `LancamentoForm` (ModalBottomSheet) deve ser ativado passando o objeto do `Lancamento` selecionado.
- No `LancamentoForm.kt`, certifique-se de que se o lançamento recebido para edição não for nulo, os campos de Valor, Descrição, Conta e Categoria sejam pré-preenchidos com os dados existentes.
- Ao clicar em "Salvar" estando em modo de edição, o repositório deve executar um `UPDATE` no registro existente (mantendo o mesmo ID) em vez de criar um novo.

### 2. Estorno de Saldo na Exclusão (Regra de Negócio)
- Criar ou ajustar o caso de uso de exclusão (ex: `DeleteTransactionUseCase.kt`).
- **Regra do Estorno:** Antes de deletar o lançamento do banco de dados, verifique o seu Tipo (Receita ou Despesa) e o seu Valor:
    - Se era uma **Despesa**, o valor deve ser **somado** de volta ao saldo da conta correspondente.
    - Se era uma **Receita**, o valor deve ser **subtraído** do saldo da conta correspondente.
    - Se era uma **Transferência**, o valor deve ser **somado** na conta de origem e **subtraído** na conta de destino.
- A atualização do saldo e a remoção física do lançamento do banco devem rodar obrigatoriamente dentro de uma transação (`@Transaction`) do Room para garantir a atomicidade.

### 3. Mudança no label do Bottom Menu Bar
- Alterar a opção "Configurações" do Bottom Menu Bar para "Menu" 

## Critérios de Aceitação
- [ ] Clicar em "Editar" abre o formulário com todos os dados do lançamento preenchidos.
- [ ] Salvar a edição altera o lançamento atual sem duplicá-lo no histórico.
- [ ] Excluir uma despesa devolve o dinheiro ao saldo da conta reativamente.
- [ ] Excluir uma receita abate o valor do saldo da conta reativamente.
- [ ] Bottom Menu Bar atualiza o label para Menu.
