# 📋 INSTRUCCIONES PARA CONFIGURAR MÉTRICAS DE PROMETHEUS EN QA Y PROD

## ℹ️ INFORMACIÓN GENERAL

Este documento contiene las instrucciones detalladas para configurar las métricas de Prometheus en los entornos QA y PROD. **NO es necesario reconstruir contenedores manualmente** ya que el pipeline de Drone CI/CD lo hace automáticamente al hacer push.

**Requisitos previos:**
- Tener acceso a los servidores QA y PROD
- Tener Git configurado con acceso al repositorio
- Tener las ramas `QA` y `main`/`master` configuradas

---

## 📦 PARTE 1: CONFIGURACIÓN DEL BACKEND (Quarkus)

### 1.1. Verificar dependencia de métricas en `backend/pom.xml`

Asegúrate de que esta dependencia esté presente en el archivo `backend/pom.xml`:

```xml
<!-- Métricas para Prometheus -->
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-smallrye-metrics</artifactId>
</dependency>
```

**Ubicación:** Dentro de la sección `<dependencies>` del archivo `backend/pom.xml`

### 1.2. Configurar métricas en archivos de propiedades

Debes agregar estas configuraciones en **AMBOS** archivos de propiedades:

#### Para QA: `backend/src/main/resources/application-qa.properties`

```properties
# Configuración de métricas para Prometheus
quarkus.smallrye-metrics.enabled=true
quarkus.smallrye-health.root-path=/q/health
quarkus.smallrye-metrics.path=/q/metrics
quarkus.smallrye-metrics.export.prometheus.enabled=true
quarkus.smallrye-metrics.export.prometheus.path=/q/metrics
quarkus.smallrye-metrics.export.prometheus.format=prometheus
```

#### Para PROD: `backend/src/main/resources/application-prod.properties`

```properties
# Configuración de métricas para Prometheus
quarkus.smallrye-metrics.enabled=true
quarkus.smallrye-health.root-path=/q/health
quarkus.smallrye-metrics.path=/q/metrics
quarkus.smallrye-metrics.export.prometheus.enabled=true
quarkus.smallrye-metrics.export.prometheus.path=/q/metrics
quarkus.smallrye-metrics.export.prometheus.format=prometheus
```

**⚠️ IMPORTANTE:** Estas configuraciones se agregan al **FINAL** de cada archivo.

---

## 🌐 PARTE 2: CONFIGURACIÓN DEL FRONTEND (Nginx)

### 2.1. Crear archivo de métricas estáticas

Crea un archivo llamado `metrics.txt` en la **raíz del proyecto** con el siguiente contenido:

**Archivo: `metrics.txt`**

```
# HELP frontend_up Frontend application status
# TYPE frontend_up gauge
frontend_up 1

# HELP frontend_requests_total Total number of HTTP requests
# TYPE frontend_requests_total counter
frontend_requests_total 0

# HELP frontend_response_time_seconds Response time in seconds
# TYPE frontend_response_time_seconds histogram
frontend_response_time_seconds_bucket{le="0.1"} 0
frontend_response_time_seconds_bucket{le="0.5"} 0
frontend_response_time_seconds_bucket{le="1.0"} 0
frontend_response_time_seconds_bucket{le="2.5"} 0
frontend_response_time_seconds_bucket{le="5.0"} 0
frontend_response_time_seconds_bucket{le="10.0"} 0
frontend_response_time_seconds_bucket{le="+Inf"} 0
frontend_response_time_seconds_count 0
frontend_response_time_seconds_sum 0

# HELP frontend_build_info Frontend build information
# TYPE frontend_build_info gauge
frontend_build_info{version="1.0.0",environment="production"} 1
```

### 2.2. Actualizar archivos de configuración Nginx

#### Para QA: `nginx.qa.conf`

**⚠️ ACTUALIZA LA IP** del backend a la IP de tu servidor QA (reemplaza `34.61.228.49` con la IP correcta de QA).

