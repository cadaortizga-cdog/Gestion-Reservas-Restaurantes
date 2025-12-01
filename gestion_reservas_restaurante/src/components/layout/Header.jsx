import React from 'react';

export const Header = ({ styles }) => (
  <header style={styles.header}>
    <div style={styles.logo}>
      <div style={styles.logoIcon}>🍽️</div>
      <h1 style={styles.title}>Restaurante</h1>
    </div>
    <div style={{ color: '#666', fontSize: '14px' }}>
      Panel de Administración
    </div>
  </header>
);