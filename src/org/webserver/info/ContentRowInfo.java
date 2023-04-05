package org.webserver.info;

public class ContentRowInfo {

	private String title;
	private String textInfo;
	private String imageUrl;
	private String author;
	private String postDate;

	public ContentRowInfo(String title, String textInfo, String imageUrl, String author, String postDate) {
		this.title = title;
		this.textInfo = textInfo;
		this.imageUrl = imageUrl;
		this.author = author;
		this.postDate = postDate;
	}

	public String getTitle() {
		return title;
	}

	public String getTextInfo() {
		return textInfo;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getAuthor() {
		return author;
	}

	public String getPostDate() {
		return postDate;
	}

}
