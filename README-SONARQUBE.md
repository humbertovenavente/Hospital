# 🎯 Configuración de SonarQube - Hospital Management System

## 📋 **Resumen de la Configuración**

Siguiendo las mejores prácticas recomendadas por el equipo de ingeniería, hemos configurado **dos proyectos separados en SonarQube**:

1. **🏥 `hospital-backend`** - Proyecto Java/Quarkus (con cobertura de tests)
2. **🎨 `hospital-frontend`** - Proyecto Vue.js/TypeScript (calidad de código)

## 🚀 **Beneficios de esta Configuración**

### **Backend (`hospital-backend`):**
- ✅ **Cobertura de Tests**: Muestra el porcentaje de código cubierto por tests unitarios
- ✅ **Análisis Java**: Detección de bugs, vulnerabilidades y code smells específicos de Java
- ✅ **Métricas Quarkus**: Optimizado para aplicaciones Quarkus
- ✅ **Quality Gates**: Control de calidad basado en métricas de cobertura

### **Frontend (`hospital-frontend`):**
- ✅ **Calidad JavaScript/TypeScript**: Análisis específico para código frontend
- ✅ **Detección de Problemas**: Bugs, vulnerabilidades y code smells en JS/TS
- ✅ **Métricas Vue.js**: Optimizado para aplicaciones Vue.js
- ✅ **Análisis de Dependencias**: Revisión de paquetes npm

## 🔧 **Archivos de Configuración**

### **1. Jenkinsfile**
- Modificado para ejecutar análisis separados
- Genera dos proyectos en SonarQube
- Configuración automática según la rama

### **2. `sonar-project-backend.properties`**
- Configuración específica para el backend
- Incluye configuración de tests y cobertura
- Optimizado para Java 17 y Quarkus

### **3. `sonar-project-frontend.properties`**
- Configuración específica para el frontend
- Incluye configuración de TypeScript y Vue.js
- Optimizado para JavaScript/TypeScript

### **4. `run-sonarqube-analysis.sh`**
- Script para ejecutar análisis manuales
- Verifica conectividad con SonarQube
- Ejecuta análisis de ambos proyectos

## 📊 **Configuración de Cobertura**

### **Backend (JaCoCo):**
```xml
<!-- Plugin JaCoCo en pom.xml -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <!-- Configuración para generar reportes XML y HTML -->
</plugin>
```

### **Frontend (Vitest):**
```typescript
// vitest.config.ts
coverage: {
  provider: 'v8',
  reporter: ['text', 'json', 'html', 'lcov'],
  reportsDirectory: './coverage'
}
```

## 🚀 **Cómo Usar**

### **1. Análisis Automático (Jenkins):**
- Se ejecuta automáticamente en cada build
- Crea/actualiza ambos proyectos en SonarQube
- Verifica Quality Gates

### **2. Análisis Manual:**
```bash
# Configurar token de SonarQube
export SONAR_TOKEN=tu-token-aqui

# Ejecutar análisis completo
./run-sonarqube-analysis.sh
```

### **3. Análisis Individual:**
```bash
# Solo backend
sonar-scanner -Dsonar.projectKey=hospital-backend [opciones]

# Solo frontend
sonar-scanner -Dsonar.projectKey=hospital-frontend [opciones]
```

## 🌐 **Acceso a SonarQube**

### **URLs de los Proyectos:**
- **Backend**: `http://localhost:9000/dashboard?id=hospital-backend`
- **Frontend**: `http://localhost:9000/dashboard?id=hospital-frontend`

### **Credenciales por Defecto:**
- **Usuario**: `admin`
- **Contraseña**: `admin`

## 📈 **Métricas Disponibles**

### **Backend:**
- Cobertura de código (tests unitarios)
- Duplicaciones
- Vulnerabilidades de seguridad
- Code smells
- Bugs
- Deuda técnica
- Complejidad ciclomática

### **Frontend:**
- Calidad del código JavaScript/TypeScript
- Duplicaciones
- Vulnerabilidades de seguridad
- Code smells
- Bugs
- Deuda técnica
- Análisis de dependencias

## 🔍 **Troubleshooting**

### **Problema**: SonarQube no está disponible
```bash
# Verificar que esté ejecutándose
docker-compose ps

# Iniciar si es necesario
docker-compose up -d
```

### **Problema**: Token de autenticación inválido
```bash
# Generar nuevo token en SonarQube
# Admin > My Account > Security > Generate Tokens

# Exportar variable
export SONAR_TOKEN=nuevo-token-aqui
```

### **Problema**: Análisis falla por falta de tests
```bash
# Ejecutar tests primero
cd backend && mvn test
cd .. && npm run test:unit
```

## 📚 **Recursos Adicionales**

- [Documentación oficial de SonarQube](https://docs.sonarqube.org/)
- [Plugin JaCoCo para Maven](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [Configuración de Vitest](https://vitest.dev/guide/coverage.html)
- [Integración Jenkins-SonarQube](https://docs.sonarqube.org/latest/analysis/jenkins/)

## 🎯 **Próximos Pasos Recomendados**

1. **Configurar Quality Gates** en SonarQube
2. **Integrar con el pipeline de CI/CD** (Jenkins)
3. **Configurar notificaciones** por email/Slack
4. **Establecer métricas de calidad** mínimas
5. **Revisar y corregir** problemas identificados

---

**💡 Nota**: Esta configuración sigue las mejores prácticas de la industria y permite un análisis más granular y específico para cada tipo de código.
