# Task 018: Refinamentos de Layout, Ajustes Visuais e Correção de Bugs

## Contexto
Aplicar um pacote de melhorias visuais e usabilidade nas listagens e menus, além de corrigir o bug de edição/exclusão de lançamentos para fechar com excelência o MVP da Fase 2.

## Especificação Técnica e Ajustes

### 1. Correção de Bug (Ações no Lançamento)
- **Tela de Fluxo de Caixa (`FluxoScreen.kt`):** Ao clicar em um item de lançamento da lista, abrir um menu suspenso (`DropdownMenu`), uma caixa de diálogo ou reabrir a `ModalBottomSheet` preenchida permitindo ao usuário escolher entre **Editar** ou **Excluir** o lançamento.

### 2. Exibição Hierárquica de Categorias no Extrato
- Na listagem de lançamentos, substituir a exibição do ID numérico pelo nome real da categoria.
- Se o lançamento pertencer a uma subcategoria, exibir no formato `CategoriaPai -> Subcategoria` (ex: `Alimentação -> Restaurante`). Caso contrário, exibir apenas o nome da categoria principal. Ajustar as Queries ou repositórios se necessário utilizando relacionamentos do Room.

### 3. Ajustes de Layout e Ordenação Alfabética (Contas e Categorias)
- **Contas (`ContasScreen.kt`):** Substituir o botão de texto "Nova Conta" por um botão de ícone de mais (`+`) minimalista alinhado à direita no topo (ou como um FAB alinhado à direita). Alterar a ordenação da lista no banco ou ViewModel para seguir a **ordem alfabética**.
- **Categorias (`CategoriasScreen.kt`):** Substituir o botão "Nova Categoria" por um ícone de mais (`+`) alinhado à direita. Alterar a ordenação das categorias principais para seguir a **ordem alfabética**.
- **Abas de Categorias:** Implementar duas abas visuais (`TabRow`) na tela de categorias para filtrar e exibir separadamente as categorias de **Despesas** e as de **Receitas**.

### 4. Ajuste de Menu Inferior (BottomBar)
- Alterar o rótulo (label) do quarto item do menu inferior de "Ajustes" para **"Config."**.

## Critérios de Aceitação
- [X] Lançamentos no extrato exibem os nomes textuais das categorias estruturados de forma hierárquica (`Pai -> Filho`) em vez de IDs.
- [X] Clicar em um lançamento abre visualmente as opções de edição e exclusão.
- [X] Os botões de adição nas telas de gestão de Contas e Categorias usam o formato compacto de ícone `+` alinhado à direita.
- [X] As listas de contas e de categorias são exibidas estritamente em ordem alfabética.
- [X] A tela de categorias possui abas navegáveis separando Despesas de Receitas.
- [X] O rótulo da última aba do menu inferior foi modificado com sucesso para "Config.".
- [X] O projeto compila e roda sem quebras, mantendo compatibilidade com o `minSdkVersion 24`.
