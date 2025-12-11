#!/bin/bash

echo "🚀 Démarrage de Yaounde Loc..."
echo ""

# Démarrer le backend
echo "📦 Démarrage du backend..."
cd yaounde-loc
mvn spring-boot:run &
BACKEND_PID=$!

# Attendre que le backend soit prêt
sleep 10

# Démarrer le frontend
echo "⚛️  Démarrage du frontend..."
cd ../frontend
npm install > /dev/null 2>&1
npm start &
FRONTEND_PID=$!

echo ""
echo "✅ Application en cours de démarrage..."
echo ""
echo "📍 Frontend  : http://localhost:3000"
echo "📍 Backend   : http://localhost:8080"
echo "📍 Swagger   : http://localhost:8080/swagger-ui.html"
echo ""
echo "⏸️  Appuyez sur Ctrl+C pour arrêter"

# Gérer l'arrêt propre
trap "kill $BACKEND_PID $FRONTEND_PID" EXIT

# Attendre la fin des processus
wait
