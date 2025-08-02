package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.PageContent;
import com.unis.repository.PageContentRepository;

import jakarta.ws.rs.NotFoundException;

public class PageContentServiceTest {

    @Mock
    PageContentRepository repository;

    @InjectMocks
    PageContentService pageContentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPublishedContent() {
        String pageName = "home";
        List<PageContent> expectedList = Arrays.asList(new PageContent(), new PageContent());
        when(repository.findPublishedByPage(pageName)).thenReturn(expectedList);

        List<PageContent> result = pageContentService.getPublishedContent(pageName);
        assertEquals(expectedList, result);
    }

    @Test
    public void testGetDraftContent() {
        List<PageContent> expectedDrafts = Arrays.asList(new PageContent(), new PageContent());
        when(repository.findDrafts()).thenReturn(expectedDrafts);

        List<PageContent> result = pageContentService.getDraftContent();
        assertEquals(expectedDrafts, result);
    }

    @Test
    public void testCreate() {
        PageContent content = new PageContent();
        content.setEditorEmail("editor@hospital.com");
        content.setLastModifiedDate(null);

        PageContent result = pageContentService.create(content);

        verify(repository, times(1)).persist(Mockito.<PageContent>any());
        assertNotNull(result.getLastModifiedDate());
    }

    @Test
    public void testCreateSinEditorEmail() {
        PageContent content = new PageContent(); // sin editorEmail

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            pageContentService.create(content);
        });

        assertEquals("Editor email es requerido", ex.getMessage());
        verify(repository, never()).persist(Mockito.<PageContent>any());
    }

    @Test
    public void testUpdateFound() {
        Long id = 1L;
        PageContent existing = new PageContent();
        existing.setStatus("DRAFT");
        existing.setImage("oldImage.png".getBytes());
        existing.setModifiedBy(111L);

        PageContent updateData = new PageContent();
        updateData.setPageName("NewPage");
        updateData.setSectionName("NewSection");
        updateData.setContentTitle("NewTitle");
        updateData.setContentBody("NewBody");
        updateData.setImage("newImage.png".getBytes());
        updateData.setModifiedBy(222L);
        updateData.setStatus("UPDATED");

        when(repository.findById(id)).thenReturn(existing);

        PageContent result = pageContentService.update(id, updateData);

        assertEquals("NewPage", result.getPageName());
        assertEquals("NewSection", result.getSectionName());
        assertEquals("NewTitle", result.getContentTitle());
        assertEquals("NewBody", result.getContentBody());
        assertArrayEquals("newImage.png".getBytes(), result.getImage());
        assertEquals(222L, result.getModifiedBy());
        assertEquals("UPDATED", result.getStatus());
        assertNotNull(result.getLastModifiedDate());
    }

    @Test
    public void testUpdateCuandoEsPublicado() {
        Long id = 1L;
        PageContent publicado = new PageContent();
        publicado.setStatus("PUBLICADO");

        PageContent nuevo = new PageContent();
        nuevo.setEditorEmail("editor@hospital.com");
        nuevo.setPageName("Nueva Página");

        when(repository.findById(id)).thenReturn(publicado);

        PageContent result = pageContentService.update(id, nuevo);

        assertEquals("PROCESO", result.getStatus());
        assertEquals("editor@hospital.com", result.getEditorEmail());
        assertEquals("Nueva Página", result.getPageName());
        assertNotNull(result.getLastModifiedDate());
        verify(repository).persist(Mockito.<PageContent>any());
    }

    @Test
    public void testUpdateNotFound() {
        when(repository.findById(1L)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            pageContentService.update(1L, new PageContent());
        });

        assertEquals("Contenido no encontrado", ex.getMessage());
    }

    @Test
    public void testPublishFound() {
        Long id = 1L;
        PageContent existing = new PageContent();
        existing.setStatus("DRAFT");
        existing.setLastModifiedDate(null);
        when(repository.findById(id)).thenReturn(existing);

        PageContent result = pageContentService.publish(id);

        assertEquals("PUBLICADO", result.getStatus());
        assertNotNull(result.getLastModifiedDate());
    }

    @Test
    public void testPublishNotFound() {
        when(repository.findById(1L)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            pageContentService.publish(1L);
        });

        assertEquals("Contenido no encontrado", ex.getMessage());
    }

    @Test
    public void testReject() {
        Long id = 1L;
        PageContent existing = new PageContent();
        existing.setStatus("DRAFT");
        when(repository.findById(id)).thenReturn(existing);

        PageContent result = pageContentService.reject(id, "Contenido no aprobado");

        assertEquals("RECHAZADO", result.getStatus());
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        when(repository.deleteById(id)).thenReturn(true);

        pageContentService.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}
