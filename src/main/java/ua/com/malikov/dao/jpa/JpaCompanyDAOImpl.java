package ua.com.malikov.dao.jpa;

import org.slf4j.Logger;
import ua.com.malikov.dao.CompanyDAO;
import ua.com.malikov.model.Company;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class JpaCompanyDAOImpl implements CompanyDAO {

    private JpaUtils jpaUtils;

    public void setJpaUtils(JpaUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    private static final Logger LOG = getLogger(JpaCompanyDAOImpl.class);

    @Override
    public Company save(Company company) {
        return jpaUtils.save(company, LOG);
    }

    @Override
    public void saveAll(List<Company> companies) {
        jpaUtils.saveAll(companies, LOG);
    }

    @Override
    public Company load(int id) {
        return jpaUtils.loadById(Company.class, id, LOG);
    }

    @Override
    public Company load(String name) {
        return jpaUtils.loadByName(name, Company.LOAD_BY_NAME, LOG);
    }

    @Override
    public List<Company> findAll() {
        return jpaUtils.findAll(Company.LOAD_ALL, LOG);
    }

    @Override
    public void deleteById(int id) {
        jpaUtils.deleteById(id, Company.DELETE_BY_ID, LOG);
    }

    @Override
    public void deleteAll() {
        jpaUtils.deleteAll(Company.DELETE_ALL, LOG);
    }
}
