# Hospital Management System

Sistema de gestión hospitalaria desarrollado con Vue.js, Quarkus y Oracle Database.

## 🏥 Características

- Gestión de pacientes
- Gestión de doctores
- Gestión de citas médicas
- Sistema de recetas
- Reportes y estadísticas
- Interfaz web moderna

## 🚀 Pipeline CI/CD

Este proyecto incluye un pipeline completo de CI/CD con Jenkins que maneja:

- ✅ Pull Requests automáticos
- ✅ Despliegues por ambiente (dev, QA, prod)
- ✅ Tests automatizados
- ✅ Notificaciones por email
- ✅ Health checks y rollback

## 📋 Prueba del Pipeline

**Fecha de prueba**: $(date)

Este cambio es para probar el pipeline de CI/CD.

# Proyecto2025

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Type Support for `.vue` Imports in TS

TypeScript cannot handle type information for `.vue` imports by default, so we replace the `tsc` CLI with `vue-tsc` for type checking. In editors, we need [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) to make the TypeScript language service aware of `.vue` types.

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Type-Check, Compile and Minify for Production

```sh
npm run build
```

### Run Unit Tests with [Vitest](https://vitest.dev/)

```sh
npm run test:unit
```

### Run End-to-End Tests with [Playwright](https://playwright.dev)

```sh
# Install browsers for the first run
npx playwright install

# When testing on CI, must build the project first
npm run build

# Runs the end-to-end tests
npm run test:e2e
# Runs the tests only on Chromium
npm run test:e2e -- --project=chromium
# Runs the tests of a specific file
npm run test:e2e -- tests/example.spec.ts
# Runs the tests in debug mode
npm run test:e2e -- --debug
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```
cd backend

./mvnw quarkus:dev Este es el que debemos usar para el backend ahora por los test
mvn quarkus:dev
mvn quarkus:dev "-Dquarkus.http.host=0.0.0.0"


npm run dev -- --host # Test webhook - Tue Aug 12 09:09:04 PM CST 2025
