package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;

/**
 * Test class for PageContent entity.
 * Tests all getters, setters, and edge cases to achieve 100% coverage.
 */
class PageContentTest {

    private PageContent pageContent;
    private Timestamp testTimestamp;

    @BeforeEach
    void setUp() {
        pageContent = new PageContent();
        testTimestamp = new Timestamp(System.currentTimeMillis());
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(pageContent);
        assertNull(pageContent.getIdContent());
        assertNull(pageContent.getPageName());
        assertNull(pageContent.getSectionName());
        assertNull(pageContent.getContentTitle());
        assertNull(pageContent.getContentBody());
        assertNull(pageContent.getImage());
        assertNull(pageContent.getLastModifiedDate());
        assertNull(pageContent.getModifiedBy());
        assertNull(pageContent.getStatus());
        assertNull(pageContent.getRejectionReason());
        assertNull(pageContent.getEditorEmail());
    }

    // ========== ID_CONTENT Tests ==========
    
    @Test
    void testSetAndGetIdContent() {
        Long id = 123L;
        pageContent.setIdContent(id);
        assertEquals(id, pageContent.getIdContent());
    }

    @Test
    void testSetAndGetIdContentWithNull() {
        pageContent.setIdContent(null);
        assertNull(pageContent.getIdContent());
    }

    @Test
    void testSetAndGetIdContentWithZero() {
        pageContent.setIdContent(0L);
        assertEquals(0L, pageContent.getIdContent());
    }

    @Test
    void testSetAndGetIdContentWithNegative() {
        pageContent.setIdContent(-1L);
        assertEquals(-1L, pageContent.getIdContent());
    }

