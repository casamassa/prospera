# Task 019: Motor de Ajuste de Saldo e Redesenho da Seleção de Categorias

## Contexto
Consolidar as operações de escrita no banco de dados garantindo que edições e exclusões recalculem o saldo das contas de forma correta e substituir o dropdown de categorias por um seletor visual dedicado.

## Especificação Técnica

### 1. Motor de Saldo e Transações (UseCases / DAOs)
- **Correção da Edição:** No formulário de inserção, garanta que se o objeto de lançamento recebido possuir um ID válido (diferente de zero), o Room execute um `UPDATE` em vez de um `INSERT` com ID novo.
- **Lógica de Recálculo de Saldo (Update/Delete):**
    - Ao **Excluir** um lançamento: Executar o estorno matemático exato no saldo da conta vinculada (Subtrair se era Receita, Somar se era Despesa).
    - Ao **Editar** um lançamento: A forma mais segura e limpa arquiteturalmente é interceptar o valor antigo no banco, reverter o saldo da conta para o estado original (antes do lançamento existir) e aplicar a nova regra matemática com os novos valores digitados.
    - Ambas as operações de ajuste de saldo e manipulação do lançamento devem rodar sob a anotação `@Transaction` do Room.

### 2. Nova UI: Tela/Componente de Seleção de Categoria
- Remover o componente `ExposedDropdownMenuBox` de categoria do `LancamentoForm.kt`.
- Ao clicar no campo de Categoria, abrir uma nova `ModalBottomSheet` ou componente de diálogo exclusivo (`CategorySelector`).
- Este seletor deve exibir as categorias ordenadas alfabeticamente, divididas por duas abas (`Receitas` e `Despesas`). As subcategorias devem aparecer listadas logo abaixo de sua categoria pai com o prefixo `-> ` ou um recuo visual de layout.

## Critérios de Aceitação
- [ ] Editar um lançamento atualiza o mesmo registro no banco de dados sem criar um duplicado.
- [ ] Excluir um lançamento atualiza reativamente o saldo da conta, executando o estorno do valor.
- [ ] Modificar o valor ou tipo de um lançamento recalcula com precisão matemática o saldo da conta afetada.
- [X] O Dropdown antigo foi removido e a seleção de categorias agora ocorre em uma interface dedicada e limpa por abas.
