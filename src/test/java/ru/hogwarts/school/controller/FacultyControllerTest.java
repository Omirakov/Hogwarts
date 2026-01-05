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
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/faculties";
    }

    @Test
    public void testGetFacultyById() {
        Long id = 1L;
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl() + "/" + id, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);
    }

    @Test
    public void testGetFacultyById_NotFound() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl() + "/999", Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetAllFaculties() {
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testGetFacultiesByColor() {
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl() + "?color=Scarlet",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().stream().allMatch(f -> "Scarlet".equalsIgnoreCase(f.getColor())))
                .isTrue();
    }

    @Test
    public void testGetFacultiesByNameOrColor() {
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl() + "/filter?query=blue",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().stream().allMatch(f ->
                "blue".equalsIgnoreCase(f.getName()) || "blue".equalsIgnoreCase(f.getColor())
        )).isTrue();
    }

    @Test
    public void testGetStudentsByFacultyId() {
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl() + "/1/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testAddFaculty() {
        Faculty newFaculty = new Faculty("Hufflepuff", "Yellow");
        HttpEntity<Faculty> request = new HttpEntity<>(newFaculty);

        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl(), request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Hufflepuff");
    }

    @Test
    public void testUpdateFaculty() {
        Long id = 1L;
        Faculty updated = new Faculty("Updated Faculty", "Purple");
        updated.setId(id);
        HttpEntity<Faculty> request = new HttpEntity<>(updated);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl(), HttpMethod.PUT, request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Updated Faculty");
    }

    @Test
    public void testUpdateFaculty_NotFound() {
        Faculty unknown = new Faculty("Unknown", "Black");
        unknown.setId(999L);
        HttpEntity<Faculty> request = new HttpEntity<>(unknown);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl(), HttpMethod.PUT, request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteFaculty() {
        Long id = 1L;
        ResponseEntity<Faculty> getBefore = restTemplate.getForEntity(baseUrl() + "/" + id, Faculty.class);
        assertThat(getBefore.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl() + "/" + id, HttpMethod.DELETE, null, Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Faculty> getAfter = restTemplate.getForEntity(baseUrl() + "/" + id, Faculty.class);
        assertThat(getAfter.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
