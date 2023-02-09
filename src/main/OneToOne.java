package main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.SessionFactoryUtil;
import modelo.ContactInfo;
import modelo.Profesor;



public class OneToOne {
	public static void main(String[] args) {

		SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		updateContactInfoToProfesor(session, 1);
		session.close();
		sessionFactory.close();

	}

	/*Lanzará una excepción
	*Ha ocurrido una excepción: A different object with the same identifier value was already associated with the session : [modelo.ContactInfo#1]
	org.hibernate.NonUniqueObjectException:
	*/
	private static void addNewContactInfoToProfesor(Session session, int profeId) {
		Transaction tx = null;

		Profesor profe = (Profesor) session.createQuery("SELECT p FROM Profesor p where p.id = :id")
				.setParameter("id", profeId).uniqueResult();

		//info está asociado a la sesión con pk=1
		ContactInfo info =  profe.getContactInfo();
		if(info!=null) {
			System.out.println("Profe: " + profe.getId() + " Contact info: " + info);
		}
		

		//creo cInfoNueva, estado transient
		ContactInfo cInfoNueva = new ContactInfo();
		cInfoNueva.setEmail("algo@algo.com");
		cInfoNueva.setTlfMovil("666 123 123");

		//Asocio cInfoNueva a profe con PK =1, por lo que cInfoNueva debería adquirir
		//al pasar a estado persistente PK =1
		// Relación bidireccional
		profe.addContactInfo(cInfoNueva);

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(profe);
			//Se detectan dos objetcos ContactInfo con id=1 => imposible porque es PK
			session.saveOrUpdate(cInfoNueva);

			tx.commit();
		} catch (Exception ex) {
			System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
			ex.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw ex;
		}
	}

	
	private static void updateContactInfoToProfesor(Session session, int profeId) {
		Transaction tx = null;

		Profesor profe = (Profesor) session.createQuery("SELECT p FROM Profesor p where p.id = :id")
				.setParameter("id", profeId).uniqueResult();

		//info está asociado a la sesión con pk=1
		ContactInfo info =  profe.getContactInfo();
		if(info!=null) {
			System.out.println("Profe: " + profe.getId() + " Contact info: " + info);
		}
		

	
		info.setEmail("algo@algo.com");
		info.setTlfMovil("666 123 123");

		profe.addContactInfo(info);

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(profe);
			session.saveOrUpdate(info);

			tx.commit();
			System.out.println("Se ha actualizado correctamente");
		} catch (Exception ex) {
			System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
			ex.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw ex;
		}
	}

	
	

}
