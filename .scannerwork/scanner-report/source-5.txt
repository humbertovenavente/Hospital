# 1️⃣ Etapa de construcción
FROM node:18 AS build
WORKDIR /app

# Copiar package.json y package-lock.json para instalar dependencias
COPY package*.json ./
RUN npm install

# Copiar todo el frontend y construir la aplicación
COPY . .
RUN npm run build

# 2️⃣ Etapa de producción con Nginx
FROM nginx:alpine

# Asegurar que el archivo nginx.conf se copia correctamente
COPY src/nginx.conf /etc/nginx/nginx.conf

# Copiar archivos compilados del frontend a Nginx
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
