// pages/MisEntradasPage.jsx — Requiere ROLE_USER
import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function MisEntradasPage() {
  const [entradas, setEntradas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchEntradas = async () => {
      try {
        const { data } = await api.get('/api/v1/entradas/mis-entradas');
        setEntradas(data);
      } catch (err) {
        console.error('Error cargando entradas:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchEntradas();
  }, []);

  if (loading) {
    return <div className="loading-screen"><div className="spinner"></div></div>;
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>🎟️ Mis Entradas</h1>
        <p>Entradas compradas</p>
      </div>

      {entradas.length === 0 ? (
        <div className="empty-state">
          <p>No tienes entradas aún. ¡Ve a la cartelera y compra una!</p>
        </div>
      ) : (
        <div className="tickets-list">
          {entradas.map((entrada) => (
            <div key={entrada.id} className="ticket-card">
              <div className="ticket-header">
                <span className="ticket-code">🎫 {entrada.codigo}</span>
                <span className={`ticket-status ${entrada.estado?.toLowerCase()}`}>
                  {entrada.estado}
                </span>
              </div>
              <div className="ticket-details">
                <span>Fila: {entrada.fila}</span>
                <span>Asiento: {entrada.asiento}</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
