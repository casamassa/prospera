# Task 008: Configuração do Pipeline de CI/CD (GitHub Actions)

## Contexto
Implementar uma esteira automatizada de Integração Contínua (CI) usando GitHub Actions. O pipeline deve ser disparado automaticamente a cada `push` ou `pull request` nas ramificações principais, garantindo a integridade do build e a qualidade do código gerado por IA.

## Configuração do Pipeline (Workflow YAML)
1. **Gatilhos (Triggers):**
   - Ativar o pipeline em qualquer `push` ou `pull_request` nas branches `main`, `master` ou `develop`.
2. **Ambiente de Execução:**
   - Rodar em uma máquina virtual Linux atualizada (`ubuntu-latest`).
3. **Etapas de Execução (Steps):**
   - **Checkout:** Baixar o código do repositório.
   - **Setup Java:** Configurar o ambiente Java com a versão correta do JDK utilizada no projeto (ex: Java 17).
   - **Setup Gradle:** Configurar e dar permissão de execução ao Gradle Wrapper (`chmod +x gradlew`).
   - **Lint Check:** Executar a checagem de qualidade e formatação de código com `./gradlew lintDebug`.
   - **Assemble Build:** Compilar o aplicativo no modo de depuração com `./gradlew assembleDebug` para garantir que não existam erros de compilação.
   - **Arquivamento do APK (Artefato):** Salvar o arquivo `.apk` gerado pelo build como um artefato do GitHub, permitindo o download direto do app direto pela interface do repositório.

## Critérios de Aceitação
- [ ] Criar o arquivo de configuração no caminho correto da raiz do projeto: `.github/workflows/android.yml`.
- [ ] O arquivo YAML deve estar formatado corretamente, sem erros de indentação.
- [ ] O pipeline utiliza ações oficiais e atualizadas do GitHub (como `actions/checkout` e `actions/setup-java`).
- [ ] O processo de build aponta para as tarefas padrões do Gradle Android (`lintDebug` e `assembleDebug`).
- [ ] O artefato final do APK gerado é exposto para download com sucesso ao final da execução.
