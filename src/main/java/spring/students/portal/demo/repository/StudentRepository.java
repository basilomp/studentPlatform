package spring.students.portal.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spring.students.portal.demo.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
