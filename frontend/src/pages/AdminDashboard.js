import React, { useState, useEffect } from 'react';
import { adminService } from '../services/api';
import '../styles/AdminDashboard.css';

const AdminDashboard = () => {
  const [annonces, setAnnonces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filter, setFilter] = useState('attente');

  useEffect(() => {
    fetchAnnonces();
  }, []);

  const fetchAnnonces = async () => {
    try {
      setLoading(true);
      const response = await adminService.getAnnoncesEnAttente();
      setAnnonces(response.data);
    } catch (err) {
      setError('Erreur lors du chargement des annonces');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleValider = async (id) => {
    try {
      await adminService.validerAnnonce(id);
      setAnnonces(annonces.filter(a => a.id !== id));
    } catch (err) {
      setError('Erreur lors de la validation');
    }
  };

  const handleRejeter = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir rejeter cette annonce ?')) {
      try {
        await adminService.rejeterAnnonce(id);
        setAnnonces(annonces.filter(a => a.id !== id));
      } catch (err) {
        setError('Erreur lors du rejet');
      }
    }
  };

  if (loading) {
    return <div className="loading">Chargement...</div>;
  }

  return (
    <div className="admin-dashboard">
      <h1>Tableau de Bord Administrateur</h1>

      {error && <div className="error-message">{error}</div>}

      <div className="annonces-en-attente">
        <h2>Annonces en Attente de Validation ({annonces.length})</h2>

        {annonces.length === 0 ? (
          <div className="no-annonces">
            <p>Aucune annonce en attente de validation.</p>
          </div>
        ) : (
          <table className="annonces-table">
            <thead>
              <tr>
                <th>Titre</th>
                <th>Propriétaire</th>
                <th>Quartier</th>
                <th>Prix</th>
                <th>Type</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {annonces.map(annonce => (
                <tr key={annonce.id}>
                  <td>{annonce.titre}</td>
                  <td>{annonce.proprietaire?.fullName}</td>
                  <td>{annonce.quartier}</td>
                  <td>{annonce.prix?.toLocaleString('fr-FR')} FCFA</td>
                  <td>{annonce.typeBien}</td>
                  <td>
                    <button
                      className="btn-accept"
                      onClick={() => handleValider(annonce.id)}
                    >
                      ✓ Valider
                    </button>
                    <button
                      className="btn-reject"
                      onClick={() => handleRejeter(annonce.id)}
                    >
                      ✗ Rejeter
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default AdminDashboard;
