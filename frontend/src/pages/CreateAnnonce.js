import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { annonceService, imageService } from '../services/api';
import '../styles/CreateAnnonce.css';

const CreateAnnonce = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const editAnnonce = location.state?.annonce;

  const [formData, setFormData] = useState({
    titre: '',
    description: '',
    typeBien: '',
    quartier: '',
    prix: '',
    nombreChambres: '',
    nombreSalles: '',
  });

  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (editAnnonce) {
      setFormData({
        titre: editAnnonce.titre || '',
        description: editAnnonce.description || '',
        typeBien: editAnnonce.typeBien || '',
        quartier: editAnnonce.quartier || '',
        prix: editAnnonce.prix || '',
        nombreChambres: editAnnonce.nombreChambres || '',
        nombreSalles: editAnnonce.nombreSalles || '',
      });
    }
  }, [editAnnonce]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleImageChange = (e) => {
    setImages(Array.from(e.target.files));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      let response;
      if (editAnnonce) {
        response = await annonceService.updateAnnonce(editAnnonce.id, formData);
      } else {
        response = await annonceService.creerAnnonce(formData);
      }

      // Upload images if provided
      if (images.length > 0) {
        for (const image of images) {
          await imageService.uploadImage(response.data.id, image);
        }
      }

      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data || 'Erreur lors de la création de l\'annonce');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="create-annonce">
      <div className="form-container">
        <h1>{editAnnonce ? 'Modifier l\'annonce' : 'Créer une nouvelle annonce'}</h1>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="titre">Titre *</label>
            <input
              type="text"
              id="titre"
              name="titre"
              value={formData.titre}
              onChange={handleChange}
              required
              placeholder="Titre de l'annonce"
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description *</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              required
              placeholder="Décrivez votre propriété"
              rows="5"
            ></textarea>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="typeBien">Type de bien *</label>
              <select
                id="typeBien"
                name="typeBien"
                value={formData.typeBien}
                onChange={handleChange}
                required
              >
                <option value="">-- Sélectionner --</option>
                <option value="Appartement">Appartement</option>
                <option value="Maison">Maison</option>
                <option value="Studio">Studio</option>
                <option value="Bureau">Bureau</option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="quartier">Quartier *</label>
              <input
                type="text"
                id="quartier"
                name="quartier"
                value={formData.quartier}
                onChange={handleChange}
                required
                placeholder="Ex: Centre-Ville"
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="prix">Prix (FCFA) *</label>
              <input
                type="number"
                id="prix"
                name="prix"
                value={formData.prix}
                onChange={handleChange}
                required
                placeholder="0"
              />
            </div>

            <div className="form-group">
              <label htmlFor="nombreChambres">Nombre de chambres</label>
              <input
                type="number"
                id="nombreChambres"
                name="nombreChambres"
                value={formData.nombreChambres}
                onChange={handleChange}
                placeholder="0"
              />
            </div>

            <div className="form-group">
              <label htmlFor="nombreSalles">Nombre de salles de bain</label>
              <input
                type="number"
                id="nombreSalles"
                name="nombreSalles"
                value={formData.nombreSalles}
                onChange={handleChange}
                placeholder="0"
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="images">Images</label>
            <input
              type="file"
              id="images"
              name="images"
              onChange={handleImageChange}
              multiple
              accept="image/*"
            />
            {images.length > 0 && (
              <p className="image-count">{images.length} image(s) sélectionnée(s)</p>
            )}
          </div>

          <div className="form-actions">
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? 'En cours...' : (editAnnonce ? 'Mettre à jour' : 'Créer l\'annonce')}
            </button>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => navigate('/dashboard')}
              disabled={loading}
            >
              Annuler
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateAnnonce;
