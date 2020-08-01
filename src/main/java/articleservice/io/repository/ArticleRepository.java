package articleservice.io.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import articleservice.io.bean.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

	public Article findBySlug(String id);

	public void deleteBySlug(String id);

	Page<Article> findAll(Pageable pageable);

}
