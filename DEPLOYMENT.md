# Guide de Déploiement - Yaounde Loc

## 🚀 Déploiement avec Docker

### Prérequis
- Docker installé
- Docker Compose installé

### Étapes de déploiement

1. **Cloner le projet**
```bash
git clone <repo-url>
cd TP331_MOO
```

2. **Construire et démarrer les services**
```bash
docker-compose up -d
```

3. **Vérifier l'état des services**
```bash
docker-compose ps
```

4. **Accéder à l'application**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Swagger API Doc: http://localhost:8080/swagger-ui.html

### Arrêter l'application
```bash
docker-compose down
```

---

## 🚀 Déploiement en Production

### Backend - Déploiement sur serveur Linux

1. **Préparer le serveur**
```bash
# Installer Java
sudo apt-get update
sudo apt-get install openjdk-17-jdk

# Installer MySQL (optionnel si utilisé distant)
sudo apt-get install mysql-server

# Créer l'utilisateur application
sudo useradd -m -s /bin/bash yaounde-loc
```

2. **Copier et configurer l'application**
```bash
# Sur le serveur
sudo mkdir -p /opt/yaounde-loc
sudo chown yaounde-loc:yaounde-loc /opt/yaounde-loc

# Copier le JAR
sudo cp yaounde_loc-0.0.1-SNAPSHOT.jar /opt/yaounde-loc/

# Configuration sécurisée
sudo vi /opt/yaounde-loc/application.properties
```

3. **Créer un service SystemD**
```bash
sudo vi /etc/systemd/system/yaounde-loc.service
```

Contenu du fichier:
```ini
[Unit]
Description=Yaounde Loc Backend
After=network.target

[Service]
Type=simple
User=yaounde-loc
WorkingDirectory=/opt/yaounde-loc
ExecStart=/usr/bin/java -jar yaounde_loc-0.0.1-SNAPSHOT.jar \
    --spring.datasource.url=jdbc:mysql://localhost:3306/yaounde_loc \
    --spring.datasource.username=vlad \
    --spring.datasource.password=vlad2004
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

4. **Démarrer le service**
```bash
sudo systemctl daemon-reload
sudo systemctl enable yaounde-loc
sudo systemctl start yaounde-loc

# Vérifier le statut
sudo systemctl status yaounde-loc

# Voir les logs
sudo journalctl -u yaounde-loc -f
```

### Frontend - Déploiement avec Nginx

1. **Installer Nginx**
```bash
sudo apt-get install nginx
```

2. **Configurer Nginx**
```bash
sudo vi /etc/nginx/sites-available/yaounde-loc
```

Contenu:
```nginx
server {
    listen 80;
    server_name your_domain.com www.your_domain.com;
    
    root /var/www/yaounde-loc/build;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
        proxy_set_header Authorization $http_authorization;
        proxy_pass_header Authorization;
    }
    
    # SSL - Décommenter après configuration Let's Encrypt
    # listen 443 ssl http2;
    # ssl_certificate /etc/letsencrypt/live/your_domain.com/fullchain.pem;
    # ssl_certificate_key /etc/letsencrypt/live/your_domain.com/privkey.pem;
}
```

3. **Activer la configuration**
```bash
sudo ln -s /etc/nginx/sites-available/yaounde-loc \
           /etc/nginx/sites-enabled/

sudo nginx -t
sudo systemctl restart nginx
```

4. **Déployer le frontend**
```bash
# Build local
npm run build

# Copier vers le serveur
scp -r build/* user@server:/var/www/yaounde-loc/build/
```

### SSL/HTTPS avec Let's Encrypt

```bash
# Installer Certbot
sudo apt-get install certbot python3-certbot-nginx

# Générer certificat
sudo certbot certonly --nginx -d your_domain.com -d www.your_domain.com

# Configuration Nginx sera faite automatiquement
sudo systemctl reload nginx
```

---

## 📊 Monitoring et Maintenance

### Vérifier la santé de l'application

```bash
# Health check backend
curl http://localhost:8080/actuator/health

# Logs backend
sudo journalctl -u yaounde-loc -f

# Logs Nginx
sudo tail -f /var/log/nginx/error.log
```

### Backup de la base de données

```bash
# Dump de la BD
mysqldump -u vlad -p yaounde_loc > backup_$(date +%Y%m%d).sql

# Restauration
mysql -u vlad -p yaounde_loc < backup_*.sql
```

### Mise à jour de l'application

```bash
# Backend
cd yaounde-loc
mvn clean package -DskipTests
sudo systemctl stop yaounde-loc
sudo cp target/yaounde_loc-0.0.1-SNAPSHOT.jar /opt/yaounde-loc/
sudo systemctl start yaounde-loc

# Frontend
npm run build
scp -r build/* user@server:/var/www/yaounde-loc/build/
sudo systemctl reload nginx
```

---

## 🔒 Configuration de Sécurité

### Variables d'environnement importantes

```bash
# Backend - .env ou environment variables
JWT_SECRET=UNE_CLE_COMPLEXE_DE_MIN_256_BITS
JWT_EXPIRATION_MS=86400000
SPRING_DATASOURCE_PASSWORD=MOT_DE_PASSE_FORT
CORS_ALLOWED_ORIGINS=https://your_domain.com
```

### Recommandations de sécurité

1. **Changez tous les mots de passe par défaut**
2. **Utilisez HTTPS en production**
3. **Activez CSRF protection si nécessaire**
4. **Gardez les dépendances à jour**
```bash
# Backend
mvn dependency:tree | grep SNAPSHOT

# Frontend
npm audit
npm update
```

5. **Configurez un WAF (Web Application Firewall)**
6. **Mettez en place une monitoring**
7. **Limitez les taux de requêtes (rate limiting)**

---

## 📈 Scaling l'application

### Avec Docker Compose

Modifier docker-compose.yml pour utiliser Load Balancer:
```yaml
services:
  # ... autres services ...
  
  backend-1:
    # Copier le service backend
    
  backend-2:
    # Duplicate avec port différent
    
  nginx-lb:
    image: nginx:latest
    # Configuration load balancing
```

### Avec Kubernetes

Voir fichiers de configuration K8s dans le dossier `k8s/`

---

## 🆘 Troubleshooting

### Le backend ne démarre pas
```bash
# Vérifier les logs
sudo journalctl -u yaounde-loc -n 50

# Vérifier la connexion MySQL
mysql -u vlad -p yaounde_loc -e "SELECT 1;"

# Vérifier les ports
sudo netstat -tlnp | grep 8080
```

### La BD est corrompue
```bash
# Réparer MySQL
sudo mysqlcheck -u root -p --all-databases --repair
```

### Frontend ne se connecte pas au backend
```bash
# Vérifier les headers CORS
curl -H "Origin: http://localhost:3000" \
     -H "Access-Control-Request-Method: POST" \
     -X OPTIONS http://localhost:8080/annonces -v
```

---

**Pour toute question** : contactez l'équipe de support
