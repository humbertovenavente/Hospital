import { ref } from "vue";
import type { Router } from "vue-router";

export const userRole = ref<number | null>(null);
export const isLoggedIn = ref<boolean>(false);
export const userId = ref<string | null>(null);

//  Función para establecer el usuario después del login

// Función para establecer el usuario después del login
export const setUser = (id: string, roleId: number, router:Router) => {
  userRole.value = roleId;
  isLoggedIn.value = true;
  userId.value = id;

  localStorage.setItem("userId", id);
  localStorage.setItem("userRole", roleId.toString());

  // Redirigir según el rol
  switch (roleId) {
    case 1:
      router.push("/");
      break;
    case 2:
      router.push("/");
      break;
    case 3:
      router.push("/");
      break;
    case 4:
      router.push("/");
      break;
    default:
      router.push("/");
  }
};

//  Función para cerrar sesión y redirigir a Home
export const logout = (router: Router) => {
  userRole.value = null;
  isLoggedIn.value = false;
  userId.value = null;
  localStorage.clear();
  router.push("/");
};
