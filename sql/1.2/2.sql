-- Version 1
SELECT
  project_name,
  project_cost
FROM
(
  SELECT
    pms.projects.id,
    SUM(salary) AS project_cost
  FROM
    pms.projects,
    pms.developers,
    pms.projects_developers
  WHERE
    pms.projects.id = project_id
    AND pms.developers.id = developer_id
  GROUP BY
    pms.projects.id
  ORDER BY
    project_cost DESC
  LIMIT 1
) AS project_max_salary,
  pms.projects
WHERE
  pms.projects.id = project_max_salary.id;

-- Version 2
SELECT
  project_name,
  project_cost
FROM
(
  SELECT
    pms.projects.id,
    SUM(salary) AS project_cost
  FROM
    pms.projects
  JOIN
    pms.projects_developers
  ON
    pms.projects.id = project_id
  JOIN
    pms.developers
  ON
    pms.developers.id = developer_id
  GROUP BY
    pms.projects.id
  ORDER BY
    project_cost DESC
  LIMIT 1
) AS project_max_salary
JOIN
  pms.projects
ON
  pms.projects.id = project_max_salary.id