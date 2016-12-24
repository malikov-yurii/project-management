-- Version 1
SELECT
  project_name,
  project_profit,
  avg_salary
FROM
(
  SELECT
    pms.projects.id,
    (cost - SUM(salary)) AS project_profit,
    AVG(salary)          AS avg_salary
  FROM
    pms.projects,
    pms.projects_developers,
    pms.developers
  WHERE
    pms.projects.id = project_id
    AND pms.developers.id = developer_id
  GROUP BY
    pms.projects.id
  ORDER BY
    project_profit
  LIMIT 1
) AS project_min_profit,
  pms.projects
WHERE
  pms.projects.id = project_min_profit.id;

-- Version 2
SELECT
  project_name,
  project_profit,
  avg_salary
FROM
  (
    SELECT
      pms.projects.id,
      (cost - SUM(salary)) AS project_profit,
      AVG(salary)          AS avg_salary
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
      project_profit
    LIMIT 1
  ) AS project_min_profit
JOIN
  pms.projects
ON
  pms.projects.id = project_min_profit.id;