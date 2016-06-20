package com.lukaszbaran.starter.dao.impl;

import com.lukaszbaran.starter.api.Camera;
import com.lukaszbaran.starter.dao.GenericHibernateDAO;
import com.lukaszbaran.starter.dao.ICameraDAO;
import org.hibernate.SessionFactory;

public class CameraDAO extends GenericHibernateDAO<Camera> implements ICameraDAO {

    public CameraDAO(SessionFactory factory) {
        super(factory, Camera.class);
    }
}
