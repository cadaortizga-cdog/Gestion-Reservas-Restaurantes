import React, { useState } from 'react';
import { API_URL } from '../../App';

export const ScheduleView = ({ schedule, loadData, styles }) => {
  const [showForm, setShowForm] = useState(false);
  const [editingSchedule, setEditingSchedule] = useState(null);
  const [formData, setFormData] = useState({
    dayOfWeek: '1',
    openTime: '10:00',
    closeTime: '22:00'
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const days = {
    '1': 'Lunes',
    '2': 'Martes',
    '3': 'Miércoles',
    '4': 'Jueves',
    '5': 'Viernes',
    '6': 'Sábado',
    '7': 'Domingo'
  };

  const resetForm = () => {
    setFormData({ dayOfWeek: '1', openTime: '10:00', closeTime: '22:00' });
    setEditingSchedule(null);
    setShowForm(false);
    setError('');
    setSuccess('');
  };

  const handleSave = async () => {
    setError('');
    setSuccess('');
    const { dayOfWeek, openTime, closeTime } = formData;
    if (!openTime || !closeTime) {
      setError('Horarios son obligatorios.');
      return;
    }
    if (openTime >= closeTime) {
      setError('La hora de cierre debe ser posterior a la apertura.');
      return;
    }
      const existingForDay = schedule.find(s => s.dayOfWeek.toString() === dayOfWeek);
      if (existingForDay && !editingSchedule) {
        setError(`Ya existe un horario configurado para ${days[dayOfWeek]}.`);
        return;
      }
    try {
      const url = editingSchedule
        ? `${API_URL}/schedule/${editingSchedule.id}`
        : `${API_URL}/schedule`;
      const method = editingSchedule ? 'PUT' : 'POST';
      const body = JSON.stringify({
        dayOfWeek: parseInt(dayOfWeek),
        openTime: `${openTime}:00`,
        closeTime: `${closeTime}:00`
      });
      const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body
      });
      if (res.ok) {
        setSuccess(`Horario ${editingSchedule ? 'actualizado' : 'creado'} para ${days[dayOfWeek]}.`);
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

  const handleEdit = (s) => {
    setEditingSchedule(s);
    setFormData({
      dayOfWeek: s.dayOfWeek.toString(),
      openTime: s.openTime.slice(0, 5),
      closeTime: s.closeTime.slice(0, 5)
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Eliminar horario del día?')) return;
    try {
      const res = await fetch(`${API_URL}/schedule/${id}`, { method: 'DELETE' });
      if (res.ok) {
        setSuccess('Horario eliminado.');
        loadData();
      } else {
        setError('Error al eliminar.');
      }
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <h2 style={{ fontSize: '32px' }}>⏰ Horarios del Restaurante</h2>
        <button style={styles.button} onClick={() => setShowForm(!showForm)}>
          {showForm ? '❌ Cancelar' : '+ Nuevo Horario'}
        </button>
      </div>
      {showForm && (
        <div style={styles.card}>
          <h3>{editingSchedule ? '✏️ Editar Horario' : '➕ Nuevo Horario'}</h3>
          {error && <div style={styles.error}>{error}</div>}
          {success && <div style={styles.success}>{success}</div>}
          <label>Día de la semana *</label>
          <select
            value={formData.dayOfWeek}
            onChange={e => setFormData({ ...formData, dayOfWeek: e.target.value })}
            style={styles.select}
          >
            {Object.entries(days).map(([key, label]) => (
              <option key={key} value={key}>
                {label}
              </option>
            ))}
          </select>
          <label>Hora de Apertura *</label>
          <input
            type="time"
            value={formData.openTime}
            onChange={e => setFormData({ ...formData, openTime: e.target.value })}
            style={styles.input}
          />
          <label>Hora de Cierre *</label>
          <input
            type="time"
            value={formData.closeTime}
            onChange={e => setFormData({ ...formData, closeTime: e.target.value })}
            style={styles.input}
          />
          <div style={{ marginTop: '20px', display: 'flex', gap: '10px' }}>
            <button onClick={handleSave} style={styles.button}>
              {editingSchedule ? '💾 Guardar' : '➕ Crear'}
            </button>
            <button onClick={resetForm} style={{ ...styles.button, background: '#6c757d' }}>
              ❌ Cancelar
            </button>
          </div>
        </div>
      )}
      <div style={styles.card}>
        <h3>📅 Horarios Configurados ({schedule.length})</h3>
        {schedule.length === 0 ? (
          <p style={{ textAlign: 'center', padding: '30px' }}>
            No hay horarios configurados. Los horarios por defecto son 10:00–22:00.
          </p>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={styles.table}>
              <thead>
                <tr>
                  <th style={styles.th}>Día</th>
                  <th style={styles.th}>Apertura</th>
                  <th style={styles.th}>Cierre</th>
                  <th style={styles.th}>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {schedule.map(s => (
                  <tr key={s.id}>
                    <td style={styles.td}><strong>{days[s.dayOfWeek]}</strong></td>
                    <td style={styles.td}>{s.openTime.slice(0, 5)}</td>
                    <td style={styles.td}>{s.closeTime.slice(0, 5)}</td>
                    <td style={styles.td}>
                      <button onClick={() => handleEdit(s)} style={{ ...styles.button, background: '#17a2b8', padding: '6px 12px', fontSize: '13px', marginRight: '5px' }}>
                        ✏️ Editar
                      </button>
                      <button onClick={() => handleDelete(s.id)} style={{ ...styles.button, background: '#dc3545', padding: '6px 12px', fontSize: '13px' }}>
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