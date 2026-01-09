ALTER TABLE student
    ADD CONSTRAINT chk_student_age CHECK (age >= 16);

ALTER TABLE student
    ADD CONSTRAINT uk_student_name UNIQUE (name);

ALTER TABLE student
    ADD CONSTRAINT chk_student_name_not_empty
    CHECK (TRIM(name) <> '' OR name IS NOT NULL);

ALTER TABLE faculty
    ADD CONSTRAINT uk_faculty_name_color UNIQUE (name, color);

ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;


