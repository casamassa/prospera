\# Arquitetura do Projeto e Estrutura de Pastas



Para manter o projeto expansível para testes no futuro, utilizaremos uma estrutura separada por camadas (Features/Layers). O Gemini deve criar os arquivos seguindo rigorosamente a estrutura abaixo:



```text

com.seuusuario.financeiro/

│

├── data/                  # Futura camada de dados (Room, DTOs)

│

├── domain/                # Futura camada de negócio (Models, UseCases)

│

├── presentation/          # Camada de UI (MVVM)

│   ├── navigation/        # Configuração do NavHost e rotas da BottomBar

│   ├── theme/             # Cores, Tipografia e Temas do Material 3

│   │

│   ├── home/              # Feature Home

│   │   ├── HomeScreen.kt  # View (Compose)

│   │   └── HomeViewModel.kt

│   │

│   └── fluxo/             # Feature Fluxo de Caixa

│       ├── FluxoScreen.kt # View (Compose)

│       └── FluxoViewModel.kt

```



\## Regras de Codificação para UI

\- Use componentes do \*\*Material Design 3\*\*.

\- Todo estado da tela deve vir do `ViewModel` correspondente via `StateFlow` ou `mutableStateOf`.

\- Dados de exibição nesta Fase 1 devem ser mockados diretamente no ViewModel.



