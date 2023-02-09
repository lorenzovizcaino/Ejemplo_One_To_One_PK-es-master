package main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import modelo.Ciclo;
import modelo.Profesor;
import util.ProfeInfo;
import util.SessionFactoryUtil;

public class Main {

	public static void main(String[] args) {
		SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();

		Session session = sessionFactory.openSession();

		{
			System.out.println("-----------Q1: Uso de list() -----------");

			List<Profesor> profesores = session.createQuery(" select p FROM Profesor p ").list();

			for (Profesor profesor : profesores) {
				System.out.println(profesor);
			}
		}

		{
			System.out.println("----------- Q2: Uso de list() con datos escalares -----------");
			List<Object[]> listDatos = session.createQuery("SELECT p.id, p.nombre FROM Profesor p").list();

			for (Object[] datos : listDatos) {
				System.out.println(datos[0] + "--" + datos[1]);
			}
		}

		{
			System.out.println("----------- Q3: Uso de list() con un único dato escalar -----------");
			List<Object[]> listDatos = session.createQuery("SELECT p.nombre FROM Profesor p").list();

			for (Object datos : listDatos) {
				System.out.println(datos);
			}
		}

		{
			System.out.println("----------- Q4: Uso de clase util.ProfeInfo-----------");
			List<ProfeInfo> profInfoList = (List<ProfeInfo>) session.createQuery(
					"SELECT new util.ProfeInfo( p.nombre || ' ' ||  p.ape1 || ' '  || p.ape2 ) " + " FROM Profesor p ")
					.list();
			for (ProfeInfo profeInfo : profInfoList) {
				System.out.println(profeInfo.getNombreCompleto());
			}
		}

		{
			System.out.println("----------- Q5: Uso de uniqueResult -----------");
			Profesor profesor = (Profesor) session.createQuery("SELECT p FROM Profesor p WHERE id=10").uniqueResult();
			System.out.println("Profesor con Id 10=" + profesor);
		}

		{
			int profId = 10;
			System.out.println("----------- Q6: Uso de uniqueResult y parámetros por nombre -----------");
			Profesor profesor = (Profesor) session.createQuery("SELECT p FROM Profesor p WHERE id= :id")
					.setParameter("id", profId).uniqueResult();
			System.out.println("Profesor con Id=" + profesor);
		}

		{
			int profId = 10;
			System.out.println("----------- Q7: Uso de uniqueResult y parámetros por posición-----------");
			Profesor profesor = (Profesor) session.createQuery("SELECT p FROM Profesor p WHERE id= ?0")
					.setParameter(0, profId).uniqueResult();
			System.out.println("Profesor con Id=" + profesor);
		}

		{
			System.out.println("----------- Q8: Uso where + in + parameter list-----------");
			List<Integer> idList = new ArrayList<Integer>();
			idList.add(3);
			idList.add(6);
			idList.add(9);
			List<String> names = (List<String>) session
					.createQuery(" SELECT p.nombre FROM Profesor p WHERE id in :listaIds")
					.setParameterList("listaIds", idList).list();

			for (String name : names) {
				System.out.println("Nombre profe en lista: " + name);
			}
		}
		{
			System.out.println("----------- Q9: Mostrar una página -----------");
			int tamanyoPagina = 5;
			int paginaAMostrar = 2;

//            org.hibernate.Query<R> is deprecated use org.hibernate.query.Query<R> instead
			Query<Profesor> query = session.createQuery("SELECT p FROM Profesor p Order By p.id");
			query.setMaxResults(tamanyoPagina);
			query.setFirstResult(paginaAMostrar * tamanyoPagina);
			List<Profesor> profesores = query.list();

			for (Profesor profesor : profesores) {
				System.out.println(profesor);
			}
		}

		{
			System.out.println("-----------Q10:  Calcular el nº de páginas -----------");
			int tamanyoPagina = 10;
			long numTotalObjetos = (Long) session.createQuery("SELECT count(*) FROM Profesor p").uniqueResult();
			int numPaginas = (int) Math.ceil((double) numTotalObjetos / (double) tamanyoPagina);

			System.out.println("Nº de páginas=" + numPaginas);
		}

		{
			System.out.println("-----------Q11:  Consultas con nombre definida en  -----------");
			List<Profesor> profesores = session.getNamedQuery("findAllProfesores").list();

			for (Profesor profesor : profesores) {
				System.out.println(profesor);
			}
		}

		{
			System.out.println("----------- Q12: Simple consulta con HQL -----------");
			List<Ciclo> ciclos = session.createQuery("SELECT c FROM Ciclo c ORDER BY c.nombre").list();

			for (Ciclo ciclo : ciclos) {
				System.out.println(ciclo.toString());
			}

		}

		{
			System.out.println("----------- Q13: Uso de AND y OR -----------");
			List<Profesor> profesores = session
					.createQuery("SELECT p FROM Profesor p WHERE nombre='Juan' AND (ape1='PEREZ' OR ape2='GARCÍA')")
					.list();

			for (Profesor profesor : profesores) {
				System.out.println(profesor.toString());
			}
		}

		{
			System.out.println("----------- Q14: Funciones de agregación -----------");
			Object[] datos = (Object[]) session
					.createQuery("SELECT AVG(c.horas),SUM(c.horas),MIN(c.horas),MAX(c.horas),COUNT(*) FROM Ciclo c")
					.uniqueResult();

			System.out.println("AVG(c.horas)=" + datos[0]);
			System.out.println("SUM(c.horas)=" + datos[1]);
			System.out.println("MIN(c.horas)=" + datos[2]);
			System.out.println("MAX(c.horas)=" + datos[3]);
			System.out.println("COUNT(*)=" + datos[4]);
		}

		{
			System.out.println("----------- Q15: Concatenar Strings -----------");
			String concatenacion = (String) session
					.createQuery("SELECT p.nombre || ' ' || p.ape1 || ' ' || p.ape2 FROM Profesor p WHERE Id=10")
					.uniqueResult();

			System.out.println("concatenación: " + concatenacion);

		}

		{
			System.out.println("----------- Q16: GROUP BY y HAVING -----------");
			List<Object[]> listDatos = session
					.createQuery("SELECT nombre,count(nombre) FROM Profesor p GROUP BY p.nombre "
							+ "HAVING count(nombre)>1 ORDER BY count(nombre)")
					.list();

			for (Object[] datos : listDatos) {
				System.out.println("El nombre \"" + datos[0] + "\" se repite  " + datos[1] + " veces");
			}
		}

		{
			System.out.println("-----------Q17: Subconsultas -----------");
			List<Object[]> listDatos = session.createQuery(
					"SELECT c.nombre, c.horas, (select AVG(c3.horas) from Ciclo c3) FROM Ciclo c WHERE c.horas >  (SELECT  AVG(c2.horas) FROM Ciclo c2)")
					.list();

			for (Object[] datos : listDatos) {
				System.out.println("El ciclo \"" + datos[0] + "\" es de " + datos[1]
						+ " horas, siendo mayor que la media de horas de todos los ciclos: " + datos[2]);
			}
		}

		{
			System.out.println("----------- Q18: Boolean -----------");
			List<Boolean> listDatos = session.createQuery("SELECT t.bit1 FROM Tiposbasicos t where t.bit1=true ")
					.list();

			for (Boolean dato : listDatos) {
				System.out.println("Dato: " + dato);
			}
		}

		{

			System.out.println("----------- Q19: Dates -----------");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date dateFrom;
			try {
				dateFrom = format.parse("1985-10-26");

				Date dateTo = format.parse("2023-12-31");
				Query<Date> queryDates = session
						.createQuery("SELECT t.dateDate FROM Tiposbasicos t where t.dateDate between :from and :to")
						.setParameter("from", dateFrom).setParameter("to", dateTo);
				List<Date> dateList = queryDates.list();
				for (Date date : dateList) {
					System.out.println(date);
				}
			} catch (ParseException e) {
				System.err.println("Ha ocurrido una exception: " + e.getMessage());
				e.printStackTrace();
			}

		}

		session.close();

		sessionFactory.close();

	}
}
