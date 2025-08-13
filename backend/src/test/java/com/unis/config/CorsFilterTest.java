package com.unis.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.MultivaluedHashMap;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class CorsFilterTest {

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private ContainerResponseContext responseContext;

    @Mock
    private MultivaluedMap<String, Object> headers;

    private CorsFilter corsFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        corsFilter = new CorsFilter();
        when(responseContext.getHeaders()).thenReturn(headers);
    }

    @Test
    void testFilterConPerfilQA() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("http://localhost:8083");
        when(requestContext.getMethod()).thenReturn("GET");
        
        // Simular que estamos en ambiente QA
        corsFilter.profile = "qa";

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers).add("Access-Control-Allow-Origin", "http://localhost:8083");
        verify(headers).add("Access-Control-Allow-Credentials", "true");
        verify(headers).add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        verify(headers).add("Access-Control-Allow-Headers", "*");
    }

    @Test
    void testFilterConPerfilDev() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("http://localhost:8083");
        
        // Simular que estamos en ambiente dev
        corsFilter.profile = "dev";

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers, never()).add(anyString(), anyString());
        verify(responseContext, never()).setStatus(anyInt());
    }

    @Test
    void testFilterConOrigenPermitido() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("http://localhost:5174");
        when(requestContext.getMethod()).thenReturn("POST");
        
        corsFilter.profile = "qa";

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers).add("Access-Control-Allow-Origin", "http://localhost:5174");
        verify(headers).add("Access-Control-Allow-Credentials", "true");
        verify(headers).add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        verify(headers).add("Access-Control-Allow-Headers", "*");
    }

    @Test
    void testFilterConOrigenNoPermitido() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("http://malicious-site.com");
        when(requestContext.getMethod()).thenReturn("GET");
        
        corsFilter.profile = "qa";

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers, never()).add(anyString(), anyString());
        verify(responseContext, never()).setStatus(anyInt());
    }

    @Test
    void testFilterConMetodoOPTIONS() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("http://localhost:8083");
        when(requestContext.getMethod()).thenReturn("OPTIONS");
        
        corsFilter.profile = "qa";

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers).add("Access-Control-Allow-Origin", "http://localhost:8083");
        verify(headers).add("Access-Control-Allow-Credentials", "true");
        verify(headers).add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        verify(headers).add("Access-Control-Allow-Headers", "*");
        verify(responseContext).setStatus(200);
    }

    @Test
    void testFilterConOrigenNull() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn(null);
        when(requestContext.getMethod()).thenReturn("GET");
        
        corsFilter.profile = "qa";

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers, never()).add(anyString(), anyString());
        verify(responseContext, never()).setStatus(anyInt());
    }

    @Test
    void testFilterConOrigenVacio() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("");
        when(requestContext.getMethod()).thenReturn("GET");
        
        corsFilter.profile = "qa";

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers, never()).add(anyString(), anyString());
        verify(responseContext, never()).setStatus(anyInt());
    }

    @Test
    void testFilterConOrigenConEspacios() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("  http://localhost:8083  ");
        when(requestContext.getMethod()).thenReturn("GET");
        
        corsFilter.profile = "qa";

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers, never()).add(anyString(), anyString());
        verify(responseContext, never()).setStatus(anyInt());
    }

    @Test
    void testFilterConPerfilNull() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("http://localhost:8083");
        when(requestContext.getMethod()).thenReturn("GET");
        
        corsFilter.profile = null;

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers, never()).add(anyString(), anyString());
        verify(responseContext, never()).setStatus(anyInt());
    }

    @Test
    void testFilterConPerfilVacio() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("http://localhost:8083");
        when(requestContext.getMethod()).thenReturn("GET");
        
        corsFilter.profile = "";

        // Act
        corsFilter.filter(requestContext, responseContext);

        // Assert
        verify(headers, never()).add(anyString(), anyString());
        verify(responseContext, never()).setStatus(anyInt());
    }

    @Test
    void testFilterConMetodosHTTPDiferentes() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("http://localhost:8083");
        
        corsFilter.profile = "qa";

        // Probar diferentes métodos HTTP
        String[] metodos = {"GET", "POST", "PUT", "DELETE", "PATCH"};
        
        for (String metodo : metodos) {
            // Resetear mocks para cada iteración
            reset(headers);
            when(responseContext.getHeaders()).thenReturn(headers);
            
            when(requestContext.getMethod()).thenReturn(metodo);
            
            // Act
            corsFilter.filter(requestContext, responseContext);
            
            // Assert
            verify(headers).add("Access-Control-Allow-Origin", "http://localhost:8083");
            verify(headers).add("Access-Control-Allow-Credentials", "true");
            verify(headers).add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            verify(headers).add("Access-Control-Allow-Headers", "*");
        }
    }

    @Test
    void testFilterConExcepcion() throws IOException {
        // Arrange
        when(requestContext.getHeaderString("Origin")).thenReturn("http://localhost:8083");
        when(requestContext.getMethod()).thenReturn("GET");
        when(responseContext.getHeaders()).thenThrow(new RuntimeException("Error en headers"));
        
        corsFilter.profile = "qa";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            corsFilter.filter(requestContext, responseContext);
        });
    }
}
