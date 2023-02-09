package main;

import java.util.Iterator;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import modelo.ComunidadAutonoma;
import modelo.Modulo;
import modelo.Profesor;
import modelo.Provincia;
import util.SessionFactoryUtil;

public class MainA3_8 {

	public static void main(String[] args) {
		SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		 crearModuloProfeNuevos(session);
        //eliminarComunidad(session, 19); // borrado de Melilla como CA
		
		crearComunidadYProvincias(session);
		//eliminarProvinciaDeComunidad(session, 100);
		session.close();
		sessionFactory.close();

	}

	private static void crearModuloProfeNuevos(Session session) {
		Modulo mod = new Modulo();
		mod.setNombre("FOL");

		Profesor profe = new Profesor("Teo", "Abad", "López");

//		mod.getProfesors().add(profe);
//		profe.getModulos().add(mod);
		
		//O bien este método
		mod.addProfesor(profe);
		//o bien este otro: profe.addModulo(mod);

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			session.save(mod);
			session.save(profe);

			tx.commit();
		} catch (Exception ex) {
			System.err.println("Ha habido una exception: " + ex.getMessage());
			if (tx != null) {
				tx.rollback();

			}
		}

		// Para debug, pueden ser útiles las siguientes sentencias SQL
		/*
		 * --declare @idProfe int = 1000 
		 * --delete from profesormodulo where  idProfesor=@idProfe 
		 * --delete from profesor where id=@idProfe
		 *  --delete from modulo where nombre='fol'
		 */

	}

	private static void eliminarComunidad(Session session, int idCA) {

		Transaction tx = session.beginTransaction();
		try {

			ComunidadAutonoma ca = session.get(ComunidadAutonoma.class, idCA);
			if (ca != null) {
				session.delete(ca);
				tx.commit();
			} else {
				System.out.println("No se puede eliminar la CA con id: " + idCA + ". No existe");
			}

		} catch (Exception ex) {
			System.err.println("Ha ocurrido una exception en delete: " + idCA + " " + ex.getMessage());
			if (tx != null) {
				tx.rollback();
			}
		}
	}

	private static void crearComunidadYProvincias(Session session) {

		Transaction tx = session.beginTransaction();
		try {

			int idCA = 200;
			ComunidadAutonoma ca = new ComunidadAutonoma();
			ca.setNombre("Nueva CA1");
			// El id no se asigna automáticamente en esta tabla
			ca.setIdCa(idCA);

			Provincia prov1 = new Provincia();
			prov1.setNombre("Nueva prov 1.1");
			// El id no se asigna automáticamente en esta tabla
			prov1.setIdProvincia(idCA + 1);

			Provincia prov2 = new Provincia();
			prov2.setNombre("Nueva prov 1.2");
			// El id no se asigna automáticamente en esta tabla
			prov2.setIdProvincia(idCA + 2);

			ca.getProvincias().add(prov1);
			prov1.setComunidadAutonoma(ca);

			ca.getProvincias().add(prov2);
			prov2.setComunidadAutonoma(ca);

			// Primero guardamos el extremo 1
			session.save(ca);

			// Después el extremo N
			//session.save(prov1);
			//session.save(prov2);

			tx.commit();
		} catch (Exception ex) {
			System.err.println("Ha ocurrido una exception en crearComunidad: " + ex.getMessage());
			if (tx != null) {
				tx.rollback();
			}
		}
	}

	// sin cascade: exception: not-null property references a null or transient
	// value : modelo.Provincia.comunidadAutonoma
	// con delete-orphan en cascade="delete-orphan" ok
	// con all: exception: not-null property references a null or transient value :
	// modelo.Provincia.comunidadAutonoma
	private static void eliminarProvinciaDeComunidad(Session session, int idCA) {

		Transaction tx = session.beginTransaction();
		try {

			ComunidadAutonoma ca = session.get(ComunidadAutonoma.class, idCA);
			Set<Provincia> provs = ca.getProvincias();
			if (provs != null) {
				Iterator<Provincia> it = ca.getProvincias().iterator();
				while (it.hasNext()) {
					Provincia prov = it.next();
					System.out.println("Eliminando provincia de la CA con id: " + ca.getIdCa() + " Prov. id: "
							+ prov.getIdProvincia());
					ca.getProvincias().remove(prov);
					prov.setComunidadAutonoma(null);
					break;
				}
			}

			// Actualizamos guardamos el extremo 1
			session.save(ca);

			tx.commit();
		} catch (Exception ex) {
			System.err.println("Ha ocurrido una exception en crearComunidad: " + ex.getMessage());
			if (tx != null) {
				tx.rollback();
			}
		}
	}

}
