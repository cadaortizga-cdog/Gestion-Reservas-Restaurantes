import React, { useState } from 'react';
import { API_URL } from '../../App';

export const TablesView = ({ tables, loadData, styles }) => {
  const [showForm, setShowForm] = useState(false);
  const [editingTable, setEditingTable] = useState(null);
  const [formData, setFormData] = useState({
    numTable: '',
    capacity: '',
    tableStatus: 'disponible',
    vipExclusive: false
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const resetForm = () => {
    setFormData({ numTable: '', capacity: '', tableStatus: 'disponible', vipExclusive: false });
    setEditingTable(null);
    setShowForm(false);
    setError('');
    setSuccess('');
  };

  const handleSave = async () => {
    setError('');
    setSuccess('');
    const numTable = parseInt(formData.numTable);
    const capacity = parseInt(formData.capacity);
    if (!numTable || !capacity) {
      setError('El número de mesa y la capacidad son obligatorios.');
      return;
    }
    if (capacity < 1) {
      setError('La capacidad debe ser al menos 1 persona.');
      return;
    }
    const duplicate = tables.find(t =>
      t.numTable === numTable && (!editingTable || t.id !== editingTable.id)
    );
    if (duplicate) {
      setError(`Ya existe una mesa con el número ${numTable}.`);
      return;
    }
    try {
      const url = editingTable
        ? `${API_URL}/tables/${editingTable.id}`
        : `${API_URL}/tables`;
      const method = editingTable ? 'PUT' : 'POST';
      const body = JSON.stringify({
        numTable,
        capacity,
        tableStatus: formData.tableStatus,
        vipExclusive: formData.vipExclusive
      });
      const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body
      });
      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || 'Error al guardar la mesa.');
      }
      setSuccess(`Mesa ${editingTable ? 'actualizada' : 'creada'} exitosamente.`);
      setTimeout(() => {
        resetForm();
        loadData();
      }, 1500);
    } catch (err) {
      setError(`Error: ${err.message}`);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Eliminar esta mesa? Esta acción es permanente.')) return;
    try {
      const res = await fetch(`${API_URL}/tables/${id}`, { method: 'DELETE' });
      if (!res.ok) throw new Error('No se puede eliminar: la mesa tiene reservas asociadas.');
      setSuccess('Mesa eliminada.');
      loadData();
    } catch (err) {
      setError(`Error: ${err.message}`);
    }
  };

  const handleEdit = (table) => {
    setEditingTable(table);
    setFormData({
      numTable: table.numTable,
      capacity: table.capacity,
      tableStatus: table.tableStatus,
      vipExclusive: table.vipExclusive
    });
    setShowForm(true);
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <h2 style={{ fontSize: '32px' }}>🪑 Gestión de Mesas</h2>
        <button style={styles.button} onClick={() => setShowForm(!showForm)}>
          {showForm ? '❌ Cancelar' : '+ Nueva Mesa'}
        </button>
      </div>
      {showForm && (
        <div style={styles.card}>
          <h3>{editingTable ? '✏️ Editar Mesa' : '➕ Crear Mesa'}</h3>
          {error && <div style={styles.error}>{error}</div>}
          {success && <div style={styles.success}>{success}</div>}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '5px' }}>Número *</label>
              <input
                type="number"
                value={formData.numTable}
                onChange={e => setFormData({ ...formData, numTable: e.target.value })}
                style={styles.input}
                min="1"
              />
            </div>
            <div>
              <label style={{ display: 'block', marginBottom: '5px' }}>Capacidad *</label>
              <input
                type="number"
                value={formData.capacity}
                onChange={e => setFormData({ ...formData, capacity: e.target.value })}
                style={styles.input}
                min="1"
              />
            </div>
          </div>
          <label style={{ display: 'block', marginBottom: '5px' }}>Estado</label>
          <select
            value={formData.tableStatus}
            onChange={e => setFormData({ ...formData, tableStatus: e.target.value })}
            style={styles.select}
          >
            <option value="disponible">✅ Disponible</option>
            <option value="fuera de servicio">🚫 Fuera de Servicio</option>
          </select>
          <label style={{ display: 'flex', alignItems: 'center', marginTop: '10px' }}>
            <input
              type="checkbox"
              checked={formData.vipExclusive}
              onChange={e => setFormData({ ...formData, vipExclusive: e.target.checked })}
              style={{ marginRight: '8px' }}
            />
            Mesa exclusiva para clientes VIP
          </label>
          <div style={{ marginTop: '20px', display: 'flex', gap: '10px' }}>
            <button onClick={handleSave} style={styles.button}>
              {editingTable ? '💾 Guardar' : '➕ Crear'}
            </button>
            <button onClick={resetForm} style={{ ...styles.button, background: '#6c757d' }}>
              ❌ Cancelar
            </button>
          </div>
        </div>
      )}
      <div style={styles.card}>
        <h3>📋 Lista de Mesas ({tables.length})</h3>
        {tables.length === 0 ? (
          <p style={{ textAlign: 'center', padding: '30px' }}>No hay mesas registradas.</p>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={styles.table}>
              <thead>
                <tr>
                  <th style={styles.th}>Mesa #</th>
                  <th style={styles.th}>Capacidad</th>
                  <th style={styles.th}>Estado</th>
                  <th style={styles.th}>Tipo</th>
                  <th style={styles.th}>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {tables.map(t => (
                  <tr key={t.id}>
                    <td style={styles.td}><strong>#{t.numTable}</strong></td>
                    <td style={styles.td}>👥 {t.capacity}</td>
                    <td style={styles.td}>
                      <span style={{
                        ...styles.badge,
                        background: t.tableStatus === 'disponible' ? '#d4edda' : '#f8d7da',
                        color: t.tableStatus === 'disponible' ? '#155724' : '#721c24'
                      }}>
                        {t.tableStatus === 'disponible' ? '✅' : '🚫'} {t.tableStatus}
                      </span>
                    </td>
                    <td style={styles.td}>
                      {t.vipExclusive ? (
                        <span style={{ ...styles.badge, background: '#ffd700', color: '#000' }}>⭐ VIP</span>
                      ) : 'Estándar'}
                    </td>
                    <td style={styles.td}>
                      <button onClick={() => handleEdit(t)} style={{ ...styles.button, background: '#17a2b8', padding: '6px 12px', fontSize: '13px', marginRight: '5px' }}>
                        ✏️ Editar
                      </button>
                      <button onClick={() => handleDelete(t.id)} style={{ ...styles.button, background: '#dc3545', padding: '6px 12px', fontSize: '13px' }}>
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