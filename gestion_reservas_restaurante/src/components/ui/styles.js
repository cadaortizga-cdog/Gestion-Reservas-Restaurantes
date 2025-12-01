export const styles = {
  container: {
    minHeight: '100vh',
    background: 'linear-gradient(135deg, #e0f7fa 0%, #b2ebf2 100%)',
    fontFamily: 'Arial, sans-serif'
  },
  header: {
    background: 'rgba(255,255,255,0.95)',
    padding: '20px 40px',
    boxShadow: '0 2px 10px rgba(0,0,0,0.1)',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  logo: {
    display: 'flex',
    alignItems: 'center',
    gap: '15px'
  },
  logoIcon: {
    width: '50px',
    height: '50px',
    background: 'linear-gradient(135deg, #80cbc4 0%, #4db6ac 100%)', 
    borderRadius: '10px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '24px',
    color: 'white'
  },
  title: {
    fontSize: '28px',
    fontWeight: 'bold',
    color: '#333',
    margin: 0
  },
  mainLayout: {
    display: 'flex',
    maxWidth: '1400px',
    margin: '0 auto'
  },
  sidebar: {
    width: '250px',
    background: '#ffffff',
    boxShadow: '0 4px 15px rgba(0,0,0,0.05)',
    padding: '30px 20px',
    minHeight: 'calc(100vh - 80px)',
    borderRight: '1px solid #e0e0e6'
  },
  navButton: {
    width: '100%',
    padding: '15px 20px',
    marginBottom: '10px',
    border: '1px solid #e0e0e6',
    borderRadius: '10px',
    cursor: 'pointer',
    fontSize: '16px',
    fontWeight: '500',
    transition: 'all 0.3s',
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    color: '#333',
    background: 'white'
  },
  content: {
    flex: 1,
    padding: '30px',
    color: '#333'
  },
  card: {
    background: 'rgba(255,255,255,0.95)',
    borderRadius: '15px',
    padding: '25px',
    marginBottom: '20px',
    boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
    color: '#333'
  },
  button: {
    padding: '12px 24px',
    background: 'linear-gradient(135deg, #80cbc4 0%, #4db6ac 100%)', 
    color: 'white',
    border: 'none',
    borderRadius: '10px',
    cursor: 'pointer',
    fontSize: '16px',
    fontWeight: '500',
    transition: 'all 0.3s'
  },
  input: {
    padding: '12px',
    borderRadius: '8px',
    border: '2px solid #e0e0e6',
    fontSize: '14px',
    width: '100%',
    marginBottom: '15px',
    boxSizing: 'border-box'
  },
  select: {
    padding: '12px',
    borderRadius: '8px',
    border: '2px solid #e0e0e6',
    fontSize: '14px',
    width: '100%',
    marginBottom: '15px',
    cursor: 'pointer',
    boxSizing: 'border-box'
  },
  table: {
    width: '100%',
    borderCollapse: 'collapse',
    marginTop: '20px'
  },
  th: {
    background: '#f8f9fa',
    padding: '15px',
    textAlign: 'left',
    fontWeight: '600',
    borderBottom: '2px solid #dee2e6'
  },
  td: {
    padding: '15px',
    borderBottom: '1px solid #dee2e6'
  },
  badge: {
    padding: '5px 12px',
    borderRadius: '20px',
    fontSize: '12px',
    fontWeight: '600',
    display: 'inline-block'
  },
  error: {
    background: '#fee',
    color: '#c33',
    padding: '10px',
    borderRadius: '8px',
    marginBottom: '15px',
    fontSize: '14px'
  },
  success: {
    background: '#d4edda',
    color: '#155724',
    padding: '10px',
    borderRadius: '8px',
    marginBottom: '15px',
    fontSize: '14px'
  },
  warning: {
    background: '#fff3cd',
    color: '#856404',
    padding: '10px',
    borderRadius: '8px',
    marginBottom: '15px',
    fontSize: '14px'
  }
};