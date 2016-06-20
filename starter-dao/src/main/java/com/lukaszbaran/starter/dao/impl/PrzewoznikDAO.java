package com.lukaszbaran.starter.dao.impl;

import com.lukaszbaran.starter.api.Przewoznik;
import com.lukaszbaran.starter.dao.GenericHibernateDAO;
import org.hibernate.SessionFactory;

public class PrzewoznikDAO extends GenericHibernateDAO<Przewoznik> {

    public PrzewoznikDAO(SessionFactory factory) {
        super(factory, Przewoznik.class);
    }
}
