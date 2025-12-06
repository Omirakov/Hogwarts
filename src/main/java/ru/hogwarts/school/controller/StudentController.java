package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Студент с ID: " + id + ", не найден"));
    }

    @GetMapping("/all")
    public Collection<Student> getAllStudents() {
        return studentService.getAll();
    }

    @GetMapping(params = "age")
    public Collection<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.getByAge(age);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@RequestBody Student student) {
        return studentService.add(student);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentService.update(student).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Студент с ID: " + student.getId() + ", не найден"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        if (!studentService.delete(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Студент с ID: " + id + ", не найден");
        }
    }
}