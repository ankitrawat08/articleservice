package io.ankit;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import articleservice.io.controller.ArticleController;
import articleservice.io.controller.request.ArticleRequest;

public class ArticleServiceTest {

	ArticleController articleController;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testcreateArticleTest_WhenDescriptionIsNull() throws JSONException {
		articleController = new ArticleController();
		thrown.expect(Exception.class);
		thrown.expectMessage(startsWith("Missing Article description"));
		ArticleRequest request = createArticleRequest();
		request.setDescription(null);
		assertFalse(articleController.validateCreateRequest(request));
	}

	@Test
	public void testcreateArticleTestWhenTitleIsNull() throws JSONException {
		articleController = new ArticleController();
		thrown.expect(Exception.class);
		thrown.expectMessage(startsWith("Missing Article description"));
		ArticleRequest request = createArticleRequest();
		request.setTitle(null);
		assertFalse(articleController.validateCreateRequest(request));
	}

	@Test
	public void testcreateArticleTestWhenBodyNull() throws JSONException {
		articleController = new ArticleController();
		thrown.expect(Exception.class);
		thrown.expectMessage(startsWith("Missing Article description"));
		ArticleRequest request = createArticleRequest();
		request.setBody(null);
		assertFalse(articleController.validateCreateRequest(request));
	}

	@Test
	public void testcreateArticleTestWhenTagsIsNull() throws JSONException {
		articleController = new ArticleController();
		thrown.expect(Exception.class);
		thrown.expectMessage(startsWith("Missing Article description"));
		ArticleRequest request = createArticleRequest();
		assertFalse(articleController.validateCreateRequest(request));
	}

	public ArticleRequest createArticleRequest() throws JSONException {
		ArticleRequest request = new ArticleRequest();
		Set<String> set = new HashSet<String>();

		request.setId("1");
		request.setDescription("Ever wonder how?");
		request.setTitle("How to learn Advance Spring Boot");
		request.setSlug("How to learn Advance Spring Boot");
		request.setBody(
				"Spring Boot is an opinionated, convention-over-configuration focused addition to the Spring platform – highly useful to get started with minimum effort and create stand-alone, production-grade applications. Spring Boot made configuring Spring easier with its auto-configuration feature. Dependency management is a critical aspects of any complex project. And doing this manually is less than ideal; the more time you spent on it the less time you have on the other important aspects of the project. Spring Boot starters were built to address exactly this problem. Starter POMs are a set of convenient dependency descriptors that you can include in your application. You get a one-stop-shop for all the Spring and related technology that you need, without having to hunt through sample code and copy-paste loads of dependency descriptors.");
		request.setTags(set);
		return request;

	}

}
