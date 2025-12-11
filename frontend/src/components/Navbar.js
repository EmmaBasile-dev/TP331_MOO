import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import '../styles/Navbar.css';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">
          🏠 Yaounde Loc
        </Link>
        
        <div className="navbar-menu">
          <Link to="/" className="nav-link">Accueil</Link>
          <Link to="/annonces" className="nav-link">Annonces</Link>
          
          {isAuthenticated ? (
            <div className="nav-authenticated">
              <span className="user-email">{user?.email}</span>
              
              {user?.role === 'ROLE_PROPRIETAIRE' && (
                <>
                  <Link to="/dashboard" className="nav-link">Mes Annonces</Link>
                  <Link to="/create-annonce" className="nav-link btn-primary">
                    + Nouvelle Annonce
                  </Link>
                </>
              )}
              
              {user?.role === 'ROLE_ADMIN' && (
                <Link to="/admin" className="nav-link btn-admin">Admin</Link>
              )}
              
              <button onClick={handleLogout} className="nav-link btn-logout">
                Déconnexion
              </button>
            </div>
          ) : (
            <div className="nav-unauthenticated">
              <Link to="/login" className="nav-link">Connexion</Link>
              <Link to="/register" className="nav-link btn-primary">S'inscrire</Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
