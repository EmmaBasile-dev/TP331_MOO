#!/bin/bash

# Script de vérification de complétude - Yaounde Loc
# Utilisation: ./verify.sh

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║     VÉRIFICATION DE COMPLÉTUDE - YAOUNDE LOC                  ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $1"
        return 0
    else
        echo -e "${RED}✗${NC} $1 (MANQUANT)"
        return 1
    fi
}

check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $1/"
        return 0
    else
        echo -e "${RED}✗${NC} $1/ (MANQUANT)"
        return 1
    fi
}

# Compter les tests réussis
PASS=0
FAIL=0

echo "📋 VÉRIFICATION DES FICHIERS BACKEND"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

check_file "yaounde-loc/pom.xml" && ((PASS++)) || ((FAIL++))
check_file "yaounde-loc/src/main/resources/application.properties" && ((PASS++)) || ((FAIL++))
check_file "yaounde-loc/.env.example" && ((PASS++)) || ((FAIL++))
check_dir "yaounde-loc/src/main/java/com/projet/yaounde_loc/controller" && ((PASS++)) || ((FAIL++))
check_dir "yaounde-loc/src/main/java/com/projet/yaounde_loc/service" && ((PASS++)) || ((FAIL++))
check_dir "yaounde-loc/src/main/java/com/projet/yaounde_loc/model" && ((PASS++)) || ((FAIL++))
check_dir "yaounde-loc/src/main/java/com/projet/yaounde_loc/security" && ((PASS++)) || ((FAIL++))

echo ""
echo "📋 VÉRIFICATION DES FICHIERS FRONTEND"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

check_file "frontend/package.json" && ((PASS++)) || ((FAIL++))
check_file "frontend/.env" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/App.js" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/index.js" && ((PASS++)) || ((FAIL++))
check_dir "frontend/src/components" && ((PASS++)) || ((FAIL++))
check_dir "frontend/src/pages" && ((PASS++)) || ((FAIL++))
check_dir "frontend/src/services" && ((PASS++)) || ((FAIL++))
check_dir "frontend/src/styles" && ((PASS++)) || ((FAIL++))

echo ""
echo "📋 VÉRIFICATION DES STYLES CSS"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

check_file "frontend/src/styles/global.css" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/styles/App.css" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/styles/Navbar.css" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/styles/Home.css" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/styles/Auth.css" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/styles/AnnonceCard.css" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/styles/AnnoncesPublique.css" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/styles/Dashboard.css" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/styles/CreateAnnonce.css" && ((PASS++)) || ((FAIL++))
check_file "frontend/src/styles/AdminDashboard.css" && ((PASS++)) || ((FAIL++))

echo ""
echo "📋 VÉRIFICATION DE LA DOCUMENTATION"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

check_file "README.md" && ((PASS++)) || ((FAIL++))
check_file "QUICKSTART.md" && ((PASS++)) || ((FAIL++))
check_file "DEPLOYMENT.md" && ((PASS++)) || ((FAIL++))
check_file "TESTING.md" && ((PASS++)) || ((FAIL++))
check_file "COMPLETENESS.md" && ((PASS++)) || ((FAIL++))
check_file "MODIFICATIONS.md" && ((PASS++)) || ((FAIL++))
check_file "INDEX.md" && ((PASS++)) || ((FAIL++))
check_file "HELP.md" && ((PASS++)) || ((FAIL++))
check_file "START.md" && ((PASS++)) || ((FAIL++))

echo ""
echo "📋 VÉRIFICATION DES SCRIPTS ET DOCKER"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

check_file "start.sh" && ((PASS++)) || ((FAIL++))
check_file "build.sh" && ((PASS++)) || ((FAIL++))
check_file "clean.sh" && ((PASS++)) || ((FAIL++))
check_file "docker-compose.yml" && ((PASS++)) || ((FAIL++))
check_file "yaounde-loc/Dockerfile" && ((PASS++)) || ((FAIL++))
check_file "frontend/Dockerfile" && ((PASS++)) || ((FAIL++))
check_file "frontend/nginx.conf" && ((PASS++)) || ((FAIL++))

echo ""
echo "📋 VÉRIFICATION DES OUTILS SYSTÈME"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | grep 'version' | head -n 1)
    echo -e "${GREEN}✓${NC} Java installé: $JAVA_VERSION"
    ((PASS++))
else
    echo -e "${RED}✗${NC} Java non installé"
    ((FAIL++))
fi

if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version 2>&1 | head -n 1)
    echo -e "${GREEN}✓${NC} Maven installé: $MVN_VERSION"
    ((PASS++))
else
    echo -e "${RED}✗${NC} Maven non installé"
    ((FAIL++))
fi

if command -v node &> /dev/null; then
    NODE_VERSION=$(node -v)
    echo -e "${GREEN}✓${NC} Node.js installé: $NODE_VERSION"
    ((PASS++))
else
    echo -e "${RED}✗${NC} Node.js non installé"
    ((FAIL++))
fi

if command -v npm &> /dev/null; then
    NPM_VERSION=$(npm -v)
    echo -e "${GREEN}✓${NC} npm installé: $NPM_VERSION"
    ((PASS++))
else
    echo -e "${RED}✗${NC} npm non installé"
    ((FAIL++))
fi

if command -v mysql &> /dev/null; then
    echo -e "${GREEN}✓${NC} MySQL installé"
    ((PASS++))
else
    echo -e "${YELLOW}⚠${NC} MySQL non installé (optionnel si utilisation Docker)"
fi

if command -v docker &> /dev/null; then
    DOCKER_VERSION=$(docker --version)
    echo -e "${GREEN}✓${NC} Docker installé: $DOCKER_VERSION"
    ((PASS++))
else
    echo -e "${YELLOW}⚠${NC} Docker non installé (optionnel)"
fi

echo ""
echo "═══════════════════════════════════════════════════════════════════"
echo ""

# Afficher le résumé
TOTAL=$((PASS + FAIL))
PERCENT=$((PASS * 100 / TOTAL))

echo "📊 RÉSUMÉ"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Fichiers vérifiés:  $TOTAL"
echo "Succès:             ${GREEN}$PASS${NC}"
echo "Échoués:            ${RED}$FAIL${NC}"
echo "Complétude:         $PERCENT%"
echo ""

if [ $FAIL -eq 0 ]; then
    echo -e "${GREEN}✅ PROJET ENTIÈREMENT COMPLET!${NC}"
    echo ""
    echo "Prochaines étapes:"
    echo "1. Lire QUICKSTART.md"
    echo "2. ./start.sh (pour démarrer)"
    echo "3. Visiter http://localhost:3000"
    exit 0
else
    echo -e "${RED}⚠️  CERTAINS FICHIERS MANQUENT${NC}"
    echo ""
    echo "Fichiers à créer/vérifier voir plus haut"
    exit 1
fi
