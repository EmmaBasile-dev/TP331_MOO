import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Intercepteur pour ajouter le token JWT
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const authService = {
  register: (userData) => apiClient.post('/auth/register', userData),
  registerProprietaire: (userData) => apiClient.post('/auth/register-proprietaire', userData),
  login: (email, password) => apiClient.post('/auth/login', { email, password }),
};

export const annonceService = {
  getAnnoncesPubliques: () => apiClient.get('/annonces'),
  creerAnnonce: (annonceData) => apiClient.post('/annonces', annonceData),
  getMyAnnonces: () => apiClient.get('/annonces/my-listings'),
  updateAnnonce: (id, annonceData) => apiClient.put(`/annonces/${id}`, annonceData),
  deleteAnnonce: (id) => apiClient.delete(`/annonces/${id}`),
};

export const adminService = {
  getAnnoncesEnAttente: () => apiClient.get('/admin/annonces/en-attente'),
  validerAnnonce: (id) => apiClient.post(`/admin/annonces/${id}/valider`),
  rejeterAnnonce: (id) => apiClient.post(`/admin/annonces/${id}/rejeter`),
};

export const imageService = {
  uploadImage: (annonceId, file) => {
    const formData = new FormData();
    formData.append('file', file);
    return apiClient.post(`/images/upload/${annonceId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
};

export default apiClient;
