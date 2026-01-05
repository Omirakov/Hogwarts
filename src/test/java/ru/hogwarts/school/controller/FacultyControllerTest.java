package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    @Test
    public void testGetFacultyById() throws Exception {
        Faculty faculty = new Faculty("Gryffindor", "Scarlet");
        faculty.setId(1L);

        when(facultyService.getFaculty(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Scarlet"));

        verify(facultyService, times(1)).getFaculty(1L);
    }

    @Test
    public void testGetFacultyById_NotFound() throws Exception {
        when(facultyService.getFaculty(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/faculties/999"))
                .andExpect(status().isNotFound());

        verify(facultyService, times(1)).getFaculty(999L);
    }

    @Test
    public void testGetAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty("Gryffindor", "Scarlet");
        faculty1.setId(1L);
        Faculty faculty2 = new Faculty("Slytherin", "Green");
        faculty2.setId(2L);

        when(facultyService.getAllFaculties()).thenReturn(Arrays.asList(faculty1, faculty2));

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[1].name").value("Slytherin"));
    }

    @Test
    public void testGetFacultiesByColor() throws Exception {
        Faculty faculty = new Faculty("Gryffindor", "Scarlet");
        faculty.setId(1L);

        when(facultyService.getFacultyByColor("Scarlet")).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculties").param("color", "Scarlet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].color").value("Scarlet"));
    }

    @Test
    public void testGetFacultiesByNameOrColor() throws Exception {
        Faculty faculty = new Faculty("Ravenclaw", "Blue");
        faculty.setId(3L);

        when(facultyService.getFacultiesByNameOrColor("blue")).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculties/filter").param("query", "blue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Ravenclaw"))
                .andExpect(jsonPath("$[0].color").value("Blue"));
    }

    @Test
    public void testGetStudentsByFacultyId() throws Exception {
        Student student1 = new Student("Hermione", 20);
        student1.setId(1L);
        Student student2 = new Student("Ron", 19);
        student2.setId(2L);
        List<Student> students = Arrays.asList(student1, student2);

        when(facultyService.getStudentsByFacultyId(1L)).thenReturn(students);

        mockMvc.perform(get("/faculties/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Hermione"))
                .andExpect(jsonPath("$[1].name").value("Ron"));
    }

    @Test
    public void testAddFaculty() throws Exception {
        Faculty faculty = new Faculty("Hufflepuff", "Yellow");
        faculty.setId(4L);

        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Hufflepuff"))
                .andExpect(jsonPath("$.color").value("Yellow"));

        verify(facultyService, times(1)).addFaculty(any(Faculty.class));
    }

    @Test
    public void testUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty("Updated Faculty", "Purple");
        faculty.setId(1L);

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(put("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Faculty"))
                .andExpect(jsonPath("$.color").value("Purple"));

        verify(facultyService, times(1)).updateFaculty(any(Faculty.class));
    }

    @Test
    public void testUpdateFaculty_NotFound() throws Exception {
        Faculty faculty = new Faculty("Unknown", "Black");
        faculty.setId(999L);

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isNotFound());

        verify(facultyService, times(1)).updateFaculty(any(Faculty.class));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        when(facultyService.deleteFaculty(1L)).thenReturn(Optional.of(new Faculty("Deleted", "Red")));

        mockMvc.perform(delete("/faculties/1"))
                .andExpect(status().isNoContent());

        verify(facultyService, times(1)).deleteFaculty(1L);
    }

    @Test
    public void testDeleteFaculty_NotFound() throws Exception {
        when(facultyService.deleteFaculty(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/faculties/999"))
                .andExpect(status().isNotFound());

        verify(facultyService, times(1)).deleteFaculty(999L);
    }
}