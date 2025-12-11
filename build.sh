#!/bin/bash

echo "🏗️  Build du projet Yaounde Loc..."
echo ""

# Build du backend
echo "📦 Build du backend..."
cd yaounde-loc
mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "❌ Erreur lors du build du backend"
    exit 1
fi

JAR_FILE="target/yaounde_loc-0.0.1-SNAPSHOT.jar"
if [ -f "$JAR_FILE" ]; then
    echo "✅ Backend build réussi : $JAR_FILE"
else
    echo "❌ Fichier JAR introuvable"
    exit 1
fi

# Build du frontend
echo ""
echo "⚛️  Build du frontend..."
cd ../frontend
npm install > /dev/null 2>&1
npm run build > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "❌ Erreur lors du build du frontend"
    exit 1
fi

if [ -d "build" ]; then
    echo "✅ Frontend build réussi : build/"
else
    echo "❌ Dossier build introuvable"
    exit 1
fi

echo ""
echo "✅ Build complet réussi!"
echo ""
echo "📦 Backend  : ../yaounde-loc/target/yaounde_loc-0.0.1-SNAPSHOT.jar"
echo "📦 Frontend : ./build/"
