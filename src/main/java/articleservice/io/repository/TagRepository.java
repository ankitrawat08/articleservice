package articleservice.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import articleservice.io.bean.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

}
