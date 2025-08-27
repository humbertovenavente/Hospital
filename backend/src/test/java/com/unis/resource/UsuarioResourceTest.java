package com.unis.resource;

import com.unis.model.Usuario;
import com.unis.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for UsuarioResource.
 * Tests all REST endpoints and business logic.
 */
class UsuarioResourceTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioResource usuarioResource;

    private Usuario testUsuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUsuario = new Usuario();
        testUsuario.setId(1L);
        testUsuario.setNombreUsuario("testuser");
        testUsuario.setCorreo("test@example.com");
        testUsuario.setContrasena("password123");
    }

    @Test
    void testRegistrarUsuario_Success() {
        // Arrange
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(testUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithNullUsuario() {
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

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(null);
    }

    @Test
    void testRegistrarUsuario_WithEmptyUsuario() {
        // Arrange
        Usuario emptyUsuario = new Usuario();
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(emptyUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithPartialUsuario() {
        // Arrange
        Usuario partialUsuario = new Usuario();
        partialUsuario.setNombreUsuario("partial");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(partialUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithSpecialCharacters() {
        // Arrange
        Usuario specialUsuario = new Usuario();
        specialUsuario.setNombreUsuario("user@123!#$");
        specialUsuario.setCorreo("test+tag@domain.co.uk");
        specialUsuario.setContrasena("p@ssw0rd!");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(specialUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithLongValues() {
        // Arrange
        Usuario longUsuario = new Usuario();
        longUsuario.setNombreUsuario("very_long_username_that_exceeds_normal_length_limits");
        longUsuario.setCorreo("very.long.email.address@very.long.domain.name.com");
        longUsuario.setContrasena("very_long_password_with_many_characters");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(longUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithUnicodeCharacters() {
        // Arrange
        Usuario unicodeUsuario = new Usuario();
        unicodeUsuario.setNombreUsuario("usér_námé");
        unicodeUsuario.setCorreo("usér@domaín.com");
        unicodeUsuario.setContrasena("cóntr@señ@");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(unicodeUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithNumbers() {
        // Arrange
        Usuario numberUsuario = new Usuario();
        numberUsuario.setNombreUsuario("user123");
        numberUsuario.setCorreo("user123@domain456.com");
        numberUsuario.setContrasena("pass123word");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(numberUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithUnderscores() {
        // Arrange
        Usuario underscoreUsuario = new Usuario();
        underscoreUsuario.setNombreUsuario("user_name");
        underscoreUsuario.setCorreo("user_name@domain_name.com");
        underscoreUsuario.setContrasena("pass_word");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(underscoreUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithHyphens() {
        // Arrange
        Usuario hyphenUsuario = new Usuario();
        hyphenUsuario.setNombreUsuario("user-name");
        hyphenUsuario.setCorreo("user-name@domain-name.com");
        hyphenUsuario.setContrasena("pass-word");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(hyphenUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithDots() {
        // Arrange
        Usuario dotUsuario = new Usuario();
        dotUsuario.setNombreUsuario("user.name");
        dotUsuario.setCorreo("user.name@domain.name.com");
        dotUsuario.setContrasena("pass.word");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(dotUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithSpaces() {
        // Arrange
        Usuario spaceUsuario = new Usuario();
        spaceUsuario.setNombreUsuario("user name");
        spaceUsuario.setCorreo("user name@domain name.com");
        spaceUsuario.setContrasena("pass word");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(spaceUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithMixedCase() {
        // Arrange
        Usuario mixedCaseUsuario = new Usuario();
        mixedCaseUsuario.setNombreUsuario("UserName");
        mixedCaseUsuario.setCorreo("User@Domain.com");
        mixedCaseUsuario.setContrasena("PassWord");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(mixedCaseUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithBoundaryValues() {
        // Arrange
        Usuario boundaryUsuario = new Usuario();
        boundaryUsuario.setId(Long.MAX_VALUE);
        boundaryUsuario.setNombreUsuario("a"); // Minimum length
        boundaryUsuario.setCorreo("a@b.c"); // Minimum email
        boundaryUsuario.setContrasena("a"); // Minimum password
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(boundaryUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithNullValues() {
        // Arrange
        Usuario nullUsuario = new Usuario();
        nullUsuario.setId(null);
        nullUsuario.setNombreUsuario(null);
        nullUsuario.setCorreo(null);
        nullUsuario.setContrasena(null);
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(nullUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithEmptyStrings() {
        // Arrange
        Usuario emptyStringUsuario = new Usuario();
        emptyStringUsuario.setId(1L);
        emptyStringUsuario.setNombreUsuario("");
        emptyStringUsuario.setCorreo("");
        emptyStringUsuario.setContrasena("");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(emptyStringUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithWhitespaceOnly() {
        // Arrange
        Usuario whitespaceUsuario = new Usuario();
        whitespaceUsuario.setId(1L);
        whitespaceUsuario.setNombreUsuario("   ");
        whitespaceUsuario.setCorreo("   ");
        whitespaceUsuario.setContrasena("   ");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(whitespaceUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithZeroId() {
        // Arrange
        Usuario zeroIdUsuario = new Usuario();
        zeroIdUsuario.setId(0L);
        zeroIdUsuario.setNombreUsuario("zero");
        zeroIdUsuario.setCorreo("zero@domain.com");
        zeroIdUsuario.setContrasena("zero");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(zeroIdUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithNegativeId() {
        // Arrange
        Usuario negativeIdUsuario = new Usuario();
        negativeIdUsuario.setId(-1L);
        negativeIdUsuario.setNombreUsuario("negative");
        negativeIdUsuario.setCorreo("negative@domain.com");
        negativeIdUsuario.setContrasena("negative");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(negativeIdUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithMinimumId() {
        // Arrange
        Usuario minIdUsuario = new Usuario();
        minIdUsuario.setId(Long.MIN_VALUE);
        minIdUsuario.setNombreUsuario("min");
        minIdUsuario.setCorreo("min@domain.com");
        minIdUsuario.setContrasena("min");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(minIdUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithMaximumId() {
        // Arrange
        Usuario maxIdUsuario = new Usuario();
        maxIdUsuario.setId(Long.MAX_VALUE);
        maxIdUsuario.setNombreUsuario("max");
        maxIdUsuario.setCorreo("max@domain.com");
        maxIdUsuario.setContrasena("max");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(maxIdUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithIPAddressEmail() {
        // Arrange
        Usuario ipEmailUsuario = new Usuario();
        ipEmailUsuario.setId(1L);
        ipEmailUsuario.setNombreUsuario("ipuser");
        ipEmailUsuario.setCorreo("user@192.168.1.1");
        ipEmailUsuario.setContrasena("ipuser");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(ipEmailUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithLocalhostEmail() {
        // Arrange
        Usuario localhostUsuario = new Usuario();
        localhostUsuario.setId(1L);
        localhostUsuario.setNombreUsuario("localhostuser");
        localhostUsuario.setCorreo("user@localhost");
        localhostUsuario.setContrasena("localhostuser");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(localhostUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithPortEmail() {
        // Arrange
        Usuario portEmailUsuario = new Usuario();
        portEmailUsuario.setId(1L);
        portEmailUsuario.setNombreUsuario("portuser");
        portEmailUsuario.setCorreo("user@domain.com:8080");
        portEmailUsuario.setContrasena("portuser");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(portEmailUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithPathEmail() {
        // Arrange
        Usuario pathEmailUsuario = new Usuario();
        pathEmailUsuario.setId(1L);
        pathEmailUsuario.setNombreUsuario("pathuser");
        pathEmailUsuario.setCorreo("user@domain.com/path");
        pathEmailUsuario.setContrasena("pathuser");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(pathEmailUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithQueryEmail() {
        // Arrange
        Usuario queryEmailUsuario = new Usuario();
        queryEmailUsuario.setId(1L);
        queryEmailUsuario.setNombreUsuario("queryuser");
        queryEmailUsuario.setCorreo("user@domain.com?param=value");
        queryEmailUsuario.setContrasena("queryuser");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(queryEmailUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithFragmentEmail() {
        // Arrange
        Usuario fragmentEmailUsuario = new Usuario();
        fragmentEmailUsuario.setId(1L);
        fragmentEmailUsuario.setNombreUsuario("fragmentuser");
        fragmentEmailUsuario.setCorreo("user@domain.com#fragment");
        fragmentEmailUsuario.setContrasena("fragmentuser");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(fragmentEmailUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_WithAllSpecialCharactersEmail() {
        // Arrange
        Usuario allSpecialEmailUsuario = new Usuario();
        allSpecialEmailUsuario.setId(1L);
        allSpecialEmailUsuario.setNombreUsuario("allspecialuser");
        allSpecialEmailUsuario.setCorreo("user!@#$%^&*()_+-=[]{}|;':\",./<>?@domain.com");
        allSpecialEmailUsuario.setContrasena("allspecialuser");
        doNothing().when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act
        Response response = usuarioResource.registrarUsuario(allSpecialEmailUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> respuesta = (Map<String, String>) response.getEntity();
        assertNotNull(respuesta);
        assertEquals("Usuario registrado con éxito", respuesta.get("mensaje"));

        // Verify service was called
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }
}
