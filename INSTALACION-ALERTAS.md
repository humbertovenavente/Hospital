# Instrucciones de Instalación - Sistema de Alertas

## 🚀 Inicio Rápido

### 1. Configurar Variables de Entorno

```bash
# Configurar Gmail para notificaciones por email
export GMAIL_USER="tu-email@gmail.com"
export GMAIL_PASSWORD="tu-app-password"  # Usa App Password de Gmail, no tu contraseña normal
export GMAIL_FROM_ADDRESS="tu-email@gmail.com"

# Opcional: Configurar Slack
export SLACK_WEBHOOK_URL="https://hooks.slack.com/services/YOUR/WEBHOOK/URL"
export SLACK_CHANNEL="#hospital-alerts"
```

**Importante:** Para usar Gmail, necesitas generar una "App Password":
1. Ve a https://myaccount.google.com/security
2. Activa la Verificación en 2 pasos
3. Ve a "App passwords" y genera una nueva
4. Usa esa contraseña (no tu contraseña normal de Gmail)

### 2. Desplegar el Sistema

```bash
# Ejecutar el script de despliegue
./deploy-monitoring.sh
```

El script te pedirá las credenciales si no las has configurado como variables de entorno.

### 3. Acceder a los Servicios

Una vez desplegado, accede a:

- **Prometheus**: http://localhost:9090
- **Alertmanager**: http://localhost:9093
- **Grafana**: http://localhost:3000
  - Usuario: `admin`
  - Contraseña: `admin123`
- **Node Exporter**: http://localhost:9100/metrics
- **cAdvisor**: http://localhost:8080

## 📊 Verificar Alertas

### Ver alertas activas

```bash
# Ver todas las alertas en Prometheus
curl http://localhost:9090/api/v1/alerts

# Ver alertas en Alertmanager
curl http://localhost:9093/api/v1/alerts

# Ver grupos de alertas
curl http://localhost:9093/api/v2/alerts/groups
```

### Probar una alerta

Para probar que las notificaciones funcionan:

1. Simular alta carga de CPU (opcional, requiere stress-ng):
   ```bash
   stress-ng --cpu 8 --timeout 600s
   ```

2. Verificar que se envió el correo de alerta

## 📧 Configurar Slack (Opcional)

### 1. Crear Incoming Webhook en Slack

1. Ve a https://api.slack.com/apps
2. Crea una nueva app
3. Activa "Incoming Webhooks"
4. Agrega el webhook a tu canal #hospital-alerts
5. Copia la URL del webhook

### 2. Actualizar Configuración

Edita `alertmanager.yml` y descomenta la sección de Slack:

```yaml
slack_configs:
  - api_url: '${SLACK_WEBHOOK_URL}'
    channel: '#hospital-alerts'
    username: 'Grafana Hospital'
    icon_emoji: ':hospital:'
    title: '🚨 ALERTA CRÍTICA - {{ .GroupLabels.alertname }}'
    text: |
      *Alerta:* {{ .GroupLabels.alertname }}
      *Severidad:* {{ .CommonLabels.severity }}
      *Componente:* {{ .CommonLabels.component }}
      
      {{ range .Alerts }}
      *{{ .Annotations.summary }}*
      {{ .Annotations.description }}
      {{ end }}
      <{{ .ExternalURL }}|Ver en Grafana>
    send_resolved: true
```

### 3. Reiniciar Alertmanager

```bash
docker restart hospital-alertmanager
```

## 🔧 Solución de Problemas

### Las alertas no se envían por email

1. Verifica que las variables de entorno están configuradas:
   ```bash
   echo $GMAIL_USER
   echo $GMAIL_PASSWORD
   ```

2. Verifica los logs de Alertmanager:
   ```bash
   docker logs hospital-alertmanager
   ```

3. Asegúrate de usar una "App Password" de Gmail, no tu contraseña normal

### Prometheus no recopila métricas

1. Verifica que los targets están UP:
   ```bash
   curl http://localhost:9090/api/v1/targets
   ```

2. Verifica que las aplicaciones están exponiendo métricas:
   ```bash
   curl http://localhost:8080/q/metrics  # Backend
   ```

### Grafana no muestra datos

1. Verifica que Prometheus está configurado como datasource en Grafana
2. Accede a Configuration > Data Sources > Prometheus y prueba la conexión
3. Verifica los logs de Grafana:
   ```bash
   docker logs hospital-grafana
   ```

## 📋 Archivos de Configuración

- `prometheus.yml`: Configuración principal de Prometheus
- `prometheus-alerts.yml`: Reglas de alerta de Prometheus
- `alertmanager.yml`: Configuración de Alertmanager y notificaciones
- `grafana-provisioning-datasources.yml`: Datasources de Grafana
- `grafana-alerts-notification-channels.yml`: Canales de notificación de Grafana
- `grafana-alert-rules.yml`: Reglas de alerta de Grafana
- `docker-compose.monitoring.yml`: Stack de monitoreo completo

## 🎯 Alertas Configuradas

### Infraestructura
- ✅ Uso de CPU > 70%
- ✅ Uso de memoria > 80%
- ✅ Espacio en disco < 20%

### Backend
- ✅ Backend fuera de línea
- ✅ Alta tasa de errores 5xx
- ✅ Latencia alta (p95 > 2s)

### Frontend
- ✅ Frontend fuera de línea

### CI/CD
- ✅ Drone Server fuera de línea
- ✅ Alta tasa de fallos en jobs

### Contenedores
- ✅ Uso de CPU elevado
- ✅ Uso de memoria elevado
- ✅ Contenedor reiniciándose

## 📈 Personalizar Umbrales

Para cambiar los umbrales de alerta, edita `prometheus-alerts.yml`:

```yaml
# Cambiar umbral de CPU del 70% al 80%
- alert: HighCPUUsage
  expr: 100 - (avg by(instance) (irate(node_cpu_seconds_total{mode="idle"}[5m])) * 100) > 80
```

Luego recarga la configuración de Prometheus:
```bash
curl -X POST http://localhost:9090/-/reload
```

## 🔄 Reiniciar Servicios

```bash
# Reiniciar todo el stack
docker-compose -f docker-compose.monitoring.yml restart

# Reiniciar un servicio específico
docker restart hospital-prometheus
docker restart hospital-alertmanager
docker restart hospital-grafana
```

## 🛑 Detener el Sistema

```bash
# Detener y eliminar contenedores
docker-compose -f docker-compose.monitoring.yml down

# También eliminar volúmenes (CUIDADO: pierdes los datos)
docker-compose -f docker-compose.monitoring.yml down -v
```

## 📖 Documentación Completa

Para más detalles, consulta [README-ALERTAS.md](README-ALERTAS.md)

## 👥 Soporte

Para problemas o preguntas:
- Verifica los logs: `docker logs <container-name>`
- Consulta la documentación oficial de [Prometheus](https://prometheus.io/docs/)
- Consulta la documentación oficial de [Grafana](https://grafana.com/docs/)

