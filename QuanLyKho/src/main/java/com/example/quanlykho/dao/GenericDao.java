package com.example.quanlykho.dao;

import com.example.quanlykho.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenericDao<T, ID> {

    private final Class<T> type;

    public GenericDao(Class<T> type) {
        this.type = type;
    }

    public void save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.getMessage();
        }
    }

    public T update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T updatedEntity = (T) session.merge(entity);
            transaction.commit();
            return updatedEntity;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(type, id);
        }
    }

    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from " + type.getName(), type).list();
        }
    }

    public List<T> findWithQuery(String hql, Object... params) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery(hql, type);
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
            return query.list();
        }
    }

    public List<Object[]> executeNativeQuery(String sql, Object... params) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session.createNativeQuery(sql, Object[].class);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i + 1, params[i]);
                }
            }
            return query.getResultList();
        }
    }

    public Object findSingleResult(String hql, Object... params) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<?> query = session.createQuery(hql, Object.class);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i + 1, params[i]);
                }
            }
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <R> List<R> executeNativeQueryToDTO(String sql, Class<R> resultType, Object... params) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session.createNativeQuery(sql, Object[].class);

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i + 1, params[i]);
                }
            }

            List<Object[]> results = query.getResultList();


            return results.stream()
                    .map(row -> mapToDTO(row, resultType))
                    .toList();
        }
    }


    private <R> R mapToDTO(Object[] row, Class<R> resultType) {
        try {
            var constructor = resultType.getDeclaredConstructors()[1];

            System.out.println(constructor);

            return resultType.cast(constructor.newInstance(row));
        } catch (Exception e) {
            throw new RuntimeException("Mapping to DTO failed: " + e.getMessage(), e);
        }
    }
}
