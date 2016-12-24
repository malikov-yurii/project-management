INSERT INTO pms.companies
		(company_name)
	VALUES 
		('Ciklum'),
		('EPAM'),
		('GlobalLogic'),
		('Luxoft'),
		('SoftServe');

INSERT INTO pms.customers 
		(customer_name)
	VALUES 
		('City Bank'),
		('Rozetka'),
		('Ukrzaliznytsya');

INSERT INTO pms.projects 
		(company_id,customer_id,project_name)
	VALUES 
		(1,1,'Financial Software'),
		(2,3,'Ticketing Software'),
		(5,2,'Website Project'),
		(5,3,'CMS Software'),
		(2,1,'Website Architecture');

INSERT INTO pms.developers
(first_name,last_name,company_id)
VALUES
	('Mykhailo','Kosinskyi',1),
	('Vladyslav','Len''',2),
	('Yurii','Malikov',2),
	('Mykhailo','Senchuk',1),
	('Oleg','Volkov',5);

INSERT INTO pms.skills
(skill_name)
VALUES
	('Java'),
	('SQL'),
	('Spring'),
	('Junit'),
	('Maven');

INSERT INTO pms.developers_skills 
		(developer_id,skill_id) 
	VALUES 
		(1,1), 
		(1,2), 
		(1,5), 
		(2,1), 
		(2,3), 
		(3,1), 
		(3,4), 
		(4,1), 
		(4,3), 
		(4,5), 
		(5,1), 
		(5,2), 
		(5,3), 
		(5,4), 
		(5,5);

INSERT INTO pms.projects_developers 
		(project_id,developer_id) 
	VALUES 
		(1,1), 
		(1,4),
		(2,2), 
		(2,3),
		(3,5),
		(4,5), 
		(5,3);

-- Added from 1.2 --
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