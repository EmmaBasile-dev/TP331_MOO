import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Home.css';

const Home = () => {
  return (
    <div className="home">
      <section className="hero">
        <div className="hero-content">
          <h1>Bienvenue sur Yaounde Loc</h1>
          <p>Trouvez votre logement idéal à Yaoundé</p>
          <div className="hero-buttons">
            <Link to="/annonces" className="btn btn-primary">
              Voir les Annonces
            </Link>
            <Link to="/register" className="btn btn-secondary">
              S'inscrire
            </Link>
          </div>
        </div>
      </section>

      <section className="features">
        <h2>Pourquoi Yaounde Loc ?</h2>
        <div className="features-grid">
          <div className="feature">
            <div className="feature-icon">🔐</div>
            <h3>Sécurisé</h3>
            <p>Transactions sécurisées avec authentification</p>
          </div>
          <div className="feature">
            <div className="feature-icon">🏠</div>
            <h3>Large Choix</h3>
            <p>Découvrez des milliers de propriétés</p>
          </div>
          <div className="feature">
            <div className="feature-icon">⚡</div>
            <h3>Rapide</h3>
            <p>Trouvez votre logement en quelques clics</p>
          </div>
          <div className="feature">
            <div className="feature-icon">📱</div>
            <h3>Accessible</h3>
            <p>Accessible sur tous les appareils</p>
          </div>
        </div>
      </section>

      <section className="cta">
        <h2>Êtes-vous propriétaire ?</h2>
        <p>Publiez vos annonces et trouvez les meilleurs locataires</p>
        <Link to="/register?role=proprietaire" className="btn btn-primary">
          Devenir Propriétaire
        </Link>
      </section>
    </div>
  );
};

export default Home;
