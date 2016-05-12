package com.lukaszbaran.starter.dao;

import org.hibernate.HibernateException;

import java.util.List;

public interface HibernateDAO<E> {
    E load(Long id) throws HibernateException;
    E save(E e) throws HibernateException;
    void saveAll(List<E> list) throws HibernateException;
    void delete(E e) throws HibernateException;
    List<E> getAll() throws HibernateException;
}
