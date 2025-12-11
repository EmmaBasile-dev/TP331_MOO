# 🚀 Quick Start - Yaounde Loc

## Installation Rapide (5 minutes)

### 1️⃣ Prérequis

Vérifiez que vous avez installé :
- [x] Java 17+ (vérifiez avec `java -version`)
- [x] Maven 3.6+ (vérifiez avec `mvn -version`)
- [x] Node.js 16+ (vérifiez avec `node -v`)
- [x] MySQL 5.7+ (vérifiez avec `mysql --version`)

### 2️⃣ Créer la base de données

```bash
# Ouvrir MySQL
mysql -u root -p

# Créer la base
CREATE DATABASE yaounde_loc;

# Créer l'utilisateur (optionnel, utiliser vlad/vlad2004)
# À adapter selon vos besoins
```

### 3️⃣ Démarrer le Backend

```bash
cd yaounde-loc

# Compiler et démarrer
mvn spring-boot:run

# ✅ Attendez: "Started YaoundeLocApplication in X seconds"
# 🌐 API disponible: http://localhost:8080
# 📚 Swagger UI: http://localhost:8080/swagger-ui.html
```

### 4️⃣ Démarrer le Frontend (nouvel terminal)

```bash
cd frontend

# Installer les dépendances (première fois seulement)
npm install

# Démarrer l'app
npm start

# ✅ Attend que l'application s'ouvre
# 🌐 App disponible: http://localhost:3000
```

---

## 🧪 Tester l'application

### 1. Créer un compte

**Locataire:**
- URL: http://localhost:3000/register
- Rôle: Locataire
- Email: `locataire@test.com`
- Mot de passe: `Password123!`

**Propriétaire:**
- URL: http://localhost:3000/register?role=proprietaire
- Rôle: Propriétaire
- Email: `proprietaire@test.com`
- Mot de passe: `Password123!`

### 2. Se connecter

- URL: http://localhost:3000/login
- Utiliser les identifiants créés

### 3. Tester les fonctionnalités

**Pour Propriétaires:**
- ➕ Créer une annonce
- ✏️ Éditer une annonce
- 🗑️ Supprimer une annonce

**Pour Locataires:**
- 🔍 Consulter les annonces
- 🔎 Filtrer par quartier, prix, type

### 4. Admin Dashboard

Pour accéder à l'admin, vous devez avoir le rôle `ROLE_ADMIN` en base de données:

```bash
mysql -u root -p yaounde_loc

# Ajouter un rôle admin à un utilisateur existant
SELECT * FROM users WHERE email = 'admin@test.com';

# INSERT/UPDATE si nécessaire
```

---

## 📝 Endpoints à tester

### Via Swagger UI (Recommandé)
http://localhost:8080/swagger-ui.html

### Via cURL

```bash
# 1. Inscription
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"user@test.com",
    "password":"Password123!",
    "fullName":"John Doe",
    "phone":"690123456"
  }'

# 2. Connexion
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email":"user@test.com",
    "password":"Password123!"
  }'

# 3. Lister les annonces (sans auth)
curl http://localhost:8080/annonces

# 4. Créer une annonce (avec token)
curl -X POST http://localhost:8080/annonces \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "titre":"Bel appartement",
    "description":"Proche du centre-ville",
    "prix":150000,
    "quartier":"Mendong",
    "typeBien":"Appartement"
  }'
```

---

## 🐛 Dépannage rapide

| Problème | Solution |
|----------|----------|
| Port 8080 déjà utilisé | `lsof -i :8080` puis `kill -9 <PID>` |
| Port 3000 déjà utilisé | `lsof -i :3000` puis `kill -9 <PID>` |
| Base de données non trouvée | Vérifiez MySQL est lancé et la DB créée |
| CORS error | Vérifiez `application.properties` et CorsConfig.java |
| npm install échoue | Supprimez `node_modules` et `package-lock.json`, réinstallez |
| Les images ne s'affichent pas | Créez le dossier `yaounde-loc/uploads` |

---

## 📚 Ressources supplémentaires

- 📖 **Documentation complète:** [README.md](README.md)
- 🚀 **Guide de déploiement:** [DEPLOYMENT.md](DEPLOYMENT.md)
- 🏗️ **Architecture:** Voir les fichiers dans `docs/`
- 🔌 **API Documentation:** http://localhost:8080/swagger-ui.html

---

## ✅ Checklist de vérification

```
Backend:
☐ Java 17+ installé
☐ Maven fonctionne
☐ MySQL lancé
☐ BD yaounde_loc créée
☐ `mvn spring-boot:run` démarre sans erreurs
☐ Swagger UI accessible

Frontend:
☐ Node.js 16+ installé
☐ npm install complété
☐ Pas d'erreurs de compilation
☐ `npm start` lance l'app
☐ Connexion API fonctionne

Application:
☐ Inscription fonctionne
☐ Connexion fonctionne
☐ Consultation annonces fonctionne
☐ CRUD annonces (pour propriétaires) fonctionne
☐ Admin validation fonctionne
```

---

## 🎯 Prochaines étapes

1. ✅ Tester les fonctionnalités de base
2. ✅ Créer des données de test
3. ✅ Vérifier l'API avec Swagger
4. ✅ Explorer le code source
5. ✅ Lire la documentation complète
6. ✅ Déployer en production (voir DEPLOYMENT.md)

---

**Besoin d'aide?** 
- 📞 Contactez l'équipe de support
- 💬 Consultez les issues GitHub
- 📖 Lire la documentation

**Bon développement! 🚀**
