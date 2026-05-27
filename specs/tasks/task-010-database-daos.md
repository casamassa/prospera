# Task 010: Criação do AppDatabase e Interfaces DAO

## Contexto
Implementar a infraestrutura de acesso ao banco de dados Room. Precisamos criar a classe central do banco de dados e as interfaces DAO para as operações de CRUD das Contas, Categorias e Lançamentos, em conformidade com as regras de negócio da Fase 2.

## Especificação Técnica (DAOs e Queries)
1. **AccountDao.kt:**
    - Inserir/Atualizar uma conta.
    - Buscar todas as contas ativas (`is_active = 1`).
    - Buscar uma conta específica pelo ID.
    - Atualizar o saldo de uma conta (`saldo_atual`) de forma direta através do ID da conta e o novo valor.
2. **CategoryDao.kt:**
    - Inserir uma categoria.
    - Buscar todas as categorias filtrando por Tipo ("RECEITA" ou "DESPESA").
    - Buscar subcategorias que possuem um `parent_category_id` específico.
3. **TransactionDao.kt:**
    - Inserir um lançamento.
    - Buscar todos os lançamentos de um determinado mês e ano (filtrando pelo intervalo de `data_timestamp`).
    - Buscar os lançamentos vinculados a uma conta específica.
4. **AppDatabase.kt:**
    - Classe abstrata estendendo `RoomDatabase`.
    - Registrar as três entidades: `AccountEntity`, `CategoryEntity` e `TransactionEntity`.
    - Declarar os métodos abstratos para fornecer os três DAOs.
    - Configurar uma versão inicial (versão 1) sem migrações necessárias por enquanto.

## Critérios de Aceitação
- [X] Os três arquivos DAO (`AccountDao`, `CategoryDao`, `TransactionDao`) foram criados dentro do pacote `data/local/dao/`.
- [X] O arquivo `AppDatabase.kt` foi criado e centraliza com sucesso o registro das entidades e DAOs.
- [X] O `AccountDao` possui a query ou método necessário para atualizar cirurgicamente o `saldo_atual` de uma conta.
- [X] O `TransactionDao` possui uma query capaz de filtrar os lançamentos com base no timestamp da data para a lógica de fluxo de caixa mensal.
- [X] O projeto compila localmente sem erros de geração de código do Room.

## Ajustes e Correções de Bugs (Refinamento)
- [X] **Correção de Ambiente KSP2 (unexpected jvm signature V):** O erro persiste no motor KSP2. Para mitigar esta incompatibilidade estrutural, altere o arquivo `gradle.properties` inserindo ou modificando a flag `ksp.useKSP2=false` para forçar o build a utilizar o motor estável do KSP1. Adicionalmente, caso necessário, instrua o catálogo de dependências `gradle/libs.versions.toml` a elevar a versão do Room para `2.7.2` ou superior, estabilizando as assinaturas de métodos suspend do Room.

