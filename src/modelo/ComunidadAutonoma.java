package modelo;
// Generated 31 ene 2023 11:04:06 by Hibernate Tools 5.6.14.Final

import java.util.HashSet;
import java.util.Set;

/**
 * ComunidadAutonoma generated by hbm2java
 */
public class ComunidadAutonoma implements java.io.Serializable {

	private int idCa;
	private String nombre;
	private Set provincias = new HashSet(0);

	public ComunidadAutonoma() {
	}

	public ComunidadAutonoma(int idCa, String nombre) {
		this.idCa = idCa;
		this.nombre = nombre;
	}

	public ComunidadAutonoma(int idCa, String nombre, Set provincias) {
		this.idCa = idCa;
		this.nombre = nombre;
		this.provincias = provincias;
	}

	public int getIdCa() {
		return this.idCa;
	}

	public void setIdCa(int idCa) {
		this.idCa = idCa;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set getProvincias() {
		return this.provincias;
	}

	public void setProvincias(Set provincias) {
		this.provincias = provincias;
	}

	public void addProvincia(Provincia prov) {

		getProvincias().add(prov); // uso de getProvincias() en lugar de this.provincias para forzar inicialización
									// del proxy
		prov.setComunidadAutonoma(this);

	}

}