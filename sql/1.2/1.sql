ALTER TABLE
  pms.developers
ADD
  salary numeric(10,2);

UPDATE
  pms.developers
SET
  salary = 10000
WHERE id = 1;
UPDATE
  pms.developers
SET
  salary = 20000
WHERE id = 2;
UPDATE
  pms.developers
SET
  salary = 30000
WHERE
  id = 3;
UPDATE
  pms.developers
SET
  salary = 40000
WHERE
  id = 4;
UPDATE
  pms.developers
SET
  salary = 50000
WHERE id = 5;