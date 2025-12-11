# 🆘 AIDE ET FAQ - Yaounde Loc

## 🎯 Aidez-moi à... 

### Démarrer l'application
- **Rapide (5 min)** → [QUICKSTART.md](QUICKSTART.md)
- **Script automatisé** → `./start.sh`
- **Docker** → `docker-compose up -d`

### Tester l'application
- **Guide complet** → [TESTING.md](TESTING.md)
- **Créer un compte** → http://localhost:3000/register
- **Se connecter** → http://localhost:3000/login
- **API Swagger** → http://localhost:8080/swagger-ui.html

### Déployer en production
- **Guide complet** → [DEPLOYMENT.md](DEPLOYMENT.md)
- **Docker Compose** → `docker-compose up -d`
- **Serveur Linux** → Voir DEPLOYMENT.md

### Comprendre le projet
- **Vue d'ensemble** → [README.md](README.md)
- **Architecture** → [README.md - Stack Technologique](README.md)
- **Endpoints API** → [README.md - Endpoints API](README.md)

### Trouver un fichier
- **Index du projet** → [INDEX.md](INDEX.md)
- **Structure** → [INDEX.md - Structure du Projet](INDEX.md)

---

## ❓ Questions Fréquentes

### ❓ Comment démarrer?

**Réponse**: Lisez [QUICKSTART.md](QUICKSTART.md) pour démarrer en 5 minutes.

```bash
# Backend
cd yaounde-loc && mvn spring-boot:run

# Frontend (nouveau terminal)
cd frontend && npm install && npm start
```

---

### ❓ L'application ne démarre pas

**Vérifier:**

1. **Java installé?**
   ```bash
   java -version  # Doit afficher Java 17+
   ```

2. **Maven installé?**
   ```bash
   mvn -version  # Doit afficher Maven 3.6+
   ```

3. **MySQL lancé?**
   ```bash
   mysql -u root -p
   SHOW DATABASES;  # yaounde_loc doit exister
   ```

4. **Node.js installé?**
   ```bash
   node -v  # Doit afficher Node 16+
   npm -v   # Doit afficher npm 7+
   ```

5. **Ports disponibles?**
   ```bash
   lsof -i :8080  # Ne doit rien afficher
   lsof -i :3000  # Ne doit rien afficher
   ```

---

### ❓ Port 8080 ou 3000 déjà utilisé

**Solution:**

```bash
# Trouver le processus qui utilise le port
lsof -i :8080  # ou :3000

# Arrêter le processus
kill -9 <PID>

# Ou spécifier un port différent
# Backend: mvn spring-boot:run -Dserver.port=8081
# Frontend: PORT=3001 npm start
```

---

### ❓ La base de données ne s'initialise pas

**Solution:**

```bash
# Créer manuellement la base
mysql -u root -p
CREATE DATABASE yaounde_loc;
exit;

# Vérifier la connexion dans application.properties:
# spring.datasource.url=jdbc:mysql://localhost:3306/yaounde_loc
# spring.datasource.username=vlad
# spring.datasource.password=vlad2004
```

---

### ❓ npm install échoue

**Solution:**

```bash
cd frontend

# Nettoyer et réinstaller
rm -rf node_modules package-lock.json
npm install

# Si toujours échoue, augmenter la mémoire Node
NODE_OPTIONS=--max-old-space-size=4096 npm install
```

---

### ❓ Erreur CORS

**Vérifier:**

1. Backend lancé sur http://localhost:8080
2. Frontend lancé sur http://localhost:3000
3. .env du frontend a la bonne URL:
   ```
   REACT_APP_API_URL=http://localhost:8080
   ```
4. SecurityConfig.java a CORS configuré (déjà fait)

---

### ❓ Token JWT invalide

**Solution:**

- Les tokens expirent après 24h
- Se reconnecter pour obtenir un nouveau token
- Token n'est pas stocké entre les rechargements? Vérifier localStorage

```javascript
// Console browser
console.log(localStorage.getItem('token'))
console.log(localStorage.getItem('user'))
```

---

### ❓ L'API ne répond pas

**Vérifier:**

```bash
# Le backend est lancé?
curl http://localhost:8080/swagger-ui.html

# L'URL dans .env est correcte?
REACT_APP_API_URL=http://localhost:8080

# Les logs du backend
# Vérifier si des erreurs en démarrage
```

---

### ❓ Les images n'apparaissent pas

**Solution:**

```bash
# Créer le dossier uploads
mkdir -p yaounde-loc/uploads

# Vérifier les permissions
chmod 755 yaounde-loc/uploads

# Vérifier dans application.properties:
# file.upload.dir=./uploads
```

