import React from 'react';

export const Sidebar = ({ activeView, setActiveView, styles, clearMessages }) => (
  <aside style={styles.sidebar}>
    {['dashboard', 'tables', 'reservations', 'clients', 'schedule'].map(view => (
      <button
        key={view}
        style={{
          ...styles.navButton,
          background: activeView === view ? '#e0f7fa' : 'white',
          color: activeView === view ? '#4CAF50' : '#333',
          border: activeView === view ? '1px solid #80cbc4' : '1px solid #e0e0e6'
        }}
        onClick={() => {
          setActiveView(view);
          clearMessages();
        }}
      >
        {view === 'dashboard' && '📊 Dashboard'}
        {view === 'tables' && '🪑 Mesas'}
        {view === 'reservations' && '📅 Reservas'}
        {view === 'clients' && '👥 Clientes'}
        {view === 'schedule' && '⏰ Horarios'}
      </button>
    ))}
  </aside>
);