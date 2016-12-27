package ua.com.malikov.dao.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.CompanyDAO;
import ua.com.malikov.model.Company;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HCompanyDAOImpl implements CompanyDAO {

    private EntityManagerFactory emf;

    private HUtils hUtils;

    public void sethUtils(HUtils hUtils) {
        this.hUtils = hUtils;
    }

    private static final Logger LOG = getLogger(HCompanyDAOImpl.class);

    @Override
    @Transactional
    public Company save(Company company) {
        return hUtils.save(company, LOG);
    }

    @Override
    @Transactional
    public void saveAll(List<Company> companies) {
        hUtils.saveAll(companies, LOG);
    }

    @Override
    @Transactional
    public Company load(int id) {
        return hUtils.loadById(Company.class, id, LOG);
    }

    @Override
    @Transactional
    public Company load(String name) {
        return hUtils.loadByName(name, Company.LOAD_BY_LAST_NAME, LOG);
    }

    @Override
    @Transactional
    public List<Company> findAll() {
        return HUtils.findAll(
                "from Company", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        HUtils.deleteById(id, "delete Company where id=:id", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void deleteAll() {
        HUtils.deleteAll("delete Company", sessionFactory.getCurrentSession(), LOG);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
