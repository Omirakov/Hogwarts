package ru.hogwarts.school.model;

import java.util.Objects;

public class Faculty {

    private Long id;
    private String name;
    private String color;

    public Faculty(Long id, String name, String color) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID не может быть меньше или равен нулю, а также null");
        }
        this.id = id;
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть null или пустой строкой");
        }
        this.name = name;
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("Цвет не может быть null или пустой строкой");
        }
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID не может быть меньше или равен нулю, а также null");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть null или пустой строкой");
        }
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("Цвет не может быть null или пустой строкой");
        }
        this.color = color;
    }

    @Override
    public String toString() {
        return "Faculty{" + "id=" + id + ", name='" + name + '\'' + ", color='" + color + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Faculty faculty)) return false;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) && Objects.equals(color, faculty.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}