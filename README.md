# Yaounde Loc - Application de Location Immobilière

## 📋 Description

Yaounde Loc est une plateforme web complète de location immobilière pour Yaoundé. Elle permet aux propriétaires de publier des annonces et aux locataires de trouver des logements. La plateforme inclut un système d'administration pour la validation des annonces.

### Fonctionnalités principales

- ✅ **Authentification et Inscription** - Système d'authentification sécurisé avec JWT
- 📱 **Trois rôles utilisateur** :
  - Locataire : Consulter les annonces
  - Propriétaire : Créer, éditer, supprimer des annonces
  - Admin : Valider/Rejeter les annonces
- 🏠 **Annonces immobilières** - Création avec images, description, prix
- 🔍 **Filtrage avancé** - Par quartier, type de bien, prix
- 💾 **Gestion d'annonces** - Créer, éditer, supprimer pour propriétaires
- ✔️ **Validation d'annonces** - Système d'approbation par admin

---

## 🛠️ Stack Technologique

### Backend
- **Java 17**
- **Spring Boot 3.3.1**
- **Spring Security** - Authentification/Autorisation
- **Spring Data JPA** - ORM
- **JWT (JSON Web Token)** - Authentification
- **MySQL** - Base de données
- **Maven** - Gestionnaire de dépendances
- **Swagger/OpenAPI** - Documentation API

### Frontend
- **React 18.2.0**
- **React Router v6** - Routing
- **Axios** - Client HTTP
- **CSS3** - Styles personnalisés
- **Node.js/npm** - Environnement d'exécution

---

## 📦 Installation

### Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- MySQL 5.7+
- Node.js 16+ et npm
- Git

### 1. Configuration Backend

#### a) Base de données

```bash
# Créer la base de données
mysql -u root -p
CREATE DATABASE yaounde_loc;
exit
```

#### b) Configuration application.properties

Modifiez `/yaounde-loc/src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yaounde_loc?useSSL=false&serverTimezone=UTC
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe

# Pour la production, changez la clé secrète JWT
jwt.secret=UNE_CLE_TRES_LONGUE_ET_COMPLEXE_POUR_LA_PRODUCTION_MIN_256_BITS
jwt.expiration.ms=86400000
```

#### c) Compilation et démarrage

```bash
cd yaounde-loc

# Compiler le projet
mvn clean compile

# Exécuter les tests
mvn test

# Démarrer l'application
mvn spring-boot:run
```

Le backend sera disponible sur `http://localhost:8080`

**Documentation API (Swagger)** : `http://localhost:8080/swagger-ui.html`

### 2. Configuration Frontend

#### a) Installation des dépendances

```bash
cd frontend

# Installer les packages
npm install
```

#### b) Configuration .env

Vérifiez que `.env` contient :

```
REACT_APP_API_URL=http://localhost:8080
REACT_APP_API_TIMEOUT=30000
```

#### c) Démarrage du frontend

```bash
# Mode développement
npm start

# L'application s'ouvrira sur http://localhost:3000
```

---

## 📖 Utilisation

### Créer un compte

1. Allez sur http://localhost:3000/register
2. Sélectionnez votre rôle :
   - **Locataire** : Pour consulter les annonces
   - **Propriétaire** : Pour publier des annonces
3. Remplissez le formulaire avec :
   - Email
   - Nom complet
   - Téléphone
   - Mot de passe

### Pour les Propriétaires

1. Connectez-vous avec vos identifiants
2. Cliquez sur "Nouvelle Annonce"
3. Remplissez les détails :
   - Titre
   - Description
   - Type de bien
   - Quartier
   - Prix
   - Nombre de chambres/salles de bain
   - Images (optionnel)
4. Soumettez - L'annonce en attente de validation admin

### Pour les Admins

1. Connectez-vous avec les identifiants admin
2. Allez sur le Tableau de Bord Admin
3. Validez ou rejetez les annonces en attente
4. Les annonces validées apparaissent publiquement

### Pour les Locataires

1. Consultez les annonces disponibles
2. Filtrez par :
   - Quartier
   - Type de bien
   - Gamme de prix
3. Consultez les détails de chaque annonce

---

## 🗄️ Structure Base de Données

