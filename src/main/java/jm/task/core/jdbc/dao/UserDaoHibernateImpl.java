package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createSQLQuery("CREATE TABLE IF NOT EXISTS " + Util.getTableName("User") +
                "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), last_name VARCHAR(255), age INT)")
                .executeUpdate();
        tx.commit();
        session.close();
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createSQLQuery("DROP TABLE IF EXISTS " + Util.getTableName("User"))
                .executeUpdate();
        tx.commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(new User(name, lastName, (byte) age));
        tx.commit();
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        User user = (User) session.get(User.class, id);
        if (null != user) {
            session.delete(user);
        }

        tx.commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        List<User> users = session.createQuery("from User").list();
        session.close();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createSQLQuery("TRUNCATE TABLE " + Util.getTableName("User"))
                .executeUpdate();
        tx.commit();
        session.close();
    }
}
