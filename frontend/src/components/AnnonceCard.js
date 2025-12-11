import React from 'react';
import '../styles/AnnonceCard.css';

const AnnonceCard = ({ annonce, onEdit, onDelete, isEditable = false }) => {
  return (
    <div className="annonce-card">
      <div className="annonce-image">
        {annonce.images && annonce.images.length > 0 ? (
          <img src={annonce.images[0].url} alt={annonce.titre} />
        ) : (
          <div className="no-image">Pas d'image</div>
        )}
        <span className={`statut statut-${annonce.statut?.toLowerCase()}`}>
          {annonce.statut}
        </span>
      </div>
      
      <div className="annonce-content">
        <h3>{annonce.titre}</h3>
        <p className="adresse">{annonce.quartier}, Yaoundé</p>
        <p className="type-bien">{annonce.typeBien}</p>
        <p className="description">{annonce.description}</p>
        
        <div className="annonce-details">
          <span className="prix">{annonce.prix?.toLocaleString('fr-FR')} FCFA</span>
          {annonce.nombreChambres && (
            <span className="chambres">🛏️ {annonce.nombreChambres} chambres</span>
          )}
        </div>
        
        {isEditable && (
          <div className="annonce-actions">
            <button className="btn-edit" onClick={() => onEdit(annonce)}>
              Éditer
            </button>
            <button className="btn-delete" onClick={() => onDelete(annonce.id)}>
              Supprimer
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default AnnonceCard;
