package org.zhengbo.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.zhengbo.backend.model.user.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
