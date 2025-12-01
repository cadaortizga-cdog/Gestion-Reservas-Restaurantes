import React, { useState } from 'react';
import { API_URL } from '../../App';
import { getLoyaltyLevel, getPointsForLevel } from '../../utils/businessRules';

export const ClientsView = ({ clients, loadData, styles }) => {
  const [showForm, setShowForm] = useState(false);
  const [editingClient, setEditingClient] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    phone: '',
    vip: false,
    points: 0
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const resetForm = () => {
    setFormData({ name: '', phone: '', vip: false, points: 0 });
    setEditingClient(null);
    setShowForm(false);
    setError('');
    setSuccess('');
  };

  const handleSave = async () => {
    setError('');
    setSuccess('');
    const { name, phone, vip, points } = formData;
    if (!name || !phone) {
      setError('Nombre y teléfono son obligatorios.');
      return;
    }
    if (!/^\d{7,15}$/.test(phone)) {
      setError('Teléfono debe tener 7-15 dígitos sin espacios.');
      return;
    }
    try {
      const url = editingClient ? `${API_URL}/clients/${editingClient.id}` : `${API_URL}/clients`;
      const method = editingClient ? 'PUT' : 'POST';
      const body = JSON.stringify({
        name: name.trim(),
        phone: phone.trim(),
        vip,
        points: vip ? parseInt(points) : 0,
        loyaltyLevel: getLoyaltyLevel(vip ? parseInt(points) : 0)
      });
      const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body
      });
      if (res.ok) {
        setSuccess(`Cliente ${editingClient ? 'actualizado' : 'creado'} exitosamente.`);
        setTimeout(() => {
          resetForm();
          loadData();
        }, 1500);
      } else {
        const text = await res.text();
        setError(`Error: ${text}`);
      }
    } catch (err) {
      setError(`Error: ${err.message}`);
    }
  };

  const handleEdit = (client) => {
    setEditingClient(client);
    setFormData({
      name: client.name,
      phone: client.phone,
      vip: client.vip,
      points: client.points || 0
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Eliminar cliente?')) return;
    try {
      const res = await fetch(`${API_URL}/clients/${id}`, { method: 'DELETE' });
      if (res.ok) {
        setSuccess('Cliente eliminado.');
        loadData();
      } else {
        setError('No se puede eliminar: tiene reservas asociadas.');
      }
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <h2 style={{ fontSize: '32px' }}>👥 Gestión de Clientes</h2>
        <button style={styles.button} onClick={() => setShowForm(!showForm)}>
          {showForm ? '❌ Cancelar' : '+ Nuevo Cliente'}
        </button>
      </div>
      {showForm && (
        <div style={styles.card}>
          <h3>{editingClient ? '✏️ Editar Cliente' : '➕ Nuevo Cliente'}</h3>
          {error && <div style={styles.error}>{error}</div>}
          {success && <div style={styles.success}>{success}</div>}
          <label>Nombre *</label>
          <input
            value={formData.name}
            onChange={e => setFormData({ ...formData, name: e.target.value })}
            style={styles.input}
          />
          <label>Teléfono *</label>
          <input
            value={formData.phone}
            onChange={e => setFormData({ ...formData, phone: e.target.value })}
            style={styles.input}
            placeholder="Ej: 3001234567"
          />
          <label style={{ display: 'flex', alignItems: 'center', marginTop: '10px' }}>
            <input
              type="checkbox"
              checked={formData.vip}
              onChange={e => {
                const checked = e.target.checked;
                setFormData({ ...formData, vip: checked, points: checked ? formData.points : 0 });
              }}
              style={{ marginRight: '8px' }}
            />
            Cliente VIP
          </label>
          {formData.vip && (
            <div style={{ marginTop: '15px', padding: '15px', background: '#f8f9fa', borderRadius: '8px' }}>
              <label>Puntos</label>
              <input
                type="number"
                value={formData.points}
                onChange={e => setFormData({ ...formData, points: e.target.value })}
                style={styles.input}
                min="0"
              />
              <div style={{ marginTop: '10px', fontSize: '14px' }}>
                Nivel: <strong style={{ color: '#667eea' }}>{getLoyaltyLevel(parseInt(formData.points) || 0)}</strong>
                {' '}(+{getPointsForLevel(getLoyaltyLevel(parseInt(formData.points) || 0))} puntos por reserva)
              </div>
            </div>
          )}
          <div style={{ marginTop: '20px', display: 'flex', gap: '10px' }}>
            <button onClick={handleSave} style={styles.button}>
              {editingClient ? '💾 Guardar' : '➕ Crear'}
            </button>
            <button onClick={resetForm} style={{ ...styles.button, background: '#6c757d' }}>
              ❌ Cancelar
            </button>
          </div>
        </div>
      )}
      <div style={styles.card}>
        <h3>📋 Clientes ({clients.length})</h3>
        {clients.length === 0 ? (
          <p style={{ textAlign: 'center', padding: '30px' }}>No hay clientes registrados.</p>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={styles.table}>
              <thead>
                <tr>
                  <th style={styles.th}>Nombre</th>
                  <th style={styles.th}>Teléfono</th>
                  <th style={styles.th}>Tipo</th>
                  <th style={styles.th}>Puntos</th>
                  <th style={styles.th}>Nivel</th>
                  <th style={styles.th}>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {clients.map(c => (
                  <tr key={c.id}>
                    <td style={styles.td}>{c.name}</td>
                    <td style={styles.td}>{c.phone}</td>
                    <td style={styles.td}>
                      {c.vip ? (
                        <span style={{ ...styles.badge, background: '#ffd700', color: '#000' }}>⭐ VIP</span>
                      ) : 'Estándar'}
                    </td>
                    <td style={styles.td}>{c.points || 0}</td>
                    <td style={styles.td}>
                      {c.vip ? getLoyaltyLevel(c.points) : '—'}
                    </td>
                    <td style={styles.td}>
                      <button onClick={() => handleEdit(c)} style={{ ...styles.button, background: '#17a2b8', padding: '6px 12px', fontSize: '13px', marginRight: '5px' }}>
                        ✏️ Editar
                      </button>
                      <button onClick={() => handleDelete(c.id)} style={{ ...styles.button, background: '#dc3545', padding: '6px 12px', fontSize: '13px' }}>
                        🗑️ Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};