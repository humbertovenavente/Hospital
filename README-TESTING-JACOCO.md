# 🧪 Configuración de Prueba para JaCoCo en Jenkins

Este directorio contiene una configuración simplificada para probar la integración de JaCoCo con SonarQube en Jenkins.

## 📁 Archivos de Prueba

### 1. `Jenkinsfile.test`
Pipeline de Jenkins simplificado que incluye:
- ✅ Checkout del código
- ✅ Configuración del entorno
- ✅ Build del backend
- ✅ Ejecución de tests unitarios
- ✅ **Análisis de SonarQube con JaCoCo**
- ✅ Manejo de errores

### 2. `sonar-project-test.properties`
Configuración de SonarQube para testing:
- Clave del proyecto: `hospital-backend-test`
- Configuración de JaCoCo para cobertura
- Configuración básica de Java 17

### 3. `test-jacoco.sh`
Script de verificación que:
- ✅ Verifica la configuración de Maven
- ✅ Ejecuta tests con JaCoCo
- ✅ Genera reportes de cobertura
- ✅ Verifica la configuración de SonarQube
- ✅ Muestra resumen completo

## 🚀 Cómo Usar en Jenkins

### Paso 1: Preparar Jenkins
```bash
# Copiar el Jenkinsfile de prueba
cp Jenkinsfile.test Jenkinsfile

# Configurar variables de entorno en Jenkins:
# SONAR_HOST=http://tu-servidor-sonar:9000
# SONAR_TOKEN=tu-token-sonar
```

### Paso 2: Ejecutar el Pipeline
1. Crear un nuevo job en Jenkins
2. Configurar como "Pipeline script from SCM"
3. Apuntar al repositorio Git
4. Ejecutar el build

### Paso 3: Verificar Resultados
- ✅ Build exitoso
- ✅ Tests ejecutados (174 tests)
- ✅ Reportes JaCoCo generados
- ✅ Análisis de SonarQube completado

## 📊 Configuración de JaCoCo

### En el Pipeline:
```bash
# Verificación de reportes JaCoCo
if [ -f target/site/jacoco/jacoco.xml ]; then
  echo "✅ Reporte de cobertura JaCoCo encontrado"
else
  echo "⚠️  Reporte de cobertura JaCoCo no encontrado"
fi

# Configuración de SonarQube con JaCoCo
sonar-scanner \
  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
  -Dsonar.coverage.jacoco.reportPaths=target/site/jacoco/jacoco.xml
```

### En SonarQube:
```properties
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
sonar.coverage.jacoco.reportPaths=target/site/jacoco/jacoco.xml
```

## 🔍 Verificación Local

### Ejecutar Script de Prueba:
```bash
chmod +x test-jacoco.sh
./test-jacoco.sh
```

### Verificar Reportes:
- **HTML**: `backend/target/site/jacoco/index.html`
- **XML**: `backend/target/site/jacoco/jacoco.xml`
- **CSV**: `backend/target/site/jacoco/jacoco.csv`

## 📈 Métricas de Cobertura

El análisis incluye:
- **Código fuente**: 125 clases analizadas
- **Tests unitarios**: 174 tests ejecutados
- **Cobertura de código**: Generada por JaCoCo
- **Calidad del código**: Analizada por SonarQube

## ⚠️ Consideraciones

### Variables de Entorno Requeridas:
- `SONAR_HOST`: URL del servidor SonarQube
- `SONAR_TOKEN`: Token de autenticación

### Configuración por Defecto:
- Si no se configuran las variables, usa valores de prueba
- `SONAR_HOST`: `http://localhost:9000`
- `SONAR_TOKEN`: `test-token`

## 🎯 Resultados Esperados

1. **Pipeline exitoso** con todos los stages completados
2. **Tests ejecutados** sin fallos
3. **Reportes JaCoCo** generados correctamente
4. **Análisis SonarQube** con métricas de cobertura
5. **Logs informativos** mostrando el progreso

## 🔧 Troubleshooting

### Si los tests fallan:
```bash
cd backend
mvn clean test
```

### Si JaCoCo no genera reportes:
```bash
cd backend
mvn clean test jacoco:report
```

### Si SonarQube falla:
- Verificar conectividad al servidor
- Verificar token de autenticación
- Verificar que el reporte JaCoCo existe

## 📝 Notas Adicionales

- Esta configuración es **SOLO PARA PRUEBAS**
- No usar en producción sin revisar configuración
- Los valores por defecto son para testing local
- Siempre configurar variables de entorno reales en Jenkins
