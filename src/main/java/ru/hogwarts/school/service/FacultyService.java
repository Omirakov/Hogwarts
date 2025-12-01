package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final HashMap<Long, Faculty> facultyHashMap = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Faculty add(Faculty faculty) {
        long id = nextId.getAndIncrement();
        faculty.setId(id);
        facultyHashMap.put(id, faculty);
        return faculty;
    }

    public Optional<Faculty> get(Long id) {
        return Optional.ofNullable(facultyHashMap.get(id));
    }

    public Collection<Faculty> getAll() {
        return facultyHashMap.values();
    }

    public Collection<Faculty> getByColor(String color) {
        return facultyHashMap.values().stream().filter(faculty -> faculty.getColor().equalsIgnoreCase(color)).collect(Collectors.toList());
    }

    public Optional<Faculty> update(Faculty faculty) {
        if (facultyHashMap.containsKey(faculty.getId())) {
            facultyHashMap.put(faculty.getId(), faculty);
            return Optional.of(faculty);
        }
        return Optional.empty();
    }

    public Optional<Faculty> delete(Long id) {
        Faculty removedFaculty = facultyHashMap.remove(id);
        return Optional.ofNullable(removedFaculty);
    }
}