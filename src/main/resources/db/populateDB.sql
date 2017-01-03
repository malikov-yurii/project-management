DELETE FROM pms.companies;
DELETE FROM pms.customers;
DELETE FROM pms.projects;
DELETE FROM pms.developers;
DELETE FROM pms.skills;
DELETE FROM pms.developers_skills;
DELETE FROM pms.projects_developers;

ALTER SEQUENCE pms.projects_id_seq RESTART WITH 1;
ALTER SEQUENCE pms.customers_id_seq RESTART WITH 1;
ALTER SEQUENCE pms.developers_id_seq RESTART WITH 1;
ALTER SEQUENCE pms.companies_id_seq RESTART WITH 1;
ALTER SEQUENCE pms.skills_id_seq RESTART WITH 1;

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
		(company_id,customer_id,project_name,cost)
	VALUES 
		(1,1,'Financial Software',100000),
		(2,3,'Ticketing Software',200000),
		(5,2,'Website Project',300000),
		(5,3,'CMS Software',400000),
		(2,1,'Website Architecture',500000);

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