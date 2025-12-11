# 🚀 Guide de Déploiement sur Render.com

## Prérequis
1. Compte Render (gratuit) - https://render.com
2. Compte GitHub avec ce projet
3. MySQL Database (gratuit sur Render ou autre)

---

## 📋 Étape 1: Préparer le Repository GitHub

### 1.1 Pousser votre code sur GitHub
```bash
cd /home/emma-basile/Téléchargements/TP331_MOO
git add .
git commit -m "Préparation pour déploiement Render"
git push origin main
```

### 1.2 Créer les fichiers de configuration Render

#### `.render/build.sh` (Script de build)
```bash
#!/bin/bash
cd yaounde-loc
mvn clean install -DskipTests
```

#### `render.yaml` (Configuration multi-service)
```yaml
services:
  - type: web
    name: yaounde-loc-backend
    env: java
    plan: free
    buildCommand: cd yaounde-loc && mvn clean install -DskipTests
    startCommand: java -jar target/yaounde_loc-0.0.1-SNAPSHOT.jar
    envVars:
      - key: SPRING_DATASOURCE_URL
        value: ${DATABASE_URL}
      - key: SPRING_DATASOURCE_USERNAME
        value: ${DB_USERNAME}
      - key: SPRING_DATASOURCE_PASSWORD
        value: ${DB_PASSWORD}
      - key: SPRING_JPA_HIBERNATE_DDL_AUTO
        value: update
      - key: JWT_SECRET
        value: ${JWT_SECRET}

  - type: web
    name: yaounde-loc-frontend
    env: static
    buildCommand: cd frontend && npm install --legacy-peer-deps && npm run build
    staticPublishPath: frontend/build
    envVars:
      - key: REACT_APP_API_URL
        value: https://yaounde-loc-backend.onrender.com
```

---

## 🗄️ Étape 2: Configurer la Base de Données MySQL

### Option A: PostgreSQL sur Render (Recommandé - Plus facile)

1. Allez sur https://render.com/dashboard
2. Cliquez sur **"New +"** → **"PostgreSQL"**
3. Nom: `yaounde-loc-db`
4. Plan: **Free**
5. Créez la base

Notez les identifiants fournis:
- **Internal Database URL**: `postgresql://user:pass@localhost:5432/db`
- **External Database URL**: `postgresql://user:pass@host:5432/db`

### Option B: MySQL externe (ex: PlanetScale, Railway)

Si vous préférez MySQL:
1. Créez un compte PlanetScale (mysql.com)
2. Créez une base `yaounde_loc`
3. Notez les credentials

---

## 📱 Étape 3: Déployer sur Render

### 3.1 Via GitHub (Méthode Recommandée)

1. **Connecter votre GitHub**
   - Aller sur https://render.com
   - Cliquer "Sign Up" → "GitHub"
   - Autoriser l'accès

2. **Créer le Backend Web Service**
   - Cliquer **"New +"** → **"Web Service"**
   - Connecter votre repository GitHub `TP331_MOO`
   - **Nom**: `yaounde-loc-backend`
   - **Environment**: `Java`
   - **Build Command**:
     ```bash
     cd yaounde-loc && mvn clean install -DskipTests
     ```
   - **Start Command**:
     ```bash
     java -jar target/yaounde_loc-0.0.1-SNAPSHOT.jar
     ```

3. **Ajouter les Variables d'Environnement**
   
   Cliquez sur **"Environment"** et ajoutez:
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://[DB_HOST]:[DB_PORT]/yaounde_loc
   SPRING_DATASOURCE_USERNAME=[DB_USER]
   SPRING_DATASOURCE_PASSWORD=[DB_PASS]
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   JWT_SECRET=your-super-secret-key-change-this
   SPRING_JPA_SHOW_SQL=false
   ```

4. **Créer le Frontend Static Site**
   - Cliquer **"New +"** → **"Static Site"**
   - Connecter le même repository
   - **Nom**: `yaounde-loc-frontend`
   - **Build Command**:
     ```bash
     cd frontend && npm install --legacy-peer-deps && npm run build
     ```
   - **Publish Directory**: `frontend/build`

5. **Configurer le Frontend .env**
   
   Créez `frontend/.env.production`:
   ```
   REACT_APP_API_URL=https://yaounde-loc-backend.onrender.com
   ```

---

## ⚙️ Étape 4: Configuration du Backend pour Render

### Modifier `yaounde-loc/src/main/resources/application.properties`

```properties
# Database
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/yaounde_loc}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:vlad}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:vlad2004}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL:false}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT
jwt.secret=${JWT_SECRET:your-default-secret-key}
jwt.expiration=86400000

