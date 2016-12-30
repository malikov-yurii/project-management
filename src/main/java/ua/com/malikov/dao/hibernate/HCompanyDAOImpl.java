package ua.com.malikov.dao.hibernate;

import org.slf4j.Logger;
import ua.com.malikov.dao.CompanyDAO;
import ua.com.malikov.model.Company;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HCompanyDAOImpl implements CompanyDAO {

    private HUtils hUtils;

    public void sethUtils(HUtils hUtils) {
        this.hUtils = hUtils;
    }

    private static final Logger LOG = getLogger(HCompanyDAOImpl.class);

    @Override
    public Company save(Company company) {
        return hUtils.save(company, LOG);
    }

    @Override
    public void saveAll(List<Company> companies) {
        hUtils.saveAll(companies, LOG);
    }

    @Override
    public Company load(int id) {
        return hUtils.loadById(Company.class, id, LOG);
    }

    @Override
    public Company load(String name) {
        return hUtils.loadByName(name, Company.LOAD_BY_NAME, LOG);
    }

    @Override
    public List<Company> findAll() {
        return hUtils.findAll(Company.LOAD_ALL, LOG);
    }

    @Override
    public void deleteById(int id) {
        hUtils.deleteById(id, Company.DELETE_BY_ID, LOG);
    }

    @Override
    public void deleteAll() {
        hUtils.deleteAll(Company.DELETE_ALL, LOG);
    }
}
