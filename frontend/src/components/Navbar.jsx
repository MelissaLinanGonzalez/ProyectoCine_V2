// components/Navbar.jsx
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/">🎬 CineApp</Link>
      </div>
      <div className="navbar-links">
        <Link to="/">Cartelera</Link>
        {user && <Link to="/mis-entradas">Mis Entradas</Link>}
        {isAdmin() && <Link to="/admin">Panel Admin</Link>}
      </div>
      <div className="navbar-auth">
        {user ? (
          <div className="user-info">
            <span className="user-email">{user.email}</span>
            <span className="user-role">{user.roles.includes('ROLE_ADMIN') ? 'ADMIN' : 'USER'}</span>
            <button onClick={handleLogout} className="btn-logout">Cerrar Sesión</button>
          </div>
        ) : (
          <Link to="/login" className="btn-login">Iniciar Sesión</Link>
        )}
      </div>
    </nav>
  );
}
