# 🧪 Guide de Test - Yaounde Loc

## 📋 Avant de commencer

Assurez-vous que:
- ✅ MySQL est lancé et la BD créée
- ✅ Backend démarre sans erreurs
- ✅ Frontend démarre sans erreurs
- ✅ Les deux services sont accessibles

```bash
# Vérifier le backend
curl -s http://localhost:8080/swagger-ui.html | head -1

# Vérifier le frontend
curl -s http://localhost:3000 | head -1

# Vérifier MySQL
mysql -u vlad -p yaounde_loc -e "SELECT 1;"
```

---

## 🔑 Scénario de Test 1: Inscription et Connexion

### Étape 1: Inscription Locataire

**Interface Web:**
1. Aller à http://localhost:3000/register
2. Sélectionner "Locataire"
3. Remplir:
   - Email: `locataire@test.com`
   - Nom complet: `Jean Dupont`
   - Téléphone: `6901234567`
   - Mot de passe: `Password123!`
4. Cliquer "S'inscrire"

**Résultat attendu:**
- ✅ Redirection vers page de connexion
- ✅ Message de succès

### Étape 2: Inscription Propriétaire

**Interface Web:**
1. Aller à http://localhost:3000/register?role=proprietaire
2. Sélectionner "Propriétaire"
3. Remplir:
   - Email: `proprietaire@test.com`
   - Nom complet: `Marie Martin`
   - Téléphone: `6907654321`
   - Mot de passe: `Password123!`
4. Cliquer "S'inscrire"

**Résultat attendu:**
- ✅ Redirection vers page de connexion
- ✅ Message de succès

### Étape 3: Connexion

**Interface Web:**
1. Aller à http://localhost:3000/login
2. Remplir:
   - Email: `locataire@test.com`
   - Mot de passe: `Password123!`
3. Cliquer "Se connecter"

**Résultat attendu:**
- ✅ Redirection vers page d'accueil
- ✅ Email affiché dans la navbar
- ✅ Token sauvegardé en localStorage

**Vérifier avec DevTools:**
```javascript
// Console browser
console.log(localStorage.getItem('token'))
console.log(localStorage.getItem('user'))
```

---

## 🏠 Scénario de Test 2: Gestion des Annonces (Propriétaire)

### Étape 1: Se connecter comme Propriétaire

1. Aller à http://localhost:3000/login
2. Email: `proprietaire@test.com`
3. Mot de passe: `Password123!`

**Résultat attendu:**
- ✅ Boutons "Mes Annonces" et "Nouvelle Annonce" visibles

### Étape 2: Créer une Annonce

1. Cliquer sur "Nouvelle Annonce"
2. Remplir le formulaire:
   - Titre: "Bel appartement 2 chambres"
   - Description: "Appartement spacieux au cœur de Yaoundé"
   - Type: "Appartement"
   - Quartier: "Mendong"
   - Prix: "150000"
   - Chambres: "2"
   - Salles de bain: "1"
3. Cliquer "Créer l'annonce"

**Résultat attendu:**
- ✅ Message de succès
- ✅ Redirection vers le dashboard
- ✅ Annonce visible dans "Mes Annonces"

### Étape 3: Éditer une Annonce

1. Dans le dashboard, cliquer sur "Éditer" d'une annonce
2. Modifier le prix: "160000"
3. Cliquer "Mettre à jour"

**Résultat attendu:**
- ✅ Annonce mise à jour
- ✅ Nouveau prix affiché

### Étape 4: Consulter le Statut

**Via Dashboard:**
- Les annonces créées affichent un badge "EN_ATTENTE"

**Via Swagger UI:**
1. Aller à http://localhost:8080/swagger-ui.html
2. Autoriser avec le token JWT
3. Tester GET `/annonces/my-listings`

---

## 🔍 Scénario de Test 3: Consultation des Annonces (Locataire)

### Étape 1: Se connecter comme Locataire

1. Aller à http://localhost:3000/login
2. Email: `locataire@test.com`
3. Mot de passe: `Password123!`

**Résultat attendu:**
- ✅ Pas de boutons "Nouvelle Annonce" ni "Mes Annonces"
- ✅ Lien "Annonces" visible

### Étape 2: Consulter les Annonces Publiques

1. Cliquer sur "Annonces"

