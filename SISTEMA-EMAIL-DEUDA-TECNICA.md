# 📧 SISTEMA DE EMAILS DE DEUDA TÉCNICA

## 🎯 **DESCRIPCIÓN DEL SISTEMA:**

Sistema automatizado para enviar reportes detallados de deuda técnica por email, integrado con SonarQube y Jenkins. Los reportes incluyen métricas completas de calidad de código, seguridad, confiabilidad y mantenibilidad.

## 🏗️ **ARQUITECTURA:**

### **Frontend (Vue.js):**
- **`src/services/technicalDebtEmailService.js`** - Servicio principal para envío de emails
- **Integración con SonarQube** - Obtiene métricas en tiempo real
- **Generación de reportes HTML** - Reportes visualmente atractivos

### **Backend (Java/Quarkus):**
- **`TechnicalDebtEmailController.java`** - API REST para envío de emails
- **`TechnicalDebtEmailService.java`** - Lógica de negocio y generación de reportes
- **`TechnicalDebtEmailRequest.java`** - DTO para solicitudes
- **`TechnicalDebtEmailResponse.java`** - DTO para respuestas

### **Scripts:**
- **`send-technical-debt-report.sh`** - Script de línea de comandos para envío manual

## 🚀 **FUNCIONALIDADES:**

### **1. Reportes Individuales:**
- **Backend**: `hospital-backend-prod`
- **Frontend**: `hospital-frontend-prod`
- **Métricas personalizadas** por proyecto

### **2. Reportes Múltiples:**
- **Envío en lote** para varios proyectos
- **Consolidación** de métricas
- **Un solo email** con toda la información

### **3. Métricas Incluidas:**
- **🛡️ Seguridad**: Rating, Security Hotspots, Vulnerabilidades
- **🔧 Confiabilidad**: Rating, Bugs, Issues de confiabilidad
- **🔨 Mantenibilidad**: Rating, Code Smells, Issues de mantenibilidad
- **🧪 Cobertura**: Porcentaje de cobertura, líneas por cubrir
- **📝 Duplicación**: Porcentaje de duplicación, líneas duplicadas
- **🚨 Issues Críticos**: High, Medium, Low

## 📊 **EJEMPLO DE REPORTE:**

### **Métricas Actuales del Frontend:**
- **Seguridad**: Rating E (11 Security Hotspots) 🚨
- **Confiabilidad**: Rating C (2 issues) ⚠️
- **Mantenibilidad**: Rating A (84 issues) ✅
- **Cobertura**: 0% 🚨
- **Duplicación**: 9.1% ⚠️

### **Recomendaciones Prioritarias:**
1. **URGENTE**: Revisar 11 Security Hotspots
2. **ALTO**: Implementar tests (0% cobertura)
3. **MEDIO**: Resolver 2 issues de confiabilidad
4. **MEDIO**: Reducir duplicación del 9.1%
5. **BAJO**: Revisar 84 issues de mantenibilidad

## 🔧 **CONFIGURACIÓN:**

### **Variables de Entorno (Backend):**
```properties
# Configuración de email
quarkus.mailer.host=localhost
quarkus.mailer.port=587
quarkus.mailer.username=noreply@hospital.com
quarkus.mailer.password=password
quarkus.mailer.from=noreply@hospital.com
quarkus.mailer.ssl=false
```

### **Configuración del Frontend:**
```javascript
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';
```

## 📧 **USO DEL SISTEMA:**

### **1. Desde el Frontend (JavaScript):**
```javascript
import technicalDebtEmailService from '@/services/technicalDebtEmailService';

// Enviar reporte de un proyecto
await technicalDebtEmailService.sendTechnicalDebtReport(
    'hospital-frontend-prod',
    'Hospital Frontend - PRODUCCIÓN',
    'admin@hospital.com'
);

// Enviar reportes de múltiples proyectos
const projects = [
    { key: 'hospital-backend-prod', name: 'Hospital Backend' },
    { key: 'hospital-frontend-prod', name: 'Hospital Frontend' }
];

await technicalDebtEmailService.sendMultiProjectTechnicalDebtReport(
    projects,
    'admin@hospital.com'
);
```

