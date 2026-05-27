# Task 005: Formulario de Lancamento via ModalBottomSheet (UI)

## Contexto
Implementar o botão flutuante (FAB) unificado e o layout visual do formulário para novos lançamentos utilizando uma `ModalBottomSheet` do Material Design 3 que se expande para ocupar a tela inteira. Todo o comportamento será estático e visual nesta fase.

## Desenho da Interface e Comportamento
1. **Botao Flutuante (FAB):**
   - Adicionar um `FloatingActionButton` com o ícone de adição (`+`) ao `Scaffold` principal do aplicativo.
   - O FAB deve ficar visível apenas quando o usuário estiver nas abas **Home** ou **Fluxo de Caixa**. Ele deve sumir na aba **Relatórios**.
2. **ModalBottomSheet Expandida:**
   - Ao clicar no FAB, disparar a abertura de uma `ModalBottomSheet`.
   - Configurar o comportamento da sheet para abrir no modo expandido (ocupando o topo/totalidade da tela).
3. **Campos do Formulario (Dentro da BottomSheet):**
   - **Cabeçalho:** Título "Novo Lançamento" e um botão "X" para fechar.
   - **Seletor de Tipo:** Um componente de botões segmentados (Segmented Buttons) para alternar entre **Despesa** e **Receita**.
   - **Campo de Valor:** Um campo de texto destacado focado no valor monetário (ex: R$ 0,00).
   - **Descrição:** Campo de texto simples para o nome/descrição do lançamento.
   - **Data:** Um campo ou botão que simula a abertura de um seletor de data (DatePicker).
   - **Seletores de Conta e Categoria:** Dois campos em formato Dropdown (Menus suspensos) exibindo as opções mockadas (ex: Contas: "Carteira", "Banco X" | Categorias: "Alimentação", "Salário").
   - **Botão Salvar:** Um botão de destaque no rodapé escrito "Salvar". Ao clicar, ele deve apenas fechar a BottomSheet (simulando o sucesso).

## Critérios de Aceitação
- [X] O FAB com ícone de `+` é exibido corretamente nas telas Home e Fluxo de Caixa, e desaparece na tela Relatórios.
- [X] Ao clicar no FAB, a `ModalBottomSheet` abre de forma fluida ocupando a totalidade da tela.
- [X] Todos os campos do formulário (Tipo, Valor, Descrição, Data, Conta, Categoria e Botão Salvar) estão renderizados e alinhados verticalmente dentro da sheet.
- [X] Os seletores de tipo (Despesa/Receita) alteram seu estado visual quando clicados.
- [X] Ao clicar no botão "Salvar" ou no "X", a BottomSheet fecha corretamente.

## Ajustes e Correções de Bugs (Refinamento)
- [X] **Correção de Layout:** No arquivo `LancamentoForm.kt`, remova a `Row` que envolve os seletores de **Conta** e **Categoria**. Altere o layout para que os dois componentes `DropdownSelector` fiquem empilhados verticalmente (um abaixo do outro), ambos utilizando `Modifier.fillMaxWidth()` para ocupar a largura total.
- [X] **Correção de API Level (Bug de Compilação):** O projeto possui o `minSdkVersion` definido como 24. No arquivo `LancamentoForm.kt`, o uso direto de `java.time.LocalDate#format` e `DateTimeFormatter` está quebrando a compilação (Requer API 26). Corrija adicionando uma checagem de versão do Android (`if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)`) ou utilize uma abordagem compatível com a API 24 (como o padrão `java.util.Date` e `SimpleDateFormat` para formatação da data exibida).
