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
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new Student("John Doe", 20);
        student.setId(1L);

        when(studentService.getStudent(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("John Doe")).andExpect(jsonPath("$.age").value(20));

        verify(studentService, times(1)).getStudent(1L);
    }

    @Test
    public void testGetStudentById_NotFound() throws Exception {
        when(studentService.getStudent(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/999")).andExpect(status().isNotFound());

        verify(studentService, times(1)).getStudent(999L);
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Student student1 = new Student("John Doe", 20);
        student1.setId(1L);
        Student student2 = new Student("Jane Smith", 22);
        student2.setId(2L);

        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].name").value("John Doe")).andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void testGetStudentsByAge() throws Exception {
        Student student = new Student("John Doe", 20);
        student.setId(1L);

        when(studentService.getStudentByAge(20)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/students").param("age", "20")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].age").value(20));

        verify(studentService, times(1)).getStudentByAge(20);
    }

    @Test
    public void testGetStudentsByAgeBetween() throws Exception {
        Student student = new Student("John Doe", 20);
        student.setId(1L);

        when(studentService.getStudentsByAgeBetween(18, 25)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/students/age").param("min", "18").param("max", "25")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].age").value(20));

        verify(studentService, times(1)).getStudentsByAgeBetween(18, 25);
    }

    @Test
    public void testGetFacultyByStudentId() throws Exception {
        Faculty faculty = new Faculty("Gryffindor", "Scarlet");
        faculty.setId(1L);

        Student student = new Student("Harry Potter", 11);
        student.setId(1L);
        student.setFaculty(faculty);

        when(studentService.getStudent(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/1/faculty")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Gryffindor")).andExpect(jsonPath("$.color").value("Scarlet"));

        verify(studentService, times(1)).getFacultyByStudentId(1L);
    }

    @Test
    public void testAddStudent() throws Exception {
        Student student = new Student("New Student", 22);
        student.setId(1L);

        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student))).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("New Student"));

        verify(studentService, times(1)).addStudent(any(Student.class));
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student("Updated Name", 23);
        student.setId(1L);

        when(studentService.updateStudent(any(Student.class))).thenReturn(Optional.of(student));

        mockMvc.perform(put("/students").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student))).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Updated Name"));

        verify(studentService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    public void testUpdateStudent_NotFound() throws Exception {
        Student student = new Student("Unknown", 20);
        student.setId(999L);

        when(studentService.updateStudent(any(Student.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/students").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student))).andExpect(status().isNotFound());

        verify(studentService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        when(studentService.deleteStudent(1L)).thenReturn(Optional.of(new Student("Deleted", 20)));

        mockMvc.perform(delete("/students/1")).andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    public void testDeleteStudent_NotFound() throws Exception {
        when(studentService.deleteStudent(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/students/999")).andExpect(status().isNotFound());

        verify(studentService, times(1)).deleteStudent(999L);
    }

    @Test
    public void testGetTotalStudentsCount() throws Exception {
        when(studentService.getTotalStudentsCount()).thenReturn(100L);

        mockMvc.perform(get("/students/total")).andExpect(status().isOk()).andExpect(content().string("100"));
    }

    @Test
    public void testGetAverageAge() throws Exception {
        when(studentService.getAverageAge()).thenReturn(20.5);

        mockMvc.perform(get("/students/average-age")).andExpect(status().isOk()).andExpect(content().string("20.5"));
    }

    @Test
    public void testGetLastFiveStudents() throws Exception {
        Student s1 = new Student("Last", 20);
        s1.setId(100L);
        when(studentService.getTop5Students()).thenReturn(Arrays.asList(s1));

        mockMvc.perform(get("/students/last-five")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].name").value("Last"));
    }
}