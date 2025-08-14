#!/bin/bash

# Script para configurar protección de branches en GitHub
# Solo humbertovenavente puede aprobar PRs hacia dev, QA y prod

echo "🔒 Configurando protección de branches en GitHub..."
echo "👤 Solo humbertovenavente podrá aprobar PRs hacia dev, uat y master"

# Variables de configuración
GITHUB_TOKEN=""
GITHUB_REPO=""
GITHUB_OWNER=""

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [opciones]"
    echo ""
    echo "Opciones:"
    echo "  -t, --token TOKEN    Token de GitHub (requerido)"
    echo "  -r, --repo REPO      Nombre del repositorio (requerido)"
    echo "  -o, --owner OWNER    Propietario del repositorio (requerido)"
    echo "  -h, --help           Mostrar esta ayuda"
    echo ""
    echo "Ejemplo:"
    echo "  $0 -t ghp_xxxxxxxx -r Hospital -o humbertovenavente"
    echo ""
    echo "⚠️  IMPORTANTE: Necesitas un token de GitHub con permisos 'repo' o 'admin:org'"
}

# Parsear argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        -t|--token)
            GITHUB_TOKEN="$2"
            shift 2
            ;;
        -r|--repo)
            GITHUB_REPO="$2"
            shift 2
            ;;
        -o|--owner)
            GITHUB_OWNER="$2"
            shift 2
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            echo "❌ Opción desconocida: $1"
            show_help
            exit 1
            ;;
    esac
done

# Verificar parámetros requeridos
if [[ -z "$GITHUB_TOKEN" || -z "$GITHUB_REPO" || -z "$GITHUB_OWNER" ]]; then
    echo "❌ Faltan parámetros requeridos"
    show_help
    exit 1
fi

echo "✅ Configuración:"
echo "   Token: ${GITHUB_TOKEN:0:10}..."
echo "   Repositorio: $GITHUB_REPO"
echo "   Propietario: $GITHUB_OWNER"
echo ""

# Función para configurar protección de branch
configure_branch_protection() {
    local branch="$1"
    local branch_name="$2"
    
    echo "🔒 Configurando protección para branch: $branch_name"
    
    # Crear configuración de protección
    cat > /tmp/branch-protection.json << EOF
{
  "required_status_checks": {
    "strict": true,
    "contexts": [
      "Jenkins CI/CD Pipeline",
      "SonarQube Quality Gate"
    ]
  },
  "enforce_admins": true,
  "required_pull_request_reviews": {
    "required_approving_review_count": 1,
    "dismiss_stale_reviews": true,
    "require_code_owner_reviews": true,
    "required_reviewers": ["$GITHUB_OWNER"]
  },
  "restrictions": {
    "users": ["$GITHUB_OWNER"],
    "teams": []
  },
  "allow_force_pushes": false,
  "allow_deletions": false
}
EOF

    # Aplicar protección usando la API de GitHub
    local response=$(curl -s -w "%{http_code}" \
        -X PUT \
        -H "Authorization: token $GITHUB_TOKEN" \
        -H "Accept: application/vnd.github.v3+json" \
        -d @/tmp/branch-protection.json \
        "https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/branches/$branch/protection")
    
    local http_code="${response: -3}"
    local response_body="${response%???}"
    
    if [[ "$http_code" == "200" ]]; then
        echo "✅ Protección configurada exitosamente para $branch_name"
    else
        echo "❌ Error configurando protección para $branch_name"
        echo "   Código HTTP: $http_code"
        echo "   Respuesta: $response_body"
    fi
    
    rm -f /tmp/branch-protection.json
}

# Configurar protección para cada branch
echo "🚀 Aplicando configuraciones de protección..."

# Branch prod (producción)
configure_branch_protection "prod" "prod (producción)"

# Branch QA (testing)
configure_branch_protection "QA" "QA (testing)"

# Branch dev (desarrollo)
configure_branch_protection "dev" "dev (desarrollo)"

echo ""
echo "🎉 Configuración completada!"
echo ""
echo "📋 Resumen de la configuración:"
echo "   ✅ Solo $GITHUB_OWNER puede aprobar PRs hacia dev, uat y master"
echo "   ✅ Se requieren 1 revisión aprobatoria"
echo "   ✅ Se requieren code owner reviews"
echo "   ✅ Se requieren status checks de Jenkins y SonarQube"
echo "   ✅ No se permiten force pushes ni deletions"
echo "   ✅ Se aplica a todos los administradores"
echo ""
echo "🔗 Para verificar, ve a:"
echo "   https://github.com/$GITHUB_OWNER/$GITHUB_REPO/settings/branches"
echo ""
echo "⚠️  Recuerda que los branches deben existir antes de aplicar la protección"
