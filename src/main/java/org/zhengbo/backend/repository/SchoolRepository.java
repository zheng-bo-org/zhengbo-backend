package org.zhengbo.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.zhengbo.backend.model.school.School;

public interface SchoolRepository extends CrudRepository<School, Long> {
}
