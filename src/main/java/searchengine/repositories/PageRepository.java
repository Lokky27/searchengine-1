package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.models.PageEntity;


@Repository
public interface PageRepository extends JpaRepository<PageEntity, Integer> {
}
