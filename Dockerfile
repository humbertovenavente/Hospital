FROM openjdk:17-jdk-slim

WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copiar archivos del proyecto backend
COPY backend/ ./backend/

# Construir la aplicación
WORKDIR /app/backend
RUN mvn clean package -DskipTests

# Exponer puerto
EXPOSE 8080

# Comando para ejecutar
CMD ["java", "-jar", "target/quarkus-app/lib/quarkus-run.jar"]