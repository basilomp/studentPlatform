package spring.students.portal.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

//    @GetMapping("/add")
//    public String showSignUpForm(Student student) {
//        return "add-student";
//    }


    @GetMapping("edit/{id}")
    public String showUpdateForm(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "index";
    }

    @PostMapping("list/add")
    public String addStudent(@Valid Student student, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(student);
            return "add-student";
        }

        studentRepository.save(student);
        return "redirect:list";
    }


//    @RequestMapping(value = "/index")
//    public String hello(Model model) {
//        model.addAttribute("students", studentRepository.findAll());
//        return "index";
//    }


//    @GetMapping("/students")
//    public List<Student> getAllStudents() {
//        return studentRepository.findAll();
//    }

    @GetMapping("/{id}")
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

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
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
