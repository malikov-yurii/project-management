-- Version 1
SELECT
  skill_name,
  SUM(salary) AS total_salary
FROM
  pms.skills,
  pms.developers,
  pms.developers_skills
WHERE
  pms.developers.id = developer_id
  AND pms.skills.id = skill_id
GROUP BY
  skill_name
HAVING
  skill_name LIKE 'Java';

-- Version 2
SELECT
  skill_name,
  SUM(salary) AS total_salary
FROM
  pms.skills
JOIN
  pms.developers_skills
ON
  pms.skills.id = skill_id
JOIN
  pms.developers
ON
  pms.developers.id = developer_id
GROUP BY
  skill_name
HAVING
  skill_name LIKE 'Java';