# 🏥 Hospital - Configuración de Oracle Compartido

## 📋 Descripción

Este proyecto ha sido configurado para usar un **contenedor Oracle compartido** entre todos los entornos (Desarrollo, QA y Producción), en lugar de crear contenedores separados para cada uno. Esto optimiza el uso de recursos y simplifica la gestión de la base de datos.

## 🗄️ Arquitectura de Base de Datos

### Contenedor Oracle Compartido
- **Nombre**: `hospital-oracle-xe`
- **Puerto**: `1521` (accesible desde todos los entornos)
- **Imagen**: `oracle-xe-con-datos:latest`
- **Contraseña SYS**: `Unis`

### Usuarios por Entorno
| Entorno | Usuario | Contraseña | Descripción |
|---------|---------|------------|-------------|
| **Desarrollo** | `C##PROYECTO` | `Unis` | Usuario principal para desarrollo |
| **QA** | `Hospital2` | `Unis` | Usuario para pruebas de calidad |
| **Producción** | `Hospital3` | `Unis` | Usuario para producción |

## 🚀 Despliegue

### Opción 1: Despliegue Automático (Recomendado)
```bash
./deploy-all-environments.sh
```

Este script:
1. Inicia el contenedor Oracle compartido
2. Configura automáticamente los usuarios
3. Despliega todos los entornos
4. Verifica el estado de los contenedores

### Opción 2: Despliegue Manual

#### Paso 1: Iniciar Oracle
```bash
docker-compose up -d oracle
```

#### Paso 2: Esperar a que Oracle esté listo
```bash
# Verificar logs
docker logs hospital-oracle-xe

# Verificar health check
docker exec hospital-oracle-xe sqlplus -L "sys/Unis@//localhost:1521/XE" AS SYSDBA @/opt/oracle/scripts/startup/setup-oracle-users.sql
```

#### Paso 3: Desplegar entornos individuales
```bash
# Producción
docker-compose up -d backend frontend

# Desarrollo
docker-compose -f docker-compose.dev.yml up -d

# QA
docker-compose -f docker-compose.qa.yml up -d
```

## 🌐 Puertos y URLs

| Servicio | Puerto | URL |
|----------|--------|-----|
| **Oracle** | 1521 | `localhost:1521` |
| **Producción** | | |
| - Backend | 8084 | `http://localhost:8084` |
| - Frontend | 5176 | `http://localhost:5176` |
| **Desarrollo** | | |
| - Backend | 8081 | `http://localhost:8081` |
| - Frontend | 5173 | `http://localhost:5173` |
| - Nginx | 8085 | `http://localhost:8085` |
| **QA** | | |
| - Backend | 8082 | `http://localhost:8082` |
| - Frontend | 5175 | `http://localhost:5175` |
| - Nginx | 8083 | `http://localhost:8083` |
| **Monitoreo** | | |
| - Prometheus | 9090 | `http://localhost:9090` |
| - Grafana | 3000 | `http://localhost:3000` |

## 🔧 Configuración de Conexiones

### Desarrollo
```properties
QUARKUS_DATASOURCE_URL=jdbc:oracle:thin:@oracle:1521/XE
QUARKUS_DATASOURCE_USERNAME=C##PROYECTO
QUARKUS_DATASOURCE_PASSWORD=Unis
```

### QA
```properties
QUARKUS_DATASOURCE_URL=jdbc:oracle:thin:@oracle:1521/XE
QUARKUS_DATASOURCE_USERNAME=Hospital2
QUARKUS_DATASOURCE_PASSWORD=Unis
```

### Producción
```properties
QUARKUS_DATASOURCE_URL=jdbc:oracle:thin:@oracle:1521/XE
QUARKUS_DATASOURCE_USERNAME=Hospital3
QUARKUS_DATASOURCE_PASSWORD=Unis
```

## 📁 Estructura de Archivos

```
Hospital-2/
├── docker-compose.yml              # Producción + Oracle compartido
├── docker-compose.dev.yml          # Desarrollo (sin Oracle)
├── docker-compose.qa.yml           # QA (sin Oracle)
├── setup-oracle-users.sql          # Script de configuración de usuarios
├── deploy-all-environments.sh      # Script de despliegue automático
└── README-ORACLE-COMPARTIDO.md    # Este archivo
```

## ✅ Ventajas de la Nueva Configuración

1. **Eficiencia de Recursos**: Un solo contenedor Oracle en lugar de tres
2. **Consistencia de Datos**: Todos los entornos usan la misma instancia
3. **Facilidad de Mantenimiento**: Una sola base de datos que gestionar
4. **Configuración Simplificada**: Menos archivos docker-compose
5. **Escalabilidad**: Fácil agregar nuevos entornos

## 🚨 Consideraciones Importantes

1. **Puerto 1521**: Asegúrate de que no esté en uso por otra instancia de Oracle
2. **Permisos de Usuario**: Cada usuario tiene su propio esquema y tablaspace
3. **Datos Compartidos**: Los datos se comparten entre entornos (útil para testing)
4. **Backup**: Hacer backup del volumen `oracle_data` para preservar todos los datos

## 🔍 Troubleshooting

### Oracle no inicia
```bash
# Verificar logs
docker logs hospital-oracle-xe

# Verificar si el puerto está en uso
netstat -tulpn | grep 1521
```

### Usuarios no creados
```bash
# Conectar manualmente y ejecutar script
docker exec -it hospital-oracle-xe sqlplus sys/Unis@//localhost:1521/XE AS SYSDBA
@/opt/oracle/scripts/startup/setup-oracle-users.sql
```

### Contenedores no pueden conectar a Oracle
```bash
# Verificar que Oracle esté ejecutándose
docker ps | grep hospital-oracle-xe

# Verificar conectividad entre contenedores
docker exec hospital-backend-dev ping oracle
```

## 📞 Soporte

Para problemas o preguntas sobre esta configuración, revisa:
1. Los logs de Docker: `docker logs <nombre-contenedor>`
2. El estado de los contenedores: `docker ps`
3. La conectividad de red: `docker network ls`
