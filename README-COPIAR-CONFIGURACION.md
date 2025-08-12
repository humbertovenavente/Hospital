# 🔄 Copiar Configuración Oracle Compartido a Ramas dev y QA

## 📋 Descripción

Este documento explica cómo copiar la configuración de **Oracle compartido** que hemos implementado en la rama `prod` hacia las ramas `dev` y `QA` del repositorio.

## 🎯 Objetivo

Sincronizar todas las ramas para que tengan la misma configuración de Oracle compartido, permitiendo que los 3 entornos usen la misma base de datos con usuarios separados.

## 🚀 Opciones de Copia

### **Opción 1: Script Automático (Recomendado)**

```bash
./copy-config-to-branches.sh
```

**Ventajas:**
- ✅ Automatiza todo el proceso
- ✅ Resuelve conflictos automáticamente
- ✅ Maneja errores y verificaciones
- ✅ Hace push a todas las ramas

**Qué hace:**
1. Verifica que estés en la rama `prod`
2. Hace commit de los cambios si es necesario
3. Hace push de la rama `prod`
4. Copia configuración a rama `dev`
5. Copia configuración a rama `QA`
6. Resuelve conflictos automáticamente
7. Verifica el estado final

### **Opción 2: Script Manual Guiado**

```bash
./copy-config-manual.sh
```

**Ventajas:**
- ✅ Te guía paso a paso
- ✅ Puedes elegir qué pasos automatizar
- ✅ Más control sobre el proceso
- ✅ Ideal para aprender el proceso

**Qué hace:**
1. Te guía a través de cada paso
2. Te permite elegir ejecución automática o manual
3. Te muestra los comandos a ejecutar
4. Verifica cada paso antes de continuar

### **Opción 3: Comandos Manuales**

Si prefieres hacer todo manualmente, aquí están los comandos:

#### **Paso 1: Commit en rama prod**
```bash
# Verificar que estés en rama prod
git branch --show-current

# Si no estás en prod, cambiar
git checkout prod

# Agregar cambios
git add .

# Hacer commit
git commit -m "🏥 Configuración Oracle compartido entre entornos"

# Push a origin
git push origin prod
```

#### **Paso 2: Copiar a rama dev**
```bash
# Cambiar a rama dev
git checkout dev

# Actualizar rama dev
git pull origin dev

# Hacer merge desde prod
git merge prod

# Push a origin dev
git push origin dev
```

#### **Paso 3: Copiar a rama QA**
```bash
# Cambiar a rama QA
git checkout QA

# Actualizar rama QA
git pull origin QA

# Hacer merge desde prod
git merge prod

# Push a origin QA
git push origin QA
```

#### **Paso 4: Volver a prod**
```bash
git checkout prod
```

## 🔍 Verificación

Después de copiar la configuración, puedes verificar que todo esté correcto:

### **Verificar ramas**
```bash
git branch -a
```

### **Verificar commits en cada rama**
```bash
# Rama prod
git log --oneline -3 prod

# Rama dev
git log --oneline -3 dev

# Rama QA
git log --oneline -3 QA
```

### **Verificar archivos en cada rama**
```bash
# Cambiar a rama dev
git checkout dev
ls -la docker-compose*.yml

# Cambiar a rama QA
git checkout QA
ls -la docker-compose*.yml

# Volver a prod
git checkout prod
```

## ⚠️ Posibles Conflictos

### **Conflictos de Merge**
Si hay conflictos durante el merge, los scripts los resolverán automáticamente manteniendo la versión de la rama `prod`.

### **Archivos Modificados en Otras Ramas**
Si las ramas `dev` o `QA` tienen modificaciones propias, se pueden perder. En ese caso:
1. Hacer backup de los cambios importantes
2. Resolver conflictos manualmente
3. Hacer commit de la resolución

## 📁 Archivos que se Copian

La configuración incluye estos archivos:

- `docker-compose.yml` - Configuración principal con Oracle compartido
- `docker-compose.dev.yml` - Entorno de desarrollo
- `docker-compose.qa.yml` - Entorno de QA
- `setup-oracle-users.sql` - Script de configuración de usuarios
- `deploy-all-environments.sh` - Script de despliegue automático
- `nginx.dev.conf` - Configuración nginx para desarrollo
- `README-ORACLE-COMPARTIDO.md` - Documentación completa

## 🎯 Resultado Esperado

Después de la copia, todas las ramas tendrán:

1. **Misma configuración de Oracle compartido**
2. **Usuarios separados por entorno:**
   - `C##PROYECTO` (Desarrollo)
   - `Hospital2` (QA)
   - `Hospital3` (Producción)
3. **Scripts de despliegue automático**
4. **Documentación completa**

## 🚨 Consideraciones Importantes

### **Antes de Copiar**
- ✅ Asegúrate de estar en la rama `prod`
- ✅ Haz commit de todos los cambios
- ✅ Haz push de la rama `prod`
- ✅ Verifica que no haya cambios pendientes

### **Después de Copiar**
- ✅ Verifica que todas las ramas tengan la configuración
- ✅ Prueba el despliegue en cada entorno
- ✅ Verifica que los usuarios de Oracle funcionen correctamente

## 🔧 Troubleshooting

### **Error: "No estás en la rama prod"**
```bash
git checkout prod
```

### **Error: "Working tree no está limpio"**
```bash
git status
git add .
git commit -m "Mensaje del commit"
```

### **Error: "Conflictos de merge"**
Los scripts los resuelven automáticamente, pero si quieres resolverlos manualmente:
```bash
git status
# Editar archivos con conflictos
git add .
git commit -m "Resuelto conflicto de merge"
```

### **Error: "Push rechazado"**
```bash
git pull origin <rama>
git push origin <rama>
```

## 📞 Soporte

Si tienes problemas durante la copia:

1. **Revisa los logs del script**
2. **Verifica el estado de git: `git status`**
3. **Revisa los conflictos: `git diff`**
4. **Consulta la documentación de Oracle compartido**

## 🎉 ¡Listo!

Una vez completada la copia, todas tus ramas tendrán la misma configuración de Oracle compartido, permitiendo un desarrollo más eficiente y consistente entre entornos.
