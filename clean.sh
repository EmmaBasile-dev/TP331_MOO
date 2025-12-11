#!/bin/bash

echo "🧹 Nettoyage du projet..."
echo ""

# Nettoyer le backend
echo "Backend..."
cd yaounde-loc
mvn clean > /dev/null 2>&1
rm -rf uploads/
cd ..

# Nettoyer le frontend
echo "Frontend..."
cd frontend
rm -rf node_modules/ build/
cd ..

echo "✅ Nettoyage terminé!"
