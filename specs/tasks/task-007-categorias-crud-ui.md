# Task 007: Gerenciamento de Categorias e Subcategorias (CRUD UI Estático)

## Contexto
Desenvolver a interface visual para o gerenciamento de categorias e subcategorias financeiras. O usuário acessará essa funcionalidade através de uma nova opção na tela de Configurações (`ConfiguracoesScreen.kt`). Todo o comportamento de persistência será simulado com dados mockados no ViewModel.

## Desenho da Interface e Fluxos (UI)
1. **Novo Item em Configurações:**
   - Adicionar uma segunda opção na `ConfiguracoesScreen.kt` escrita "Gerenciar Categorias".
   - Ao clicar, o app deve navegar (ou alternar a visualização) para a listagem de categorias.
2. **Tela de Listagem de Categorias (`CategoriasScreen.kt`):**
   - **Topo:** Botão de voltar (para retornar à tela de Configurações) e o título "Categorias".
   - **Corpo:** Uma lista vertical (`LazyColumn`) mostrando as categorias existentes. Cada item de categoria principal deve ser expansível ou conter:
     - Nome da categoria.
     - Ícone ou indicador visual simples.
     - Uma sublista recuada exibindo suas respectivas subcategorias (ex: "Alimentação" exibindo "Supermercado" e "Restaurante" logo abaixo).
     - Ícones de lápis (Editar) e lixeira (Excluir) tanto para a categoria principal quanto para as subcategorias.
   - **Botão Adicionar:** Um botão flutuante ou de destaque escrito "Nova Categoria".
3. **Formulários de Adicionar/Editar (`AlertDialog`):**
   - Ao clicar em "Nova Categoria" ou no ícone de editar, abrir uma `AlertDialog`.
   - O diálogo deve conter os seguintes campos:
     - Nome da Categoria/Subcategoria (Campo de texto).
     - Tipo da Categoria (Botões segmentados para selecionar se ela se aplica a **Despesa** ou **Receita**).
     - Vínculo (Um Dropdown Menu opcional que permite escolher uma Categoria Pai. Se selecionada, o item vira uma Subcategoria; se deixado em branco, vira uma Categoria Principal).
   - Botões "Cancelar" e "Salvar" que apenas fecham o diálogo, simulando o sucesso da ação.

## Critérios de Aceitação
- [X] A tela `ConfiguracoesScreen.kt` agora possui a opção "Gerenciar Categorias" posicionada abaixo de "Gerenciar Contas".
- [X] A navegação para a `CategoriasScreen` funciona perfeitamente ao clicar na opção.
- [X] Criar a `CategoriasScreen.kt` estruturando a listagem hierárquica (Categoria -> Subcategorias) usando dados mockados do ViewModel.
- [X] Ao clicar para criar ou editar, o diálogo é exibido com os campos de Nome, Tipo (Receita/Despesa) e Seleção de Categoria Pai.
- [X] Os botões de ação do diálogo fecham o componente visual perfeitamente ao serem clicados.
- [X] Certifique-se de que nenhum código utilize APIs do Android superiores ao `minSdkVersion 24`.
