#!/bin/bash

# Script para enviar reportes de deuda técnica por email
# Uso: ./send-technical-debt-report.sh [email]

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración
API_URL="http://localhost:8090"
DEFAULT_EMAIL="humbertovenavente7@gmail.com"

# Función para mostrar ayuda
show_help() {
    echo -e "${BLUE}📧 Script de Reportes de Deuda Técnica${NC}"
    echo ""
    echo "Uso: $0 [email]"
    echo ""
    echo "Argumentos:"
    echo "  email    Email del destinatario (opcional, por defecto: $DEFAULT_EMAIL)"
    echo ""
    echo "Ejemplos:"
    echo "  $0                           # Envía a $DEFAULT_EMAIL"
    echo "  $0 tu@email.com              # Envía a tu@email.com"
    echo "  $0 jefe@hospital.com         # Envía a jefe@hospital.com"
    echo ""
    echo "Proyectos disponibles:"
    echo "  - hospital-backend-prod      (Backend Producción)"
    echo "  - hospital-frontend-prod     (Frontend Producción)"
    echo ""
}

# Función para verificar si el backend está funcionando
check_backend() {
    echo -e "${BLUE}🔍 Verificando backend...${NC}"
    
    if curl -s "$API_URL/health" > /dev/null 2>&1; then
        echo -e "${GREEN}Backend funcionando en $API_URL${NC}"
        return 0
    else
        echo -e "${RED}Backend no disponible en $API_URL${NC}"
        echo -e "${YELLOW}Ssegúrate de que el backend esté ejecutándose${NC}"
        return 1
    fi
}

# Función para enviar reporte de backend
send_backend_report() {
    local email=$1
    local project_key="hospital-backend-prod"
    local project_name="Hospital Backend - PRODUCCIÓN (Java/Quarkus)"
    
    echo -e "${BLUE}Enviando reporte de deuda técnica del BACKEND...${NC}"
    echo -e "   Proyecto: $project_name"
    echo -e "   Email: $email"
    
    local response=$(curl -s -X POST "$API_URL/api/email/technical-debt" \
        -H "Content-Type: application/json" \
        -d "{
            \"projectKey\": \"$project_key\",
            \"projectName\": \"$project_name\",
            \"recipientEmail\": \"$email\"
        }")
    
    if echo "$response" | grep -q '"success":true'; then
        echo -e "${GREEN}Reporte del BACKEND enviado exitosamente${NC}"
        return 0
    else
        echo -e "${RED}Error enviando reporte del BACKEND${NC}"
        echo -e "${YELLOW}Respuesta: $response${NC}"
        return 1
    fi
}

# Función para enviar reporte de frontend
send_frontend_report() {
    local email=$1
    local project_key="hospital-frontend-prod"
    local project_name="Hospital Frontend - PRODUCCIÓN (Vue.js/TypeScript)"
    
    echo -e "${BLUE}Enviando reporte de deuda técnica del FRONTEND...${NC}"
    echo -e "   Proyecto: $project_name"
    echo -e "   Email: $email"
    
    local response=$(curl -s -X POST "$API_URL/api/email/technical-debt" \
        -H "Content-Type: application/json" \
        -d "{
            \"projectKey\": \"$project_key\",
            \"projectName\": \"$project_name\",
            \"recipientEmail\": \"$email\"
        }")
    
    if echo "$response" | grep -q '"success":true'; then
        echo -e "${GREEN}Reporte del FRONTEND enviado exitosamente${NC}"
        return 0
    else
        echo -e "${RED}Error enviando reporte del FRONTEND${NC}"
        echo -e "${YELLOW}Respuesta: $response${NC}"
        return 1
    fi
}

# Función para enviar reporte de múltiples proyectos
send_multi_project_report() {
    local email=$1
    
    echo -e "${BLUE}Enviando reporte de múltiples proyectos...${NC}"
    echo -e "   Email: $email"
    
    local response=$(curl -s -X POST "$API_URL/api/email/technical-debt/multi-project" \
        -H "Content-Type: application/json" \
        -d "{
            \"projects\": [
                {
                    \"key\": \"hospital-backend-prod\",
                    \"name\": \"Hospital Backend - PRODUCCIÓN (Java/Quarkus)\"
                },
                {
                    \"key\": \"hospital-frontend-prod\",
                    \"name\": \"Hospital Frontend - PRODUCCIÓN (Vue.js/TypeScript)\"
                }
            ],
            \"recipientEmail\": \"$email\"
        }")
    
    if echo "$response" | grep -q '"success":true'; then
        echo -e "${GREEN}Reporte de múltiples proyectos enviado exitosamente${NC}"
        return 0
    else
        echo -e "${RED}Error enviando reporte de múltiples proyectos${NC}"
        echo -e "${YELLOW}Respuesta: $response${NC}"
        return 1
    fi
}

# Función principal
main() {
    # Verificar argumentos
    if [[ "$1" == "-h" || "$1" == "--help" ]]; then
        show_help
        exit 0
    fi
    
    # Email del destinatario
    local email=${1:-$DEFAULT_EMAIL}
    
    echo -e "${BLUE} Iniciando envío de reportes de deuda técnica...${NC}"
    echo -e "   Email destinatario: $email"
    echo ""
    
    # Verificar backend
    if ! check_backend; then
        exit 1
    fi
    
    echo ""
    
    # Enviar reportes
    local success_count=0
    local total_count=0
    
    # Reporte de backend
    total_count=$((total_count + 1))
    if send_backend_report "$email"; then
        success_count=$((success_count + 1))
    fi
    
    echo ""
    
    # Reporte de frontend
    total_count=$((total_count + 1))
    if send_frontend_report "$email"; then
        success_count=$((success_count + 1))
    fi
    
    echo ""
    
    # Resumen
    echo -e "${BLUE} Resumen del envío:${NC}"
    echo -e "   Exitosos: $success_count/$total_count"
    echo -e "   Fallidos: $((total_count - success_count))/$total_count"
    
    if [[ $success_count -eq $total_count ]]; then
        echo -e "${GREEN}¡Todos los reportes fueron enviados exitosamente!${NC}"
        echo -e "${BLUE} Revisa tu bandeja de entrada en: $email${NC}"
    else
        echo -e "${YELLOW}Algunos reportes fallaron. Revisa los errores arriba.${NC}"
        exit 1
    fi
}

# Ejecutar función principal
main "$@"
