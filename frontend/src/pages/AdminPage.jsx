// pages/AdminPage.jsx — Requiere ROLE_ADMIN
import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function AdminPage() {
  const [peliculas, setPeliculas] = useState([]);
  const [salas, setSalas] = useState([]);
  const [activeTab, setActiveTab] = useState('peliculas');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [pelRes, salasRes] = await Promise.all([
          api.get('/api/v1/peliculas'),
          api.get('/api/v1/salas'),
        ]);
        setPeliculas(pelRes.data);
        setSalas(salasRes.data);
      } catch (err) {
        console.error('Error cargando datos admin:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleDeletePelicula = async (id) => {
    if (!confirm('¿Eliminar esta película?')) return;
    try {
      await api.delete(`/api/v1/peliculas/${id}`);
      setPeliculas((prev) => prev.filter((p) => p.id !== id));
    } catch (err) {
      alert('Error al eliminar: ' + (err.response?.data?.error || err.message));
    }
  };

  const handleDeleteSala = async (id) => {
    if (!confirm('¿Eliminar esta sala?')) return;
    try {
      await api.delete(`/api/v1/salas/${id}`);
      setSalas((prev) => prev.filter((s) => s.id !== id));
    } catch (err) {
      alert('Error al eliminar: ' + (err.response?.data?.error || err.message));
    }
  };

  if (loading) {
    return <div className="loading-screen"><div className="spinner"></div></div>;
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>⚙️ Panel de Administración</h1>
        <p>Gestión de películas y salas</p>
      </div>

      <div className="admin-tabs">
        <button
          className={`tab-btn ${activeTab === 'peliculas' ? 'active' : ''}`}
          onClick={() => setActiveTab('peliculas')}
        >
          🎬 Películas ({peliculas.length})
        </button>
        <button
          className={`tab-btn ${activeTab === 'salas' ? 'active' : ''}`}
          onClick={() => setActiveTab('salas')}
        >
          🏛️ Salas ({salas.length})
        </button>
      </div>

      {activeTab === 'peliculas' && (
        <div className="admin-table-container">
          <table className="admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Título</th>
                <th>Duración</th>
                <th>Edad Mínima</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {peliculas.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.titulo}</td>
                  <td>{p.duracion} min</td>
                  <td>+{p.edadMinima}</td>
                  <td>
                    <button className="btn-danger" onClick={() => handleDeletePelicula(p.id)}>
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {activeTab === 'salas' && (
        <div className="admin-table-container">
          <table className="admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Capacidad</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {salas.map((s) => (
                <tr key={s.id}>
                  <td>{s.id}</td>
                  <td>{s.nombre}</td>
                  <td>{s.capacidad}</td>
                  <td>
                    <button className="btn-danger" onClick={() => handleDeleteSala(s.id)}>
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
