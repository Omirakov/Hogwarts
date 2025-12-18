select *
from student s
where s.age > 10 and s.age < 20

select name
from student s

select  *
from  student
where name ilike '%o%';

select *
from student s
where s.age < 20

select  *
from  student
order by age asc;

select s.id AS student_id, s.name AS student_name, s.age, f.id AS faculty_id, f.name AS faculty_name, f.color AS faculty_color
from student s join faculty f on s.faculty_id = f.id
where s.id = 3;

select  *
from student
where faculty_id = 2;
