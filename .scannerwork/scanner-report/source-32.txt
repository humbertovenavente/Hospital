# Dockerfile para el backend del Hospital
FROM registry.access.redhat.com/ubi8/openjdk-17:1.18

# Metadatos
LABEL maintainer="Hospital Team"
LABEL version="1.0"
LABEL description="Backend del Sistema Hospital"

# Crear directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR del proyecto
COPY backend/target/quarkus-app/lib/ /app/lib/
COPY backend/target/quarkus-app/quarkus-run.jar /app/quarkus-run.jar
COPY backend/target/quarkus-app/app/ /app/app/
COPY backend/target/quarkus-app/quarkus/ /app/quarkus/

# Exponer puerto
EXPOSE 8080

# Variables de entorno
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "quarkus-run.jar"] 