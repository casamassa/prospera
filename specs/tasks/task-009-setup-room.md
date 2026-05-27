# Task 009: Configuração do Room e Criação das Entidades

## Contexto
Instalar o Room Database no projeto Android e estruturar as entidades de dados (tabelas) na camada correspondente de acordo com o design de banco definido.

## Objetivos
1. Adicionar as dependências necessárias do Room (`androidx.room:room-runtime`, `room-ktx` e o compilador `room-compiler` via KSP ou KAPT) no arquivo `build.gradle.kts`.
2. Criar as classes de dados anotadas como `@Entity` dentro do pacote `data/local/entities/`:
    - `AccountEntity`
    - `CategoryEntity`
    - `TransactionEntity`
3. Configurar os relacionamentos e chaves estrangeiras (`ForeignKeys`) conforme mapeado no arquivo `03-database-design.md`.

## Critérios de Aceitação
- [ ] As dependências do Room foram inseridas corretamente e o projeto sincroniza o Gradle sem erros.
- [ ] A entidade de Contas possui o campo booleano `is_active`.
- [ ] A entidade de Categorias possui a auto-relação opcional `parent_category_id`.
- [ ] A entidade de Lançamentos possui as restrições de chaves estrangeiras configuradas com `RESTRICT` para evitar a exclusão acidental de registros vinculados.
- [ ] O pipeline do GitHub Actions roda e o projeto compila com sucesso com as novas classes do Room.
