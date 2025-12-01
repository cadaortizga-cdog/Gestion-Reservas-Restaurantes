// src/App.jsx
import React, { useState, useEffect } from 'react';
import { Header } from './components/layout/Header';
import { Sidebar } from './components/layout/Sidebar';
import { Dashboard } from './components/dashboard/Dashboard';
import { TablesView } from './components/tables/TablesView';
import { ReservationsView } from './components/reservations/ReservationsView';
import { ClientsView } from './components/clients/ClientsView';
import { ScheduleView } from './components/schedule/ScheduleView';
import { styles } from './components/ui/styles';

export const API_URL = 'http://localhost:8080/api';

function App() {
  const [activeView, setActiveView] = useState('dashboard');
  const [tables, setTables] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [clients, setClients] = useState([]);
  const [schedule, setSchedule] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    setError('');
    try {
      const [tablesRes, reservationsRes, clientsRes, scheduleRes] = await Promise.all([
        fetch(`${API_URL}/tables`).then(r => r.json()).catch(() => []),
        fetch(`${API_URL}/reservations`).then(r => r.json()).catch(() => []),
        fetch(`${API_URL}/clients`).then(r => r.json()).catch(() => []),
        fetch(`${API_URL}/schedule`).then(r => r.json()).catch(() => [])
      ]);
      setTables(tablesRes);
      setReservations(reservationsRes);
      setClients(clientsRes);
      setSchedule(scheduleRes);
    } catch (err) {
      console.error('Error loading data:', err);
      setError('Error al cargar los datos. Revise la conexión con el backend.');
    }
    setLoading(false);
  };

  const getBusinessHoursForDate = (date) => {
    const dayOfWeek = date.getDay();
    const backendDay = dayOfWeek === 0 ? 7 : dayOfWeek;
    const daySchedule = schedule.find(s => s.dayOfWeek === backendDay);
    if (!daySchedule) {
      return { open: '10:00', close: '22:00' };
    }
    const formatTime = (timeStr) => timeStr ? timeStr.slice(0, 5) : '';
    return {
      open: formatTime(daySchedule.openTime),
      close: formatTime(daySchedule.closeTime)
    };
  };

  const clearMessages = () => {
    setError('');
    setSuccess('');
  };

  return (
    <div style={styles.container}>
      <Header styles={styles} />
      <div style={styles.mainLayout}>
        <Sidebar
          activeView={activeView}
          setActiveView={setActiveView}
          styles={styles}
          clearMessages={clearMessages}
        />
        <main style={styles.content}>
          {loading && (
            <div style={styles.card}>
              <p style={{ textAlign: 'center' }}>Cargando datos del backend...</p>
            </div>
          )}
          {error && <div style={styles.error}>{error}</div>}
          {success && <div style={styles.success}>{success}</div>}
          {!loading && (
            <>
              {activeView === 'dashboard' && (
                <Dashboard
                  reservations={reservations}
                  tables={tables}
                  clients={clients}
                  styles={styles}
                  loadData={loadData}
                />
              )}
              {activeView === 'tables' && <TablesView tables={tables} loadData={loadData} styles={styles} />}
              {activeView === 'reservations' && (
                <ReservationsView
                  reservations={reservations}
                  tables={tables}
                  clients={clients}
                  schedule={schedule}
                  loadData={loadData}
                  styles={styles}
                  getBusinessHoursForDate={getBusinessHoursForDate}
                />
              )}
              {activeView === 'clients' && <ClientsView clients={clients} loadData={loadData} styles={styles} />}
              {activeView === 'schedule' && <ScheduleView schedule={schedule} loadData={loadData} styles={styles} />}
            </>
          )}
        </main>
      </div>
    </div>
  );
}

export default App;