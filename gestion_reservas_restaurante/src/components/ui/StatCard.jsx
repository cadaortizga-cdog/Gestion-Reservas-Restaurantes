import React from 'react';

export const StatCard = ({ title, value, icon }) => {
  return (
    <div style={{
      background: 'white',
      borderRadius: '15px',
      padding: '25px',
      boxShadow: '0 4px 15px rgba(0,0,0,0.1)',
      position: 'relative',
      overflow: 'hidden'
    }}>
      <div style={{ position: 'absolute', top: '-20px', right: '-20px', fontSize: '80px', opacity: 0.1 }}>
        {icon}
      </div>
      <div style={{ position: 'relative', zIndex: 1 }}>
        <div style={{ fontSize: '14px', color: '#666', marginBottom: '10px' }}>{title}</div>
        <div style={{ fontSize: '36px', fontWeight: 'bold', color: '#667eea' }}>{value}</div>
      </div>
    </div>
  );
};