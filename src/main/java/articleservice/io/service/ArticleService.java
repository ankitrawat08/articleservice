package articleservice.io.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import articleservice.io.bean.Article;
import articleservice.io.controller.request.ArticleRequest;
import articleservice.io.repository.ArticleRepository;

@Service
public class ArticleService {

	@Autowired
	ArticleRepository articleRepository;

	@Value("${average.speed}")
	private Integer speedOfAverageHuman;

	private Integer IN = 1;
	private Integer OUT = 0;

	public Article createArticle(ArticleRequest articleRequest) {
		Article article = new Article(articleRequest.getTitle(), articleRequest.getDescription(),
				articleRequest.getBody(), articleRequest.getTags());

		return articleRepository.save(article);
	}

	public Article updateArticle(String str, String slugId) {
		Article article = getArticle(slugId);
		article.setSlug(createSlug(str));
		article.setUpdatedAt(LocalDateTime.now());
		return articleRepository.save(article);
	}

	public void updateArticleTitleAndSlug(String str, String slugId) {
		Article article = getArticle(slugId);
		article.setTitle(slugId);
		article.setSlug(slugId);
		articleRepository.save(article);
	}

	// doing pagination
	public Map<String, Object> getAllArticles(int page, int size) {
		List<Article> articles = new ArrayList<Article>();
		Pageable paging = PageRequest.of(page, size);
		Page<Article> pageArticle;
		pageArticle = articleRepository.findAll(paging);

		articles = pageArticle.getContent();

		Map<String, Object> response = new HashMap<>();
		response.put("articles", articles);
		response.put("currentPage", pageArticle.getNumber());
		response.put("totalItems", pageArticle.getTotalElements());
		response.put("totalPages", pageArticle.getTotalPages());

		return response;
	}

	public Map<String, Object> getReadTime(String slugUuid) {
		Article article = getArticle(slugUuid);
		int words = countWords(article.getBody());
		Map<String, Integer> timeToRead = new HashMap<>();

		timeToRead.put("mins", words / speedOfAverageHuman);
		timeToRead.put("seconds", (words % speedOfAverageHuman) * 60 / speedOfAverageHuman);

		Map<String, Object> response = new HashMap<>();
		response.put("articleId", slugUuid);
		response.put("timeToRead", timeToRead);

		return response;
	}

	public Article getArticle(String slugId) {
		return articleRepository.findBySlug(slugId);
	}

	public void deleteArticle(String slugId) {
		Article article = getArticle(slugId);
		articleRepository.deleteById(article.getId());
	}

	private String createSlug(String str) {
		String words[] = str.toLowerCase().split(" ");
		String sentence = "";

		for (String word : words) {
			sentence += word + "-";
		}
		return sentence.substring(0, sentence.length() - 1);

	}

	private int countWords(String data) {
		int state = OUT;
		int i = 0;
		int wordCount = 0;

		while (i < data.length()) {
			if (data.charAt(i) == ' ' || data.charAt(i) == '\n' || data.charAt(i) == '\t') {
				state = OUT;
			}
			if (state == OUT) {
				state = IN;
				wordCount++;
			}
			++i;
		}

		return wordCount;
	}

}
