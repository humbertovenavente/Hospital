# Configuración SMTP para Jenkins - Deuda Técnica

## Resumen del Problema
El servicio de correo de deuda técnica no estaba enviando emails desde Jenkins porque tenía valores por defecto hardcodeados que sobrescribían la configuración real de Gmail.

## Solución Implementada

### 1. Configuración Unificada
- **SÍ puedes usar la misma clave de Google App** para ambos servicios
- El servicio de deuda técnica ahora usa la misma configuración que el servicio principal
- Configuración centralizada en `application.properties`

### 2. Cambios Realizados

#### A. TechnicalDebtEmailService.java
- Eliminados valores por defecto hardcodeados
- Agregado logging detallado para debugging
- Agregados métodos getter para configuración SMTP

#### B. application.properties
- Configuración SMTP unificada para Gmail
- Agregadas configuraciones adicionales para mejor conectividad:
  ```properties
  quarkus.mailer.auth-methods=PLAIN,LOGIN
  quarkus.mailer.max-message-size=10485760
  quarkus.mailer.connection-timeout=30000
  quarkus.mailer.read-timeout=30000
  ```

#### C. Nuevos Endpoints de Prueba
- `GET /api/email/technical-debt/status` - Estado del servicio
- `GET /api/email/technical-debt/test-smtp` - Prueba SMTP
- `POST /api/email/technical-debt/send-report` - Envío simplificado

## Pasos para Jenkins

### 1. Verificar Configuración
```bash
# Desde Jenkins, ejecutar:
curl http://localhost:8080/api/email/technical-debt/status
```

### 2. Probar Conectividad SMTP
```bash
# Probar la conexión SMTP:
curl http://localhost:8080/api/email/technical-debt/test-smtp
```

### 3. Enviar Reporte de Prueba
```bash
# Enviar reporte de prueba:
curl -X POST http://localhost:8080/api/email/technical-debt/send-report \
  -H "Content-Type: application/json" \
  -d '{"recipientEmail": "jflores@unis.edu.gt"}'
```

### 4. Usar Script de Prueba
```bash
# Ejecutar script completo:
./test-smtp-jenkins.sh
```

## Logs de Debugging

El servicio ahora genera logs detallados que incluyen:
- Configuración SMTP utilizada
- Estado de cada paso del envío
- Errores específicos con contexto

### Ejemplo de Logs:
```
=== INICIO ENVÍO REPORTE DEUDA TÉCNICA ===
Proyecto: hospital-pipeline - Hospital Pipeline - Análisis Automático
Configuración SMTP:
  Host: smtp.gmail.com
  Puerto: 587
  Usuario: humbertovenavente7@gmail.com
  From: humbertovenavente7@gmail.com
  SSL: false
  StartTLS: true
```

## Verificación en Jenkins

### 1. Pipeline Step
```groovy
stage('Test SMTP') {
    steps {
        script {
            // Verificar estado del servicio
            sh 'curl -s http://localhost:8080/api/email/technical-debt/status'
            
            // Probar envío
            sh 'curl -s -X POST http://localhost:8080/api/email/technical-debt/send-report -H "Content-Type: application/json" -d \'{"recipientEmail": "jflores@unis.edu.gt"}\''
        }
    }
}
```

### 2. Verificar Logs
- Revisar logs del backend en Jenkins
- Buscar mensajes de "=== INICIO ENVÍO REPORTE DEUDA TÉCNICA ==="
- Verificar que no haya errores de autenticación SMTP

## Troubleshooting

### Problemas Comunes

1. **Error de Autenticación**
   - Verificar que la clave de Google App sea correcta
   - Confirmar que `start-tls=required` esté configurado

2. **Timeout de Conexión**
   - Verificar conectividad de red desde Jenkins
   - Revisar firewalls y proxies

3. **Configuración Incorrecta**
   - Usar endpoint `/status` para verificar configuración
   - Comparar con configuración en `application.properties`

### Comandos de Verificación
```bash
# Verificar que el servicio esté corriendo
netstat -tlnp | grep 8080

# Verificar conectividad SMTP
telnet smtp.gmail.com 587

# Verificar logs del backend
docker logs <container-id> | grep "DEUDA TÉCNICA"
```

## Notas Importantes

- **La misma clave de Google App funciona para ambos servicios**
- **No se necesitan configuraciones separadas**
- **El logging detallado ayuda a identificar problemas específicos**
- **Los endpoints de prueba permiten verificar la funcionalidad sin afectar el pipeline principal**