# Server
server.port=8080
server.servlet.context-path=/

# CORS
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

---

## 📤 Étape 5: Pousser les Changements

```bash
cd /home/emma-basile/Téléchargements/TP331_MOO

# Ajouter la configuration Render
git add render.yaml DEPLOY_RENDER.md frontend/.env.production

# Commiter
git commit -m "Configuration pour déploiement Render"

# Pousser
git push origin main
```

Render détectera automatiquement les changements et redéploiera.

---

## 🔗 Étape 6: Configurer CORS et URLs

### Frontend
Dans `frontend/src/services/api.js`:
```javascript
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});
```

### Backend
Vérifiez que `CorsConfig.java` permet les requêtes du frontend:
```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

---

## 🧪 Vérification du Déploiement

### 1. Vérifier le statut
```bash
# Sur le dashboard Render, cherchez:
- yaounde-loc-backend → Logs
- yaounde-loc-frontend → Logs
```

### 2. Tester les endpoints
```bash
# URL Backend
https://yaounde-loc-backend.onrender.com/

# URL Frontend
https://yaounde-loc-frontend.onrender.com/

# API Health
https://yaounde-loc-backend.onrender.com/health
```

### 3. Tester Login
```bash
curl -X POST https://yaounde-loc-backend.onrender.com/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@yaoundeloc.com","password":"password"}'
```

---

## 🐛 Dépannage

### Backend ne démarre pas
- Vérifiez les logs: Dashboard → Service → Logs
- Vérifiez les variables d'environnement
- Vérifiez la base de données est accessible

### CORS Error au frontend
- Vérifiez `REACT_APP_API_URL` dans frontend build
- Vérifiez `CorsConfig.java` au backend

### Base de données n'existe pas
```bash
# Se connecter à la BD
mysql -h [DB_HOST] -u [DB_USER] -p[DB_PASS]

# Créer la BD
CREATE DATABASE yaounde_loc;
CREATE USER 'vlad'@'%' IDENTIFIED BY 'vlad2004';
GRANT ALL PRIVILEGES ON yaounde_loc.* TO 'vlad'@'%';
FLUSH PRIVILEGES;
```

---

## 💰 Tarification Render (Gratuit)

- **Web Services**: 0.5 CPU, 0.5 GB RAM (Gratuit)
- **Static Sites**: Gratuit
- **PostgreSQL**: Gratuit (5 GB)

⚠️ **Limitations Gratuit**:
- Services dorment après 15 min d'inactivité
- Bande passante limitée

**Upgrade recommandé pour production**:
- Web Service Standard: $7/mois
- PostgreSQL Standard: $15/mois

---

## 📝 Résumé des URLs Finales

| Service | URL |
|---------|-----|
| Frontend | `https://yaounde-loc-frontend.onrender.com` |
| Backend API | `https://yaounde-loc-backend.onrender.com` |
| Swagger Docs | `https://yaounde-loc-backend.onrender.com/swagger-ui.html` |

---

## ✅ Checklist Final

- [ ] Repository GitHub préparé
- [ ] Base de données créée (MySQL/PostgreSQL)
- [ ] Deux services créés sur Render (Backend + Frontend)
- [ ] Variables d'environnement configurées
- [ ] Application.properties à jour
- [ ] Frontend .env.production créé
- [ ] CORS configuré
- [ ] Tests des endpoints validés
- [ ] Comptes de test créés

Bravo! 🎉 Votre application est maintenant en production sur Render!