**Résultat attendu:**
- ✅ Liste vide (les annonces en attente ne s'affichent pas)

### Étape 3: Filtrer les Annonces

1. Remplir les filtres:
   - Quartier: "Mendong"
   - Type: "Appartement"
   - Prix min: "100000"
   - Prix max: "200000"

**Résultat attendu:**
- ✅ Filtres appliqués en temps réel
- ✅ Messages appropriés si aucune annonce

---

## ✅ Scénario de Test 4: Admin - Validation d'Annonces

### Prérequis
Il faut créer un compte admin en base de données:

```bash
mysql -u root -p yaounde_loc

# Voir les utilisateurs
SELECT id, email FROM users;

# Ajouter le rôle admin (remplacer USER_ID et ROLE_ID)
INSERT INTO user_roles (user_id, role_id) VALUES (1, 3);
```

### Étape 1: Se connecter comme Admin

1. Aller à http://localhost:3000/login
2. Email: (compte admin créé en base)
3. Mot de passe: (mot de passe du compte)

**Résultat attendu:**
- ✅ Bouton "Admin" visible dans la navbar

### Étape 2: Consulter les Annonces en Attente

1. Cliquer sur "Admin"
2. Consulter le tableau

**Résultat attendu:**
- ✅ Annonces créées par propriétaire visibles
- ✅ Boutons "Valider" et "Rejeter"

### Étape 3: Valider une Annonce

1. Cliquer sur "Valider" pour une annonce

**Résultat attendu:**
- ✅ Annonce disparaît du tableau
- ✅ Annonce visible dans les annonces publiques

### Étape 4: Rejeter une Annonce

1. Cliquer sur "Rejeter" pour une annonce
2. Confirmer si demandé

**Résultat attendu:**
- ✅ Annonce disparaît du tableau
- ✅ Propriétaire ne voit plus l'annonce

---

## 🔗 Scénario de Test 5: API Swagger

### Accéder à Swagger UI

1. Aller à http://localhost:8080/swagger-ui.html
2. Autoriser avec un token JWT

### Tester les Endpoints

#### Auth
```bash
# 1. Register
POST /auth/register
{
  "email": "test@example.com",
  "password": "Password123!",
  "fullName": "Test User",
  "phone": "6901234567"
}

# 2. Login
POST /auth/login
{
  "email": "test@example.com",
  "password": "Password123!"
}

# 3. Copier le token de la réponse
```

#### Annonces
```bash
# 1. Lister les annonces (public, pas de token nécessaire)
GET /annonces

# 2. Créer une annonce (besoin du token)
POST /annonces
{
  "titre": "Test Annonce",
  "description": "Description test",
  "prix": 100000,
  "quartier": "Centre",
  "typeBien": "Appartement"
}

# 3. Récupérer mes annonces
GET /annonces/my-listings

# 4. Éditer une annonce
PUT /annonces/{id}

# 5. Supprimer une annonce
DELETE /annonces/{id}
```

#### Admin
```bash
# 1. Lister les annonces en attente
GET /admin/annonces/en-attente

# 2. Valider une annonce
POST /admin/annonces/{id}/valider

# 3. Rejeter une annonce
POST /admin/annonces/{id}/rejeter
```

---

## 🧪 Test avec cURL

### Inscription

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"curl-test@test.com",
    "password":"Password123!",
    "fullName":"Curl Test",
    "phone":"6901234567"
  }'
```

### Login

```bash
TOKEN=$(curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email":"curl-test@test.com",
    "password":"Password123!"
  }' | jq -r '.token')

echo "Token: $TOKEN"
```

### Créer une Annonce

```bash
curl -X POST http://localhost:8080/annonces \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titre":"Curl Test Annonce",
    "description":"Créée via curl",
    "prix":120000,
    "quartier":"Yaoundé",
    "typeBien":"Studio"
  }'
```

### Lister les Annonces

```bash
curl -s http://localhost:8080/annonces | jq '.[] | {id, titre, prix}'
```

---

## ⚠️ Test des Erreurs

### Authentification incorrecte

```bash
# Login avec mauvais mot de passe
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email":"test@test.com",
    "password":"WrongPassword"
  }'

# Résultat attendu: 401 Unauthorized
```

### Token invalide

```bash
curl -X GET http://localhost:8080/annonces/my-listings \
  -H "Authorization: Bearer INVALID_TOKEN"

# Résultat attendu: 401 Unauthorized
```

### Accès non autorisé

```bash
# Essayer de valider une annonce sans être admin
curl -X POST http://localhost:8080/admin/annonces/1/valider \
  -H "Authorization: Bearer LOCATAIRE_TOKEN"

# Résultat attendu: 403 Forbidden
```

---

## 📊 Rapport de Test

Après avoir complété tous les scénarios, vérifiez:

| Test | Status | Notes |
|------|--------|-------|
| Inscription Locataire | ✅ | |
| Inscription Propriétaire | ✅ | |
| Connexion | ✅ | |
| Déconnexion | ✅ | |
| Créer annonce | ✅ | |
| Éditer annonce | ✅ | |
| Supprimer annonce | ✅ | |
| Consulter annonces | ✅ | |
| Filtrer annonces | ✅ | |
| Valider annonce (admin) | ✅ | |
| Rejeter annonce (admin) | ✅ | |
| API Swagger | ✅ | |
| CORS | ✅ | |
| JWT Token | ✅ | |
| Erreurs | ✅ | |

---

## 🐛 Problèmes Courants

### "API not responding"
- Vérifier que le backend est lancé
- Vérifier l'URL API dans .env
- Vérifier CORS configuration

### "Token expired"
- Tokens JWT expirent après 24h
- Se reconnecter pour obtenir un nouveau token

### "Annonces ne s'affichent pas"
- Vérifier que l'annonce est validée (admin)
- Vérifier que l'annonce a le statut "ACCEPTÉE"

### "Upload d'images échoue"
- Vérifier que le dossier `uploads/` existe
- Vérifier les permissions du dossier
- Vérifier la taille des fichiers

---

## ✅ Conclusion

Si tous les tests passent, l'application est **100% fonctionnelle** et prête pour:
- 🚀 Déploiement en production
- 📱 Utilisation en production
- 👥 Utilisation multi-utilisateurs
- 🔄 Maintenance et évolutions

---

**Bon testing! 🧪**
