// src/utils/businessRules.js
export const RESERVATION_MIN_DURATION = 60; 
export const CONFIRMATION_DEADLINE = 30; 
export const WAITLIST_PERCENTAGE = 0.20;

export const getLoyaltyLevel = (points) => {
  if (points >= 100) return 'Oro';
  if (points >= 50) return 'Plata';
  return 'Bronce';
};

export const getPointsForLevel = (level) => {
  switch (level) {
    case 'Oro': return 15;
    case 'Plata': return 10;
    default: return 5;
  }
};