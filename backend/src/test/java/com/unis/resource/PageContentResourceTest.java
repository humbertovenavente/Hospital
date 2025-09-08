package com.unis.resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.unis.model.PageContent;
import com.unis.service.PageContentService;

import jakarta.ws.rs.core.Response;

/**
 * Test unitario para {@link PageContentResource}.
 */
public class PageContentResourceTest {

    @Mock
    private PageContentService service;

    @InjectMocks
    private PageContentResource resource;

    private PageContent testContent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testContent = new PageContent();
        testContent.setIdContent(1L);
        testContent.setContentTitle("Test Content");
        testContent.setContentBody("Test content body");
        testContent.setStatus("PROCESO");
        testContent.setPageName("test-page");
    }

    @Test
    void testGetPublished() {
        // Given
        String pageName = "test-page";
        List<PageContent> expectedContent = Arrays.asList(testContent);
        when(service.getPublishedContent(pageName)).thenReturn(expectedContent);

        // When
        List<PageContent> result = resource.getPublished(pageName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testContent, result.get(0));
        verify(service).getPublishedContent(pageName);
    }

    @Test
    void testGetPublishedWithEmptyPageName() {
        // Given
        String pageName = "";
        List<PageContent> expectedContent = Arrays.asList();
        when(service.getPublishedContent(pageName)).thenReturn(expectedContent);

        // When
        List<PageContent> result = resource.getPublished(pageName);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service).getPublishedContent(pageName);
    }

    @Test
    void testGetPublishedWithNullPageName() {
        // Given
        String pageName = null;
        List<PageContent> expectedContent = Arrays.asList();
        when(service.getPublishedContent(pageName)).thenReturn(expectedContent);

        // When
        List<PageContent> result = resource.getPublished(pageName);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service).getPublishedContent(pageName);
    }

    @Test
    void testGetDrafts() {
        // Given
        List<PageContent> expectedDrafts = Arrays.asList(testContent);
        when(service.getDraftContent()).thenReturn(expectedDrafts);

        // When
        List<PageContent> result = resource.getDrafts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testContent, result.get(0));
        verify(service).getDraftContent();
    }

    @Test
    void testGetDraftsWithEmptyList() {
        // Given
        List<PageContent> expectedDrafts = Arrays.asList();
        when(service.getDraftContent()).thenReturn(expectedDrafts);

        // When
        List<PageContent> result = resource.getDrafts();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service).getDraftContent();
    }

    @Test
    void testGetPendientesModeracion() {
        // Given
        List<PageContent> expectedPendientes = Arrays.asList(testContent);
        when(service.getByStatus("PROCESO")).thenReturn(expectedPendientes);

        // When
        List<PageContent> result = resource.getPendientesModeracion();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testContent, result.get(0));
        verify(service).getByStatus("PROCESO");
    }

    @Test
    void testGetPendientesModeracionWithEmptyList() {
        // Given
        List<PageContent> expectedPendientes = Arrays.asList();
        when(service.getByStatus("PROCESO")).thenReturn(expectedPendientes);

        // When
        List<PageContent> result = resource.getPendientesModeracion();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service).getByStatus("PROCESO");
    }

    @Test
    void testGetByIdWithValidId() {
        // Given
        Long id = 1L;
        when(service.findById(id)).thenReturn(testContent);

        // When
        Response response = resource.getById(id);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testContent, response.getEntity());
        verify(service).findById(id);
    }

    @Test
    void testGetByIdWithNullContent() {
        // Given
        Long id = 999L;
        when(service.findById(id)).thenReturn(null);

        // When
        Response response = resource.getById(id);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(service).findById(id);
    }

    @Test
    void testGetByIdWithZeroId() {
        // Given
        Long id = 0L;
        when(service.findById(id)).thenReturn(null);

        // When
        Response response = resource.getById(id);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(service).findById(id);
    }

    @Test
    void testCreateWithValidContent() {
        // Given
        PageContent contentToCreate = new PageContent();
        contentToCreate.setContentTitle("New Content");
        contentToCreate.setContentBody("New content body");
        contentToCreate.setPageName("new-page");
        
        when(service.create(any(PageContent.class))).thenReturn(testContent);

        // When
        Response response = resource.create(contentToCreate);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(testContent, response.getEntity());
        assertEquals("PROCESO", contentToCreate.getStatus());
        verify(service).create(contentToCreate);
    }

    @Test
    void testCreateWithNullContent() {
        // Given
        PageContent contentToCreate = null;
        
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            resource.create(contentToCreate);
        });
        
        // No se debe llamar al servicio si el contenido es null
        verify(service, never()).create(any(PageContent.class));
    }

    @Test
    void testCreateWithContentAlreadyHavingStatus() {
        // Given
        PageContent contentToCreate = new PageContent();
        contentToCreate.setContentTitle("New Content");
        contentToCreate.setStatus("PUBLISHED");
        
        when(service.create(any(PageContent.class))).thenReturn(testContent);

        // When
        Response response = resource.create(contentToCreate);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("PROCESO", contentToCreate.getStatus()); // Debe cambiar a PROCESO
        verify(service).create(contentToCreate);
    }

    @Test
    void testUpdateWithValidId() {
        // Given
        Long id = 1L;
        PageContent contentToUpdate = new PageContent();
        contentToUpdate.setContentTitle("Updated Content");
        contentToUpdate.setContentBody("Updated content body");
        
        when(service.update(eq(id), any(PageContent.class))).thenReturn(testContent);

        // When
        Response response = resource.update(id, contentToUpdate);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testContent, response.getEntity());
        verify(service).update(id, contentToUpdate);
    }

    @Test
    void testUpdateWithZeroId() {
        // Given
        Long id = 0L;
        PageContent contentToUpdate = new PageContent();
        contentToUpdate.setContentTitle("Updated Content");
        
        when(service.update(eq(id), any(PageContent.class))).thenReturn(testContent);

        // When
        Response response = resource.update(id, contentToUpdate);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(service).update(id, contentToUpdate);
    }

    @Test
    void testUpdateWithNullContent() {
        // Given
        Long id = 1L;
        PageContent contentToUpdate = null;
        
        // Mock para simular el comportamiento del servicio
        when(service.update(eq(id), eq(contentToUpdate))).thenThrow(new NullPointerException("updatedContent cannot be null"));

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            resource.update(id, contentToUpdate);
        });
        
        verify(service).update(id, contentToUpdate);
    }

    @Test
    void testPublishWithValidId() {
        // Given
        Long id = 1L;
        when(service.publish(id)).thenReturn(testContent);

        // When
        Response response = resource.publish(id);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testContent, response.getEntity());
        verify(service).publish(id);
    }

    @Test
    void testPublishWithZeroId() {
        // Given
        Long id = 0L;
        when(service.publish(id)).thenReturn(testContent);

        // When
        Response response = resource.publish(id);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(service).publish(id);
    }

    @Test
    void testRejectWithValidIdAndMotivo() {
        // Given
        Long id = 1L;
        String motivo = "Contenido inapropiado";
        when(service.reject(eq(id), eq(motivo))).thenReturn(testContent);

        // When
        Response response = resource.reject(id, motivo);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testContent, response.getEntity());
        verify(service).reject(id, motivo);
    }

    @Test
    void testRejectWithValidIdAndEmptyMotivo() {
        // Given
        Long id = 1L;
        String motivo = "";
        when(service.reject(eq(id), eq(motivo))).thenReturn(testContent);

        // When
        Response response = resource.reject(id, motivo);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(service).reject(id, motivo);
    }

    @Test
    void testRejectWithValidIdAndNullMotivo() {
        // Given
        Long id = 1L;
        String motivo = null;
        when(service.reject(eq(id), eq(motivo))).thenReturn(testContent);

        // When
        Response response = resource.reject(id, motivo);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(service).reject(id, motivo);
    }

    @Test
    void testRejectWithZeroId() {
        // Given
        Long id = 0L;
        String motivo = "Test motivo";
        when(service.reject(eq(id), eq(motivo))).thenReturn(testContent);

        // When
        Response response = resource.reject(id, motivo);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(service).reject(id, motivo);
    }

    @Test
    void testDeleteWithValidId() {
        // Given
        Long id = 1L;
        doNothing().when(service).delete(id);

        // When
        Response response = resource.delete(id);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(service).delete(id);
    }

    @Test
    void testDeleteWithZeroId() {
        // Given
        Long id = 0L;
        doNothing().when(service).delete(id);

        // When
        Response response = resource.delete(id);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(service).delete(id);
    }

    @Test
    void testDeleteWithNullId() {
        // Given
        Long id = null;
        doNothing().when(service).delete(id);

        // When
        Response response = resource.delete(id);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(service).delete(id);
    }

    @Test
    void testGetPublishedWithSpecialCharacters() {
        // Given
        String pageName = "test-page-123!@#$%^&*()";
        List<PageContent> expectedContent = Arrays.asList(testContent);
        when(service.getPublishedContent(pageName)).thenReturn(expectedContent);

        // When
        List<PageContent> result = resource.getPublished(pageName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(service).getPublishedContent(pageName);
    }

    @Test
    void testGetPublishedWithWhitespace() {
        // Given
        String pageName = "  test-page  ";
        List<PageContent> expectedContent = Arrays.asList(testContent);
        when(service.getPublishedContent(pageName)).thenReturn(expectedContent);

        // When
        List<PageContent> result = resource.getPublished(pageName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(service).getPublishedContent(pageName);
    }
}