```nginx
server {
    listen 80;
    server_name _;

    root /usr/share/nginx/html;
    index index.html;

    # Logs
    access_log /var/log/nginx/access.log;
    error_log /var/log/nginx/error.log;

    # Configuración para Vue Router (modo history)
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Proxy para el backend - QA
    location /api {
        proxy_pass http://34.61.228.49:8030;  # ⚠️ CAMBIAR A LA IP DE QA
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Endpoint de métricas para Prometheus
    location /metrics {
        add_header Content-Type "text/plain; charset=utf-8" always;
        add_header Cache-Control "no-cache" always;
        try_files /metrics.txt =404;
    }

    # Configuración de caché para assets estáticos
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

#### Para PROD: `nginx.prod.conf`

**⚠️ ACTUALIZA LA IP** del backend a la IP de tu servidor PROD (reemplaza `34.61.228.49` con la IP correcta de PROD).

```nginx
server {
    listen 80;
    server_name _;

    root /usr/share/nginx/html;
    index index.html;

    # Logs
    access_log /var/log/nginx/access.log;
    error_log /var/log/nginx/error.log;

    # Configuración para Vue Router (modo history)
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Proxy para el backend - PROD
    location /api {
        proxy_pass http://34.61.228.49:8020;  # ⚠️ CAMBIAR A LA IP DE PROD
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Endpoint de métricas para Prometheus
    location /metrics {
        add_header Content-Type "text/plain; charset=utf-8" always;
        add_header Cache-Control "no-cache" always;
        try_files /metrics.txt =404;
    }

    # Configuración de caché para assets estáticos
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### 2.3. Actualizar Dockerfiles del Frontend

#### Para QA: `Dockerfile.frontend.qa`

Verifica que incluya la copia del archivo `metrics.txt`:

```dockerfile
FROM node:18-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY --from=build-stage /app/metrics.txt /usr/share/nginx/html/metrics.txt
COPY nginx.qa.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

#### Para PROD: `Dockerfile.frontend.prod` (o `Dockerfile.frontend.cloud`)

Verifica que incluya la copia del archivo `metrics.txt`:

```dockerfile
FROM node:18-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY --from=build-stage /app/metrics.txt /usr/share/nginx/html/metrics.txt
COPY nginx.prod.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

---

## 📊 PARTE 3: CONFIGURACIÓN DE PROMETHEUS

### 3.1. Actualizar `prometheus.yml`

**⚠️ REEMPLAZA TODAS LAS IPs** `34.61.228.49` con las IPs correspondientes de tus servidores QA y PROD.

```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s
  external_labels:
    monitor: 'hospital-monitoring'

rule_files:
  - "prometheus-rules.yml"

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093

scrape_configs:
  # Prometheus itself
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']
    scrape_interval: 15s

  # Backend Quarkus - Pipeline (3 instancias para gráficas comparativas)
  - job_name: 'hospital-backend-pipeline'
    static_configs:
      - targets:
        - 'IP_PROD:8020'  # ⚠️ CAMBIAR IP_PROD
        - 'IP_QA:8030'    # ⚠️ CAMBIAR IP_QA
        - 'IP_DEV:8060'   # ⚠️ CAMBIAR IP_DEV
    metrics_path: '/q/metrics'
    scrape_interval: 30s
    honor_labels: true
    relabel_configs:
      - source_labels: [__address__]
        target_label: environment
        regex: 'IP_PROD:8020'  # ⚠️ CAMBIAR IP_PROD
        replacement: 'prod'
      - source_labels: [__address__]
        target_label: environment
        regex: 'IP_QA:8030'    # ⚠️ CAMBIAR IP_QA
        replacement: 'qa'
      - source_labels: [__address__]
        target_label: environment
        regex: 'IP_DEV:8060'   # ⚠️ CAMBIAR IP_DEV
        replacement: 'dev'

  # Backend Quarkus - Monitoreo de Producción (solo para alertas)
  - job_name: 'hospital-backend-prod-monitoring'
    static_configs:
      - targets: ['IP_PROD:8020']  # ⚠️ CAMBIAR IP_PROD
    metrics_path: '/q/metrics'
    scrape_interval: 10s
    honor_labels: true

  # Frontend - Pipeline (3 instancias para gráficas comparativas)
  - job_name: 'hospital-frontend-pipeline'
    static_configs:
      - targets:
        - 'IP_PROD:8021'  # ⚠️ CAMBIAR IP_PROD
        - 'IP_QA:8031'    # ⚠️ CAMBIAR IP_QA
        - 'IP_DEV:8061'   # ⚠️ CAMBIAR IP_DEV
    metrics_path: '/metrics'
    scrape_interval: 30s
    scrape_protocols: ['OpenMetricsText1.0.0', 'PrometheusText1.0.0']
    fallback_scrape_protocol: 'PrometheusText0.0.4'
    honor_labels: true
    relabel_configs:
      - source_labels: [__address__]
        target_label: environment
        regex: 'IP_PROD:8021'  # ⚠️ CAMBIAR IP_PROD
        replacement: 'prod'
      - source_labels: [__address__]
        target_label: environment
        regex: 'IP_QA:8031'    # ⚠️ CAMBIAR IP_QA
        replacement: 'qa'
      - source_labels: [__address__]
        target_label: environment
        regex: 'IP_DEV:8061'   # ⚠️ CAMBIAR IP_DEV
        replacement: 'dev'

  # Frontend - Monitoreo de Producción (solo para alertas)
  - job_name: 'hospital-frontend-prod-monitoring'
    static_configs:
      - targets: ['IP_PROD:8021']  # ⚠️ CAMBIAR IP_PROD
    metrics_path: '/metrics'
    scrape_interval: 10s
    scrape_protocols: ['OpenMetricsText1.0.0', 'PrometheusText1.0.0']
    fallback_scrape_protocol: 'PrometheusText0.0.4'

  # Node Exporter - Métricas del sistema
  - job_name: 'node-exporter'
    static_configs:
      - targets: ['IP_SERVIDOR:9100']  # ⚠️ CAMBIAR IP_SERVIDOR
    scrape_interval: 15s

  # cAdvisor - Métricas de contenedores
  - job_name: 'cadvisor'
    static_configs:
      - targets: ['IP_SERVIDOR:8080']  # ⚠️ CAMBIAR IP_SERVIDOR
    scrape_interval: 15s

  # Drone CI/CD - Métricas del pipeline
  - job_name: 'drone-server'
    static_configs:
      - targets: ['IP_SERVIDOR:8002']  # ⚠️ CAMBIAR IP_SERVIDOR
    metrics_path: '/metrics'
    scrape_interval: 15s
```

**📝 NOTA:** Este archivo debe actualizarse en el servidor donde corre Prometheus (probablemente el mismo servidor que tiene DEV o uno dedicado para monitoreo).

---

## 🚀 PARTE 4: DESPLIEGUE Y VERIFICACIÓN

### 4.1. Para QA (Rama `QA`)

```bash
# 1. Cambiar a la rama QA
git checkout QA

# 2. Hacer merge de los cambios desde dev (si es necesario)
git merge dev

# 3. Verificar que todos los archivos estén correctos
git status

# 4. Hacer commit de los cambios
git add .
git commit -m "feat: Configurar métricas de Prometheus para QA"

# 5. Push - El pipeline de Drone reconstruirá automáticamente
git push origin QA
```

### 4.2. Para PROD (Rama `main` o `master`)

```bash
# 1. Cambiar a la rama PROD
git checkout main  # o git checkout master

# 2. Hacer merge de los cambios desde QA (si es necesario)
git merge QA

# 3. Verificar que todos los archivos estén correctos
git status

# 4. Hacer commit de los cambios
git add .
git commit -m "feat: Configurar métricas de Prometheus para PROD"

# 5. Push - El pipeline de Drone reconstruirá automáticamente
git push origin main  # o git push origin master
```

### 4.3. Reiniciar Prometheus (en el servidor de monitoreo)

```bash
# Conectarse al servidor de monitoreo
ssh usuario@IP_SERVIDOR_PROMETHEUS

# Reiniciar el contenedor de Prometheus para cargar la nueva configuración
docker restart prometheus-cloud
```

---

## ✅ PARTE 5: VERIFICACIÓN

### 5.1. Verificar métricas del Backend

**Para QA:**
```bash
curl http://IP_QA:8030/q/metrics
```

**Para PROD:**
```bash
curl http://IP_PROD:8020/q/metrics
```

**Resultado esperado:** Deberías ver métricas en formato Prometheus como:
```
# HELP base_cpu_processCpuLoad CPU load of the JVM
# TYPE base_cpu_processCpuLoad gauge
base_cpu_processCpuLoad 0.123456
...
```

### 5.2. Verificar métricas del Frontend

**Para QA:**
```bash
curl http://IP_QA:8031/metrics
```

**Para PROD:**
```bash
curl http://IP_PROD:8021/metrics
```

**Resultado esperado:** Deberías ver el contenido del archivo `metrics.txt`:
```
# HELP frontend_up Frontend application status
# TYPE frontend_up gauge
frontend_up 1
...
```

### 5.3. Verificar en Prometheus

1. Acceder a Prometheus: `http://IP_PROMETHEUS:9090`

2. Ir a **Status > Targets**

3. Verificar que todos los targets estén **UP** (verde):
   - `hospital-backend-pipeline`: 3 instancias (PROD, QA, DEV)
   - `hospital-backend-prod-monitoring`: 1 instancia (PROD)
   - `hospital-frontend-pipeline`: 3 instancias (PROD, QA, DEV)
   - `hospital-frontend-prod-monitoring`: 1 instancia (PROD)
   - `drone-server`: 1 instancia

4. Probar queries en **Graph**:
   ```promql
   # Ver el estado de todos los backends
   up{job="hospital-backend-pipeline"}
   
   # Ver métricas de CPU por ambiente
   base_cpu_processCpuLoad{environment=~"prod|qa|dev"}
   
   # Ver estado del frontend
   frontend_up
   ```

---

## 📈 PARTE 6: CREAR GRÁFICAS EN GRAFANA

### 6.1. Gráficas del Pipeline (3 ambientes)

Estas gráficas muestran los 3 ambientes (PROD, QA, DEV) en la misma visualización:

**Query para CPU del Backend:**
```promql
base_cpu_processCpuLoad{job="hospital-backend-pipeline"}
```

**Query para Memoria del Backend:**
```promql
base_memory_usedHeap{job="hospital-backend-pipeline"} / base_memory_maxHeap{job="hospital-backend-pipeline"} * 100
```

**Query para Requests HTTP:**
```promql
rate(base_REST_request_total{job="hospital-backend-pipeline"}[5m])
```

**Query para Estado del Frontend:**
```promql
frontend_up{job="hospital-frontend-pipeline"}
```

### 6.2. Gráficas de Monitoreo (solo PROD para alertas)

Estas gráficas son solo para producción con intervalos más cortos:

**Query para CPU PROD:**
```promql
base_cpu_processCpuLoad{job="hospital-backend-prod-monitoring"}
```

**Query para Memoria PROD:**
```promql
base_memory_usedHeap{job="hospital-backend-prod-monitoring"} / base_memory_maxHeap{job="hospital-backend-prod-monitoring"} * 100
```

---

## 🔧 TROUBLESHOOTING

### Problema: Target aparece DOWN en Prometheus

**Solución:**
1. Verificar que el contenedor esté corriendo:
   ```bash
   docker ps | grep hospital
   ```

2. Verificar que el endpoint responde:
   ```bash
   curl http://IP:PUERTO/q/metrics  # Para backend
   curl http://IP:PUERTO/metrics     # Para frontend
   ```

3. Verificar logs del contenedor:
   ```bash
   docker logs hospital-backend-qa-cloud
   docker logs hospital-frontend-qa-cloud
   ```

### Problema: Content-Type incorrecto en Frontend

**Solución:**
Verificar que el archivo `metrics.txt` se haya copiado correctamente al contenedor:
```bash
docker exec hospital-frontend-qa-cloud ls -la /usr/share/nginx/html/metrics.txt
docker exec hospital-frontend-qa-cloud cat /usr/share/nginx/html/metrics.txt
```

### Problema: Pipeline de Drone no se ejecuta

**Solución:**
1. Verificar que el webhook de GitHub esté configurado correctamente
2. Verificar que Drone Server y Runner estén corriendo:
   ```bash
   docker ps | grep drone
   ```
3. Ver logs de Drone:
   ```bash
   docker logs drone-server
   docker logs drone-runner
   ```

---

## 📌 RESUMEN DE ARCHIVOS MODIFICADOS

### Archivos del Backend:
- ✅ `backend/pom.xml` - Agregar dependencia de métricas
- ✅ `backend/src/main/resources/application-qa.properties` - Configurar métricas
- ✅ `backend/src/main/resources/application-prod.properties` - Configurar métricas

### Archivos del Frontend:
- ✅ `metrics.txt` - Crear archivo nuevo en la raíz
- ✅ `nginx.qa.conf` - Actualizar IP y agregar endpoint /metrics
- ✅ `nginx.prod.conf` - Actualizar IP y agregar endpoint /metrics
- ✅ `Dockerfile.frontend.qa` - Agregar copia de metrics.txt
- ✅ `Dockerfile.frontend.prod` - Agregar copia de metrics.txt

### Archivos de Monitoreo:
- ✅ `prometheus.yml` - Actualizar con IPs correctas y nuevos jobs

---

## 🎯 OBJETIVO FINAL

Al terminar esta configuración tendrás:

1. ✅ **Backend QA y PROD** exponiendo métricas en `/q/metrics`
2. ✅ **Frontend QA y PROD** exponiendo métricas en `/metrics`
3. ✅ **Prometheus** scrapeando métricas de los 3 ambientes (DEV, QA, PROD)
4. ✅ **Gráficas comparativas** en Grafana mostrando los 3 ambientes juntos
5. ✅ **Alertas** configuradas solo para PROD con intervalos más cortos
6. ✅ **Drone** con métricas del pipeline CI/CD

---

## 📞 CONTACTO

Si tienes problemas durante la configuración, verifica:
1. Logs de los contenedores con `docker logs NOMBRE_CONTENEDOR`
2. Status de Prometheus en `http://IP:9090/targets`
3. Que todas las IPs estén correctas en todos los archivos

**¡Buena suerte con la configuración! 🚀**

