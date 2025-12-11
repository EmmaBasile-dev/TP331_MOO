import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AnnonceCard from '../components/AnnonceCard';
import { annonceService } from '../services/api';
import '../styles/Dashboard.css';

const Dashboard = () => {
  const [annonces, setAnnonces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchMyAnnonces();
  }, []);

  const fetchMyAnnonces = async () => {
    try {
      setLoading(true);
      const response = await annonceService.getMyAnnonces();
      setAnnonces(response.data);
    } catch (err) {
      setError('Erreur lors du chargement de vos annonces');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette annonce ?')) {
      try {
        await annonceService.deleteAnnonce(id);
        setAnnonces(annonces.filter(a => a.id !== id));
      } catch (err) {
        setError('Erreur lors de la suppression');
      }
    }
  };

  const handleEdit = (annonce) => {
    navigate('/create-annonce', { state: { annonce } });
  };

  if (loading) {
    return <div className="loading">Chargement...</div>;
  }

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1>Mes Annonces</h1>
        <button 
          className="btn btn-primary"
          onClick={() => navigate('/create-annonce')}
        >
          + Nouvelle Annonce
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      {annonces.length === 0 ? (
        <div className="no-annonces">
          <p>Vous n'avez pas encore créé d'annonces.</p>
          <button 
            className="btn btn-primary"
            onClick={() => navigate('/create-annonce')}
          >
            Créer votre première annonce
          </button>
        </div>
      ) : (
        <div className="annonces-grid">
          {annonces.map(annonce => (
            <AnnonceCard
              key={annonce.id}
              annonce={annonce}
              onEdit={handleEdit}
              onDelete={handleDelete}
              isEditable={true}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default Dashboard;
