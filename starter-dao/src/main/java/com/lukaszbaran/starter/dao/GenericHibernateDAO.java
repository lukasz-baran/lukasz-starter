package com.lukaszbaran.starter.dao;


import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class GenericHibernateDAO<E> implements HibernateDAO<E> {
    private static final Logger LOGGER = Logger.getLogger(GenericHibernateDAO.class);

    private SessionFactory sessionFactory;
    private Class<E> entityClass;

    public GenericHibernateDAO(SessionFactory sessionFactory, Class<E> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    protected Session getSession() throws HibernateException {
        return sessionFactory.getCurrentSession();
    }

    protected Criteria getCriteria() throws HibernateException {
        return getSession().createCriteria(entityClass);
    }

    @Override
    public E load(Long id) throws HibernateException {
        if (id == null)
            return null;
        return (E) getSession().get(entityClass, id);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public E save(E e) throws HibernateException {
        LOGGER.debug("LOGGER - saving object");
        getSession().saveOrUpdate(e);
        LOGGER.debug("LOGGER - object saved");
        return e;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public void saveAll(List<E> list) throws HibernateException {
        if (list != null && list.size() > 0) {
            for (E e : list) {
                save(e);
            }
        }
    }

    @Override
    public void delete(E e) throws HibernateException {
        Session session = getSession();
        session.refresh(e);
        session.delete(e);
    }

    @Override
    public List<E> getAll() throws HibernateException {
        return getCriteria().list();
    }
}

