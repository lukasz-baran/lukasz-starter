package com.lukaszbaran.starter.dao.impl;

import com.lukaszbaran.starter.api.Camera;
import com.lukaszbaran.starter.dao.ICameraDAO;
import org.hibernate.HibernateException;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import java.util.List;

public class CameraDAO extends HibernateDaoSupport implements ICameraDAO {

    @Override
    public List<Camera> getAll() throws HibernateException {
        return getHibernateTemplate().loadAll(Camera.class);
    }

}
