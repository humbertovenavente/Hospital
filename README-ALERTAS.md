# Sistema de Alertas del Hospital

Este documento describe la configuración del sistema de alertas para el monitoreo del Hospital, que incluye notificaciones por correo electrónico y Slack a través de Grafana.

## 📋 Contenido

- [Descripción General](#descripción-general)
- [Componentes del Sistema](#componentes-del-sistema)
- [Configuración de Alertas](#configuración-de-alertas)
- [Notificaciones](#notificaciones)
- [Despliegue](#despliegue)
- [Configuración de Variables de Entorno](#configuración-de-variables-de-entorno)
- [Ejemplos de Uso](#ejemplos-de-uso)

## 🎯 Descripción General

El sistema de alertas está diseñado para monitorear continuamente la infraestructura y las aplicaciones del Hospital, enviando notificaciones automáticas cuando se detectan problemas. Las alertas se configuran con patrones lógicos específicos y se notifican por medio de correo electrónico y Slack.

### Patrones de Alerta

Las alertas siguen un patrón lógico basado en umbrales de rendimiento y disponibilidad:

- **CPU**: Alerta cuando el uso supera el **70%**
- **Memoria**: Alerta cuando el uso supera el **80%**
- **Disco**: Alerta cuando el espacio disponible es menor al **20%**
- **Errores del Backend**: Alerta cuando la tasa de errores 5xx supera **0.1 req/s**
- **Latencia del Backend**: Alerta cuando el p95 de latencia supera **2 segundos**

## 🔧 Componentes del Sistema

### 1. Prometheus

Recopila y almacena métricas de los diferentes componentes del sistema.

**Archivos de configuración:**
- `prometheus.yml`: Configuración principal de Prometheus
- `prometheus-alerts.yml`: Reglas de alerta de Prometheus

**Alertas configuradas:**
- Alertas de infraestructura (CPU, memoria, disco)
- Alertas del backend (disponibilidad, errores, latencia)
- Alertas del frontend (disponibilidad)
- Alertas de CI/CD (estado de Drone)
- Alertas de contenedores
- Alertas de base de datos

### 2. Alertmanager

Gestiona las alertas generadas por Prometheus y envía notificaciones.

**Archivo de configuración:**
- `alertmanager.yml`: Configuración de Alertmanager con canales de notificación

**Características:**
- Agrupación de alertas por severidad
- Deduplicación de alertas
- Rutas de notificación personalizadas
- Inhibiciones para evitar alertas redundantes

### 3. Grafana

Visualiza las métricas y gestiona alertas con notificaciones por email y Slack.

**Configuraciones:**
- `grafana-provisioning-datasources.yml`: Fuentes de datos
- `grafana-alerts-notification-channels.yml`: Canales de notificación
- `grafana-alert-rules.yml`: Reglas de alerta de Grafana

**Canales de notificación:**
- Email: jflores@unis.edu.gt, jnajar@unis.edu.gt
- Slack (configurable)

### 4. Node Exporter

Recopila métricas del sistema operativo (CPU, memoria, disco, red).

### 5. cAdvisor

Recopila métricas de contenedores Docker.

### 6. VictoriaMetrics (Opcional)

Base de datos de series temporales como alternativa a Prometheus.

## 📊 Configuración de Alertas

### Alertas de Infraestructura

#### 1. Uso de CPU (High CPU Usage)
```yaml
Expresión: 100 - (avg by(instance) (irate(node_cpu_seconds_total{mode="idle"}[5m])) * 100) > 70
Duración: 5 minutos
Severidad: Warning
Componente: Infrastructure
```

#### 2. Uso de Memoria (High Memory Usage)
```yaml
Expresión: (1 - (node_memory_MemAvailable_bytes / node_memory_MemTotal_bytes)) * 100 > 80
Duración: 5 minutos
Severidad: Warning
Componente: Infrastructure
```

#### 3. Espacio en Disco (High Disk Usage)
```yaml
Expresión: (node_filesystem_avail_bytes{mountpoint="/"} / node_filesystem_size_bytes{mountpoint="/"}) * 100 < 20
Duración: 5 minutos
Severidad: Warning
Componente: Infrastructure
```

### Alertas del Backend

#### 1. Backend Caído (Backend Down)
```yaml
Expresión: up{job="hospital-backend-prod-monitoring"} == 0
Duración: 2 minutos
Severidad: Critical
Componente: Backend
```

#### 2. Alta Tasa de Errores (High Error Rate)
```yaml
Expresión: rate(http_server_requests_seconds_count{status=~"5.."}[5m]) > 0.1
Duración: 5 minutos
Severidad: Critical
Componente: Backend
```

#### 3. Alta Latencia (High Response Time)
```yaml
Expresión: histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m])) > 2
Duración: 10 minutos
Severidad: Warning
Componente: Backend
```

### Alertas del Frontend

#### 1. Frontend Caído (Frontend Down)
```yaml
Expresión: up{job="hospital-frontend-prod-monitoring"} == 0
Duración: 2 minutos
Severidad: Critical
Componente: Frontend
```

### Alertas de CI/CD

#### 1. Drone Server Caído
```yaml
Expresión: up{job="drone-server"} == 0
Duración: 5 minutos
Severidad: Critical
Componente: CI/CD
```

## 📧 Notificaciones

### Correo Electrónico

**Configuración:**
- SMTP Host: smtp.gmail.com:587
- From: (configurable via GMAIL_FROM_ADDRESS)
- To: jflores@unis.edu.gt, jnajar@unis.edu.gt

**Características:**
- Templates HTML personalizados
- Enlaces directos a Grafana
- Envío de alertas resueltas
- Agrupación por severidad

### Slack (Configurable)

**Configuración:**
1. Crear un Incoming Webhook en Slack
2. Configurar la variable de entorno `SLACK_WEBHOOK_URL`
3. Descomentar la configuración de Slack en `alertmanager.yml`

**Ejemplo de configuración:**
```yaml
slack_configs:
  - api_url: 'https://hooks.slack.com/services/YOUR/WEBHOOK/URL'
    channel: '#hospital-alerts'
    username: 'Grafana Hospital'
    title: '🚨 ALERTA CRÍTICA'
```

## 🚀 Despliegue

### Opción 1: Usando Docker Compose

```bash
# Configurar variables de entorno
export GMAIL_USER="tu-email@gmail.com"
export GMAIL_PASSWORD="tu-app-password"
export GMAIL_FROM_ADDRESS="tu-email@gmail.com"

# Iniciar el stack de monitoreo
docker-compose -f docker-compose.monitoring.yml up -d

# Verificar servicios
docker-compose -f docker-compose.monitoring.yml ps
```

### Opción 2: Usando el script de despliegue

```bash
# Hacer ejecutable el script
chmod +x deploy-monitoring.sh

# Ejecutar el script
./deploy-monitoring.sh
```

### Acceso a los servicios

Una vez desplegado, los servicios estarán disponibles en:

- **Prometheus**: http://localhost:9090
- **Alertmanager**: http://localhost:9093
- **Grafana**: http://localhost:3000
  - Usuario: admin
  - Password: admin123
- **Node Exporter**: http://localhost:9100/metrics
- **cAdvisor**: http://localhost:8080

## 🔐 Configuración de Variables de Entorno

### Variables requeridas para notificaciones por email

```bash
export GMAIL_USER="tu-email@gmail.com"
export GMAIL_PASSWORD="tu-app-password-de-gmail"  # Usar App Password de Gmail
export GMAIL_FROM_ADDRESS="tu-email@gmail.com"
```

**Nota:** Para usar Gmail, necesitas generar una "Contraseña de aplicación" en tu cuenta de Google:
1. Ve a tu cuenta de Google
2. Seguridad > Verificación en 2 pasos
3. Al final de la página, selecciona "Contraseñas de aplicaciones"
4. Genera una nueva contraseña para "Correo"

### Variables opcionales para notificaciones de Slack

```bash
export SLACK_WEBHOOK_URL="https://hooks.slack.com/services/YOUR/WEBHOOK/URL"
export SLACK_CHANNEL="#hospital-alerts"
```

## 📝 Ejemplos de Uso

### Verificar que las alertas están funcionando

```bash
# Ver alertas activas en Prometheus
curl http://localhost:9090/api/v1/alerts

# Ver alertas en Alertmanager
curl http://localhost:9093/api/v1/alerts

# Ver grupos de alertas
curl http://localhost:9093/api/v2/alerts/groups
```

### Probar una alerta manualmente

```bash
# Obtener la IP del servidor
IP=$(hostname -I | awk '{print $1}')

# Simular alta carga de CPU (requiere tener stress-ng instalado)
stress-ng --cpu 8 --timeout 600s
```

### Ver logs de los servicios

```bash
# Logs de Prometheus
docker logs hospital-prometheus

# Logs de Alertmanager
docker logs hospital-alertmanager

# Logs de Grafana
docker logs hospital-grafana
```

## 📈 Dashboard de Grafana

### Acceso al dashboard

1. Inicia sesión en http://localhost:3000
2. Usuario: admin
3. Contraseña: admin123
4. Importa el dashboard desde el catálogo de Grafana o crea uno personalizado

### Métricas disponibles

- CPU por instancia
- Memoria por instancia
- Disco por instancia
- Métricas del backend (requests, latencia, errores)
- Métricas del frontend
- Métricas de contenedores
- Métricas de CI/CD

## 🛠️ Resolución de Problemas

### Las alertas no se envían por email

1. Verifica que las variables de entorno están configuradas
2. Verifica los logs de Alertmanager:
   ```bash
   docker logs hospital-alertmanager
   ```
3. Verifica la configuración de SMTP en `alertmanager.yml`

### Las alertas no aparecen en Grafana

1. Verifica que la conexión a Prometheus está activa en Grafana
2. Verifica que las reglas de alerta están cargadas en Prometheus
3. Revisa los logs de Grafana:
   ```bash
   docker logs hospital-grafana
   ```

### Prometheus no recolecta métricas

1. Verifica que los targets están UP:
   ```bash
   curl http://localhost:9090/api/v1/targets
   ```
2. Verifica que los jobs están configurados correctamente en `prometheus.yml`
3. Verifica que las aplicaciones están exponiendo métricas en las rutas correctas

## 📚 Referencias

- [Prometheus Alerting Rules](https://prometheus.io/docs/prometheus/latest/configuration/alerting_rules/)
- [Alertmanager Configuration](https://prometheus.io/docs/alerting/latest/configuration/)
- [Grafana Alerting](https://grafana.com/docs/grafana/latest/alerting/)
- [Node Exporter Metrics](https://github.com/prometheus/node_exporter)

## 👥 Mantenimiento

### Cambiar umbrales de alerta

Edita el archivo `prometheus-alerts.yml` y modifica los valores en las expresiones:

```yaml
# Cambiar umbral de CPU del 70% al 80%
- alert: HighCPUUsage
  expr: 100 - (avg by(instance) (irate(node_cpu_seconds_total{mode="idle"}[5m])) * 100) > 80
```

### Agregar nuevos canales de notificación

Edita el archivo `alertmanager.yml` y agrega un nuevo receiver:

```yaml
- name: 'pagerduty-alerts'
  pagerduty_configs:
    - service_key: 'your-service-key'
```

### Reiniciar servicios después de cambios

```bash
# Recargar configuración de Prometheus
curl -X POST http://localhost:9090/-/reload

# Reiniciar Alertmanager
docker restart hospital-alertmanager

# Reiniciar Grafana
docker restart hospital-grafana
```

