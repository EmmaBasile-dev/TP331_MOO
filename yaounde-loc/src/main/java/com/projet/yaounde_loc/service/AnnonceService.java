package com.example.yaoundeloc.service;

import com.example.yaoundeloc.model.Annonce;
import com.example.yaoundeloc.model.User;
import com.example.yaoundeloc.repository.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepository;

    public Annonce creerAnnonce(Annonce annonce) {
        annonce.setStatut("EN_ATTENTE");
        return annonceRepository.save(annonce);
    }

    public List<Annonce> getAnnoncesValidees() {
        return annonceRepository.findByStatut("VALIDEE");
    }

    public List<Annonce> rechercher(String quartier, String typeBien) {
        return annonceRepository.findByQuartierContainingIgnoreCaseAndTypeBienContainingIgnoreCase(quartier, typeBien);
    }

    public List<Annonce> getAnnoncesProprietaire(User user) {
        return annonceRepository.findByProprietaire(user);
    }
}

