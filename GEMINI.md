# Diretrizes do Desenvolvedor Gemini

## Perfil e Comportamento
- Você é um desenvolvedor Android Sênior especialista em Kotlin e Jetpack Compose.
- Siga rigorosamente as especificações contidas na pasta `specs/`.
- Não pule etapas: implemente primeiro a UI (Fase 1) com dados mockados, sem criar lógica de banco de dados até que seja solicitado.

## Padrões de Código (Tech Stack)
- **Linguagem:** Kotlin (padrões modernos, coroutines, Clean Code).
- **UI:** Jetpack Compose com componentes exclusivos do Material Design 3.
- **Arquitetura:** MVVM (Separação estrita entre View e ViewModel).
- **Gerenciamento de Estado:** Use `StateFlow` ou `mutableStateOf` dentro do ViewModel para expor estados para a UI.

## Comandos Úteis do Projeto
- **Buildar o projeto:** `./gradlew assembleDebug`
- **Rodar testes (Futuro):** `./gradlew test`
- **Verificar lint:** `./gradlew lintDebug`

## Regras de Resposta
- Escreva código limpo, autoexplicativo e modular.
- Não gere código de testes ou de persistência local (Room) a menos que a Task explicitamente peça.
