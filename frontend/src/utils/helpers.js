// Fonction pour formater une date
export const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

// Fonction pour formater le temps écoulé
export const timeAgo = (dateString) => {
  const date = new Date(dateString);
  const now = new Date();
  const seconds = Math.floor((now - date) / 1000);

  let interval = seconds / 31536000;
  if (interval > 1) return Math.floor(interval) + ' ans';
  
  interval = seconds / 2592000;
  if (interval > 1) return Math.floor(interval) + ' mois';
  
  interval = seconds / 86400;
  if (interval > 1) return Math.floor(interval) + ' jours';
  
  interval = seconds / 3600;
  if (interval > 1) return Math.floor(interval) + ' heures';
  
  interval = seconds / 60;
  if (interval > 1) return Math.floor(interval) + ' minutes';
  
  return Math.floor(seconds) + ' secondes';
};

// Fonction pour formater le prix
export const formatPrice = (price) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XAF'
  }).format(price);
};

// Fonction pour valider l'email
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

// Fonction pour valider le mot de passe
export const isValidPassword = (password) => {
  return password && password.length >= 8;
};

// Fonction pour valider le téléphone
export const isValidPhone = (phone) => {
  const phoneRegex = /^[0-9]{10}$/;
  return phoneRegex.test(phone);
};

export default {
  formatDate,
  timeAgo,
  formatPrice,
  isValidEmail,
  isValidPassword,
  isValidPhone
};
