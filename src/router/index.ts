import { createRouter, createWebHistory } from "vue-router";
import type { RouteLocationNormalized, NavigationGuardNext } from "vue-router";

// ** Views públicas **
import HomeView             from "../views/HomeView.vue";
import SubhomeUView         from "../views/SubhomeUView.vue";
import SubhomeDView         from "../views/SubhomeDView.vue";
import SignUp               from "../views/SignUp.vue";
import Login                from "../views/Login.vue";
import FaqView              from "../views/FaqView.vue";
import MedicalServices      from "../views/MedicalServices.vue";
import HistoriaView         from "../views/HistoriaView.vue";
import CatalogoDoctores     from "../views/CatalogoDoctores.vue";
import DynamicPage          from "../views/DynamicPage.vue";
import Draft                from "../views/admin/Draft.vue";

// ** Paciente **
import Patient              from "../components/Patient.vue";
import ConsultarHistorial   from "../views/ConsultarHistorial.vue";
import PacienteHistorialPagos from "@/views/vistasSimuladas/PacienteHistorialPagos.vue";
import PacienteReportes     from "@/views/vistasSimuladas/PacienteReportes.vue";
import MyAccountPaciente    from "../views/MyAccountPaciente.vue";

// ** Doctor **
import DoctorImageView      from "../views/DoctorImageView.vue";
import RecetaView           from "../views/RecetaView.vue";
import RegistrarAtencion    from "../views/RegistrarAtencion.vue";
import MedicalAppointments  from "../views/doctores/MedicalAppointments.vue";
import MedicalSchedule      from "../views/doctores/MedicalSchedule.vue";
import MyAccountDoctor      from "../views/myAccountDoctor.vue";

// ** Empleado **
import FichaTecnicaView     from "../views/FichaTecnicaView.vue";
import HistorialPagos       from "@/views/vistasSimuladas/HistorialPagos.vue";
import Reportes             from "@/views/vistasSimuladas/Reportes.vue";
import StockMedicamentos    from "@/views/vistasSimuladas/StockMedicamentos.vue";
import MyAccountEmpleado    from "../views/MyAccountEmpleado.vue";

// ** Admin **
import AdminPortal          from "../views/AdminPortal.vue";
import AdminFaqView         from "../views/admin/AdminFaqView.vue";
import AdminUsuarios        from "../components/AdminUsuarios.vue";
import MedicalPrescription  from "../views/doctores/MedicalPrescription.vue";
import AdminHistoriaView    from "../views/admin/AdminHistoriaView.vue";
import PacienteAdmin        from "../views/PacienteAdmin.vue";
import EmpleadosAdmin       from "../views/EmpleadosAdmin.vue";
import AdminServiciosView   from "@/views/admin/AdminServiciosView.vue";
import DoctorAdmin          from "../views/DoctorAdmin.vue";
import AdminFichasTecnicas  from "../views/admin/AdminFichasTecnicas.vue";
// import ReporteView          from "@/views/admin/ReporteView.vue";
import ReporteMedicinasView from "@/views/admin/ReporteMedicinasView.vue";
import ReporteModeracionView from "@/views/admin/ReporteModeracionView.vue";
import AdminDynamicPages    from "@/views/admin/AdminDynamicPages.vue";
import AdminModerationView  from "@/views/admin/AdminModerationView.vue";
import UsuarioInterAdmin from "@/views/UsuarioInterAdmin.vue";
import MyAccountUsuarioInter from "@/views/MyAccountUsuarioInter.vue";
import SolicitudHospitalView from "@/views/SolicitudHospitalView.vue";
import CitasPorAseguradora from "@/views/CitasPorAseguradora.vue";
import MyAccountAdmin       from "../views/MyAccountAdmin.vue";

// ** Constantes de rol **
const ROLES = {
  Admin:    1,
  Doctor:   2,
  Empleado: 3,
  Paciente: 4,
  UsuarioInter: 5,
} as const;

// ** Guards **
// const requireAuth = (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
//   const userId = localStorage.getItem("userId");
//   return userId ? next() : next("/login");
// };

