# Configuración de Perfiles - Backend Hospital

Este proyecto utiliza perfiles de Maven/Quarkus para manejar diferentes configuraciones según el entorno.

## Archivos de Configuración

### `application.properties`
- **Propósito**: Configuración común para todos los perfiles
- **Contiene**: Configuraciones de pool de conexiones, CORS general, servidor

### `application-dev.properties`
- **Propósito**: Configuración específica para desarrollo
- **Base de datos**: H2 (en memoria)
- **CORS**: Permite localhost:5173, 5174, 5176, 8085
- **Hibernate**: Genera esquemas automáticamente
- **Consola H2**: Habilitada en `/h2-console`

### `application-prod.properties`
- **Propósito**: Configuración específica para producción
- **Base de datos**: Oracle XE
- **CORS**: Permite solo localhost:5176, 8085
- **Hibernate**: No genera esquemas (usa existente)

## Cómo Usar los Perfiles

### Desarrollo Local
```bash
# Usar perfil de desarrollo (por defecto)
mvn quarkus:dev

# O explícitamente
mvn quarkus:dev -Dquarkus.profile=dev
```

### Producción
```bash
# Usar perfil de producción
mvn quarkus:dev -Dquarkus.profile=prod

# O para build
mvn clean package -Dquarkus.profile=prod
```

## Cambiar Perfil en Runtime

### Variable de Entorno
```bash
export QUARKUS_PROFILE=dev
mvn quarkus:dev
```

### Propiedad del Sistema
```bash
mvn quarkus:dev -Dquarkus.profile=dev
```

## Datos de Prueba (Desarrollo)

El perfil de desarrollo incluye:
- **Usuarios de prueba**:
  - `admin@hospital.com` / `admin123` (ADMIN)
  - `doctor@hospital.com` / `doctor123` (DOCTOR)
  - `paciente@hospital.com` / `paciente123` (PACIENTE)
  - `empleado@hospital.com` / `empleado123` (EMPLEADO)

- **Datos básicos**: Hospital, servicios, aseguradora

## Consola H2 (Solo Desarrollo)

- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: (vacía)

## Solución de Problemas

### Error de CORS
- Verificar que el puerto del frontend esté en `quarkus.http.cors.origins`
- Reiniciar el backend después de cambios en CORS

### Error de Base de Datos
- **Desarrollo**: Verificar que H2 esté en las dependencias
- **Producción**: Verificar conectividad a Oracle y credenciales

### Cambio de Perfil No Funciona
- Verificar que el archivo del perfil existe
- Reiniciar completamente el backend
- Verificar logs de Quarkus para confirmar perfil activo
