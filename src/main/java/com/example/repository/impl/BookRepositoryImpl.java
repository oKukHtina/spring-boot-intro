package com.example.repository.impl;

import com.example.entity.Book;
import com.example.exception.DataProcessingException;
import com.example.exception.EntityNotFoundException;
import com.example.repository.BookRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save a book:  " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Book", Book.class).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Failed to find all books " + e);
        }
    }

    @Override
    public Book getBookById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Book "
                                    + "where id =:id", Book.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            throw new EntityNotFoundException("Entity with id: " + id + " not found");
        }
    }
}
