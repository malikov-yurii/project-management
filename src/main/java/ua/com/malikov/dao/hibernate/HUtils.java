package ua.com.malikov.dao.hibernate;

import org.slf4j.Logger;
import ua.com.malikov.model.NamedEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

class HUtils {

    private EntityManagerFactory emf;

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    <T extends NamedEntity> T save(T t, Logger LOG) {
        boolean isNew = t.isNew();
        if (isNew) {
            getEntityManager().persist(t);
        } else {
            getEntityManager().merge(t);
        }
        LOG.info(t + " was " + (isNew ? "created" : "updated") + " successfully.");
        return t;
    }

    <T extends NamedEntity> void saveAll(List<T> list, Logger LOG) {
        list.forEach(getEntityManager()::persist);
        LOG.info("All entities of " + list.getClass().getSuperclass().getSimpleName() + " were saved successfully.");
    }

    <T extends NamedEntity> T loadById(Class<T> tClass, int id, Logger LOG) {
        T t = getEntityManager().find(tClass, id);
        if (t == null) {
            LOG.error("Cannot find " + tClass.getSuperclass().getSimpleName() + " by id ID: " + id);
        } else {
            LOG.info(t + " successfully loaded from DB by ID.");
        }
        return t;
    }

    public <T extends NamedEntity> List<T> findAll(String hql, Logger LOG) {
        List<T> resultList = (List<T>) getEntityManager().createQuery(hql).getResultList();
        if (resultList == null) {
            LOG.error("Search for all entities has failed.");
        } else {
            LOG.info("Search for all entities of " +
                    resultList.getClass().getSuperclass().getSimpleName().toLowerCase() + " has been successful.");
        }
        return resultList;
    }

    public <T extends NamedEntity> void deleteById(int id, String hql, Logger LOG) {
        Query query = getEntityManager().createNamedQuery(hql);
        query.setParameter("id", id);
        if (query.executeUpdate() == 1) {
            LOG.info("Deleting entity by ID has been successful.");
        } else {
            LOG.error("Deleting entity by ID has been failed.");
        }
    }

    public <T extends NamedEntity> void deleteAll(String hql, Logger LOG) {
        Query query = getEntityManager().createNamedQuery(hql);
        if (query.executeUpdate() != 0) {
            LOG.info("Deleting of all entities has been successful.");
        } else {
            LOG.error("Deleting of all entities has failed.");
        }
    }

    public <T extends NamedEntity> T loadByName(String name, String hql, Logger LOG) {
        Query query = getEntityManager().createNamedQuery(hql);
        query.setParameter("name", name);
        T t = (T) query.getSingleResult();
        if (t == null) {
            LOG.error("Cannot find entity by name: " + name);
        } else {
            LOG.error("Entity" + t.getClass().getSimpleName().toLowerCase() + " has been successfully found by name: " + name);
        }
        return t;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