---

### ❓ Comment créer un compte admin?

**Solution:**

```bash
# 1. Créer d'abord un compte normal via l'interface

# 2. Ajouter le rôle admin en base
mysql -u root -p yaounde_loc

# Voir les utilisateurs
SELECT id, email FROM users;

# Voir les rôles
SELECT id, nom FROM roles;

# Ajouter le rôle admin (remplacer 1 par l'id de l'utilisateur)
INSERT INTO user_roles (user_id, role_id) 
VALUES (1, 3);  # 3 = ROLE_ADMIN

exit;
```

---

### ❓ Swagger UI est vierge

**Solution:**

- Rafraîchir la page
- Vérifier le backend est lancé
- Vérifier l'URL: http://localhost:8080/swagger-ui.html
- Vérifier les logs du backend

---

### ❓ Comment tester l'API?

**Options:**

1. **Swagger UI** (Recommandé)
   - URL: http://localhost:8080/swagger-ui.html

2. **cURL**
   ```bash
   curl -X GET http://localhost:8080/annonces
   ```

3. **Postman**
   - Importer le endpoint depuis Swagger
   - Ajouter le token JWT en Authorization

---

### ❓ Comment déployer en production?

**Réponse complète:** [DEPLOYMENT.md](DEPLOYMENT.md)

**Résumé:**

```bash
# 1. Build
./build.sh

# 2. Docker
docker-compose -f docker-compose.prod.yml up -d

# 3. Configuration
# - Changer JWT_SECRET
# - Configurer la BD
# - Configurer les URLs
# - Configurer HTTPS
```

---

### ❓ Comment contribuer?

1. Fork le projet
2. Créer une branche feature
3. Faire les modifications
4. Tester avec [TESTING.md](TESTING.md)
5. Faire un pull request

---

### ❓ Comment signaler un bug?

1. Vérifier que c'est un vrai bug (voir FAQ)
2. Reproduire le bug
3. Documenter les étapes
4. Ouvrir une issue GitHub

---

## 🔍 Troubleshooting Avancé

### Logs du Backend

```bash
# Voir les logs en temps réel
sudo journalctl -u yaounde-loc -f

# Voir les 50 dernières lignes
sudo journalctl -u yaounde-loc -n 50

# Rechercher une erreur
sudo journalctl -u yaounde-loc | grep ERROR
```

### Logs du Frontend

**Dans le navigateur:**
- F12 → Console
- Chercher les messages rouges (errors)
- Chercher les messages orange (warnings)

### Base de Données

```bash
# Vérifier la connexion
mysql -h localhost -u vlad -p yaounde_loc -e "SELECT 1;"

# Réparer la base (si corrompue)
sudo mysqlcheck -u root -p --all-databases --repair

# Exporter les données
mysqldump -u vlad -p yaounde_loc > backup.sql

# Importer les données
mysql -u vlad -p yaounde_loc < backup.sql
```

---

## 📚 Ressources Supplémentaires

- **Spring Boot** : https://spring.io/projects/spring-boot
- **React** : https://react.dev
- **MySQL** : https://dev.mysql.com
- **JWT** : https://jwt.io
- **Docker** : https://www.docker.com
- **Nginx** : https://nginx.org

---

## 🎓 Pour Apprendre

### Comprendre le Backend
1. Lire [README.md](README.md)
2. Examiner `controller/` pour les endpoints
3. Examiner `service/` pour la logique métier
4. Examiner `model/` pour les entités
5. Examiner `security/` pour la sécurité

### Comprendre le Frontend
1. Lire `src/App.js` pour le routing
2. Examiner `pages/` pour les pages
3. Examiner `components/` pour les composants
4. Examiner `services/api.js` pour l'API
5. Examiner `context/` pour l'état

---

## 💡 Conseils et Bonnes Pratiques

1. **Toujours lire la doc** avant de poser une question
2. **Vérifier les logs** quand quelque chose échoue
3. **Utiliser Swagger UI** pour tester l'API
4. **Utiliser DevTools** pour déboguer le frontend
5. **Faire des backups** avant de modifier la BD

---

## 🆘 Besoin d'aide supplémentaire?

1. **Vérifier INDEX.md** pour trouver le bon document
2. **Lire QUICKSTART.md** pour les bases
3. **Consulter cette FAQ**
4. **Contacter l'équipe de support**
5. **Vérifier les issues GitHub**

---

**Dernière mise à jour:** 11 décembre 2024
