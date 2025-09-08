#!/bin/bash

# Script para configurar el pipeline directamente en la instancia de Google Cloud
set -e

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para crear el directorio del pipeline
create_pipeline_directory() {
    log "Creando directorio del pipeline..."
    mkdir -p ~/pipeline-project
    cd ~/pipeline-project
    log "✅ Directorio creado: ~/pipeline-project"
}

# Función para crear el archivo .drone.yml
create_drone_yml() {
    log "Creando archivo .drone.yml..."
    cat > .drone.yml << 'EOF'
# ========================================
# PIPELINE CI/CD HOSPITAL - 11 PASOS
# ========================================
# Pipeline que sigue el flujo exacto de Jenkins
# con 11 pasos secuenciales

# Variables globales
---
kind: pipeline
type: docker
name: global-config
steps:
  - name: detect-environment
    image: alpine:latest
    commands:
      - echo "🔍 Detectando ambiente basado en rama: ${DRONE_BRANCH}"
      - |
        case "${DRONE_BRANCH}" in
          prod)
            echo "ENVIRONMENT=prod" >> /tmp/env
            echo "DOCKER_COMPOSE_FILE=docker-compose.prod.yml" >> /tmp/env
            echo "PORT_PREFIX=82" >> /tmp/env
            echo "DB_PORT=1523" >> /tmp/env
            echo "NODE_ENV=production" >> /tmp/env
            echo "LOG_LEVEL=warn" >> /tmp/env
            ;;
          QA)
            echo "ENVIRONMENT=qa" >> /tmp/env
            echo "DOCKER_COMPOSE_FILE=docker-compose.qa.yml" >> /tmp/env
            echo "PORT_PREFIX=81" >> /tmp/env
            echo "DB_PORT=1522" >> /tmp/env
            echo "NODE_ENV=qa" >> /tmp/env
            echo "LOG_LEVEL=info" >> /tmp/env
            ;;
          dev|feature/*)
            echo "ENVIRONMENT=dev" >> /tmp/env
            echo "DOCKER_COMPOSE_FILE=docker-compose.dev.yml" >> /tmp/env
            echo "PORT_PREFIX=80" >> /tmp/env
            echo "DB_PORT=1521" >> /tmp/env
            echo "NODE_ENV=development" >> /tmp/env
            echo "LOG_LEVEL=debug" >> /tmp/env
            ;;
          *)
            echo "ENVIRONMENT=dev" >> /tmp/env
            echo "DOCKER_COMPOSE_FILE=docker-compose.dev.yml" >> /tmp/env
            echo "PORT_PREFIX=80" >> /tmp/env
            echo "DB_PORT=1521" >> /tmp/env
            echo "NODE_ENV=development" >> /tmp/env
            echo "LOG_LEVEL=debug" >> /tmp/env
            ;;
        esac
      - echo "📋 Configuración detectada:"
      - cat /tmp/env
    volumes:
      - name: env-config
        path: /tmp

# Pipeline principal con 11 pasos
---
kind: pipeline
type: docker
name: hospital-pipeline
depends_on: [global-config]

# Configuración del workspace
workspace:
  base: /drone/src
  path: .

# Variables de entorno
environment:
  DOCKER_REGISTRY: 34.10.223.20:5000
  PROJECT_NAME: hospital
  ENVIRONMENT: dev
  DOCKER_COMPOSE_FILE: docker-compose.dev.yml
  PORT_PREFIX: 80
  DB_PORT: 1521
  NODE_ENV: development
  LOG_LEVEL: debug

steps:
  # PASO 1: Setup Environment
  - name: setup-environment
    image: alpine:latest
    commands:
      - echo "🔧 PASO 1: Setup Environment"
      - source /tmp/env
      - echo "ENVIRONMENT=$ENVIRONMENT" >> /drone/src/.env
      - echo "DOCKER_COMPOSE_FILE=$DOCKER_COMPOSE_FILE" >> /drone/src/.env
      - echo "PORT_PREFIX=$PORT_PREFIX" >> /drone/src/.env
      - echo "DB_PORT=$DB_PORT" >> /drone/src/.env
      - echo "NODE_ENV=$NODE_ENV" >> /drone/src/.env
      - echo "LOG_LEVEL=$LOG_LEVEL" >> /drone/src/.env
      - echo "📋 Configuración cargada:"
      - cat /drone/src/.env
      - echo "✅ Setup Environment completado"
    volumes:
      - name: env-config
        path: /tmp

  # PASO 2: Build Backend
  - name: build-backend
    image: node:18-alpine
    commands:
      - echo "🔨 PASO 2: Build Backend"
      - cd backend
      - npm install
      - |
        case "${NODE_ENV}" in
          production)
            npm run build:prod
            ;;
          qa)
            npm run build:qa
            ;;
          *)
            npm run build
            ;;
        esac
      - echo "✅ Build Backend completado"
    depends_on: [setup-environment]

  # PASO 3: Unit Tests Backend
  - name: unit-tests-backend
    image: node:18-alpine
    commands:
      - echo "🧪 PASO 3: Unit Tests Backend"
      - cd backend
      - npm install
      - |
        case "${NODE_ENV}" in
          production)
            npm run test:prod
            ;;
          qa)
            npm run test:qa
            ;;
          *)
            npm run test
            ;;
        esac
      - echo "✅ Unit Tests Backend completados"
    depends_on: [build-backend]

  # PASO 4: Code Quality Check
  - name: code-quality-check
    image: sonarqube:latest
    commands:
      - echo "🔍 PASO 4: Code Quality Check"
      - |
        sonar-scanner \
          -Dsonar.projectKey=hospital-${ENVIRONMENT} \
          -Dsonar.sources=./backend \
          -Dsonar.host.url=${SONAR_HOST_URL} \
          -Dsonar.login=${SONAR_TOKEN} \
          -Dsonar.branch.name=${DRONE_BRANCH}
      - echo "✅ Code Quality Check completado"
    environment:
      SONAR_TOKEN: ${SONAR_TOKEN}
      SONAR_HOST_URL: ${SONAR_HOST_URL}
    depends_on: [unit-tests-backend]

  # PASO 5: Build Frontend
  - name: build-frontend
    image: node:18-alpine
    commands:
      - echo "🎨 PASO 5: Build Frontend"
      - cd frontend
      - npm install
      - |
        case "${NODE_ENV}" in
          production)
            npm run build:prod
            ;;
          qa)
            npm run build:qa
            ;;
          *)
            npm run build
            ;;
        esac
      - echo "✅ Build Frontend completado"
    depends_on: [code-quality-check]

  # PASO 6: Unit Tests Frontend
  - name: unit-tests-frontend
    image: node:18-alpine
    commands:
      - echo "🧪 PASO 6: Unit Tests Frontend"
      - cd frontend
      - npm install
      - npm run test
      - echo "✅ Unit Tests Frontend completados"
    depends_on: [build-frontend]

  # PASO 7: Integration Tests
  - name: integration-tests
    image: cypress/included:latest
    commands:
      - echo "🔗 PASO 7: Integration Tests"
      - npm install
      - npm run test:integration
      - echo "✅ Integration Tests completados"
    depends_on: [unit-tests-frontend]

  # PASO 8: Build Docker Images
  - name: build-docker-images
    image: plugins/docker
    settings:
      registry: ${DOCKER_REGISTRY}
      repo: ${DOCKER_REGISTRY}/${PROJECT_NAME}/backend
      tags: [${ENVIRONMENT}, latest-${ENVIRONMENT}, v${DRONE_BUILD_NUMBER}]
      dockerfile: Dockerfile
      context: ./backend
      build_args:
        - NODE_ENV=${NODE_ENV}
        - LOG_LEVEL=${LOG_LEVEL}
    depends_on: [integration-tests]

  # PASO 9: Deploy to Development
  - name: deploy-to-development
    image: plugins/docker-compose
    settings:
      config: docker-compose.dev.yml
      environment: dev
      compose_file: docker-compose.dev.yml
    environment:
      NODE_ENV: development
      DB_PORT: 1521
      LOG_LEVEL: debug
    when:
      branch: [dev, feature/*]
    depends_on: [build-docker-images]

  # PASO 10: Deploy to QA
  - name: deploy-to-qa
    image: plugins/docker-compose
    settings:
      config: docker-compose.qa.yml
      environment: qa
      compose_file: docker-compose.qa.yml
    environment:
      NODE_ENV: qa
      DB_PORT: 1522
      LOG_LEVEL: info
    when:
      branch: [QA]
    depends_on: [build-docker-images]

  # PASO 11: Deploy to Production
  - name: deploy-to-production
    image: plugins/docker-compose
    settings:
      config: docker-compose.prod.yml
      environment: prod
      compose_file: docker-compose.prod.yml
    environment:
      NODE_ENV: production
      DB_PORT: 1523
      LOG_LEVEL: warn
    when:
      branch: [prod]
    depends_on: [build-docker-images]

  # Health check final
  - name: health-check
    image: curlimages/curl
    commands:
      - echo "🏥 Ejecutando health check final..."
      - sleep 30
      - curl -f http://34.10.223.20:${PORT_PREFIX}/health || exit 1
      - echo "✅ Health check exitoso"
    depends_on: [deploy-to-development, deploy-to-qa, deploy-to-production]

  # Análisis con Grok AI (solo para producción)
  - name: grok-analysis
    image: curlimages/curl
    commands:
      - echo "🤖 Ejecutando análisis con Grok AI..."
      - |
        curl -X POST "${GROK_BASE_URL}/chat/completions" \
          -H "Authorization: Bearer ${GROK_API_KEY}" \
          -H "Content-Type: application/json" \
          -d '{
            "model": "llama-3.1-70b-versatile",
            "messages": [
              {
                "role": "system",
                "content": "Eres un asistente de análisis de código. Analiza el deployment y proporciona recomendaciones."
              },
              {
                "role": "user",
                "content": "Deployment completado en ambiente '${ENVIRONMENT}' para rama '${DRONE_BRANCH}'. Proporciona un resumen y recomendaciones."
              }
            ],
            "max_tokens": 500
          }' > grok_analysis.json
      - echo "📊 Análisis de Grok completado"
      - cat grok_analysis.json
    environment:
      GROK_API_KEY: ${GROK_API_KEY}
      GROK_BASE_URL: ${GROK_BASE_URL}
    when:
      branch: [prod]
    depends_on: [deploy-to-production]

  # Notificación final
  - name: notify-deployment
    image: plugins/slack
    settings:
      webhook: ${SLACK_WEBHOOK}
      channel: deployments
      message: "🚀 Hospital app deployed to ${ENVIRONMENT^^} environment (${DRONE_BRANCH}) - Pipeline completado exitosamente"
    when:
      status: success
    depends_on: [health-check, grok-analysis]

# Pipeline para Oracle Database (se ejecuta en paralelo)
---
kind: pipeline
type: docker
name: oracle-database
depends_on: []

steps:
  # Backup de base de datos
  - name: backup-database
    image: oracle/database:19.3.0-ee
    commands:
      - echo "💾 Creando backup de base de datos..."
      - expdp system/Oracle123@DB1 directory=DATA_PUMP_DIR dumpfile=backup_${DRONE_BUILD_NUMBER}.dmp schemas=HOSPITAL
    when:
      event: [push, pull_request]

  # Deploy de cambios de base de datos
  - name: deploy-database-changes
    image: oracle/database:19.3.0-ee
    commands:
      - echo "🗄️ Desplegando cambios de base de datos..."
      - sqlplus system/Oracle123@DB1 @database/migrations/001_initial_schema.sql
    when:
      branch: [prod]

# Configuración de triggers
trigger:
  branch: [prod, QA, dev, feature/*]
  event: [push, pull_request, tag]

# Volúmenes compartidos
volumes:
  - name: env-config
    temp: {}
EOF
    log "✅ Archivo .drone.yml creado"
}

# Función para crear el archivo de configuración de Drone
create_drone_config() {
    log "Creando configuración de Drone..."
    cat > docker-compose.drone.yml << 'EOF'
version: '3.8'

services:
  # Drone Server
  drone-server:
    image: drone/drone:2
    container_name: drone-server
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - drone_data:/data
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      # Configuración de Drone
      - DRONE_GITHUB_CLIENT_ID=${DRONE_GITHUB_CLIENT_ID}
      - DRONE_GITHUB_CLIENT_SECRET=${DRONE_GITHUB_CLIENT_SECRET}
      - DRONE_RPC_SECRET=${DRONE_RPC_SECRET}
      - DRONE_SERVER_HOST=${DRONE_SERVER_HOST}
      - DRONE_SERVER_PROTO=${DRONE_SERVER_PROTO}
      - DRONE_USER_CREATE=username:${DRONE_ADMIN_USER},admin:true
      - DRONE_LOGS_DEBUG=true
      - DRONE_LOGS_TRACE=true
      # Configuración de Docker
      - DRONE_DOCKER_ENABLED=true
      - DRONE_DOCKER_HOST=unix:///var/run/docker.sock
      # Configuración de ambientes
      - DRONE_ENVIRONMENT=dev,qa,prod
    restart: unless-stopped
    networks:
      - drone-network

  # Drone Runner
  drone-runner:
    image: drone/drone-runner-docker:1
    container_name: drone-runner
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      - DRONE_RPC_PROTO=${DRONE_RPC_PROTO}
      - DRONE_RPC_HOST=${DRONE_RPC_HOST}
      - DRONE_RPC_SECRET=${DRONE_RPC_SECRET}
      - DRONE_DEBUG=true
      - DRONE_TRACE=true
      # Configuración de Docker
      - DRONE_DOCKER_ENABLED=true
      - DRONE_DOCKER_HOST=unix:///var/run/docker.sock
      # Límites de recursos
      - DRONE_RUNNER_CAPACITY=3
      - DRONE_RUNNER_LABELS=os:linux,arch:amd64
    restart: unless-stopped
    depends_on:
      - drone-server
    networks:
      - drone-network

  # Nginx Reverse Proxy
  nginx:
    image: nginx:alpine
    container_name: drone-nginx
    ports:
      - "8080:80"
    volumes:
      - ./nginx/drone.conf:/etc/nginx/conf.d/default.conf
    depends_on:
      - drone-server
    restart: unless-stopped
    networks:
      - drone-network

volumes:
  drone_data:
    driver: local

networks:
  drone-network:
    driver: bridge
EOF
    log "✅ Configuración de Drone creada"
}

# Función para crear el archivo de variables de entorno
create_env_file() {
    log "Creando archivo de variables de entorno..."
    cat > drone.env << 'EOF'
# Drone CI/CD Configuration
# =========================

# GitHub OAuth App Configuration
# Crear en: https://github.com/settings/applications/new
DRONE_GITHUB_CLIENT_ID=your_github_client_id_here
DRONE_GITHUB_CLIENT_SECRET=your_github_client_secret_here

# GitHub Personal Access Token
GITHUB_TOKEN=your_github_token_here

# Drone Server Configuration
DRONE_SERVER_HOST=34.10.223.20:8080
DRONE_SERVER_PROTO=http

# RPC Configuration (comunicación entre server y runner)
DRONE_RPC_PROTO=http
DRONE_RPC_HOST=drone-server:80
DRONE_RPC_SECRET=your_super_secret_rpc_key_here

# Admin User
DRONE_ADMIN_USER=humbertovenavente

# Environment Configuration
DRONE_ENVIRONMENT=dev,qa,prod

# Docker Configuration
DRONE_DOCKER_ENABLED=true
DRONE_DOCKER_HOST=unix:///var/run/docker.sock

# Logging
DRONE_LOGS_DEBUG=true
DRONE_LOGS_TRACE=true
DRONE_DEBUG=true
DRONE_TRACE=true

# Runner Configuration
DRONE_RUNNER_CAPACITY=3
DRONE_RUNNER_LABELS=os:linux,arch:amd64

# SonarQube Configuration
SONAR_TOKEN=your_sonar_token_here
SONAR_HOST_URL=http://34.10.223.20:9000

# Grok Configuration
GROK_API_KEY=your_grok_api_key_here
GROK_BASE_URL=https://api.groq.com/openai/v1

# Slack Configuration (opcional)
SLACK_WEBHOOK=your_slack_webhook_here
EOF
    log "✅ Archivo de variables de entorno creado"
}

# Función para crear la configuración de Nginx
create_nginx_config() {
    log "Creando configuración de Nginx..."
    mkdir -p nginx
    cat > nginx/drone.conf << 'EOF'
server {
    listen 80;
    server_name _;

    # Drone Server
    location / {
        proxy_pass http://drone-server:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # WebSocket support
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # API endpoints
    location /api/ {
        proxy_pass http://drone-server:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # WebSocket for real-time updates
    location /ws/ {
        proxy_pass http://drone-server:80;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
EOF
    log "✅ Configuración de Nginx creada"
}

# Función para generar secretos
generate_secrets() {
    log "Generando secretos para Drone..."
    
    # Generar RPC secret
    RPC_SECRET=$(openssl rand -hex 16)
    sed -i "s/your_super_secret_rpc_key_here/$RPC_SECRET/g" drone.env
    
    log "✅ Secretos generados exitosamente"
}

# Función para mostrar información
show_info() {
    log "Información del despliegue:"
    echo ""
    echo "🚀 PIPELINE CI/CD HOSPITAL CONFIGURADO"
    echo "======================================"
    echo ""
    echo "📁 Directorio: ~/pipeline-project"
    echo "🔧 Archivos creados:"
    echo "  - .drone.yml (Pipeline de 11 pasos)"
    echo "  - docker-compose.drone.yml (Configuración de Drone)"
    echo "  - drone.env (Variables de entorno)"
    echo "  - nginx/drone.conf (Configuración de Nginx)"
    echo ""
    echo "🔑 Tokens configurados:"
    echo "  - GitHub: your_github_token_here"
    echo "  - SonarQube: your_sonar_token_here"
    echo "  - Grok: your_grok_api_key_here"
    echo ""
    echo "🏥 Ambientes configurados:"
    echo "  - Desarrollo (dev): Puerto 80, DB 1521"
    echo "  - QA: Puerto 81, DB 1522"
    echo "  - Producción (prod): Puerto 82, DB 1523"
    echo ""
    echo "📋 Próximos pasos:"
    echo "  1. Configurar GitHub OAuth en Drone"
    echo "  2. Ejecutar: docker-compose -f docker-compose.drone.yml --env-file drone.env up -d"
    echo "  3. Acceder a: http://34.10.223.20:8080"
    echo ""
}

# Función principal
main() {
    log "Iniciando configuración del pipeline CI/CD..."
    
    create_pipeline_directory
    create_drone_yml
    create_drone_config
    create_env_file
    create_nginx_config
    generate_secrets
    show_info
    
    log "Configuración completada exitosamente!"
    echo ""
    echo "🎉 PIPELINE DE 11 PASOS CONFIGURADO"
    echo "==================================="
    echo ""
    echo "El pipeline incluye:"
    echo "  ✅ Setup Environment"
    echo "  ✅ Build Backend"
    echo "  ✅ Unit Tests Backend"
    echo "  ✅ Code Quality Check (SonarQube)"
    echo "  ✅ Build Frontend"
    echo "  ✅ Unit Tests Frontend"
    echo "  ✅ Integration Tests"
    echo "  ✅ Build Docker Images"
    echo "  ✅ Deploy to Development"
    echo "  ✅ Deploy to QA"
    echo "  ✅ Deploy to Production"
    echo ""
    echo "¡Tu pipeline está listo para usar! 🚀"
}

# Ejecutar función principal
main "$@"
