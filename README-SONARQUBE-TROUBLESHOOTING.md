# 🔍 Solución de Problemas de SonarQube - Hospital Management System

## Problema Identificado

El análisis del frontend con SonarQube está fallando debido a problemas del **bridge server** para JavaScript/TypeScript:

```
ERROR: The bridge server is unresponsive
java.net.http.HttpTimeoutException: request timed out
```

## Soluciones Implementadas

### 1. Scripts Robustos de Análisis

Se han creado scripts específicos para manejar mejor los problemas de timeout:

- **`analyze-frontend-sonar.sh`** - Script principal para análisis del frontend
- **`run-sonarqube-analysis.sh`** - Script genérico con reintentos
- **`test-sonarqube-frontend.sh`** - Script de verificación de configuración

### 2. Configuración Mejorada

#### Archivo `sonar-project-frontend.properties`
```properties
# Timeouts aumentados para evitar problemas de bridge server
sonar.javascript.timeout=600000
sonar.typescript.timeout=600000
sonar.javascript.bridge.timeout=600000
sonar.javascript.bridge.connectionTimeout=600000
sonar.javascript.bridge.readTimeout=600000
sonar.javascript.bridge.serverTimeout=600000
sonar.javascript.bridge.keepAlive=true
sonar.javascript.bridge.maxRetries=5

# Configuración de memoria
sonar.javascript.bridge.memory=4096
sonar.javascript.bridge.maxMemory=8192
```

#### Jenkinsfile Modificado
El pipeline ahora usa el script robusto cuando está disponible:

```groovy
// ANÁLISIS DEL FRONTEND (usando script robusto)
if [ -f "./analyze-frontend-sonar.sh" ]; then
    echo "   Usando script robusto para análisis del frontend..."
    chmod +x ./analyze-frontend-sonar.sh
    ./analyze-frontend-sonar.sh
else
    echo "   Script robusto no encontrado, usando configuración estándar..."
    // Configuración robusta inline
fi
```

## Cómo Usar

### 1. Verificar Configuración
```bash
./test-sonarqube-frontend.sh
```

### 2. Ejecutar Análisis del Frontend
```bash
# Usando el script robusto
./analyze-frontend-sonar.sh

# O usando el script genérico
./run-sonarqube-analysis.sh hospital-frontend 'Hospital Frontend' 1.0
```

### 3. Variables de Entorno Requeridas
```bash
export SONAR_HOST=http://localhost:9000
export SONAR_TOKEN=tu-token-aqui
export BUILD_NUMBER=1.0
```

## Configuraciones Adicionales

### 1. Aumentar Memoria del Scanner
```bash
export SONAR_SCANNER_OPTS="-Xmx4g -XX:MaxPermSize=512m"
```

### 2. Configuración de Jenkins
En Jenkins, asegúrate de que las credenciales estén configuradas:
- **SonarQube servers**: `SonarQube` → `http://localhost:9000`
- **Credentials**: `sonarqube-token` con tu token de SonarQube

### 3. Verificar SonarQube
```bash
# Verificar estado
curl -f http://localhost:9000/api/system/status

# Verificar plugins JavaScript/TypeScript
curl -s http://localhost:9000/api/plugins/installed | grep -i javascript
```

## Troubleshooting Común

### Problema: Bridge Server Timeout
**Síntomas**: `The bridge server is unresponsive`
**Solución**: 
- Aumentar timeouts en la configuración
- Usar scripts robustos con reintentos
- Verificar memoria disponible

### Problema: Archivos TypeScript No Encontrados
**Síntomas**: `File '@vue/tsconfig/tsconfig.dom.json' not found`
**Solución**:
- Usar `tsconfig.app.json` en lugar de `tsconfig.json`
- Verificar que las dependencias estén instaladas
- Limpiar directorio `.scannerwork`

### Problema: Memoria Insuficiente
**Síntomas**: `OutOfMemoryError`
**Solución**:
- Aumentar `SONAR_SCANNER_OPTS`
- Configurar `sonar.javascript.bridge.memory`
- Reducir archivos analizados con exclusiones

## Estructura de Archivos

```
Hospital/
├── analyze-frontend-sonar.sh          # Script robusto para frontend
├── run-sonarqube-analysis.sh          # Script genérico con reintentos
├── test-sonarqube-frontend.sh         # Script de verificación
├── sonar-project-frontend.properties  # Configuración del frontend
├── sonar-project-backend.properties   # Configuración del backend
└── Jenkinsfile                        # Pipeline con scripts robustos
```

## Monitoreo y Logs

### 1. Logs de SonarQube
```bash
# Ver logs del servidor
docker logs sonarqube

# Ver logs del scanner
sonar-scanner -Dsonar.log.level=DEBUG
```

### 2. Métricas de Calidad
- **Backend**: http://localhost:9000/dashboard?id=hospital-backend
- **Frontend**: http://localhost:9000/dashboard?id=hospital-frontend

### 3. Quality Gates
- Verificar que `sonar.qualitygate.wait=true` esté configurado
- Revisar reglas de calidad en SonarQube

## Próximos Pasos

1. **Ejecutar análisis del frontend** usando los scripts robustos
2. **Monitorear logs** para identificar problemas específicos
3. **Ajustar configuración** según el comportamiento observado
4. **Integrar en CI/CD** con Jenkins usando el Jenkinsfile modificado

## Contacto

Para problemas adicionales, revisar:
- Logs de Jenkins
- Logs de SonarQube
- Configuración de plugins JavaScript/TypeScript
- Memoria y recursos del sistema
