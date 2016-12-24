package ua.com.malikov.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import ua.com.malikov.model.NamedEntity;

import java.util.List;

class HUtils {

    static <T extends NamedEntity> T save(T t, Session session, Logger LOG) {
        boolean isNew = t.isNew();
        session.save(t);
        LOG.info(t + " was " + (isNew ? "created" : "updated") + " successfully.");
        return t;
    }

    static <T extends NamedEntity> void saveAll(List<T> list, Session session, Logger LOG) {
        list.forEach(session::save);
        LOG.info("All entities of " + list.getClass().getSuperclass().getSimpleName() + " were saved successfully.");
    }

    static <T extends NamedEntity> T loadById(Class<T> tClass, int id, Session session, Logger LOG) {

        T t = session.load(tClass, id);
        if (t == null) {
            LOG.error("Cannot find " + tClass.getSuperclass().getSimpleName() + " by id ID: " + id);
        } else {
            LOG.info(t + " successfully loaded from DB by ID.");
        }
        return t;
    }

    public static <T extends NamedEntity> List<T> findAll(String hql, Session session, Logger LOG) {
        List<T> resultList = (List<T>) session.createQuery(hql).list();
        if (resultList == null) {
            LOG.error("Search for all entities has failed.");
        } else {
            LOG.info("Search for all entities of " +
                    resultList.getClass().getSuperclass().getSimpleName().toLowerCase() + " has been successful.");
        }
        return resultList;
    }

    public static <T extends NamedEntity> void delete(T t, Session session, Logger LOG) {
        session.delete(t);
        LOG.info("Entity " + t.getClass().getSimpleName().toLowerCase() + " has been deleted successfully.");
    }

    public static <T extends NamedEntity> void deleteById(int id, String hsql, Session session, Logger LOG) {
        Query query = session.createQuery(hsql);
        query.setParameter("id", id);
        if (query.executeUpdate() == 1) {
            LOG.info("Deleting entity by ID has been successful.");
        } else {
            LOG.error("Deleting entity by ID has been failed.");
        }
    }

    public static <T extends NamedEntity> void deleteAll(String hsql, Session session, Logger LOG) {
        Query query = session.createQuery(hsql);
        if (query.executeUpdate() != 0) {
            LOG.info("Deleting of all entities has been successful.");
        } else {
            LOG.error("Deleting of all entities has failed.");
        }
    }

    public static <T extends NamedEntity> T loadByName(String name, String hql, Session session, Logger LOG) {
        Query query = session.createQuery(hql);
        query.setParameter("name", name);
        T t = (T) query.uniqueResult();
        if (t == null) {
            LOG.error("Cannot find entity by name: " + name);
        } else {
            LOG.error("Entity" + t.getClass().getSimpleName().toLowerCase() + " has been successfully found by name: " + name);
        }
        return t;
    }
}
