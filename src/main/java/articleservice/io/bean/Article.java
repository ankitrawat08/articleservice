package articleservice.io.bean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Article {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public String id;

	@Column(columnDefinition = "TEXT")
	public String slug;

	@Column(columnDefinition = "TEXT")
	public String title;

	@Column(columnDefinition = "TEXT")
	public String description;

	@Column(columnDefinition = "TEXT")
	public String body;

	public LocalDateTime createdAt = LocalDateTime.now();
	public LocalDateTime updatedAt = LocalDateTime.now();
	public Boolean favorited = false;
	public Integer favoritesCount = 0;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "article_tag", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tag> tagSet = new HashSet<>();

	public Article() {

	}

	public Article(String title, String description, String body, Set<Tag> tag) {
		super();

		this.title = title;
		this.slug = createSlug(title);
		this.description = description;
		this.body = body;
		this.tagSet = tag;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean getFavorited() {
		return favorited;
	}

	public void setFavorited(Boolean favorited) {
		this.favorited = favorited;
	}

	public Integer getFavoritesCount() {
		return favoritesCount;
	}

	public void setFavoritesCount(Integer favoritesCount) {
		this.favoritesCount = favoritesCount;
	}

	public Set<String> getTagSet() {
		Set<String> tagList = new HashSet<>();
		for (Tag tag : this.tagSet) {
			tagList.add(tag.getTagName());
		}
		return tagList;
	}

	public void setTagSet(Set<Tag> tagSet) {
		this.tagSet = tagSet;
	}

	private String createSlug(String title) {
		String words[] = title.toLowerCase().split(" ");
		String sentence = "";

		for (String word : words) {
			sentence += word + "-";
		}
		return sentence.substring(0, sentence.length() - 1);

	}

}
