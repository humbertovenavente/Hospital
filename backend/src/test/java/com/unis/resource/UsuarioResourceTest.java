package com.unis.resource;

import com.unis.model.Usuario;
import com.unis.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioResourceTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioResource usuarioResource;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("testuser");
        usuario.setCorreo("test@example.com");
        usuario.setContrasena("password123");
    }

    @Test
    void testRegistrarUsuarioExitoso() {
        // Arrange
        doNothing().when(usuarioService).registrarUsuario(usuario);

        // Act
        Response response = usuarioResource.registrarUsuario(usuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuario);
    }

    @Test
    void testRegistrarUsuarioConUsuarioNull() {
        // Arrange
        doNothing().when(usuarioService).registrarUsuario(null);

        // Act
        Response response = usuarioResource.registrarUsuario(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(null);
    }

    @Test
    void testRegistrarUsuarioConUsuarioVacio() {
        // Arrange
        Usuario usuarioVacio = new Usuario();
        doNothing().when(usuarioService).registrarUsuario(usuarioVacio);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioVacio);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioVacio);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConDatosCompletos() {
        // Arrange
        Usuario usuarioCompleto = new Usuario();
        usuarioCompleto.setId(999L);
        usuarioCompleto.setNombreUsuario("usuario_completo");
        usuarioCompleto.setCorreo("completo@example.com");
        usuarioCompleto.setContrasena("password123");
        usuarioCompleto.setEstado(1);
        
        doNothing().when(usuarioService).registrarUsuario(usuarioCompleto);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioCompleto);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioCompleto);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConNombreUsuarioLargo() {
        // Arrange
        Usuario usuarioLargo = new Usuario();
        usuarioLargo.setNombreUsuario("usuario_con_nombre_muy_largo_que_puede_exceder_el_limite_de_caracteres");
        usuarioLargo.setCorreo("largo@example.com");
        usuarioLargo.setContrasena("password123");
        
        doNothing().when(usuarioService).registrarUsuario(usuarioLargo);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioLargo);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioLargo);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConCorreoLargo() {
        // Arrange
        Usuario usuarioCorreoLargo = new Usuario();
        usuarioCorreoLargo.setNombreUsuario("test");
        usuarioCorreoLargo.setCorreo("usuario.con.correo.muy.largo.que.puede.exceder.el.limite.de.caracteres@dominio.muy.largo.com");
        usuarioCorreoLargo.setContrasena("password123");
        
        doNothing().when(usuarioService).registrarUsuario(usuarioCorreoLargo);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioCorreoLargo);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioCorreoLargo);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConContrasenaLarga() {
        // Arrange
        Usuario usuarioContrasenaLarga = new Usuario();
        usuarioContrasenaLarga.setNombreUsuario("test");
        usuarioContrasenaLarga.setCorreo("test@example.com");
        usuarioContrasenaLarga.setContrasena("contrasena_muy_larga_que_puede_exceder_el_limite_de_caracteres_123456789");
        
        doNothing().when(usuarioService).registrarUsuario(usuarioContrasenaLarga);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioContrasenaLarga);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioContrasenaLarga);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConCaracteresEspeciales() {
        // Arrange
        Usuario usuarioEspecial = new Usuario();
        usuarioEspecial.setNombreUsuario("usuario@#$%");
        usuarioEspecial.setCorreo("especial@example.com");
        usuarioEspecial.setContrasena("pass@#$%");
        
        doNothing().when(usuarioService).registrarUsuario(usuarioEspecial);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioEspecial);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioEspecial);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConNumeros() {
        // Arrange
        Usuario usuarioNumeros = new Usuario();
        usuarioNumeros.setNombreUsuario("user123");
        usuarioNumeros.setCorreo("123@example.com");
        usuarioNumeros.setContrasena("pass123");
        
        doNothing().when(usuarioService).registrarUsuario(usuarioNumeros);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioNumeros);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioNumeros);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConEspacios() {
        // Arrange
        Usuario usuarioEspacios = new Usuario();
        usuarioEspacios.setNombreUsuario("  usuario  ");
        usuarioEspacios.setCorreo("  espacios@example.com  ");
        usuarioEspacios.setContrasena("  pass  ");
        
        doNothing().when(usuarioService).registrarUsuario(usuarioEspacios);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioEspacios);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioEspacios);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConMayusculas() {
        // Arrange
        Usuario usuarioMayusculas = new Usuario();
        usuarioMayusculas.setNombreUsuario("USUARIO");
        usuarioMayusculas.setCorreo("MAYUSCULAS@EXAMPLE.COM");
        usuarioMayusculas.setContrasena("PASSWORD");
        
        doNothing().when(usuarioService).registrarUsuario(usuarioMayusculas);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioMayusculas);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioMayusculas);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConMinusculas() {
        // Arrange
        Usuario usuarioMinusculas = new Usuario();
        usuarioMinusculas.setNombreUsuario("usuario");
        usuarioMinusculas.setCorreo("minusculas@example.com");
        usuarioMinusculas.setContrasena("password");
        
        doNothing().when(usuarioService).registrarUsuario(usuarioMinusculas);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioMinusculas);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioMinusculas);
    }

    @Test
    void testRegistrarUsuarioConUsuarioConMezcla() {
        // Arrange
        Usuario usuarioMezcla = new Usuario();
        usuarioMezcla.setNombreUsuario("Usuario123");
        usuarioMezcla.setCorreo("Mezcla@Example.COM");
        usuarioMezcla.setContrasena("Pass123");
        
        doNothing().when(usuarioService).registrarUsuario(usuarioMezcla);

        // Act
        Response response = usuarioResource.registrarUsuario(usuarioMezcla);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));
        
        verify(usuarioService).registrarUsuario(usuarioMezcla);
    }
}
