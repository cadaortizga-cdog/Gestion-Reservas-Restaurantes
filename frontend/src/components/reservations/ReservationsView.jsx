import React, { useState, useEffect } from 'react';
import { API_URL } from '../../App';
import { WAITLIST_PERCENTAGE } from '../../utils/businessRules';

export const ReservationsView = ({ reservations, tables, clients, schedule, loadData, styles, getBusinessHoursForDate }) => {
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    clientId: '',
    tableId: '',
    dateTime: '',
    endTime: '',
    numPeople: '',
    specialRequests: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [availableTables, setAvailableTables] = useState([]);
  const [waitlist, setWaitlist] = useState([]);

  useEffect(() => {
    if (showForm) {
      const loadWaitlist = async () => {
        try {
          const res = await fetch(`${API_URL}/vip-waitlist`);
          if (res.ok) setWaitlist(await res.json());
        } catch (err) { console.error('Error cargando waitlist:', err); }
      };
      loadWaitlist();
    }
  }, [showForm]);

  const loadAvailableTables = async () => {
    const { clientId, dateTime, endTime, numPeople } = formData;
    if (!clientId || !dateTime || !endTime || !numPeople) {
      setAvailableTables([]);
      return;
    }
    try {
      const client = clients.find(c => c.id === parseInt(clientId));
      const vipOnly = client?.vip || false;
      const params = new URLSearchParams({
        start: new Date(dateTime).toISOString(),
        end: new Date(endTime).toISOString(),
        numPeople: numPeople,
        vipOnly: vipOnly.toString()
      });
      const res = await fetch(`${API_URL}/reservations/available-tables?${params}`);
      if (res.ok) {
        const tableIds = await res.json();
        const tablesList = tables.filter(t =>
          tableIds.includes(t.id) && t.tableStatus === 'disponible'
        );
        setAvailableTables(tablesList);
      } else {
        setError(`Error al consultar mesas: ${await res.text()}`);
      }
    } catch (err) {
      console.error('Error:', err);
      setError('No se pudieron cargar las mesas disponibles.');
    }
  };

  useEffect(() => {
    if (showForm) {
      loadAvailableTables();
    }
  }, [formData.clientId, formData.dateTime, formData.endTime, formData.numPeople, tables]);

  const resetForm = () => {
    setFormData({
      clientId: '',
      tableId: '',
      dateTime: '',
      endTime: '',
      numPeople: '',
      specialRequests: ''
    });
    setShowForm(false);
    setError('');
    setSuccess('');
    setAvailableTables([]);
  };

  const handleCreate = async () => {
    setError('');
    setSuccess('');

    if (!formData.clientId || !formData.tableId || !formData.dateTime || !formData.endTime || !formData.numPeople) {
      setError('Todos los campos son obligatorios.');
      return;
    }

    const numPeople = parseInt(formData.numPeople, 10);
    const start = new Date(formData.dateTime);
    const end = new Date(formData.endTime);
    const now = new Date();

    if (start <= now) {
      setError('La fecha y hora de inicio debe ser futura.');
      return;
    }

    if (end <= start) {
      setError('La fecha y hora de fin debe ser posterior a la de inicio.');
      return;
    }

    const durationMinutes = (end - start) / (1000 * 60);
    if (durationMinutes < 60) {
      setError('La reserva debe durar al menos 1 hora.');
      return;
    }

    const selectedTable = tables.find(t => t.id === parseInt(formData.tableId, 10));
    if (selectedTable && numPeople > selectedTable.capacity) {
      setError(`El número de personas (${numPeople}) excede la capacidad de la mesa #${selectedTable.numTable} (${selectedTable.capacity} personas).`);
      return;
    }

    const selectedClient = clients.find(c => c.id === parseInt(formData.clientId, 10));
    if (selectedTable && selectedTable.vipExclusive && selectedClient && !selectedClient.vip) {
      setError(`La mesa #${selectedTable.numTable} es exclusiva para clientes VIP.`);
      return;
    }

    const hasConflict = reservations.some(r => 
    r.tableId === parseInt(formData.tableId, 10) &&
    r.status !== 'cancelada' &&
    r.status !== 'finalizada' &&
    (
      (start >= new Date(r.dateTime) && start < new Date(r.endTime)) ||
      (end > new Date(r.dateTime) && end <= new Date(r.endTime)) ||
      (new Date(r.dateTime) >= start && new Date(r.endTime) <= end)
    )
  );

  if (hasConflict) {
    setError('La mesa ya está reservada en ese horario.');
    return;
  } 

    try {
      const body = JSON.stringify({
        clientId: parseInt(formData.clientId, 10),
        tableId: parseInt(formData.tableId, 10),
        dateTime: formData.dateTime,
        endTime: formData.endTime,
        numPeople,
        status: 'pendiente',
        specialRequests: formData.specialRequests || null
      });

      const res = await fetch(`${API_URL}/reservations`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body
      });

      if (res.ok) {
        const reservation = await res.json();
        const client = clients.find(c => c.id === reservation.clientId);
        setSuccess(`✅ Reserva creada para ${client?.name || 'el cliente'} (${reservation.numPeople} personas).`);
        setTimeout(() => {
          resetForm();
          loadData();
        }, 2000);
      } else {
        const text = await res.text();
        // Manejar caso de "no hay mesas → VIP → lista de espera"
        if (text.includes('lista de espera')) {
          const client = clients.find(c => c.id === parseInt(formData.clientId, 10));
          if (client?.vip) {
            if (window.confirm(`⚠️ No hay mesas disponibles.\n¿Agregar a lista de espera VIP?`)) {
              try {
                const wlRes = await fetch(`${API_URL}/vip-waitlist`, {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify({
                    clientId: parseInt(formData.clientId, 10),
                    dateTime: formData.dateTime,
                    numPeople,
                    listStatus: 'en_espera'
                  })
                });
                if (wlRes.ok) {
                  setSuccess('✅ Añadido a lista de espera VIP');
                  setTimeout(() => {
                    resetForm();
                    loadData();
                  }, 1500);
                } else {
                  throw new Error(await wlRes.text());
                }
              } catch (e) {
                setError(`Error en lista de espera: ${e.message}`);
              }
            } else {
              setError('Operación cancelada por el usuario.');
            }
          } else {
            setError(text);
          }
        } else {
          setError(text);
        }
      }
    } catch (err) {
      setError(`Error: ${err.message}`);
    }
  };

  const handleConfirm = async (id) => {
    if (!window.confirm('¿Confirmar esta reserva?')) return;
    try {
      const res = await fetch(`${API_URL}/reservations/${id}/confirm`, { method: 'PUT' });
      if (res.ok) {
        setSuccess('✅ Reserva confirmada.');
        setTimeout(loadData, 1000);
      } else {
        const text = await res.text();
        setError(`❌ ${text}`);
      }
    } catch (err) {
      setError(`Error: ${err.message}`);
    }
  };

  const handleFinish = async (id) => {
    if (!window.confirm('¿Finalizar esta reserva?')) return;
    try {
      const res = await fetch(`${API_URL}/reservations/${id}/finish`, { method: 'PUT' });
      if (res.ok) {
        setSuccess('✅ Reserva finalizada. Puntos actualizados.');
        setTimeout(loadData, 1000);
      } else {
        const text = await res.text();
        setError(`❌ ${text}`);
      }
    } catch (err) {
      setError(`Error: ${err.message}`);
    }
  };

  const handleCancel = async (id) => {
    if (!window.confirm('¿Cancelar esta reserva?')) return;
    try {
      const res = await fetch(`${API_URL}/reservations/${id}`, { method: 'DELETE' });
      if (res.ok) {
        setSuccess('✅ Reserva cancelada.');
        setTimeout(loadData, 1000);
      } else {
        setError('❌ No se pudo cancelar la reserva.');
      }
    } catch (err) {
      setError(`Error: ${err.message}`);
    }
  };

  const removeFromWaitlist = async (id) => {
    if (!window.confirm('¿Eliminar de lista de espera?')) return;
    try {
      const res = await fetch(`${API_URL}/vip-waitlist/${id}`, { method: 'DELETE' });
      if (res.ok) {
        setSuccess('✅ Eliminado de lista de espera.');
        setTimeout(() => {
          loadData();
        }, 1000);
      }
    } catch (err) {
      setError(`Error: ${err.message}`);
    }
  };

  const maxWaitlist = Math.ceil(tables.length * WAITLIST_PERCENTAGE);

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <h2 style={{ fontSize: '32px' }}>📅 Gestión de Reservas</h2>
        <button style={styles.button} onClick={() => setShowForm(!showForm)}>
          {showForm ? '❌ Cancelar' : '+ Nueva Reserva'}
        </button>
      </div>
      {error && <div style={styles.error}>{error}</div>}
      {success && <div style={styles.success}>{success}</div>}

      {/* Lista de espera VIP */}
      {waitlist.length > 0 && (
        <div style={{ ...styles.card, background: '#fff3cd', border: '2px solid #ffc107', marginBottom: '20px' }}>
          <h3>📋 Lista de Espera VIP ({waitlist.length}/{maxWaitlist})</h3>
          <div style={{ overflowX: 'auto' }}>
            <table style={styles.table}>
              <thead>
                <tr>
                  <th style={styles.th}>Cliente</th>
                  <th style={styles.th}>Personas</th>
                  <th style={styles.th}>Horario</th>
                  <th style={styles.th}>Estado</th>
                  <th style={styles.th}>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {waitlist.map(w => {
                  const client = clients.find(c => c.id === w.clientId);
                  return (
                    <tr key={w.id}>
                      <td style={styles.td}>{client?.name || '—'}</td>
                      <td style={styles.td}>👥 {w.numPeople}</td>
                      <td style={styles.td}>
                        {new Date(w.dateTime).toLocaleDateString()}<br />
                        {new Date(w.dateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </td>
                      <td style={styles.td}>
                        <span style={{ ...styles.badge, background: '#ffd700', color: '#000' }}>
                          {w.listStatus}
                        </span>
                      </td>
                      <td style={styles.td}>
                        <button onClick={() => removeFromWaitlist(w.id)} style={{ ...styles.button, background: '#6c757d', padding: '5px 10px', fontSize: '12px' }}>
                          🗑️ Eliminar
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Formulario */}
      {showForm && (
        <div style={styles.card}>
          <h3>➕ Crear Nueva Reserva</h3>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px' }}>
            <div>
              <label>Cliente *</label>
              <select
                value={formData.clientId}
                onChange={e => setFormData({ ...formData, clientId: e.target.value })}
                style={styles.select}
                required
              >
                <option value="">— Seleccione —</option>
                {clients.map(c => (
                  <option key={c.id} value={c.id}>
                    {c.name} {c.vip && '⭐'}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label>Mesa *</label>
              <select
                value={formData.tableId}
                onChange={e => setFormData({ ...formData, tableId: e.target.value })}
                style={styles.select}
                disabled={availableTables.length === 0}
                required
              >
                <option value="">— Seleccione —</option>
                {availableTables.map(t => (
                  <option key={t.id} value={t.id}>
                    #{t.numTable} · {t.capacity} pers · {t.vipExclusive ? '⭐ VIP' : 'Estándar'}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <label>Fecha y Hora Inicio *</label>
          <input
            type="datetime-local"
            value={formData.dateTime}
            onChange={e => setFormData({ ...formData, dateTime: e.target.value })}
            min={new Date().toISOString().slice(0, 16)}
            style={styles.input}
          />
          <label>Fecha y Hora Fin *</label>
          <input
            type="datetime-local"
            value={formData.endTime}
            onChange={e => setFormData({ ...formData, endTime: e.target.value })}
            min={formData.dateTime || new Date().toISOString().slice(0, 16)}
            style={styles.input}
          />
          <label>Número de Personas *</label>
          <input
            type="number"
            min="1"
            value={formData.numPeople}
            onChange={e => setFormData({ ...formData, numPeople: e.target.value })}
            style={styles.input}
          />
          <label>Solicitudes Especiales</label>
          <textarea
            value={formData.specialRequests}
            onChange={e => setFormData({ ...formData, specialRequests: e.target.value })}
            placeholder="Ej: cumpleaños, alergias..."
            style={{ ...styles.input, minHeight: '80px', resize: 'vertical' }}
          />
          <div style={{ marginTop: '20px', display: 'flex', gap: '10px' }}>
            <button onClick={handleCreate} style={styles.button}>✅ Crear Reserva</button>
            <button onClick={resetForm} style={{ ...styles.button, background: '#6c757d' }}>❌ Cancelar</button>
          </div>
        </div>
      )}

      {/* Tabla de reservas */}
      <div style={styles.card}>
        <h3>📋 Reservas ({reservations.length})</h3>
        {reservations.length === 0 ? (
          <p style={{ textAlign: 'center', padding: '30px' }}>No hay reservas registradas.</p>
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
                  <th style={styles.th}>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {reservations
                  .sort((a, b) => new Date(b.dateTime) - new Date(a.dateTime))
                  .map(r => {
                    const client = clients.find(c => c.id === r.clientId);
                    const table = tables.find(t => t.id === r.tableId);
                    return (
                      <tr key={r.id}>
                        <td style={styles.td}>
                          {client?.name || '—'} {client?.vip && '⭐'}
                        </td>
                        <td style={styles.td}>
                          {new Date(r.dateTime).toLocaleDateString()}<br />
                          <strong>{new Date(r.dateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</strong>
                        </td>
                        <td style={styles.td}>#{table?.numTable || '?'}</td>
                        <td style={styles.td}>👥 {r.numPeople}</td>
                        <td style={styles.td}>
                          <span style={{
                            ...styles.badge,
                            background: r.status === 'pendiente' ? '#fff3cd' :
                              r.status === 'confirmada' ? '#d4edda' :
                                r.status === 'finalizada' ? '#d1ecf1' : '#f8d7da',
                            color: r.status === 'pendiente' ? '#856404' :
                              r.status === 'confirmada' ? '#155724' :
                                r.status === 'finalizada' ? '#0c5460' : '#721c24'
                          }}>
                            {r.status === 'pendiente' && '⏳'}
                            {r.status === 'confirmada' && '✅'}
                            {r.status === 'finalizada' && '🏁'}
                            {r.status === 'cancelada' && '❌'}
                            {' '}{r.status}
                          </span>
                        </td>
                        <td style={styles.td}>
                          <div style={{ display: 'flex', gap: '5px', flexWrap: 'wrap' }}>
                            {r.status === 'pendiente' && (
                              <button onClick={() => handleConfirm(r.id)} style={{ ...styles.button, padding: '5px 10px', fontSize: '12px', background: '#28a745' }}>
                                ✅ Confirmar
                              </button>
                            )}
                            {r.status === 'confirmada' && (
                              <button onClick={() => handleFinish(r.id)} style={{ ...styles.button, padding: '5px 10px', fontSize: '12px', background: '#17a2b8' }}>
                                🏁 Finalizar
                              </button>
                            )}
                            {(r.status === 'pendiente' || r.status === 'confirmada') && (
                              <button onClick={() => handleCancel(r.id)} style={{ ...styles.button, padding: '5px 10px', fontSize: '12px', background: '#dc3545' }}>
                                ❌ Cancelar
                              </button>
                            )}
                          </div>
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