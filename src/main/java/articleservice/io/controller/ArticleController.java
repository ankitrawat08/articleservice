package articleservice.io.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import articleservice.io.bean.Article;
import articleservice.io.controller.request.ArticleRequest;
import articleservice.io.repository.ArticleRepository;
import articleservice.io.service.ArticleService;
import articleservice.io.service.TagService;
import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

@RestController
public class ArticleController {

	Logger log = LoggerFactory.getLogger(ArticleController.class);

	@Autowired
	ArticleService articleService;

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	TagService tagService;

	Article article;

	// create an article
	@RequestMapping(value = "/api/articles", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Article> createArticle(@RequestBody ArticleRequest request) {
		List<String> errors = validateCreateRequest(request);
		try {
			if (errors.size() == 0) {
				article = articleService.createArticle(request);
			} else
				return new ResponseEntity<Article>(HttpStatus.BAD_REQUEST);

			return ResponseEntity.status(HttpStatus.CREATED).body(article);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Article>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Get List of articles
	@RequestMapping(value = "/api/articles", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllArticles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		try {
			return new ResponseEntity<>(articleService.getAllArticles(page, size), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// Get an article based on slug-id
	@RequestMapping(value = "/api/articles/{slug-uuid}", method = RequestMethod.GET)
	public ResponseEntity<Article> getArticle(@PathVariable(value = "slug-uuid") String slugUuid) throws Exception {
		List<String> errors = validate(slugUuid);
		if (errors.size() == 0) {
			article = articleService.getArticle(slugUuid);
		} else {
			return new ResponseEntity<Article>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.status(HttpStatus.OK).body(article);
	}

	// Get Tag based metrics
	@RequestMapping(value = "/api/articles/tags", method = RequestMethod.GET)
	public ResponseEntity<String> getTags() throws JSONException {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(tagService.getTagCount());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	// Update an article based on slug-id
	@RequestMapping(value = "/api/articles/{slug-uuid}", method = RequestMethod.PATCH)
	public ResponseEntity<Article> updateArticle(@PathVariable(value = "slug-uuid") String slugUuid,
			@RequestBody ArticleRequest articles) {
		List<String> errors = validateUpdateRequest(articles);
		if (errors.size() == 0) {
			article = articleService.updateArticle(articles.getTitle(), slugUuid);
		} else {
			return new ResponseEntity<Article>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.status(HttpStatus.OK).body(article);
	}

	// Delete an article based on slug-id
	@RequestMapping(value = "/api/articles/{slug-uuid}", method = RequestMethod.DELETE)
	public void deleteArticle(@PathVariable(value = "slug-uuid") String slugUuid) {
		List<String> errors = validate(slugUuid);
		if (errors.size() == 0) {
			articleService.deleteArticle(slugUuid);
		} else {
			ResponseEntity.status(HttpStatus.BAD_REQUEST);
		}
		ResponseEntity.status(HttpStatus.NO_CONTENT);
	}

	// Get time to read for an article
	@RequestMapping(value = "/api/articles/{slug-uuid}/timetoread", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getReadTime(@PathVariable(value = "slug-uuid") String slugUuid) {
		try {
			return new ResponseEntity<>(articleService.getReadTime(slugUuid), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	public List<String> validateCreateRequest(ArticleRequest articleRequest) {
		List<String> errors = new ArrayList<>();

		if (articleRequest.getDescription() == null || articleRequest.getDescription().length() == 0) {
			errors.add("Missing Article description");
		}
		if (articleRequest.getTitle() == null || articleRequest.getTitle().length() == 0) {
			errors.add("Missing Article title");
		}
		if (articleRequest.getBody() == null || articleRequest.getBody().length() == 0) {
			errors.add("Missing Article body");
		}
		if (articleRequest.getTags() == null || articleRequest.getTags().size() == 0) {
			errors.add("Missing Article tags");
		}

		if (validateBody(articleRequest))
			errors.add("Article body already exists");

		return errors;
	}

	public List<String> validateUpdateRequest(ArticleRequest articleRequest) {
		List<String> errors = new ArrayList<>();

		if (articleRequest.getTitle() == null || articleRequest.getTitle().length() == 0) {
			errors.add("Missing Article Body");
		}
		if (validateBody(articleRequest))
			errors.add("Article body already exists");

		return errors;
	}

	public List<String> validate(String id) {
		List<String> errors = new ArrayList<>();

		if (id == null || id.length() == 0) {
			errors.add("Missing Article SlugId");
		}
		return errors;
	}

	public Boolean validateBody(ArticleRequest articleRequest) {

		List<Article> articles = articleRepository.findAll();
		SimilarityStrategy strategy = new JaroWinklerStrategy();
		StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
		String target = articleRequest.getBody();
		for (Article article : articles) {
			String source = article.getBody();
			if (service.score(source, target) > 0.70)
				return true;
		}

		return false;
	}

}
