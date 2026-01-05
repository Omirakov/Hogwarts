package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/students";
    }

    @Test
    public void testGetStudentById() {
        Long id = 1L;
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl() + "/" + id, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);
    }

    @Test
    public void testGetAllStudents() {
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testGetStudentsByAge() {
        int age = 20;
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl() + "?age=" + age,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().stream().allMatch(s -> s.getAge() == age)).isTrue();
    }

    @Test
    public void testGetStudentsByAgeBetween() {
        int min = 18, max = 25;
        String url = baseUrl() + "/age?min=" + min + "&max=" + max;
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().stream().allMatch(s -> s.getAge() >= min && s.getAge() <= max)).isTrue();
    }

    @Test
    public void testGetFacultyByStudentId() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl() + "/1/faculty", Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testAddStudent() {
        Student newStudent = new Student("New Student", 22);
        HttpEntity<Student> request = new HttpEntity<>(newStudent);

        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl(), request, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Student");
    }

    @Test
    public void testUpdateStudent() {
        Long id = 1L;
        ResponseEntity<Student> getBefore = restTemplate.getForEntity(baseUrl() + "/" + id, Student.class);
        assertThat(getBefore.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student updated = getBefore.getBody();

        assert updated != null;
        updated.setName("Updated Name");

        HttpEntity<Student> request = new HttpEntity<>(updated);
        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl(), HttpMethod.PUT, request, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
    }

    @Test
    public void testDeleteStudent() {
        Long id = 1L;

        ResponseEntity<Student> getBefore = restTemplate.getForEntity(baseUrl() + "/" + id, Student.class);
        assertThat(getBefore.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl() + "/" + id, HttpMethod.DELETE, null, Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Student> getAfter = restTemplate.getForEntity(baseUrl() + "/" + id, Student.class);
        assertThat(getAfter.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}