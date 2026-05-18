// pages/UnauthorizedPage.jsx
import { Link } from 'react-router-dom';

export default function UnauthorizedPage() {
  return (
    <div className="page-container centered">
      <div className="error-card">
        <h1>🚫 403</h1>
        <h2>Acceso Denegado</h2>
        <p>No tienes permisos para acceder a esta sección.</p>
        <Link to="/" className="btn-primary">Volver a la Cartelera</Link>
      </div>
    </div>
  );
}
