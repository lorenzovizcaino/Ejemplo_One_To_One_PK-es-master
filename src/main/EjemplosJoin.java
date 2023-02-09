package main;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


import modelo.Direccion;

import util.SessionFactoryUtil;

public class EjemplosJoin {

	public static void main(String[] args) {
		SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();

		Session session = sessionFactory.openSession();
		
		{
			System.out.println("----------- Q1: Uso de cross join: Cada fila de una tabla combinada con todas las filas de la otra tabla -----------");

			List<Object[]> datos = session.createQuery(
					" select p.nombre, p.ape1, p.ape2, m.nombre FROM Profesor p, Modulo m ")
					.list();

			for (Object[] fila : datos) {
				System.out.println("Profesor: " + fila[0] + " " + fila[1] + " " + fila[2] + " modulo: " + fila[3]);
			}
		}

		{
			System.out.println("-----------Q2: Uso de cross join filtrado con where y member of-----------");

			List<Object[]> datos = session.createQuery(
					" select p.nombre, p.ape1, p.ape2, m.nombre FROM Profesor p, Modulo m where m member of p.modulos")
					.list();

			for (Object[] fila : datos) {
				System.out.println("Profesor: " + fila[0] + " " + fila[1] + " " + fila[2] + " modulo: " + fila[3]);
			}
		}

		

		{
			System.out.println("-----------Q3: Uso de inner join: Profesores y los módulos que imparten (Mismo resultado que arriba) -----------");

			List<Object[]> datos = session
					.createQuery("select p.nombre, p.ape1, p.ape2, m.nombre FROM Profesor p inner join p.modulos m")
					.list();

			for (Object[] fila : datos) {
				System.out.println("Profesor: " + fila[0] + " " + fila[1] + " " + fila[2] + " modulo: " + fila[3]);
			}
		}
		{
			System.out.println("-----------Q4: Uso de  join (sin inner) (Mismo resultado que con inner) -----------");

			List<Object[]> datos = session
					.createQuery(" select p.nombre, p.ape1, p.ape2, m.nombre FROM Profesor p  join p.modulos m").list();

			for (Object[] fila : datos) {
				System.out.println("Profesor: " + fila[0] + " " + fila[1] + " " + fila[2] + " modulo: " + fila[3]);
			}
		}
		
		{
			System.out.println("-----------Q5: Uso de join para obtener las dos entidades profesor y modulo-----------");

			List<Object[]> datos = session.createQuery(" select p, m FROM Profesor p join p.modulos m").list();

			for (Object[] fila : datos) {
				System.out.println("Profesor: " + fila[0] + " modulo: " + fila[1]);
			}
		}
		
		{
			System.out.println("-----------Q6: Uso de left join: Todos los profesores con sus módulos o con null si no imparten módulos-----------");

			List<Object[]> datos = session
					.createQuery(" select p.nombre, p.ape1, p.ape2, m.nombre FROM Profesor p left join p.modulos m")
					.list();

			for (Object[] fila : datos) {
				System.out.println("Profesor: " + fila[0] + " " + fila[1] + " " + fila[2] + " modulo: " + fila[3]);
			}
		}
	

		

		{
			System.out.println(
					"-----------Q7: Uso de  join a través de las propiedades (Crea inner join. Esta es la forma recomendada.) -----------");

			List<Direccion> direcciones = session.createQuery(" select d FROM Direccion d join d.provincia p"
					+ " join p.comunidadAutonoma ca where ca.nombre like 'Galicia'").list();

			for (Direccion dir : direcciones) {
				System.out.println("Direccion: " + dir);
			}
		}

		{
			System.out.println("-----------Q8: Uso directo de las propiedades (Crea cross join) -----------");

			List<Direccion> direcciones = session
					.createQuery(" select d FROM Direccion d where d.provincia.comunidadAutonoma.nombre like 'Galicia'")
					.list();

			for (Direccion dir : direcciones) {
				System.out.println("Direccion: " + dir);
			}
		}

	

		session.close();
		sessionFactory.close();
	}

}
