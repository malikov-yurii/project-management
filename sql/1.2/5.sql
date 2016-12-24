-- Version 1
WITH customers_profits
AS
(
  SELECT
    pms.companies.id    AS company_id,
    pms.customers.id    AS customer_id,
    SUM(project_profit) AS customer_profit
  FROM
    (
      SELECT
        pms.projects.id      AS id,
        (cost - SUM(salary)) AS project_profit
      FROM
        pms.projects,
        pms.projects_developers,
        pms.developers
      WHERE
        projects.id = project_id
        AND developers.id = developer_id
      GROUP BY projects.id
    ) AS projects_profits,
    pms.projects,
    pms.companies,
    pms.customers
  WHERE
    projects_profits.id = pms.projects.id
    AND company_id = pms.companies.id
    AND customer_id = pms.customers.id
  GROUP BY
    pms.companies.id,
    pms.customers.id
)

SELECT
  company_name,
  customer_name,
  lowest_profit
FROM
(
  SELECT
    company_id,
    MIN(customer_profit) AS lowest_profit
  FROM
    customers_profits
  GROUP BY company_id
) AS lowest_profits,
  pms.companies,
  customers_profits,
  pms.customers
WHERE
  pms.companies.id = lowest_profits.company_id
  AND customer_profit = lowest_profit
  AND customers_profits.customer_id = pms.customers.id;

-- Version 2
WITH customers_profits
AS
(
  SELECT
    pms.companies.id    AS company_id,
    pms.customers.id    AS customer_id,
    SUM(project_profit) AS customer_profit
  FROM
    (
      SELECT
        pms.projects.id      AS id,
        (cost - SUM(salary)) AS project_profit
      FROM
        pms.projects
      JOIN
        pms.projects_developers
      ON
        projects.id = project_id
      JOIN
          pms.developers
      ON
          developers.id = developer_id
      GROUP BY projects.id
    ) AS projects_profits
  JOIN
    pms.projects
  ON
    projects_profits.id = pms.projects.id
  JOIN
    pms.companies
  ON
    company_id = pms.companies.id
  JOIN
    pms.customers
  ON
    customer_id = pms.customers.id
  GROUP BY
    pms.companies.id,
    pms.customers.id
)

SELECT
  company_name,
  customer_name,
  lowest_profit
FROM
  (
    SELECT
      company_id,
      MIN(customer_profit) AS lowest_profit
    FROM
      customers_profits
    GROUP BY company_id
  ) AS lowest_profits
  JOIN
    pms.companies
  ON
    pms.companies.id = lowest_profits.company_id
  JOIN
    customers_profits
  ON
    customer_profit = lowest_profit
  JOIN
    pms.customers
  ON
    customers_profits.customer_id = pms.customers.id;