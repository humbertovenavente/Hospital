# 🔒 Configuración de Protección de Branches en GitHub

## 🎯 Objetivo
Configurar tu usuario `humbertovenavente` como el **único** que puede aprobar pull requests hacia los branches:
- `dev` (desarrollo)
- `QA` (testing) 
- `prod` (producción)

## 🚀 Configuración Automática (Recomendado)

### 1. Crear Token de GitHub
1. Ve a [GitHub Settings > Developer settings > Personal access tokens](https://github.com/settings/tokens)
2. Crea un nuevo token con permisos `repo` o `admin:org`
3. Copia el token generado

### 2. Ejecutar Script Automático
```bash
./setup-github-protection.sh -t TU_TOKEN_AQUI -r Hospital -o humbertovenavente
```

**Ejemplo:**
```bash
./setup-github-protection.sh -t ghp_1234567890abcdef -r Hospital -o humbertovenavente
```

## ⚙️ Configuración Manual (Alternativa)

### 1. Ir a Settings del Repositorio
Ve a: `https://github.com/humbertovenavente/Hospital/settings/branches`

### 2. Configurar Protección para cada Branch

#### Branch: `prod`
- ✅ **Require a pull request before merging**
- ✅ **Require approvals**: 1
- ✅ **Dismiss stale PR reviews when new commits are pushed**
- ✅ **Require review from code owners**
- ✅ **Restrict pushes that create files that match the specified pattern**
- ✅ **Require status checks to pass before merging**
  - Jenkins CI/CD Pipeline
  - SonarQube Quality Gate
- ✅ **Require branches to be up to date before merging**
- ✅ **Include administrators**
- ✅ **Restrict who can push to matching branches**
  - Solo `humbertovenavente`

#### Branch: `QA`
- Misma configuración que `prod`

#### Branch: `dev`
- Misma configuración que `prod`

## 📋 Verificación

Después de la configuración:

1. **Crear un PR** desde cualquier branch hacia `dev`, `QA` o `prod`
2. **Verificar** que solo `humbertovenavente` puede aprobar
3. **Confirmar** que se requieren los status checks de Jenkins y SonarQube

## 🔧 Archivos Creados

- `.github/branch-protection.yml` - Configuración de protección
- `.github/CODEOWNERS` - Propietario del código
- `setup-github-protection.sh` - Script de configuración automática

## ⚠️ Importante

- Los branches `dev`, `QA` y `prod` deben existir antes de aplicar la protección
- Solo `humbertovenavente` podrá hacer merge hacia estos branches
- Se requieren status checks exitosos de Jenkins y SonarQube
- Se requieren code owner reviews

## 🆘 Solución de Problemas

### Error: "Branch not found"
- Asegúrate de que los branches `dev`, `uat` y `master` existan
- Crea los branches si no existen

### Error: "Insufficient permissions"
- Verifica que tu token tenga permisos `repo` o `admin:org`
- Asegúrate de ser administrador del repositorio

### Error: "Required status checks are not set"
- Verifica que Jenkins y SonarQube estén configurados
- Los status checks se configurarán automáticamente

## 📞 Soporte

Si tienes problemas, verifica:
1. Permisos del token de GitHub
2. Existencia de los branches
3. Permisos de administrador en el repositorio
