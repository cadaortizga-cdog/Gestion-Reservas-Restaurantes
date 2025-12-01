import React, { useEffect } from 'react';
import { StatCard } from '../ui/StatCard';
import { CONFIRMATION_DEADLINE, getLoyaltyLevel } from '../../utils/businessRules';

export const Dashboard = ({ reservations, tables, clients, styles, loadData }) => {
  const activeReservations = reservations.filter(r =>
    r.status === 'pendiente' || r.status === 'confirmada'
  );
  const availableTables = tables.filter(t => t.tableStatus === 'disponible');
  const vipClients = clients.filter(c => c.vip);
  const pendingConfirmation = reservations.filter(r => r.status === 'pendiente');
  const urgentConfirmations = pendingConfirmation.filter(r => {
    const now = new Date();
    const reservationTime = new Date(r.dateTime);
    const timeDiff = (reservationTime - now) / (1000 * 60);
    return timeDiff <= CONFIRMATION_DEADLINE && timeDiff > 0;
  });

  useEffect(() => {
    const interval = setInterval(() => loadData(), 30000);
    return () => clearInterval(interval);
  }, [loadData]);

  return (
    <div>
      <h2 style={{ fontSize: '32px', marginBottom: '30px' }}>📊 Panel de Control</h2>
      {urgentConfirmations.length > 0 && (
        <div style={{ ...styles.warning, marginBottom: '20px' }}>
          <strong>⚠️ Atención:</strong> {urgentConfirmations.length} reserva(s) pendiente(s)
          necesitan confirmación urgente (menos de {CONFIRMATION_DEADLINE} minutos).
        </div>
      )}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
        gap: '20px',
        marginBottom: '30px'
      }}>
        <StatCard title="Reservas Activas" value={activeReservations.length} icon="📅" />
        <StatCard title="Mesas Disponibles" value={`${availableTables.length}/${tables.length}`} icon="🪑" />
        <StatCard title="Clientes VIP" value={vipClients.length} icon="⭐" />
        <StatCard title="Total Clientes" value={clients.length} icon="👥" />
      </div>
      <div style={styles.card}>
        <h3 style={{ fontSize: '24px', marginBottom: '20px' }}>📅 Reservas Activas</h3>
        {activeReservations.length === 0 ? (
          <p style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
            No hay reservas activas
          </p>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={styles.table}>
              <thead>
                <tr>
                  <th style={styles.th}>Cliente</th>
                  <th style={styles.th}>Fecha y Hora</th>
                  <th style={styles.th}>Mesa</th>
                  <th style={styles.th}>Personas</th>
                  <th style={styles.th}>Estado</th>
                </tr>
              </thead>
              <tbody>
                {activeReservations
                  .sort((a, b) => new Date(a.dateTime) - new Date(b.dateTime))
                  .slice(0, 10)
                  .map(r => {
                    const client = clients.find(c => c.id === r.clientId);
                    const table = tables.find(t => t.id === r.tableId);
                    const now = new Date();
                    const reservationTime = new Date(r.dateTime);
                    const isUrgent = r.status === 'pendiente' &&
                      (reservationTime - now) / (1000 * 60) <= CONFIRMATION_DEADLINE &&
                      (reservationTime - now) > 0;
                    return (
                      <tr key={r.id} style={{ background: isUrgent ? '#fff3cd' : 'transparent' }}>
                        <td style={styles.td}>
                          <strong>{client?.name || '—'}</strong>
                          {client?.vip && (
                            <span style={{
                              marginLeft: '8px',
                              background: getLoyaltyLevel(client.points) === 'Oro' ? '#ffd700' :
                                getLoyaltyLevel(client.points) === 'Plata' ? '#c0c0c0' : '#cd7f32',
                              color: '#000',
                              padding: '2px 6px',
                              borderRadius: '4px',
                              fontSize: '10px'
                            }}>
                              ⭐ {getLoyaltyLevel(client.points)}
                            </span>
                          )}
                        </td>
                        <td style={styles.td}>
                          {new Date(r.dateTime).toLocaleDateString('es-ES')}<br />
                          <strong>{new Date(r.dateTime).toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' })}</strong>
                        </td>
                        <td style={styles.td}>#{table?.numTable || '?'}</td>
                        <td style={styles.td}>👥 {r.numPeople}</td>
                        <td style={styles.td}>
                          <span style={{
                            ...styles.badge,
                            background: r.status === 'pendiente' ? '#fff3cd' :
                              r.status === 'confirmada' ? '#d4edda' : '#f8d7da',
                            color: r.status === 'pendiente' ? '#856404' :
                              r.status === 'confirmada' ? '#155724' : '#721c24'
                          }}>
                            {r.status === 'pendiente' && '⏳'}
                            {r.status === 'confirmada' && '✅'}
                            {' '}{r.status}
                          </span>
                        </td>
                      </tr>
                    );
                  })}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};