package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculties")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Факультет с ID: " + id + ", не найден"));
    }

    @GetMapping("/all")
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAll();
    }

    @GetMapping(params = "color")
    public Collection<Faculty> getFacultiesByColor(@RequestParam String color) {
        return facultyService.getByColor(color);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.add(faculty);
    }

    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.update(faculty).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Факультет с ID: " + faculty.getId() + ", не найден"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFaculty(@PathVariable Long id) {
        if (!facultyService.delete(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Факультет с ID: " + id + ", не найден");
        }
    }
}