// components/ProtectedRoute.jsx — Protección de rutas por rol
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute({ allowedRoles }) {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div className="loading-screen">
        <div className="spinner"></div>
      </div>
    );
  }

  // No autenticado → login
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // Si se pasan roles requeridos, verificar
  if (allowedRoles && allowedRoles.length > 0) {
    const hasAccess = user.roles.some((role) => allowedRoles.includes(role));
    if (!hasAccess) {
      return <Navigate to="/unauthorized" replace />;
    }
  }

  return <Outlet />;
}
