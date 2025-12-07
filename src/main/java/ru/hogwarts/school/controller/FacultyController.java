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
        return facultyService.getFaculty(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Факультет с ID: " + id + ", не найден"));
    }

    @GetMapping
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @GetMapping(params = "color")
    public Collection<Faculty> getFacultiesByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Факультет с ID: " + faculty.getId() + ", не найден"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFaculty(@PathVariable Long id) {
        if (!facultyService.deleteFaculty(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Факультет с ID: " + id + ", не найден");
        }
    }
}