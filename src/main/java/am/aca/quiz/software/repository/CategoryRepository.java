package am.aca.quiz.software.repository;

import am.aca.quiz.software.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    CategoryEntity findCategoryEntityByType(String type);

    CategoryEntity removeById(Long id);
}
