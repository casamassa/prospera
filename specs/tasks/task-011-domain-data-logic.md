# Task 011: Camada Domain, Repositórios e Lógica de Negócio (Saldos e Lançamentos)

## Contexto
Implementar as camadas de negócio puro (Domain) e repositórios (Data) para as três entidades (Contas, Categorias, Lançamentos). Criar a lógica que garante que a inserção, edição ou exclusão de um lançamento atualize automaticamente o saldo atual da conta vinculada.

## Especificação Técnica (Domain & Data Layers)
1. **Modelos do Domínio (Domain Models):**
    - Criar classes de dados Kotlin puros (sem anotações do Room) dentro do pacote `domain/model/` para representar `Account`, `Category` e `FinancialTransaction`.
    - Adicionar funções de extensão para converter as Entidades do Room para os Modelos do Domínio (Mappers).
2. **Contratos dos Repositórios (Domain Interfaces):**
    - Criar as interfaces de repositórios em `domain/repository/`: `AccountRepository`, `CategoryRepository` e `TransactionRepository`.
3. **Implementação dos Repositórios (Data Layer):**
    - Implementar os repositórios em `data/repository/` injetando os respectivos DAOs criados na Task 010.
4. **Casos de Uso Essenciais (Domain Use Cases):**
    - Criar o pacote `domain/use_case/`.
    - **`InsertTransactionUseCase.kt`:** Deve conter o método principal que recebe um lançamento, avalia se ele é uma `DESPESA` ou `RECEITA`, calcula o novo valor do saldo atual da conta afetada e executa ambas as ações de escrita (inserir lançamento + atualizar saldo da conta) utilizando a transação garantida pelo banco.
    - **`DeleteTransactionUseCase.kt`:** Faz a lógica inversa. Se remover uma despesa, soma o valor de volta ao saldo da conta; se remover uma receita, subtrai.

## Critérios de Aceitação
- [X] Os modelos de domínio e os mappers foram criados sem vazamento de dependências de infraestrutura do Room.
- [X] As interfaces de repositório e suas implementações foram criadas nos pacotes corretos definidos na arquitetura.
- [X] O caso de uso `InsertTransactionUseCase` calcula matematicamente e altera com sucesso o saldo da conta com base no tipo de lançamento.
- [X] O projeto compila sem erros, respeitando o limite do `minSdkVersion 24` para manipulação de dados ou data.
