# Dockerfile pour le frontend React

FROM node:18-alpine as builder

WORKDIR /app

# Copier les fichiers de dépendances
COPY package.json package-lock.json ./

# Installer les dépendances
RUN npm ci

# Copier le code source
COPY . .

# Builder l'application
RUN npm run build

# Stage final avec serveur Nginx
FROM nginx:alpine

# Copier les fichiers buildés depuis le stage de build
COPY --from=builder /app/build /usr/share/nginx/html

# Copier la configuration Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exposer le port
EXPOSE 3000

# Démarrer Nginx
CMD ["nginx", "-g", "daemon off;"]
