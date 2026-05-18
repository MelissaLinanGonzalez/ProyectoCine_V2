// context/AuthContext.jsx — Gestión global de autenticación
import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { jwtDecode } from 'jwt-decode';
import api from '../api/axios';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Extraer datos del token
  const parseToken = useCallback((token) => {
    try {
      const decoded = jwtDecode(token);
      return {
        email: decoded.sub,
        roles: decoded.roles || [],
      };
    } catch {
      return null;
    }
  }, []);

  // Al montar: comprobar si hay token guardado
  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      const userData = parseToken(token);
      if (userData) {
        setUser(userData);
      } else {
        localStorage.clear();
      }
    }
    setLoading(false);
  }, [parseToken]);

  const login = async (email, password) => {
    const { data } = await api.post('/api/v1/auth/login', { email, password });
    localStorage.setItem('accessToken', data.accessToken);
    localStorage.setItem('refreshToken', data.refreshToken);
    const userData = parseToken(data.accessToken);
    setUser(userData);
    return userData;
  };

  const register = async (email, password) => {
    const { data } = await api.post('/api/v1/auth/register', { email, password });
    localStorage.setItem('accessToken', data.accessToken);
    localStorage.setItem('refreshToken', data.refreshToken);
    const userData = parseToken(data.accessToken);
    setUser(userData);
    return userData;
  };

  const logout = () => {
    localStorage.clear();
    setUser(null);
  };

  const hasRole = (role) => {
    return user?.roles?.includes(role) || false;
  };

  const isAdmin = () => hasRole('ROLE_ADMIN');
  const isUser = () => hasRole('ROLE_USER');

  return (
    <AuthContext.Provider
      value={{ user, loading, login, register, logout, hasRole, isAdmin, isUser }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth debe usarse dentro de AuthProvider');
  return context;
};
