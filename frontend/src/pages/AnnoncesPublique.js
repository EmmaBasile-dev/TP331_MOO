import React, { useState, useEffect } from 'react';
import AnnonceCard from '../components/AnnonceCard';
import { annonceService } from '../services/api';
import '../styles/AnnoncesPublique.css';

const AnnoncesPublique = () => {
  const [annonces, setAnnonces] = useState([]);
  const [filteredAnnonces, setFilteredAnnonces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filters, setFilters] = useState({
    quartier: '',
    typeBien: '',
    prixMin: '',
    prixMax: '',
  });

  useEffect(() => {
    fetchAnnonces();
  }, []);

  const fetchAnnonces = async () => {
    try {
      setLoading(true);
      const response = await annonceService.getAnnoncesPubliques();
      setAnnonces(response.data);
      setFilteredAnnonces(response.data);
    } catch (err) {
      setError('Erreur lors du chargement des annonces');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const applyFilters = () => {
    let result = annonces;

    if (filters.quartier) {
      result = result.filter(a => 
        a.quartier?.toLowerCase().includes(filters.quartier.toLowerCase())
      );
    }

    if (filters.typeBien) {
      result = result.filter(a => 
        a.typeBien?.toLowerCase().includes(filters.typeBien.toLowerCase())
      );
    }

    if (filters.prixMin) {
      result = result.filter(a => a.prix >= parseInt(filters.prixMin));
    }

    if (filters.prixMax) {
      result = result.filter(a => a.prix <= parseInt(filters.prixMax));
    }

    setFilteredAnnonces(result);
  };

  useEffect(() => {
    applyFilters();
  }, [filters]);

  if (loading) {
    return <div className="loading">Chargement des annonces...</div>;
  }

  return (
    <div className="annonces-publique">
      <h1>Annonces Disponibles</h1>

      <div className="filters-section">
        <h3>Filtrer les annonces</h3>
        <div className="filters-grid">
          <div className="filter-group">
            <label>Quartier</label>
            <input
              type="text"
              name="quartier"
              value={filters.quartier}
              onChange={handleFilterChange}
              placeholder="Ex: Centre-Ville"
            />
          </div>

          <div className="filter-group">
            <label>Type de bien</label>
            <input
              type="text"
              name="typeBien"
              value={filters.typeBien}
              onChange={handleFilterChange}
              placeholder="Ex: Appartement"
            />
          </div>

          <div className="filter-group">
            <label>Prix minimum</label>
            <input
              type="number"
              name="prixMin"
              value={filters.prixMin}
              onChange={handleFilterChange}
              placeholder="0"
            />
          </div>

          <div className="filter-group">
            <label>Prix maximum</label>
            <input
              type="number"
              name="prixMax"
              value={filters.prixMax}
              onChange={handleFilterChange}
              placeholder="1000000"
            />
          </div>
        </div>
      </div>

      {error && <div className="error-message">{error}</div>}

      {filteredAnnonces.length === 0 ? (
        <div className="no-results">
          <p>Aucune annonce ne correspond à vos critères.</p>
        </div>
      ) : (
        <div className="annonces-grid">
          {filteredAnnonces.map(annonce => (
            <AnnonceCard
              key={annonce.id}
              annonce={annonce}
              isEditable={false}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default AnnoncesPublique;
