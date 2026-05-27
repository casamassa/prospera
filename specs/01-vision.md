# Visão Geral do Projeto - Organizador Financeiro Local

## Objetivo
Desenvolver um aplicativo Android nativo para organização financeira pessoal. O aplicativo funcionará de forma 100% off-line nesta fase inicial, priorizando a privacidade e a velocidade.

## Stack Tecnológica
- **Linguagem:** Kotlin
- **Interface:** Jetpack Compose (Material Design 3)
- **Arquitetura:** MVVM (Model-View-ViewModel) + Clean Architecture princípios básicos
- **Navegação:** Jetpack Navigation Compose
- **Banco de Dados (Fase Futura):** Room SQLite

## Diretrizes do MVP (Fase 2: Persistência e Regras de Negócio)
- Idioma único: Português (pt-BR) | Moeda única: Real (R$).
- **Foco atual:** Substituir os dados mockados das ViewModels por persistência real utilizando o Room Database.
- Implementar as tabelas e DAOs respeitando as chaves estrangeiras e integridade referencial.
- Toda operação de escrita no banco que altere saldos deve ocorrer dentro de transações na camada Domain/Data.