    @Test
    void testSetAndGetIdContentWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        pageContent.setIdContent(maxValue);
        assertEquals(maxValue, pageContent.getIdContent());
    }

    // ========== PAGE_NAME Tests ==========
    
    @Test
    void testSetAndGetPageName() {
        String pageName = "Home Page";
        pageContent.setPageName(pageName);
        assertEquals(pageName, pageContent.getPageName());
    }

    @Test
    void testSetAndGetPageNameWithNull() {
        pageContent.setPageName(null);
        assertNull(pageContent.getPageName());
    }

    @Test
    void testSetAndGetPageNameWithEmptyString() {
        pageContent.setPageName("");
        assertEquals("", pageContent.getPageName());
    }

    @Test
    void testSetAndGetPageNameWithWhitespace() {
        String pageName = " About Us ";
        pageContent.setPageName(pageName);
        assertEquals(pageName, pageContent.getPageName());
    }

    @Test
    void testSetAndGetPageNameWithSpecialCharacters() {
        String pageName = "Página de Inicio - Home";
        pageContent.setPageName(pageName);
        assertEquals(pageName, pageContent.getPageName());
    }

    // ========== SECTION_NAME Tests ==========
    
    @Test
    void testSetAndGetSectionName() {
        String sectionName = "Header";
        pageContent.setSectionName(sectionName);
        assertEquals(sectionName, pageContent.getSectionName());
    }

    @Test
    void testSetAndGetSectionNameWithNull() {
        pageContent.setSectionName(null);
        assertNull(pageContent.getSectionName());
    }

    @Test
    void testSetAndGetSectionNameWithEmptyString() {
        pageContent.setSectionName("");
        assertEquals("", pageContent.getSectionName());
    }

    @Test
    void testSetAndGetSectionNameWithWhitespace() {
        String sectionName = " Footer ";
        pageContent.setSectionName(sectionName);
        assertEquals(sectionName, pageContent.getSectionName());
    }

    @Test
    void testSetAndGetSectionNameWithSpecialCharacters() {
        String sectionName = "Sección Principal - Main";
        pageContent.setSectionName(sectionName);
        assertEquals(sectionName, pageContent.getSectionName());
    }

    // ========== CONTENT_TITLE Tests ==========
    
    @Test
    void testSetAndGetContentTitle() {
        String contentTitle = "Welcome to Our Hospital";
        pageContent.setContentTitle(contentTitle);
        assertEquals(contentTitle, pageContent.getContentTitle());
    }

    @Test
    void testSetAndGetContentTitleWithNull() {
        pageContent.setContentTitle(null);
        assertNull(pageContent.getContentTitle());
    }

    @Test
    void testSetAndGetContentTitleWithEmptyString() {
        pageContent.setContentTitle("");
        assertEquals("", pageContent.getContentTitle());
    }

    @Test
    void testSetAndGetContentTitleWithWhitespace() {
        String contentTitle = " Bienvenidos a Nuestro Hospital ";
        pageContent.setContentTitle(contentTitle);
        assertEquals(contentTitle, pageContent.getContentTitle());
    }

    @Test
    void testSetAndGetContentTitleWithLongText() {
        String contentTitle = "Comprehensive Healthcare Services - Providing Excellence in Medical Care Since 1990";
        pageContent.setContentTitle(contentTitle);
        assertEquals(contentTitle, pageContent.getContentTitle());
    }

    // ========== CONTENT_BODY Tests ==========
    
    @Test
    void testSetAndGetContentBody() {
        String contentBody = "<h1>Welcome</h1><p>This is the main content.</p>";
        pageContent.setContentBody(contentBody);
        assertEquals(contentBody, pageContent.getContentBody());
    }

    @Test
    void testSetAndGetContentBodyWithNull() {
        pageContent.setContentBody(null);
        assertNull(pageContent.getContentBody());
    }

    @Test
    void testSetAndGetContentBodyWithEmptyString() {
        pageContent.setContentBody("");
        assertEquals("", pageContent.getContentBody());
    }

    @Test
    void testSetAndGetContentBodyWithWhitespace() {
        String contentBody = " <p>Content with whitespace</p> ";
        pageContent.setContentBody(contentBody);
        assertEquals(contentBody, pageContent.getContentBody());
    }

    @Test
    void testSetAndGetContentBodyWithHTML() {
        String contentBody = "<div><h1>Title</h1><p>Paragraph with <strong>bold</strong> text.</p></div>";
        pageContent.setContentBody(contentBody);
        assertEquals(contentBody, pageContent.getContentBody());
    }

    // ========== IMAGE Tests ==========
    
    @Test
    void testSetAndGetImage() {
        byte[] image = "test image data".getBytes();
        pageContent.setImage(image);
        assertEquals(image, pageContent.getImage());
    }

    @Test
    void testSetAndGetImageWithNull() {
        pageContent.setImage(null);
        assertNull(pageContent.getImage());
    }

    @Test
    void testSetAndGetImageWithEmptyArray() {
        byte[] image = new byte[0];
        pageContent.setImage(image);
        assertEquals(image, pageContent.getImage());
    }

    @Test
    void testSetAndGetImageWithLargeArray() {
        byte[] image = new byte[1024];
        for (int i = 0; i < image.length; i++) {
            image[i] = (byte) (i % 256);
        }
        pageContent.setImage(image);
        assertEquals(image, pageContent.getImage());
    }

    // ========== LAST_MODIFIED_DATE Tests ==========
    
    @Test
    void testSetAndGetLastModifiedDate() {
        pageContent.setLastModifiedDate(testTimestamp);
        assertEquals(testTimestamp, pageContent.getLastModifiedDate());
    }

    @Test
    void testSetAndGetLastModifiedDateWithNull() {
        pageContent.setLastModifiedDate(null);
        assertNull(pageContent.getLastModifiedDate());
    }

    @Test
    void testSetAndGetLastModifiedDateWithCurrentTime() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        pageContent.setLastModifiedDate(currentTime);
        assertEquals(currentTime, pageContent.getLastModifiedDate());
    }

    @Test
    void testSetAndGetLastModifiedDateWithPastTime() {
        Timestamp pastTime = new Timestamp(1000000000000L); // 2001-09-09
        pageContent.setLastModifiedDate(pastTime);
        assertEquals(pastTime, pageContent.getLastModifiedDate());
    }

    // ========== MODIFIED_BY Tests ==========
    
    @Test
    void testSetAndGetModifiedBy() {
        Long modifiedBy = 456L;
        pageContent.setModifiedBy(modifiedBy);
        assertEquals(modifiedBy, pageContent.getModifiedBy());
    }

    @Test
    void testSetAndGetModifiedByWithNull() {
        pageContent.setModifiedBy(null);
        assertNull(pageContent.getModifiedBy());
    }

    @Test
    void testSetAndGetModifiedByWithZero() {
        pageContent.setModifiedBy(0L);
        assertEquals(0L, pageContent.getModifiedBy());
    }

    @Test
    void testSetAndGetModifiedByWithNegative() {
        pageContent.setModifiedBy(-1L);
        assertEquals(-1L, pageContent.getModifiedBy());
    }

    @Test
    void testSetAndGetModifiedByWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        pageContent.setModifiedBy(maxValue);
        assertEquals(maxValue, pageContent.getModifiedBy());
    }

    // ========== STATUS Tests ==========
    
    @Test
    void testSetAndGetStatus() {
        String status = "PUBLISHED";
        pageContent.setStatus(status);
        assertEquals(status, pageContent.getStatus());
    }

    @Test
    void testSetAndGetStatusWithNull() {
        pageContent.setStatus(null);
        assertNull(pageContent.getStatus());
    }

    @Test
    void testSetAndGetStatusWithEmptyString() {
        pageContent.setStatus("");
        assertEquals("", pageContent.getStatus());
    }

    @Test
    void testSetAndGetStatusWithDifferentValues() {
        String[] statuses = {"DRAFT", "PUBLISHED", "REJECTED", "PENDING", "ARCHIVED"};
        
        for (String status : statuses) {
            pageContent.setStatus(status);
            assertEquals(status, pageContent.getStatus());
        }
    }

    @Test
    void testSetAndGetStatusWithWhitespace() {
        String status = " DRAFT ";
        pageContent.setStatus(status);
        assertEquals(status, pageContent.getStatus());
    }

    // ========== REJECTION_REASON Tests ==========
    
    @Test
    void testSetAndGetRejectionReason() {
        String rejectionReason = "Content contains inappropriate language";
        pageContent.setRejectionReason(rejectionReason);
        assertEquals(rejectionReason, pageContent.getRejectionReason());
    }

    @Test
    void testSetAndGetRejectionReasonWithNull() {
        pageContent.setRejectionReason(null);
        assertNull(pageContent.getRejectionReason());
    }

    @Test
    void testSetAndGetRejectionReasonWithEmptyString() {
        pageContent.setRejectionReason("");
        assertEquals("", pageContent.getRejectionReason());
    }

    @Test
    void testSetAndGetRejectionReasonWithLongText() {
        String rejectionReason = "This content has been rejected due to multiple violations of our content guidelines, including inappropriate language, unverified medical claims, and failure to meet editorial standards. Please review and revise accordingly.";
        pageContent.setRejectionReason(rejectionReason);
        assertEquals(rejectionReason, pageContent.getRejectionReason());
    }

    @Test
    void testSetAndGetRejectionReasonWithWhitespace() {
        String rejectionReason = " Content needs revision ";
        pageContent.setRejectionReason(rejectionReason);
        assertEquals(rejectionReason, pageContent.getRejectionReason());
    }

    // ========== EDITOR_EMAIL Tests ==========
    
    @Test
    void testSetAndGetEditorEmail() {
        String editorEmail = "editor@hospital.com";
        pageContent.setEditorEmail(editorEmail);
        assertEquals(editorEmail, pageContent.getEditorEmail());
    }

    @Test
    void testSetAndGetEditorEmailWithNull() {
        pageContent.setEditorEmail(null);
        assertNull(pageContent.getEditorEmail());
    }

    @Test
    void testSetAndGetEditorEmailWithEmptyString() {
        pageContent.setEditorEmail("");
        assertEquals("", pageContent.getEditorEmail());
    }

    @Test
    void testSetAndGetEditorEmailWithDifferentFormats() {
        String[] emails = {
            "user@example.com",
            "admin@hospital.org",
            "test.user+tag@domain.co.uk",
            "firstname.lastname@company-name.com",
            "123@456.789"
        };
        
        for (String email : emails) {
            pageContent.setEditorEmail(email);
            assertEquals(email, pageContent.getEditorEmail());
        }
    }

    @Test
    void testSetAndGetEditorEmailWithWhitespace() {
        String editorEmail = " editor@hospital.com ";
        pageContent.setEditorEmail(editorEmail);
        assertEquals(editorEmail, pageContent.getEditorEmail());
    }

    // ========== Comprehensive Tests ==========
    
    @Test
    void testCompletePageContentSetup() {
        // Arrange
        Long id = 999L;
        String pageName = "Services Page";
        String sectionName = "Main Content";
        String contentTitle = "Our Medical Services";
        String contentBody = "<h1>Services</h1><p>We offer comprehensive medical care.</p>";
        byte[] image = "hospital logo".getBytes();
        Long modifiedBy = 123L;
        String status = "PUBLISHED";
        String rejectionReason = null;
        String editorEmail = "admin@hospital.com";

        // Act
        pageContent.setIdContent(id);
        pageContent.setPageName(pageName);
        pageContent.setSectionName(sectionName);
        pageContent.setContentTitle(contentTitle);
        pageContent.setContentBody(contentBody);
        pageContent.setImage(image);
        pageContent.setLastModifiedDate(testTimestamp);
        pageContent.setModifiedBy(modifiedBy);
        pageContent.setStatus(status);
        pageContent.setRejectionReason(rejectionReason);
        pageContent.setEditorEmail(editorEmail);

        // Assert
        assertEquals(id, pageContent.getIdContent());
        assertEquals(pageName, pageContent.getPageName());
        assertEquals(sectionName, pageContent.getSectionName());
        assertEquals(contentTitle, pageContent.getContentTitle());
        assertEquals(contentBody, pageContent.getContentBody());
        assertEquals(image, pageContent.getImage());
        assertEquals(testTimestamp, pageContent.getLastModifiedDate());
        assertEquals(modifiedBy, pageContent.getModifiedBy());
        assertEquals(status, pageContent.getStatus());
        assertEquals(rejectionReason, pageContent.getRejectionReason());
        assertEquals(editorEmail, pageContent.getEditorEmail());
    }

    @Test
    void testMultipleUpdates() {
        // Arrange
        pageContent.setContentTitle("Initial Title");
        pageContent.setStatus("DRAFT");
        
        // Act - Update values
        pageContent.setContentTitle("Updated Title");
        pageContent.setStatus("PUBLISHED");
        
        // Assert
        assertEquals("Updated Title", pageContent.getContentTitle());
        assertEquals("PUBLISHED", pageContent.getStatus());
    }

    @Test
    void testResetToNull() {
        // Arrange
        pageContent.setContentTitle("Test Title");
        pageContent.setStatus("DRAFT");
        
        // Act - Reset to null
        pageContent.setContentTitle(null);
        pageContent.setStatus(null);
        
        // Assert
        assertNull(pageContent.getContentTitle());
        assertNull(pageContent.getStatus());
    }

    @Test
    void testEdgeCaseValues() {
        // Arrange & Act - Set edge case values
        pageContent.setIdContent(0L);
        pageContent.setModifiedBy(0L);
        pageContent.setPageName("");
        pageContent.setSectionName("   ");
        pageContent.setContentTitle("0");
        pageContent.setStatus("-1");
        
        // Assert
        assertEquals(0L, pageContent.getIdContent());
        assertEquals(0L, pageContent.getModifiedBy());
        assertEquals("", pageContent.getPageName());
        assertEquals("   ", pageContent.getSectionName());
        assertEquals("0", pageContent.getContentTitle());
        assertEquals("-1", pageContent.getStatus());
    }

    @Test
    void testAllFieldsTogether() {
        // Arrange
        Long id = 777L;
        String pageName = "Contact Page";
        String sectionName = "Contact Form";
        String contentTitle = "Get in Touch";
        String contentBody = "<h1>Contact Us</h1><p>Fill out the form below.</p>";
        byte[] image = "contact image".getBytes();
        Long modifiedBy = 456L;
        String status = "PENDING";
        String rejectionReason = "Under review";
        String editorEmail = "reviewer@hospital.com";

        // Act
        pageContent.setIdContent(id);
        pageContent.setPageName(pageName);
        pageContent.setSectionName(sectionName);
        pageContent.setContentTitle(contentTitle);
        pageContent.setContentBody(contentBody);
        pageContent.setImage(image);
        pageContent.setLastModifiedDate(testTimestamp);
        pageContent.setModifiedBy(modifiedBy);
        pageContent.setStatus(status);
        pageContent.setRejectionReason(rejectionReason);
        pageContent.setEditorEmail(editorEmail);

        // Assert - Verify all fields are set correctly
        assertEquals(id, pageContent.getIdContent());
        assertEquals(pageName, pageContent.getPageName());
        assertEquals(sectionName, pageContent.getSectionName());
        assertEquals(contentTitle, pageContent.getContentTitle());
        assertEquals(contentBody, pageContent.getContentBody());
        assertEquals(image, pageContent.getImage());
        assertEquals(testTimestamp, pageContent.getLastModifiedDate());
        assertEquals(modifiedBy, pageContent.getModifiedBy());
        assertEquals(status, pageContent.getStatus());
        assertEquals(rejectionReason, pageContent.getRejectionReason());
        assertEquals(editorEmail, pageContent.getEditorEmail());
    }

    @Test
    void testDataIntegrity() {
        // Arrange
        String originalTitle = "Original Title";
        String originalStatus = "DRAFT";
        
        // Act
        pageContent.setContentTitle(originalTitle);
        pageContent.setStatus(originalStatus);
        
        // Assert - Verify data doesn't change after multiple reads
        String title1 = pageContent.getContentTitle();
        String title2 = pageContent.getContentTitle();
        String status1 = pageContent.getStatus();
        String status2 = pageContent.getStatus();
        
        assertEquals(title1, title2);
        assertEquals(status1, status2);
        assertEquals(originalTitle, title1);
        assertEquals(originalStatus, status1);
    }
}

