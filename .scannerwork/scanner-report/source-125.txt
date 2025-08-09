
package com.unis.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
/**
 * Entity representing the content of a web page.
 * <p>
 * This entity is used to manage dynamic sections within pages of the system.
 * Each content block is associated with a specific page and section,
 * and may include HTML content, images, and metadata for moderation and versioning.
 * </p>
 * 
 * <p>
 * Content can be in one of several states such as DRAFT, PUBLISHED, or REJECTED,
 * and is tracked by the user and timestamp of the last modification.
 * </p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "PAGE_CONTENT")
public class PageContent {

    /** The unique identifier of the page content. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pageContentSeq")
    @SequenceGenerator(name = "pageContentSeq", sequenceName = "PAGE_CONTENT_SEQ", allocationSize = 1)
    @Column(name = "ID_CONTENT")
    private Long idContent;

    /** The name of the page associated with the content. */
    @Column(name = "PAGE_NAME", nullable = false, length = 100)
    private String pageName;

    /** The name of the section within the page. */
    @Column(name = "SECTION_NAME", nullable = false, length = 100)
    private String sectionName;

    /** The title of the content. */
    @Column(name = "CONTENT_TITLE", length = 200)
    private String contentTitle;

    /** The body of the content. */
    @Lob
    @Column(name = "CONTENT_BODY", nullable = false)
    private String contentBody;

    /** The image associated with the content. */
    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    /** The last modified date of the content. */
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    /** The ID of the user who last modified the content. */
    @Column(name = "MODIFIED_BY", nullable = false)
    private Long modifiedBy;

    /** The status of the content (e.g., DRAFT, PUBLISHED, REJECTED). */
    @Column(name = "STATUS", length = 20)
    private String status;

    /** The reason for rejection, if applicable. */
    @Column(name = "REJECTION_REASON", length = 500)
    private String rejectionReason;

    /** The email of the editor who last modified the content. */
    @Column(name = "EDITOR_EMAIL", length = 150)
    private String editorEmail;

    /** Default constructor required by Hibernate. */
    public PageContent() {}

    // Getters and Setters

    /** @return the unique identifier of the page content. */
    public Long getIdContent() {
        return idContent;
    }

    /** @param idContent the unique identifier of the page content. */
    public void setIdContent(Long idContent) {
        this.idContent = idContent;
    }

    /** @return the name of the page associated with the content. */
    public String getPageName() {
        return pageName;
    }

    /** @param pageName the name of the page associated with the content. */
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    /** @return the name of the section within the page. */
    public String getSectionName() {
        return sectionName;
    }

    /** @param sectionName the name of the section within the page. */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /** @return the title of the content. */
    public String getContentTitle() {
        return contentTitle;
    }

    /** @param contentTitle the title of the content. */
    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    /** @return the body of the content. */
    public String getContentBody() {
        return contentBody;
    }

    /** @param contentBody the body of the content. */
    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    /** @return the image associated with the content. */
    public byte[] getImage() {
        return image;
    }

    /** @param image the image associated with the content. */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /** @return the last modified date of the content. */
    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    /** @param lastModifiedDate the last modified date of the content. */
    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /** @return the ID of the user who last modified the content. */
    public Long getModifiedBy() {
        return modifiedBy;
    }

    /** @param modifiedBy the ID of the user who last modified the content. */
    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /** @return the status of the content. */
    public String getStatus() {
        return status;
    }

    /** @param status the status of the content. */
    public void setStatus(String status) {
        this.status = status;
    }

    /** @return the reason for rejection, if applicable. */
    public String getRejectionReason() {
        return rejectionReason;
    }

    /** @param rejectionReason the reason for rejection, if applicable. */
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    /** @return the email of the editor who last modified the content. */
    public String getEditorEmail() {
        return editorEmail;
    }

    /** @param editorEmail the email of the editor who last modified the content. */
    public void setEditorEmail(String editorEmail) {
        this.editorEmail = editorEmail;
    }
}
