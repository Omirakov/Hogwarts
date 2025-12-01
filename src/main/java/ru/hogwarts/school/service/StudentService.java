package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final HashMap<Long, Student> studentHashMap = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Student add(Student student) {
        long id = nextId.getAndIncrement();
        student.setId(id);
        studentHashMap.put(id, student);
        return student;
    }

    public Optional<Student> get(Long id) {
        return Optional.ofNullable(studentHashMap.get(id));
    }

    public Collection<Student> getAll() {
        return studentHashMap.values();
    }

    public Collection<Student> getByAge(int age) {
        return studentHashMap.values().stream().filter(student -> student.getAge() == age).collect(Collectors.toList());
    }

    public Optional<Student> update(Student student) {
        if (studentHashMap.containsKey(student.getId())) {
            studentHashMap.put(student.getId(), student);
            return Optional.of(student);
        }
        return Optional.empty();
    }

    public Optional<Student> delete(Long id) {
        Student removedStudent = studentHashMap.remove(id);
        return Optional.ofNullable(removedStudent);
    }
}
