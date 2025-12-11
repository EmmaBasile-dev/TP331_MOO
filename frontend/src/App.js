import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import Dashboard from './pages/Dashboard';
import CreateAnnonce from './pages/CreateAnnonce';
import AdminDashboard from './pages/AdminDashboard';
import AnnoncesPublique from './pages/AnnoncesPublique';
import './styles/App.css';

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="App">
          <Navbar />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/annonces" element={<AnnoncesPublique />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            
            {/* Routes protégées pour propriétaires */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute roles={['ROLE_PROPRIETAIRE']}>
                  <Dashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/create-annonce"
              element={
                <ProtectedRoute roles={['ROLE_PROPRIETAIRE']}>
                  <CreateAnnonce />
                </ProtectedRoute>
              }
            />
            
            {/* Routes protégées pour admins */}
            <Route
              path="/admin"
              element={
                <ProtectedRoute roles={['ROLE_ADMIN']}>
                  <AdminDashboard />
                </ProtectedRoute>
              }
            />
            
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
