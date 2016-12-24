package ua.com.malikov.dao;

import ua.com.malikov.model.Company;

public interface CompanyDAO extends AbstractDAO<Company> {
    Company load(String name);
}
