# Task 006: Gerenciamento de Contas via Aba Configurações (CRUD UI Estático)

## Contexto
Desenvolver a interface visual para o gerenciamento de contas bancárias. O usuário acessará essa funcionalidade através de uma nova aba "Configurações" instalada no menu inferior (`BottomBar`). Todo o comportamento de persistência será simulado com dados mockados no ViewModel.

## Desenho da Interface e Fluxos (UI)
1. **Nova Aba na BottomBar:**
   - Adicionar uma quarta opção no menu fixo inferior chamada **Configurações** (Ícone de engrenagem).
   - Configurar o `NavHost` para incluir a rota da tela de Configurações.
2. **Tela de Configurações (`ConfiguracoesScreen.kt`):**
   - Uma tela simples com uma lista de opções de ajustes. A primeira e principal opção deve ser um card ou botão escrito "Gerenciar Contas".
   - Ao clicar em "Gerenciar Contas", o app deve navegar (ou alternar a visualização) para a listagem de contas.
3. **Tela de Listagem de Contas (`ContasScreen.kt`):**
   - **Topo:** Botão de voltar (para retornar à tela de Configurações) e o título "Minhas Contas".
   - **Corpo:** Uma lista (`LazyColumn`) mostrando todas as contas existentes. Cada item exibe o nome da conta e o saldo inicial. Ao lado de cada conta, exibir um ícone de lápis (Editar) e um de lixeira (Excluir).
   - **Rodapé ou FAB:** Um botão de destaque escrito "Nova Conta".
4. **Formulários de Adicionar/Editar (`AlertDialog`):**
   - Ao clicar em "Nova Conta" ou no ícone de "Editar", abrir uma caixa de diálogo (`AlertDialog`).
   - O diálogo deve conter os seguintes campos de entrada de texto:
     - Nome da Conta (ex: Banco Inter, Dinheiro em Espécie).
     - Saldo Inicial (campo formatado em R$).
   - Botões de ação no diálogo: "Cancelar" (fecha o diálogo) e "Salvar" (simula o salvamento e fecha o diálogo).

## Critérios de Aceitação
- [X] O menu inferior (`BottomBar`) agora exibe 4 itens: Home, Fluxo de Caixa, Relatórios e Configurações.
- [X] A navegação para a `ConfiguracoesScreen` funciona corretamente ao clicar no ícone de engrenagem.
- [X] Criar a `ContasScreen.kt` e estruturar o layout de listagem com dados estáticos do ViewModel.
- [X] Ao clicar em "Nova Conta", uma `AlertDialog` de formulário é exibida em destaque na tela.
- [X] Ao clicar no ícone de "Editar" de uma conta da lista, a mesma `AlertDialog` abre simulando a edição com os campos preenchidos.
- [X] Os botões "Salvar" e "Cancelar" do diálogo fecham o componente visual perfeitamente ao serem clicados.
- [X] Certifique-se de que nenhum código utilize APIs do Android superiores ao `minSdkVersion 24` para evitar bugs de compilação.
