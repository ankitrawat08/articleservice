package articleservice.io.controller.request;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import articleservice.io.bean.Tag;

public class ArticleRequest implements Serializable {

	public String id;
	public String slug;
	public String title;
	public String description;
	public String body;
	public Set<Tag> tags;

	public ArticleRequest() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = new HashSet<>();
		for (String s : tags) {
			Tag tag = new Tag(s);
			this.tags.add(tag);
		}
	}

}
