# Task 021: Filtragem de Lançamentos de Contas Inativas no Extrato

## Contexto
Garantir que a listagem de lançamentos (Extrato/Fluxo de Caixa) exiba apenas movimentações pertencentes a contas ativas (`is_active = true`), ocultando o histórico de contas que sofreram exclusão lógica para manter a consistência da interface.

## Especificação Técnica
- **Ajuste na Query do Room (DAO):** Na query que recupera os lançamentos para a tela de Fluxo de Caixa (provavelmente no `TransactionDao.kt`), realizar um `INNER JOIN` com a tabela `contas` (ou filtrar a subquery).
- **Regra de Filtragem:** Adicionar a cláusula `WHERE contas.is_active = 1` (ou `true`) na seleção dos lançamentos.
- **Resultado Esperado:** Quando uma conta sofrer exclusão lógica (`is_active = false`), todos os lançamentos atrelados a ela devem sumir reativamente da tela de Fluxo de Caixa, ajustando também o totalizador do extrato se aplicável.

## Critérios de Aceitação
- [X] O extrato de lançamentos não exibe transações de contas desativadas.
- [X] Ao desativar uma conta no gerenciamento, seus lançamentos somem imediatamente do histórico de fluxo.
- [X] O projeto compila normalmente respeitando a integridade referencial do banco de dados Room.
