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

## 🔧 Configuración de Archivos de Propiedades

### Archivo Principal (`application.properties`)
Contiene la configuración común para todos los perfiles:
- Configuración de pool de conexiones
- Configuración de CORS
- Configuración del servidor
- Configuración de JWT
- Configuración común de Oracle y Hibernate

### Perfil de Desarrollo (`application-dev.properties`)
- **Base de datos**: Oracle XE
- **Usuario**: `C##PROYECTO`
- **Generación de esquema**: `drop-and-create`
- **Log SQL**: Habilitado
- **Script de carga**: `import.sql`

### Perfil de QA (`application-qa.properties`)
- **Base de datos**: Oracle XE
- **Usuario**: `Hospital2`
- **Generación de esquema**: `none`
- **Log SQL**: Habilitado
- **Logging**: Nivel INFO con DEBUG para com.unis

### Perfil de Producción (`application-prod.properties`)
- **Base de datos**: Oracle XE
- **Usuario**: `Hospital3`
- **Generación de esquema**: `none`
- **Log SQL**: Deshabilitado
- **Logging**: Nivel de producción

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
QUARKUS_PROFILE=dev
```

### QA
```properties
QUARKUS_DATASOURCE_URL=jdbc:oracle:thin:@oracle:1521/XE
QUARKUS_DATASOURCE_USERNAME=Hospital2
QUARKUS_DATASOURCE_PASSWORD=Unis
QUARKUS_PROFILE=qa
```

### Producción
```properties
QUARKUS_DATASOURCE_URL=jdbc:oracle:thin:@oracle:1521/XE
QUARKUS_DATASOURCE_USERNAME=Hospital3
QUARKUS_DATASOURCE_PASSWORD=Unis
QUARKUS_PROFILE=prod
```

## 🧪 Pruebas y Verificación

### Script de Prueba de Conexiones
```bash
./test-database-connections.sh
```

Este script verifica:
- Estado de todos los contenedores
- Usuarios configurados en Oracle
- Conexiones de base de datos de cada entorno
- Respuesta de las aplicaciones
- Logs de errores

### Verificación Manual
```bash
# Verificar usuarios en Oracle
docker exec hospital-oracle-xe sqlplus -L "sys/Unis@//localhost:1521/XE" AS SYSDBA <<< "
SELECT username, account_status, default_tablespace 
FROM dba_users 
WHERE username IN ('C##PROYECTO', 'Hospital2', 'Hospital3')
ORDER BY username;
"

# Verificar logs de un contenedor específico
docker logs hospital-backend-prod
docker logs hospital-backend-dev
docker logs hospital-backend-qa
```

## 📁 Estructura de Archivos

```
Hospital-2/
├── docker-compose.yml              # Producción + Oracle compartido
├── docker-compose.dev.yml          # Entorno de desarrollo
├── docker-compose.qa.yml           # Entorno de QA
├── backend/src/main/resources/
│   ├── application.properties      # Configuración común
│   ├── application-dev.properties  # Configuración de desarrollo
│   ├── application-qa.properties   # Configuración de QA
│   └── application-prod.properties # Configuración de producción
├── setup-oracle-users.sql          # Script de configuración de usuarios
├── deploy-all-environments.sh      # Script de despliegue automático
├── test-database-connections.sh    # Script de prueba de conexiones
├── nginx.dev.conf                  # Configuración nginx para desarrollo
└── README-ORACLE-COMPARTIDO.md    # Este archivo
```

## ✅ Ventajas de la Nueva Configuración

1. **Eficiencia de Recursos**: Un solo contenedor Oracle en lugar de tres
2. **Consistencia de Datos**: Todos los entornos usan la misma instancia
3. **Facilidad de Mantenimiento**: Una sola base de datos que gestionar
4. **Configuración Simplificada**: Menos archivos docker-compose
5. **Escalabilidad**: Fácil agregar nuevos entornos
6. **Configuración por Perfiles**: Cada entorno tiene su configuración específica
7. **Pruebas Automatizadas**: Script para verificar conexiones

## 🚨 Consideraciones Importantes

### **Antes de Desplegar**
- ✅ Asegúrate de que el puerto 1521 no esté en uso
- ✅ Verifica que la imagen `oracle-xe-con-datos:latest` esté disponible
- ✅ Asegúrate de que los archivos de configuración estén correctos

### **Durante el Despliegue**
- ✅ Espera a que Oracle esté completamente inicializado
- ✅ Verifica que los usuarios se creen correctamente
- ✅ Comprueba que cada entorno use su perfil correcto

### **Después del Despliegue**
- ✅ Ejecuta el script de prueba de conexiones
- ✅ Verifica que cada entorno responda correctamente
- ✅ Comprueba los logs de cada contenedor

## 🔍 Troubleshooting

### **Oracle no inicia**
```bash
# Verificar logs
docker logs hospital-oracle-xe

# Verificar si el puerto está en uso
netstat -tulpn | grep 1521
```

### **Usuarios no creados**
```bash
# Conectar manualmente y ejecutar script
docker exec -it hospital-oracle-xe sqlplus sys/Unis@//localhost:1521/XE AS SYSDBA
@/opt/oracle/scripts/startup/setup-oracle-users.sql
```

### **Contenedores no pueden conectar a Oracle**
```bash
# Verificar que Oracle esté ejecutándose
docker ps | grep hospital-oracle-xe

# Verificar conectividad entre contenedores
docker exec hospital-backend-prod ping oracle
```

### **Error de perfil de Quarkus**
```bash
# Verificar que el archivo de propiedades esté montado correctamente
docker exec hospital-backend-prod ls -la /app/config/

# Verificar logs de Quarkus
docker logs hospital-backend-prod | grep -i "profile\|datasource"
```

## 📞 Soporte

Para problemas o preguntas sobre esta configuración, revisa:

1. **Los logs de Docker**: `docker logs <nombre-contenedor>`
2. **El estado de los contenedores**: `docker ps`
3. **La conectividad de red**: `docker network ls`
4. **El script de prueba**: `./test-database-connections.sh`
5. **La documentación de Oracle compartido**

## 🎉 ¡Listo!

Una vez completada la configuración, todos tus entornos tendrán:
- **Misma base de datos Oracle** con usuarios separados
- **Configuración específica por perfil** para cada entorno
- **Scripts de despliegue y prueba automatizados**
- **Gestión eficiente de recursos**
- **Desarrollo consistente entre entornos**
