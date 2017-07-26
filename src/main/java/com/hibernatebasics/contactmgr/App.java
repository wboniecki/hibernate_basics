package com.hibernatebasics.contactmgr;


import com.hibernatebasics.contactmgr.model.Contact;
import com.hibernatebasics.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class App {
    // hold a reusable reference to a SessionFactory (since we need one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Name", "Lastname")
                .withEmail("email@example.com")
                .withPhone(123123123L)
                .build();
        save(contact);
        //Display the contacts table

        //for (Contact c : fetchAllContacts()) { System.out.println(c);}
        fetchAllContacts().stream().forEach(System.out::println);
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        // Open a session
        Session session = sessionFactory.openSession();
        //Create the Criteria object
        //createCriteria is deprecated in Hibernate 5.2+, this line below won't work
        //Criteria criteria = session.createCriteria(Contact.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Contact> criteria = builder.createQuery(Contact.class);
        Root<Contact> variableRoot = criteria.from(Contact.class);
        criteria.select(variableRoot);

        List<Contact> contacts = session.createQuery(criteria).getResultList();
        //Close session
        session.close();
        return contacts;
    }

    private static void save(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        //Use the session to save the contact
        session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }
}
