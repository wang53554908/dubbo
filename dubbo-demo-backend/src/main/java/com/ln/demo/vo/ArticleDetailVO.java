package com.ln.demo.vo;

import java.util.Date;

/**
 * 文章dto类
 * 
 * @author wjp
 * @date 2019-03-21
 */
public final class ArticleDetailVO implements java.io.Serializable {

    private static final long serialVersionUID = 4368450080812978404L;

    private int id;

    private String title;

    private String author;
    
    private Date displayTime;
    
    private int importance;
    
    private String contentShort;

    private String content;

    private String imageUri;

    private String status;

    private int commentDisabled;
    
    private String platforms;
    
    private String sourceUri;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDisplayTime() {
		return displayTime;
	}

	public void setDisplayTime(Date displayTime) {
		this.displayTime = displayTime;
	}

	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}

	public String getContentShort() {
		return contentShort;
	}

	public void setContentShort(String contentShort) {
		this.contentShort = contentShort;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCommentDisabled() {
		return commentDisabled;
	}

	public void setCommentDisabled(int commentDisabled) {
		this.commentDisabled = commentDisabled;
	}

	public String getPlatforms() {
		return platforms;
	}

	public void setPlatforms(String platforms) {
		this.platforms = platforms;
	}

	public String getSourceUri() {
		return sourceUri;
	}

	public void setSourceUri(String sourceUri) {
		this.sourceUri = sourceUri;
	}

}
