package ua.com.malikov.dao.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import ua.com.malikov.dao.CompanyDAO;
import ua.com.malikov.model.Company;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HCompanyDAOImpl implements CompanyDAO {

    private SessionFactory sessionFactory;

    private static final Logger LOG = getLogger(HCompanyDAOImpl.class);

    @Override
    public Company save(Company company) {
        return HUtils.save(company, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void saveAll(List<Company> companies) {
        HUtils.saveAll(companies, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public Company load(int id) {
        return HUtils.loadById(Company.class, id, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public Company load(String name) {
        return HUtils.loadByName(name, "from Company where name like :name"
                , sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public List<Company> findAll() {
        return HUtils.findAll(
                "from Company", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void delete(Company company) {
        HUtils.delete(company, sessionFactory.getCurrentSession(), LOG);

    }

    @Override
    public void deleteById(int id) {
        HUtils.deleteById(id, "delete Company where id=:id", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void deleteAll() {
        HUtils.deleteAll("delete Company", sessionFactory.getCurrentSession(), LOG);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
