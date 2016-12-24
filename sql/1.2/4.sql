ALTER TABLE
  pms.projects
ADD
  cost numeric(10,2);

UPDATE
  pms.projects
SET
  cost = 500000
WHERE
  id = 1;
UPDATE
  pms.projects
SET
  cost = 550000
WHERE
  id = 2;
UPDATE
  pms.projects
SET
  cost = 600000
WHERE
  id = 3;
UPDATE
  pms.projects
SET
  cost = 650000
WHERE
  id = 4;
UPDATE
  pms.projects
SET
  cost = 700000
WHERE
  id = 5;