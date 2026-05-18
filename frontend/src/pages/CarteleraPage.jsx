// pages/CarteleraPage.jsx — Pública
import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function CarteleraPage() {
  const [peliculas, setPeliculas] = useState([]);
  const [funciones, setFunciones] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [pelRes, funcRes] = await Promise.all([
          api.get('/api/v1/peliculas'),
          api.get('/api/v1/funciones'),
        ]);
        setPeliculas(pelRes.data);
        setFunciones(funcRes.data);
      } catch (err) {
        console.error('Error cargando cartelera:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) {
    return <div className="loading-screen"><div className="spinner"></div></div>;
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>🎬 Cartelera</h1>
        <p>Descubre las películas en cartel</p>
      </div>

      {peliculas.length === 0 ? (
        <div className="empty-state">
          <p>No hay películas disponibles</p>
        </div>
      ) : (
        <div className="movies-grid">
          {peliculas.map((pelicula) => (
            <div key={pelicula.id} className="movie-card">
              <div className="movie-poster">
                <span className="movie-emoji">🎞️</span>
              </div>
              <div className="movie-info">
                <h3>{pelicula.titulo}</h3>
                <div className="movie-meta">
                  <span>⏱ {pelicula.duracion} min</span>
                  <span>🔞 +{pelicula.edadMinima}</span>
                </div>
                {pelicula.directorNombre && (
                  <p className="movie-director">🎬 {pelicula.directorNombre}</p>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
