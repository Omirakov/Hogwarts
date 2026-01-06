package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> getStudent(Long id) {
        return studentRepository.findById(id);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getStudentsByAgeBetween(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min не может быть больше max");
        }
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        return studentRepository.findById(studentId).map(Student::getFaculty).orElseThrow(() -> new IllegalArgumentException("Студент с ID " + studentId + " не найден"));
    }

    public Optional<Student> updateStudent(Student student) {
        if (student.getId() == null) {
            return Optional.empty();
        }
        if (!studentRepository.existsById(student.getId())) {
            return Optional.empty();
        }
        Student updated = studentRepository.save(student);
        return Optional.of(updated);
    }

    public Optional<Student> deleteStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
        }
        return student;
    }

    public Long getTotalStudentsCount() {
        return studentRepository.countAllStudents();
    }

    public double getAverageAge() {
        return Optional.ofNullable(studentRepository.getAverageAge()).orElse(0.0);
    }

    public List<Student> getTop5Students() {
        return studentRepository.findTop5ByOrderByIdDesc();
    }
}
