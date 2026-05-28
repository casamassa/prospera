# Task 016: Integração do CRUD de Categorias com Persistência Real

## Contexto
Conectar a tela de gerenciamento de categorias (`CategoriasScreen.kt`) e o seu respectivo diálogo de formulário à camada de dados do `CategoryRepository`. Implementar as ações reais de listar, criar, editar e excluir categorias e subcategorias de forma hierárquica utilizando o Room.

## Especificação Técnica (UI, ViewModel & Hierarquia)
1. **Refatoração do `CategoriasViewModel.kt` (or similar):**
    - Injetar o `CategoryRepository` no construtor do ViewModel.
    - Expor o estado reativo da lista de categorias. A lógica deve buscar todas as categorias e agrupá-las de forma que as subcategorias (registros com `parent_category_id` preenchido) fiquem atreladas à sua categoria pai correspondente.
    - **Operação de Salvar (Criar/Editar):** Capturar os dados do diálogo, identificar se é uma categoria principal ou subcategoria (com base no Dropdown de seleção de Categoria Pai) e realizar o `insert` ou `update` no Room.
    - **Operação de Excluir:** Ao clicar na lixeira, deletar o registro. Caso o usuário exclua uma categoria pai, configurar ou respeitar o comportamento `CASCADE` para remover as subcategorias atreladas.
2. **Ajustes na `CategoriasScreen.kt`:**
    - Alimentar a `LazyColumn` para renderizar a estrutura visual de árvore (Nome da Categoria Principal e, logo abaixo com um recuo/indentação, as suas respectivas Subcategorias).
    - O diálogo de criação/edição deve ler do banco todas as categorias macro disponíveis para preencher o Dropdown "Categoria Pai" dinamicamente.
    - Limpar os campos do formulário após fechar ou salvar com sucesso.

## Critérios de Aceitação
- [X] A listagem de categorias exibe os dados reais do banco de dados Room de forma hierárquica (Categoria -> Subcategorias).
- [X] O formulário de criação permite inserir uma nova categoria principal (Tipo Receita ou Despesa).
- [X] O formulário permite criar uma subcategoria associando-a a uma categoria pai existente através do Dropdown.
- [X] Os botões de editar e excluir operam diretamente nos registros do banco de dados com sucesso.
- [X] O projeto compila sem erros, garantindo compatibilidade estrita com a `minSdkVersion 24`.
