# Migración a SonarQube Developer Edition

Este documento te guía paso a paso para migrar de SonarQube Community Edition a Developer Edition.

## 🚨 Consideraciones Importantes

### Antes de comenzar:
- **Licencia**: Necesitas una licencia válida de SonarQube Developer Edition
- **Datos**: Los datos de análisis existentes se perderán durante la migración
- **Tiempo**: La migración puede tomar 10-30 minutos dependiendo de tu sistema

### Diferencias principales:
- **Community Edition**: Usa H2 (base de datos embebida)
- **Developer Edition**: Requiere PostgreSQL o SQL Server

## 📋 Prerrequisitos

1. **Docker y Docker Compose** instalados y funcionando
2. **Licencia** de SonarQube Developer Edition
3. **Acceso** a internet para descargar imágenes de Docker

## 🔄 Pasos de Migración

### Paso 1: Hacer Backup (Recomendado)
```bash
./backup-sonarqube-community.sh
```

### Paso 2: Obtener Licencia
1. Contacta a SonarSource para obtener tu licencia
2. Coloca el archivo de licencia en el directorio `sonar-license/`
3. El archivo debe llamarse `sonar-license.txt`

### Paso 3: Ejecutar Migración
```bash
./migrate-sonarqube-to-dev.sh
```

## 📁 Estructura de Archivos

```
Hospital-1/
├── docker-compose.sonarqube.yml          # Configuración actual (Community)
├── docker-compose.sonarqube-dev.yml      # Nueva configuración (Developer)
├── migrate-sonarqube-to-dev.sh           # Script de migración
├── backup-sonarqube-community.sh         # Script de backup
├── sonar-license/                        # Directorio para licencia
│   └── sonar-license.txt                 # Tu licencia aquí
└── README-SONARQUBE-DEV-MIGRATION.md     # Este archivo
```

## 🗄️ Configuración de Base de Datos

### PostgreSQL:
- **Host**: localhost
- **Puerto**: 5432
- **Usuario**: sonar
- **Contraseña**: sonar123
- **Base de datos**: sonar

### Variables de entorno SonarQube:
```yaml
SONAR_JDBC_URL: jdbc:postgresql://db:5432/sonar
SONAR_JDBC_USERNAME: sonar
SONAR_JDBC_PASSWORD: sonar123
SONAR_LICENSE_FILE: /opt/sonarqube/license/sonar-license.txt
```

## 🚀 Comandos Útiles

### Verificar estado:
```bash
docker-compose -f docker-compose.sonarqube-dev.yml ps
```

### Ver logs:
```bash
docker-compose -f docker-compose.sonarqube-dev.yml logs -f
```

### Detener servicios:
```bash
docker-compose -f docker-compose.sonarqube-dev.yml down
```

### Iniciar servicios:
```bash
docker-compose -f docker-compose.sonarqube-dev.yml up -d
```

## 🔧 Solución de Problemas

### PostgreSQL no inicia:
```bash
docker logs sonarqube-postgres
```

### SonarQube no inicia:
```bash
docker logs sonarqube-dev
```

### Problemas de permisos:
```bash
sudo chown -R 1000:1000 sonar-license/
```

### Verificar conectividad de base de datos:
```bash
docker exec sonarqube-postgres pg_isready -U sonar
```

## 📊 Monitoreo

### Health Checks:
- **PostgreSQL**: Verifica cada 10 segundos
- **SonarQube**: Verifica cada 30 segundos

### Logs importantes:
- **PostgreSQL**: `docker logs sonarqube-postgres`
- **SonarQube**: `docker logs sonarqube-dev`

## 🔒 Seguridad

### Cambiar contraseñas por defecto:
1. Accede a SonarQube (admin/admin)
2. Ve a **Administration > Users**
3. Cambia la contraseña del usuario admin
4. Cambia la contraseña de PostgreSQL si es necesario

### Firewall:
- Puerto 9000: SonarQube
- Puerto 5432: PostgreSQL (solo si necesitas acceso externo)

## 📞 Soporte

### Si tienes problemas:
1. Revisa los logs de Docker
2. Verifica que Docker esté funcionando
3. Asegúrate de que los puertos no estén ocupados
4. Verifica que tengas suficiente espacio en disco

### Recursos adicionales:
- [Documentación oficial de SonarQube](https://docs.sonarqube.org/)
- [Docker Hub SonarQube](https://hub.docker.com/_/sonarqube)
- [PostgreSQL Docker](https://hub.docker.com/_/postgres)

## ✅ Verificación Final

Después de la migración, verifica que:

1. ✅ PostgreSQL esté funcionando
2. ✅ SonarQube esté accesible en http://localhost:9000
3. ✅ Puedas iniciar sesión con admin/admin
4. ✅ La licencia esté activa
5. ✅ Los análisis de código funcionen correctamente

## 🔄 Rollback

Si necesitas volver a Community Edition:

```bash
# Detener Developer Edition
docker-compose -f docker-compose.sonarqube-dev.yml down

# Iniciar Community Edition
docker-compose -f docker-compose.sonarqube.yml up -d
```

---

**Nota**: Este proceso migra la configuración pero no los datos de análisis. Los proyectos y configuraciones deberán ser reconfigurados en Developer Edition.