### **2. Desde el Backend (API REST):**
```bash
# Enviar reporte de un proyecto
curl -X POST http://localhost:8080/api/email/technical-debt \
  -H "Content-Type: application/json" \
  -d '{
    "projectKey": "hospital-frontend-prod",
    "projectName": "Hospital Frontend - PRODUCCIÓN",
    "recipientEmail": "admin@hospital.com"
  }'

# Enviar reportes de múltiples proyectos
curl -X POST http://localhost:8080/api/email/technical-debt/multi-project \
  -H "Content-Type: application/json" \
  -d '{
    "projects": [
      {"key": "hospital-backend-prod", "name": "Hospital Backend"},
      {"key": "hospital-frontend-prod", "name": "Hospital Frontend"}
    ],
    "recipientEmail": "admin@hospital.com"
  }'
```

### **3. Desde Línea de Comandos:**
```bash
# Enviar a email por defecto
./send-technical-debt-report.sh

# Enviar a email específico
./send-technical-debt-report.sh tu@email.com

# Ver ayuda
./send-technical-debt-report.sh --help
```

## 🔄 **INTEGRACIÓN CON JENKINS:**

### **Pipeline Automático:**
```groovy
// En el Jenkinsfile, después del análisis de SonarQube
stage('Enviar Reporte de Deuda Técnica') {
    steps {
        script {
            // Enviar reporte por email
            sh '''
                curl -X POST http://localhost:8080/api/email/technical-debt \\
                  -H "Content-Type: application/json" \\
                  -d '{
                    "projectKey": "hospital-frontend-prod",
                    "projectName": "Hospital Frontend - PRODUCCIÓN",
                    "recipientEmail": "equipo@hospital.com"
                  }'
            '''
        }
    }
}
```

## 📋 **ENDPOINTS DISPONIBLES:**

### **1. Envío de Reporte Individual:**
- **POST** `/api/email/technical-debt`
- **Body**: `TechnicalDebtEmailRequest`
- **Response**: `TechnicalDebtEmailResponse`

### **2. Envío de Reportes Múltiples:**
- **POST** `/api/email/technical-debt/multi-project`
- **Body**: `TechnicalDebtEmailRequest` con array de proyectos
- **Response**: `List<TechnicalDebtEmailResponse>`

### **3. Estado del Servicio:**
- **GET** `/api/email/technical-debt/status`
- **Response**: Estado de salud del servicio

## 🎨 **CARACTERÍSTICAS DEL EMAIL:**

### **Diseño Responsivo:**
- **HTML5 + CSS3** moderno
- **Grid layout** adaptable
- **Colores por rating** (A=Verde, E=Rojo)
- **Alertas visuales** para prioridades

### **Contenido del Email:**
- **Header con gradiente** y logo del proyecto
- **Métricas organizadas** en tarjetas
- **Recomendaciones prioritarias** numeradas
- **Enlaces directos** a SonarQube
- **Footer con información** del sistema

## 🚨 **PRIORIDADES DE DEUDA TÉCNICA:**

### **🔴 CRÍTICO (Inmediato):**
- Security Hotspots (Rating E)
- Cobertura de tests (0%)

### **🟡 ALTO (Esta semana):**
- Issues de confiabilidad (Rating C)
- Duplicación de código (>5%)

### **🟢 MEDIO (Este mes):**
- Issues de mantenibilidad
- Code smells

### **🔵 BAJO (Próximo sprint):**
- Optimizaciones menores
- Refactoring opcional

## 📈 **BENEFICIOS:**

### **Para el Equipo:**
- **Visibilidad completa** de la deuda técnica
- **Priorización clara** de tareas
- **Seguimiento automático** de métricas
- **Reportes profesionales** para stakeholders

### **Para el Proyecto:**
- **Calidad del código** monitoreada
- **Seguridad** auditada regularmente
- **Mantenibilidad** mejorada
- **Cobertura de tests** incrementada

## 🔮 **PRÓXIMOS PASOS:**

### **1. Implementación:**
- [ ] Agregar dependencias de JavaMail al `pom.xml`
- [ ] Configurar variables de email en `application.properties`
- [ ] Probar endpoints con Postman/curl

### **2. Integración:**
- [ ] Agregar al pipeline de Jenkins
- [ ] Configurar envío automático semanal
- [ ] Integrar con sistema de notificaciones

### **3. Mejoras:**
- [ ] Plantillas personalizables
- [ ] Programación de envíos
- [ ] Dashboard de métricas históricas

¡Sistema completo de reportes de deuda técnica listo para implementar! 🎉
