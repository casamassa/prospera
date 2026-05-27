# Design do Banco de Dados (Room SQLite)

## Entidades (Tabelas)

### 1. Tabela `contas` (AccountEntity)
- `id`: Long (Primary Key, AutoIncrement)
- `nome`: String
- `saldo_inicial`: Double
- `saldo_atual`: Double
- `is_active`: Boolean (Default: true)

### 2. Tabela `categorias` (CategoryEntity)
- `id`: Long (Primary Key, AutoIncrement)
- `nome`: String
- `tipo`: String ("RECEITA" ou "DESPESA")
- `parent_category_id`: Long? (Chave estrangeira opcional apontando para `categorias.id` com `onDelete = ForeignKey.CASCADE`)

### 3. Tabela `lancamentos` (TransactionEntity)
- `id`: Long (Primary Key, AutoIncrement)
- `descricao`: String
- `valor`: Double
- `data_timestamp`: Long (Data armazenada em milissegundos / Unix Timestamp)
- `tipo`: String ("RECEITA" ou "DESPESA")
- `conta_id`: Long (Chave estrangeira apontando para `contas.id` com `onDelete = ForeignKey.RESTRICT`)
- `categoria_id`: Long (Chave estrangeira apontando para `categorias.id` com `onDelete = ForeignKey.RESTRICT`)
- `transfer_target_account_id`: Long? (Opcional, chave estrangeira para `contas.id` reservada para futuras transferências)
