package com.projet.yaounde_loc.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    // On stocke les fichiers dans un dossier 'uploads' dans le répertoire courant de l'application
    private final Path fileStorageLocation = Paths.get("./uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            // Crée le répertoire s'il n'existe pas
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de créer le répertoire de stockage des fichiers.", ex);
        }
    }

    // 1. Sauvegarde le fichier et retourne son nom unique
    public String storeFile(MultipartFile file) {
        // Normaliser le nom du fichier (supprimer les chemins relatifs pour la sécurité)
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        // Crée un nom de fichier unique (UUID)
        String fileName = UUID.randomUUID().toString() + extension;
        
        try {
            // Vérifier si le nom du fichier contient des caractères invalides
            if(fileName.contains("..")) {
                throw new RuntimeException("Nom de fichier invalide.");
            }
            
            // Copie le fichier dans le chemin cible (remplace le fichier existant si besoin)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Impossible de stocker le fichier " + fileName + ". Veuillez réessayer!", ex);
        }
    }

    // 2. Charge le fichier pour le servir via HTTP
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Fichier non trouvé " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Fichier non trouvé " + fileName, ex);
        }
    }
}
