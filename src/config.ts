// Configuración dinámica basada en el entorno
const getApiUrl = () => {
  // Si hay una variable de entorno definida, usarla
  if (typeof __API_URL__ !== 'undefined') {
    return __API_URL__;
  }
  
  // Detectar entorno basado en la URL actual
  const hostname = window.location.hostname;
  const port = window.location.port;
  
  // Desarrollo
  if (hostname === 'localhost' || hostname === '127.0.0.1' || port === '5173' || port === '5174') {
    return 'http://34.46.73.44:8060'; // Backend Dev
  }
  
  // QA
  if (port === '8031' || hostname.includes('qa')) {
    return 'http://34.46.73.44:8030'; // Backend QA
  }
  
  // Producción
  if (port === '8021' || hostname.includes('prod') || hostname.includes('production')) {
    return 'http://34.46.73.44:8020'; // Backend Prod
  }
  
  // Por defecto, usar desarrollo
  return 'http://34.46.73.44:8060';
};

const API_URL = getApiUrl();

// Debug: Mostrar qué backend se está usando
console.log('🔧 Configuración de API:', {
  API_URL,
  hostname: window.location.hostname,
  port: window.location.port,
  fullUrl: window.location.href
});

const FRONTEND_URL = 'http://localhost:5174'; // Puerto del frontend en QA

const NGINX_URL = 'http://localhost:8083'; // Puerto de nginx en QA

export default {
  API_URL,
  FRONTEND_URL,
  NGINX_URL,
  // Endpoints específicos para QA
  endpoints: {
    // Autenticación
    login: `${API_URL}/api/auth/login`,
    register: `${API_URL}/api/auth/register`,
    refresh: `${API_URL}/api/auth/refresh`,

    // Usuarios
    users: `${API_URL}/api/users`,
    userProfile: `${API_URL}/api/users/profile`,

    // Citas
    appointments: `${API_URL}/api/appointments`,
    appointmentById: (id: string) => `${API_URL}/api/appointments/${id}`,

    // Pacientes
    patients: `${API_URL}/api/patients`,
    patientById: (id: string) => `${API_URL}/api/patients/${id}`,

    // Doctores
    doctors: `${API_URL}/api/doctors`,
    doctorById: (id: string) => `${API_URL}/api/doctors/${id}`,

    // Servicios
    services: `${API_URL}/api/services`,
    serviceById: (id: string) => `${API_URL}/api/services/${id}`,

    // Reportes
    reports: `${API_URL}/api/reports`,
    medicineReports: `${API_URL}/api/reports/medicine`,
    moderationReports: `${API_URL}/api/reports/moderation`,

    // FAQ
    faqs: `${API_URL}/api/faqs`,

    // Historia clínica
    medicalHistory: `${API_URL}/api/medical-history`,
    medicalHistoryById: (id: string) => `${API_URL}/api/medical-history/${id}`,

    // Recetas
    prescriptions: `${API_URL}/api/prescriptions`,
    prescriptionById: (id: string) => `${API_URL}/api/prescriptions/${id}`,

    // Medicamentos
    medicines: `${API_URL}/api/medicines`,
    medicineById: (id: string) => `${API_URL}/api/medicines/${id}`,

    // Aseguradoras
    insurance: `${API_URL}/api/insurance`,

    // Solicitudes de hospital
    hospitalRequests: `${API_URL}/api/hospital-requests`,

    // Health check
    health: `${API_URL}/q/health`
  }
};