```
USERS
├── id (PK)
├── email (UNIQUE)
├── password
├── fullName
├── phone
├── enabled
└── roles (M:N)

ROLES
├── id (PK)
├── nom
└── users (M:N)

ANNONCES
├── id (PK)
├── titre
├── description
├── prix
├── quartier
├── typeBien
├── statut (EN_ATTENTE, ACCEPTÉE, REJETÉE)
├── imageUrl
├── dateCreation
├── proprietaire_id (FK)
└── images (1:N)

IMAGES
├── id (PK)
├── url
└── annonce_id (FK)

FAVORIS
├── id (PK)
├── user_id (FK)
└── annonce_id (FK)
```

---

## 🔐 Authentification et Sécurité

### JWT (JSON Web Token)

- Les tokens JWT sont générés lors de la connexion
- Stockés dans `localStorage` côté client
- Valables 24h par défaut
- Envoyés dans le header `Authorization: Bearer <token>`

### Rôles et Permissions

| Action | Locataire | Propriétaire | Admin |
|--------|-----------|-------------|-------|
| Consulter annonces | ✅ | ✅ | ✅ |
| Créer annonce | ❌ | ✅ | ❌ |
| Éditer annonce | ❌ | ✅* | ❌ |
| Supprimer annonce | ❌ | ✅* | ❌ |
| Valider annonce | ❌ | ❌ | ✅ |
| Rejeter annonce | ❌ | ❌ | ✅ |

*Seulement sa propre annonce

---

## 🧪 Tests

### Backend

```bash
cd yaounde-loc

# Exécuter tous les tests
mvn test

# Exécuter les tests avec rapport
mvn test jacoco:report

# Tests spécifiques
mvn test -Dtest=UserServiceTest
```

### Frontend

```bash
cd frontend

# Exécuter les tests
npm test

# Avec couverture
npm test -- --coverage
```

---

## 🚀 Déploiement

### Build Backend (Production)

```bash
cd yaounde-loc
mvn clean package -DskipTests

# JAR généré : target/yaounde_loc-0.0.1-SNAPSHOT.jar
java -jar target/yaounde_loc-0.0.1-SNAPSHOT.jar
```

### Build Frontend (Production)

```bash
cd frontend
npm run build

# Contenu statique dans : build/
# Déployer le dossier 'build' sur votre serveur
```

---

## 🐛 Dépannage

### Le backend ne démarre pas

```bash
# Vérifier MySQL
mysql -u root -p
SHOW DATABASES;

# Vérifier les dépendances
mvn dependency:tree

# Nettoyer et reconstruire
mvn clean install
```

### Le frontend ne se connecte pas au backend

```bash
# Vérifier l'URL API dans .env
REACT_APP_API_URL=http://localhost:8080

# Vérifier CORS sur le backend (SecurityConfig.java)
```

### Les images n'apparaissent pas

```bash
# Vérifier le dossier uploads
mkdir -p yaounde-loc/uploads

# Vérifier les permissions
chmod 755 yaounde-loc/uploads
```

---

## 📝 Endpoints API

### Authentification

```
POST   /auth/register              - Inscription locataire
POST   /auth/register-proprietaire - Inscription propriétaire
POST   /auth/login                 - Connexion
```

### Annonces

```
GET    /annonces                   - Lister les annonces validées
GET    /annonces/my-listings       - Mes annonces (propriétaire)
POST   /annonces                   - Créer une annonce
PUT    /annonces/{id}              - Modifier une annonce
DELETE /annonces/{id}              - Supprimer une annonce
GET    /annonces/{id}              - Détails d'une annonce
```

### Admin

```
GET    /admin/annonces/en-attente  - Annonces en attente
POST   /admin/annonces/{id}/valider - Valider une annonce
POST   /admin/annonces/{id}/rejeter - Rejeter une annonce
```

### Images

```
POST   /images/upload/{annonceId}  - Upload image
GET    /files/{filename}           - Télécharger image
```

### Favoris

```
GET    /favoris                    - Mes favoris
POST   /favoris                    - Ajouter aux favoris
DELETE /favoris/{id}               - Supprimer des favoris
```

---

## 👥 Contributeurs

- **Développement** : Equipe TP331_MOO

---

## 📄 Licence

Ce projet est sous licence MIT.

---

## 📞 Support

Pour toute question ou problème, veuillez contacter l'équipe de développement ou consulter la documentation complète.

---

## ✨ Améliorations Futures

- [ ] System de notifications par email
- [ ] Chat en direct propriétaire-locataire
- [ ] Système de notation/avis
- [ ] Galerie d'images améliorée
- [ ] Recherche géographique avec carte
- [ ] Application mobile (React Native)
- [ ] Intégration paiement en ligne
- [ ] Système d'annulation/réservation

---

**Dernière mise à jour** : 11 décembre 2024