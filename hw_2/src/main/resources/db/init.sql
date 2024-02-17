CREATE TABLE IF NOT EXISTS department
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS project
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS employee
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255),
    department_id BIGINT,
    FOREIGN KEY (department_id) REFERENCES department (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS employee_project
(
    employee_id BIGINT,
    project_id  BIGINT,
    PRIMARY KEY (employee_id, project_id),
    FOREIGN KEY (employee_id) REFERENCES employee (id),
    FOREIGN KEY (project_id) REFERENCES project (id)
);