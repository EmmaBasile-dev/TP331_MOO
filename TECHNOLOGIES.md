# 📚 Guide Complet des Technologies - Yaounde Loc

## Table des Matières
1. [Architecture Globale](#architecture-globale)
2. [Backend Spring Boot](#backend-spring-boot)
3. [Frontend React](#frontend-react)
4. [Base de Données](#base-de-données)
5. [Sécurité et Authentification](#sécurité-et-authentification)
6. [Déploiement](#déploiement)
7. [Outils et Build](#outils-et-build)

---

## Architecture Globale

```
┌─────────────────────────────────────────────────────────────┐
│                    INTERNET / UTILISATEUR                   │
└──────────────────────┬──────────────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
   ┌─────────┐   ┌──────────┐   ┌──────────┐
   │ NGINX   │   │ Frontend │   │ Browser  │
   │ (Port  │   │  React   │   │  (Port   │
   │  80/   │   │ (Port    │   │  3000)   │
   │ 443)   │   │  3000)   │   └──────────┘
   └────┬────┘   └────┬─────┘
        │             │
        │ HTTP/HTTPS  │
        │             │
        └──────┬──────┘
               │
        ┌──────▼──────────┐
        │    Backend      │
        │  Spring Boot    │
        │  (Port 8080)    │
        └────────┬────────┘
                 │
                 │ JDBC
                 │
        ┌────────▼────────┐
        │   MySQL DB      │
        │  (Port 3306)    │
        └─────────────────┘
```

---

## Backend Spring Boot

### 1. **Framework: Spring Boot 3.3.1**

Spring Boot est un framework Java qui facilite la création d'applications web robustes et scalables.

**Pourquoi Spring Boot?**
- Dépendances automatiques (autoconfiguration)
- Embedded Tomcat (serveur inclus)
- Production-ready en peu de temps
- Écosystème riche

**Version**: `3.3.1`
**Java**: `21 LTS` (Long Term Support)

```xml
<!-- pom.xml -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.1</version>
</parent>
```

---

### 2. **Web & REST API: Spring Web**

Crée des endpoints REST HTTP pour communiquer avec le frontend.

**Endpoints principaux:**
```
POST   /auth/login              → Authentifier un utilisateur
POST   /auth/register           → Créer un nouveau compte
GET    /annonces                → Lister toutes les annonces
POST   /annonces                → Créer une nouvelle annonce
GET    /annonces/{id}           → Détails d'une annonce
PUT    /annonces/{id}           → Modifier une annonce
DELETE /annonces/{id}           → Supprimer une annonce
POST   /admin/validate/{id}     → Valider une annonce (admin)
POST   /favoris/{id}            → Ajouter aux favoris
```

**Architecture REST:**
- **Resource**: `/annonces` = Collection d'annonces
- **HTTP Methods**: GET (lecture), POST (création), PUT (modification), DELETE (suppression)
- **Status Codes**: 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found, 500 Error

```java
@RestController
@RequestMapping("/annonces")
public class AnnonceController {
    
    @GetMapping
    public List<Annonce> getAllAnnonces() {
        // Retourne: HTTP 200 + JSON array
    }
    
    @PostMapping
    public Annonce createAnnonce(@RequestBody Annonce annonce) {
        // Retourne: HTTP 201 Created + JSON object
    }
}
```

---

### 3. **Base de Données: Spring Data JPA + Hibernate**

**JPA** = Java Persistence API (standard pour accéder aux bases de données)
**Hibernate** = Implémentation JPA la plus populaire

**Avantages:**
- SQL automatique généré
- Objets Java mappés aux tables
- Requêtes orientées objet (JPQL)
- Gestion des migrations

**Exemple:**
```java
@Entity
@Table(name = "annonces")
public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titre;
    
    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private User proprietaire;
}
```

**SQL généré:**
```sql
CREATE TABLE annonces (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    proprietaire_id BIGINT,
    FOREIGN KEY (proprietaire_id) REFERENCES users(id)
);
```

---

### 4. **Base de Données: MySQL 5.7+**

MySQL est une base de données relationnelle (RDBMS).

**Schéma:**
```
┌─────────────┐
│   USERS     │
├─────────────┤
│ id (PK)     │
│ email       │◄──────────┐
│ password    │           │
│ full_name   │           │
│ phone       │           │
│ enabled     │           │
└─────────────┘           │
        │                 │
        │ M:M (roles)     │
        ▼                 │
┌─────────────┐    ┌──────────────┐
│ USER_ROLES  │    │    ROLES     │
├─────────────┤    ├──────────────┤
│ user_id(FK) │───→│ id (PK)      │
│ role_id(FK) │───→│ name (ENUM)  │
└─────────────┘    │ - ADMIN      │
        │          │ - LOCATAIRE  │
        │          │ - PROP.      │
        │          └──────────────┘
        │
        └────────────────┐
                         │
┌──────────────┐    ┌────▼────────┐
│   FAVORIS    │    │  ANNONCES   │
├──────────────┤    ├─────────────┤
│ id (PK)      │    │ id (PK)     │
│ user_id (FK) │───→│ titre       │
│ annonce_id   │    │ description │
│ (FK)         │───→│ prix        │
│ date_ajout   │    │ quartier    │
└──────────────┘    │ type_bien   │
                    │ statut      │
                    │ proprietaire│
                    │ (FK)        │
                    └─────┬───────┘
                          │
                    ┌─────▼──────┐
                    │   IMAGES   │
                    ├────────────┤
                    │ id (PK)    │
                    │ file_name  │
                    │ file_path  │
                    │ file_type  │
                    │ annonce_id │
                    │ (FK)       │
                    └────────────┘
```

**Connexion:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yaounde_loc
spring.datasource.username=vlad
spring.datasource.password=vlad2004
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

---

### 5. **Sécurité: Spring Security + JWT**

#### 5.1 Spring Security

Framework de sécurité qui gère:
- Authentification (qui êtes-vous?)
- Autorisation (qu'avez-vous le droit de faire?)
- Protection CSRF
- Filtrage des requêtes

```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(csrf -> csrf.disable())
           .authorizeHttpRequests(auth -> auth
               .requestMatchers("/auth/login", "/auth/register").permitAll()
               .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
               .anyRequest().authenticated()
           );
        return http.build();
    }
}
```

#### 5.2 JWT (JSON Web Tokens)

Token stateless pour l'authentification sans session.

**Structure JWT:**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJhZG1pbkB5YW91bmRlbG9jLmNvbSIsImlhdCI6MTUxNjIzOTAyMn0.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

Headers.Payload.Signature
```

**Avantages:**
- Stateless (pas de session serveur)
- Scalable (peut être partagé entre serveurs)
- Sécurisé (signature)
- Expiration automatique

**Implémentation:**
```java
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret = "super-secret-key";
    
    @Value("${jwt.expiration}")
    private long jwtExpirationMs = 86400000; // 24 heures
    
    public String generateToken(String email) {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }
    
    public String getUserEmailFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
```

**Flow d'authentification:**
```
1. Client envoie email + password
2. Backend valide les credentials
3. Backend génère JWT token
4. Client reçoit token
5. Client ajoute token aux headers: Authorization: Bearer <token>
6. Backend vérifie le token à chaque requête
7. Si token expiré → 401 Unauthorized
```

#### 5.3 BCrypt

Hachage sécurisé des mots de passe.

**Sans BCrypt (DANGEREUX):**
```
Password: "password123" → Stocké en clair dans DB ❌
Pirate accède à DB → Peut se connecter ❌
```

**Avec BCrypt (SÉCURISÉ):**
```
Password: "password123" → Hash: $2a$10$slYQmyNdGzin7olVN3p36... → Stocké en DB ✓
Pirate accède à DB → Hash inutilisable ✓
Hash inclut salt → Difficile à casser ✓
```

```java
@Component
public class PasswordEncoder {
    
    public String encode(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }
    
    public boolean matches(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
```

#### 5.4 CORS (Cross-Origin Resource Sharing)

Permet au frontend (port 3000) de communiquer avec le backend (port 8080).

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
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

---

### 6. **Validation: Bean Validation**

Valide les données à la réception.

```java
@Entity
public class User {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
```

---

### 7. **Logging: SLF4J + Logback**

Enregistre les événements pour le débogage.

```java
@Slf4j
@Service
public class AnnonceService {
    
    public Annonce createAnnonce(Annonce annonce) {
        log.info("Creating annonce with title: {}", annonce.getTitre());
        try {
            return annonceRepository.save(annonce);
        } catch (Exception e) {
            log.error("Error creating annonce", e);
            throw new RuntimeException("Failed to create annonce");
        }
    }
}
```

---

## Frontend React

### 1. **Framework: React 18.2.0**

React est une libraire JavaScript pour construire des interfaces utilisateur interactives.

**Concepts clés:**
- **Components**: Blocs de réutilisables d'UI
- **JSX**: Syntaxe HTML en JavaScript
- **State**: Données mutables du composant
- **Props**: Propriétés passées aux composants
- **Virtual DOM**: Optimise les mises à jour

**Structure:**
```
App.js (Composant principal)
├── Navbar.js (Navigation)
├── Routes (React Router)
│   ├── Home.js
│   ├── Login.js
│   ├── Register.js
│   ├── AnnoncesPublique.js
│   ├── Dashboard.js (Propriétaire)
│   ├── AdminDashboard.js (Admin)
│   └── CreateAnnonce.js
└── AuthContext.js (État global)
```

---

### 2. **Routage: React Router v6**

Gère la navigation entre les pages sans rechargement.

```javascript
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/annonces" element={<AnnoncesPublique />} />
                <Route 
                    path="/dashboard" 
                    element={
                        <ProtectedRoute>
                            <Dashboard />
                        </ProtectedRoute>
                    } 
                />
            </Routes>
        </BrowserRouter>
    );
}
```

**Types de routes:**
- **Public**: `/`, `/login`, `/register` → Accessible sans authentification
- **Protected**: `/dashboard`, `/admin` → Nécessite authentification
- **Dynamic**: `/annonces/:id` → URL paramétrée

---

### 3. **Gestion d'État: Context API**

Partage l'état d'authentification entre tous les composants.

```javascript
// AuthContext.js
const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token'));
    
    const login = async (email, password) => {
        const response = await api.post('/auth/login', { email, password });
        const { token, user } = response.data;
        setToken(token);
        setUser(user);
        localStorage.setItem('token', token);
    };
    
    const logout = () => {
        setUser(null);
        setToken(null);
        localStorage.removeItem('token');
    };
    
    return (
        <AuthContext.Provider value={{ user, token, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
```

**Utilisation:**
```javascript
function Dashboard() {
    const { user, logout } = useAuth();
    
    return (
        <div>
            <h1>Bienvenue {user.fullName}</h1>
            <button onClick={logout}>Déconnexion</button>
        </div>
    );
}
```

---

### 4. **HTTP Client: Axios**

Effectue les requêtes HTTP vers le backend.

```javascript
// services/api.js
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Intercepteur pour ajouter le token JWT
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Intercepteur pour gérer les erreurs
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;
```

**Exemples d'utilisation:**
```javascript
// GET - Lister les annonces
async function getAnnonces() {
    const response = await api.get('/annonces');
    return response.data;
}

// POST - Créer une annonce
async function createAnnonce(annonceData) {
    const response = await api.post('/annonces', annonceData);
    return response.data;
}

// PUT - Modifier une annonce
async function updateAnnonce(id, annonceData) {
    const response = await api.put(`/annonces/${id}`, annonceData);
    return response.data;
}

// DELETE - Supprimer une annonce
async function deleteAnnonce(id) {
    await api.delete(`/annonces/${id}`);
}
```

---

### 5. **Styling: CSS3 + Flexbox + Grid**

CSS moderne pour une UI responsive.

**Techniques utilisées:**

#### Flexbox (disposition linéaire)
```css
.navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 1rem;
}
```

#### Grid (disposition en grille)
```css
.annonces-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 2rem;
    padding: 2rem;
}
```

#### Responsive Design (Media Queries)
```css
@media (max-width: 768px) {
    .annonces-grid {
        grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    }
    
    .navbar {
        flex-direction: column;
    }
}
```

---

### 6. **Build: Create React App + npm**

Crée et empaquète l'application React.

```bash
# package.json
{
  "name": "yaounde-loc-frontend",
  "version": "0.1.0",
  "private": true,
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.x.x",
    "axios": "^1.x.x"
  },
  "scripts": {
    "start": "react-scripts start",        # npm start → Port 3000
    "build": "react-scripts build",        # npm run build → Crée 'build/'
    "test": "react-scripts test"           # npm test → Lance tests
  }
}
```

---

## Base de Données

### 1. **MySQL vs PostgreSQL**

| Feature | MySQL | PostgreSQL |
|---------|-------|-----------|
| Type | Relationnel simple | Relationnel avancé |
| ACID | ✓ | ✓ |
| Scalabilité | Bonne | Excellente |
| JSON | Support | Support natif |
| Coût | Gratuit | Gratuit |
| Production | Très populaire | Montée en charge |

**Yaounde Loc**: Utilise MySQL pour simplicité, peut migrer à PostgreSQL facilement.

---

### 2. **Schéma Normalisé**

La base suit la **3ème forme normale (3NF)** pour éviter les redondances.

**Tables:**
- **users**: Utilisateurs (Locataires, Propriétaires, Admins)
- **roles**: Rôles (ROLE_ADMIN, ROLE_LOCATAIRE, ROLE_PROPRIETAIRE)
- **user_roles**: Liaison Many-to-Many (un user peut avoir plusieurs rôles)
- **annonces**: Annonces immobilières
- **images**: Images des annonces
- **favoris**: Favoris des locataires

---

## Sécurité et Authentification

### Flow Complet d'Authentification

```
1. REGISTRATION (Inscription)
   └─ Client: POST /auth/register {email, password, ...}
   └─ Backend: Valide, hash password, crée User
   └─ Backend: Retourne 201 Created + User

2. LOGIN (Connexion)
   └─ Client: POST /auth/login {email, password}
   └─ Backend: Valide email + password (BCrypt)
   └─ Backend: Génère JWT token (24h expiration)
   └─ Backend: Retourne {token, user}
   └─ Frontend: Stocke token en localStorage

3. AUTHENTICATED REQUEST (Requête authentifiée)
   └─ Client: GET /dashboard
   └─ Client: Headers: Authorization: Bearer <token>
   └─ Intercepteur: Ajoute token automatiquement
   └─ Backend: JwtAuthenticationFilter valide token
   └─ Backend: Extrait userId du token
   └─ Backend: Traite la requête
   └─ Backend: Retourne réponse

4. TOKEN EXPIRATION
   └─ Client: POST /annonces (token expiré)
   └─ Backend: JWT invalid → 401 Unauthorized
   └─ Intercepteur: Redirige vers /login
   └─ Frontend: Utilisateur doit se reconnecter
```

---

### Roles & Permissions

```
ROLE_ADMIN
└─ Accès: /admin/dashboard
└─ Actions: Valider/Rejeter annonces
└─ Permissions: Toutes

ROLE_PROPRIETAIRE
└─ Accès: /dashboard (mes annonces)
└─ Actions: Créer, modifier, supprimer propres annonces
└─ Upload: Images d'annonces
└─ Limites: Seulement ses propres annonces

ROLE_LOCATAIRE
└─ Accès: /annonces (publiques)
└─ Actions: Consulter annonces, ajouter favoris
└─ Limites: Lecture seule
```

---

## Déploiement

### 1. **Docker**

Containerise l'application pour portabilité.

**docker-compose.yml:**
```yaml
services:
  mysql:
    image: mysql:5.7
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: yaounde_loc
      MYSQL_USER: vlad
      MYSQL_PASSWORD: vlad2004
  
  backend:
    build: ./yaounde-loc
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/yaounde_loc
      SPRING_DATASOURCE_USERNAME: vlad
      SPRING_DATASOURCE_PASSWORD: vlad2004
  
  frontend:
    build: ./frontend
    ports:
      - "3000:3000"
    environment:
      REACT_APP_API_URL: http://backend:8080
```

**Commandes:**
```bash
docker-compose up -d     # Démarrer tous les services
docker-compose ps        # Voir l'état
docker-compose logs      # Voir les logs
docker-compose down      # Arrêter tout
```

---

### 2. **Render.com (Production)**

Plateforme PaaS pour déployer facilement.

**Services sur Render:**
1. **PostgreSQL** (Base de données gérée)
2. **Backend Web Service** (Spring Boot sur Java)
3. **Frontend Static Site** (React sur CDN global)

**URLs finales:**
- Frontend: `https://yaounde-loc-frontend.onrender.com`
- Backend: `https://yaounde-loc-backend.onrender.com`
- API: `https://yaounde-loc-backend.onrender.com/swagger-ui.html`

---

## Outils et Build

### 1. **Maven**

Gestionnaire de dépendances et build pour Java.

```bash
mvn clean              # Nettoie les builds précédents
mvn compile            # Compile le code
mvn test               # Lance les tests unitaires
mvn package            # Crée le JAR
mvn spring-boot:run    # Démarre l'application
mvn clean install      # Compile + teste + package
```

**pom.xml (Configuration):**
```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.projet</groupId>
    <artifactId>yaounde_loc</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
    </dependencies>
</project>
```

---

### 2. **npm / Node.js**

Gestionnaire de paquets JavaScript pour React.

```bash
npm install                    # Installe dépendances
npm start                      # Démarrage dev (port 3000)
npm run build                  # Build production (dossier 'build/')
npm test                       # Tests
npm install package-name       # Installe un paquet
npm install --legacy-peer-deps # Installe avec anciennes versions
```

**package.json:**
```json
{
  "name": "yaounde-loc-frontend",
  "version": "0.1.0",
  "dependencies": {
    "react": "^18.2.0",
    "react-router-dom": "^6.x.x",
    "axios": "^1.x.x"
  },
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build"
  }
}
```

---

### 3. **Git & GitHub**

Versionning et collaboration.

```bash
git clone URL              # Cloner le repo
git add .                  # Préparer les changements
git commit -m "message"    # Commiter
git push origin main       # Pousser vers GitHub
git pull origin main       # Récupérer les changements
```

---

## 📊 Résumé des Technologies

| Layer | Technology | Version | Purpose |
|-------|-----------|---------|---------|
| **Language** | Java | 21 LTS | Backend logic |
| **Framework** | Spring Boot | 3.3.1 | Web framework |
| **Security** | Spring Security + JWT | 3.3.1 | Auth & authorization |
| **DB ORM** | Hibernate JPA | 6.5.2 | Database abstraction |
| **Database** | MySQL | 5.7+ | Data storage |
| **Frontend** | React | 18.2.0 | UI framework |
| **Routing** | React Router | 6.x | Client-side routing |
| **HTTP** | Axios | 1.x | HTTP requests |
| **Styling** | CSS3 | ES2021 | UI design |
| **Build** | Maven | 3.8.7 | Java build tool |
| **Package Mgr** | npm | 9.2.0 | Node dependencies |
| **Server** | Tomcat | 10.1.25 | Servlet container |
| **Containerization** | Docker | 28.2.2 | Deployment |
| **Deployment** | Render | - | Cloud hosting |
| **Version Control** | Git/GitHub | - | Code management |

---

## 🎯 Flux de Données Complet

```
USER ACTION
    ↓
React Component (Frontend)
    ↓ (JSX rendered to HTML/CSS)
Browser Display
    ↓ (User clicks button)
Event Handler
    ↓ (axios.post())
HTTP Request (with JWT token)
    ↓ (HTTPS)
Render.com Server
    ↓
Spring Boot Controller
    ↓ (Validate token)
JwtAuthenticationFilter
    ↓
Business Logic (Service)
    ↓
JPA Repository
    ↓ (Hibernate generates SQL)
MySQL Database
    ↓ (SQL Query)
Data Retrieved/Updated
    ↓
Entity converted to JSON
    ↓
HTTP Response (200 OK + JSON)
    ↓ (HTTPS)
Browser
    ↓ (axios response interceptor)
React State Update
    ↓ (re-render component)
Display Updated UI
```

---

## 🚀 Points Clés

✅ **Sécurité:**
- Mots de passe hashés (BCrypt)
- Tokens JWT avec expiration
- CORS configuré
- Requêtes authentifiées

✅ **Scalabilité:**
- Architecture microservices ready
- API REST stateless
- Base de données normalisée
- Déploiement containerisé

✅ **Maintenabilité:**
- Code modulaire et organisé
- Séparation des concerns
- Logging intégré
- Versions LTS des frameworks

✅ **Performance:**
- Caching JWT
- Lazy loading React
- CDN pour static assets (Render)
- Optimisation images

---

## 📚 Ressources Additionnelles

**Documentation officielle:**
- Spring Boot: https://spring.io/projects/spring-boot
- React: https://react.dev
- MySQL: https://dev.mysql.com
- JWT: https://jwt.io
- Render: https://render.com/docs

**Tutoriels:**
- Spring Security: https://spring.io/guides/gs/securing-web/
- React Hooks: https://react.dev/reference/react/hooks
- REST API Design: https://restfulapi.net

---

**Créé**: 11 Décembre 2025
**Projet**: Yaounde Loc - Plateforme de Location Immobilière
**Version**: 1.0.0
