// App.jsx — Rutas principales con ProtectedRoute
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import LoginPage from './pages/LoginPage';
import CarteleraPage from './pages/CarteleraPage';
import MisEntradasPage from './pages/MisEntradasPage';
import AdminPage from './pages/AdminPage';
import UnauthorizedPage from './pages/UnauthorizedPage';
import './index.css';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Navbar />
        <main>
          <Routes>
            {/* Públicas */}
            <Route path="/" element={<CarteleraPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/unauthorized" element={<UnauthorizedPage />} />

            {/* Requiere autenticación (USER o ADMIN) */}
            <Route element={<ProtectedRoute allowedRoles={['ROLE_USER', 'ROLE_ADMIN']} />}>
              <Route path="/mis-entradas" element={<MisEntradasPage />} />
            </Route>

            {/* Solo ADMIN */}
            <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']} />}>
              <Route path="/admin" element={<AdminPage />} />
            </Route>
          </Routes>
        </main>
      </AuthProvider>
    </Router>
  );
}

export default App;
