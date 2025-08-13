// Configuración para el entorno de QA
const API_URL = 'http://localhost:8090'; // Puerto del backend en QA

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
