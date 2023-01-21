package searchengine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.models.SiteEntity;

public interface SiteRepository extends JpaRepository<SiteEntity, Integer> {
}