const requireRole = (allowedRoles: number[]) => (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const userRole = Number(localStorage.getItem("userRole"));
  return allowedRoles.includes(userRole) ? next() : next("/");
};

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // — Public —
    { path: "/",             name: "home",                component: HomeView },
    { path: "/subhome1",      name: "subhome1",            component: SubhomeUView },
    { path: "/subhome2",      name: "subhome2",            component: SubhomeDView },
    { path: "/signup",        name: "signup",              component: SignUp },
    { path: "/login",         name: "login",               component: Login },
    { path: "/faq",           name: "faq",                 component: FaqView },
    { path: "/servicios-medicos", name: "servicios-medicos", component: MedicalServices },
    { path: "/historia",      name: "historia",            component: HistoriaView },
    { path: "/doctores",      name: "catalogo-doctores",   component: CatalogoDoctores },
    { path: "/pages/:pageName", name: "dynamic-pages",     component: DynamicPage },
    { path: "/drafts/:id",    name: "draft-hospital",      component: Draft },

    // — Paciente (4) + Admin (1) —
    {
      path: "/recetas-pacientes",
      name: "recetas-pacientes",
      component: Patient,
      beforeEnter: requireRole([ROLES.Paciente, ROLES.Admin])
    },
    {
      path: "/consultar-historial",
      name: "consultar-historial",
      component: ConsultarHistorial,
      beforeEnter: requireRole([ROLES.Paciente, ROLES.Admin, ROLES.UsuarioInter])
    },
    {
      path: "/historial-pagos-pacientes",
      name: "historial-pagos-pacientes",
      component: PacienteHistorialPagos,
      beforeEnter: requireRole([ROLES.Paciente, ROLES.Admin])
    },
    {
      path: "/reportes-pacientes",
      name: "reportes-pacientes",
      component: PacienteReportes,
      beforeEnter: requireRole([ROLES.Paciente, ROLES.Admin])
    },
    {
      path: "/my-account-paciente",
      name: "my-account-paciente",
      component: MyAccountPaciente,
      beforeEnter: requireRole([ROLES.Paciente, ROLES.Admin])
    },

    // — Doctor (2) + Admin (1) —
    {
      path: "/doctorimagen",
      name: "doctor-imagen",
      component: DoctorImageView,
      beforeEnter: requireRole([ROLES.Doctor, ROLES.Admin])
    },
    {
      path: "/recetas",
      name: "recetas",
      component: RecetaView,
      beforeEnter: requireRole([ROLES.Doctor, ROLES.Admin])
    },
    {
      path: "/registrar-atencion",
      name: "registrar-atencion",
      component: RegistrarAtencion,
      beforeEnter: requireRole([ROLES.Doctor, ROLES.Admin, ROLES.UsuarioInter])
    },
    {
      path: "/admin/doctores-recetas",
      name: "doctores-recetas",
      component: MedicalPrescription,
      beforeEnter: requireRole([ROLES.Doctor, ROLES.Admin])
    },
    {
      path: "/admin/doctores-citas",
      name: "doctores-citas",
      component: MedicalAppointments,
      beforeEnter: requireRole([ROLES.Doctor, ROLES.Admin])
    },
    {
      path: "/admin/doctores-agenda",
      name: "doctores-agenda",
      component: MedicalSchedule,
      beforeEnter: requireRole([ROLES.Doctor, ROLES.Admin])
    },
    {
      path: "/my-account-doctor",
      name: "my-account-doctor",
      component: MyAccountDoctor,
      beforeEnter: requireRole([ROLES.Doctor, ROLES.Admin])
    },

    // — Empleado (3) + Admin (1) —
    {
      path: "/admin/crear-ficha-tecnica",
      name: "ficha-tecnica",
      component: FichaTecnicaView,
      beforeEnter: requireRole([ROLES.Empleado, ROLES.Admin])
    },
    {
      path: "/admin/historial-pago",
      name: "historial-pago",
      component: HistorialPagos,
      beforeEnter: requireRole([ROLES.Empleado, ROLES.Admin])
    },
    {
      path: "/admin/reportes",
      name: "reportes",
      component: Reportes,
      beforeEnter: requireRole([ROLES.Empleado, ROLES.Admin])
    },
    {
      path: "/admin/stock-medicamentos",
      name: "stock-medicamentos",
      component: StockMedicamentos,
      beforeEnter: requireRole([ROLES.Empleado, ROLES.Admin])
    },
    {
      path: "/my-account-empleado",
      name: "my-account-empleado",
      component: MyAccountEmpleado,
      beforeEnter: requireRole([ROLES.Empleado, ROLES.Admin])
    },

    // — Admin (1) Sólo —
    {
      path: "/admin-portal",
      name: "admin-portal",
      component: AdminPortal,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/faq",
      name: "admin-faq",
      component: AdminFaqView,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/usuarios",
      name: "admin-usuarios",
      component: AdminUsuarios,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/historia",
      name: "admin-historia",
      component: AdminHistoriaView,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/pacientes",
      name: "admin-pacientes",
      component: PacienteAdmin,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/empleados",
      name: "admin-empleados",
      component: EmpleadosAdmin,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/servicios",
      name: "admin-servicios",
      component: AdminServiciosView,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/doctor",
      name: "admin-doctor",
      component: DoctorAdmin,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/fichas-tecnicas",
      name: "admin-fichas-tecnicas",
      component: AdminFichasTecnicas,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/reportes-medicina",
      name: "admin-reportes-medicina",
      component: ReporteMedicinasView,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/reportes-moderacion",
      name: "admin-reportes-moderacion",
      component: ReporteModeracionView,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/moderacion",
      name: "admin-moderacion",
      component: AdminModerationView,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/admin/pages",
      name: "admin-dynamic-pages",
      component: AdminDynamicPages,
      beforeEnter: requireRole([ROLES.Admin])
    },
    {
      path: "/my-account-admin",
      name: "my-account-admin",
      component: MyAccountAdmin,
      beforeEnter: requireRole([ROLES.Admin])
    },
    { path: "/admin/usuario-interconexion",
      name: "admin-usuario-interconexion",
      component: UsuarioInterAdmin,
      beforeEnter: requireRole([ROLES.Admin]) },

    { path: "/my-account-interconexion",
      name: "my-account-interconexion",
      component: MyAccountUsuarioInter,
      beforeEnter: requireRole([ROLES.Admin]) },

    { path: "/solicitud-hospital",
      name: "solicitud-hospital",
      component: SolicitudHospitalView,
      beforeEnter: requireRole([ROLES.Admin, ROLES.UsuarioInter])},
    {
      path: "/citas-aseguradora",
      name: "citas-aseguradora",
      component: CitasPorAseguradora,
      beforeEnter: requireRole([ROLES.Admin])
    },
  ]
});

export default router;
