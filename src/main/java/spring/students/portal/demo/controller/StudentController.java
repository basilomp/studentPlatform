package spring.students.portal.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.students.portal.demo.exception.ResourceNotFoundException;
import spring.students.portal.demo.model.Student;
import spring.students.portal.demo.repository.StudentRepository;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping()
public class StudentController {

    @Autowired
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


//    @GetMapping("students/list")
//    public String showUpdateForm(Model model) {
//        model.addAttribute("students", studentRepository.findAll());
//        return "index";
//    }

    @GetMapping()
    public String hello(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "Hello";
    }


    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") Integer studentId)
        throws ResourceNotFoundException {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Failed to find a student for the id :: " + studentId));
                    return ResponseEntity.ok().body(student);
    }

    @PostMapping("/students")
    public Student createStudent(@Valid @RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PutMapping("students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") Integer studentId,
                                                 @Valid @RequestBody Student studentDetails) throws ResourceNotFoundException {
        Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Failed to find a student for the id :: " + studentId));

        student.setEmail(studentDetails.getEmail());
        student.setName(studentDetails.getName());
        student.setPhone(studentDetails.getPhone());

        final Student updatedStudent = studentRepository.save(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("students/{id}")
    public Map<String, Boolean> deleteStudent(@PathVariable(value = "id") Integer studentId)
        throws ResourceNotFoundException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Failed to find a student for this id :: " + studentId));

        studentRepository.delete(student);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
