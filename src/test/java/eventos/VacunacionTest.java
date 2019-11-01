package eventos;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import clasesDAOjpa.ConfigFichaDAOjpa;
import clasesDAOjpa.DuenoDAOjpa;
import clasesDAOjpa.EventoDAOjpa;
import clasesDAOjpa.MascotaDAOjpa;
import factory.FactoryDAO;
import model.ConfigFicha;
import model.Vacunacion;
import model.Dueno;
import model.Mascota;

public class VacunacionTest {

	private static Mascota mascota;
	private static ConfigFicha config;
	private static Dueno duenoMascota;
	private static Vacunacion eventoD1;
	private static MascotaDAOjpa mascotaJPA = FactoryDAO.getMascotaDAO();
	private static DuenoDAOjpa duenoJPA = FactoryDAO.getDuenoDAO();
	private static ConfigFichaDAOjpa configFichaJPA = FactoryDAO.getConfigFichaDAO();
	private static EventoDAOjpa eventoJPA = FactoryDAO.getEventoDAO();

	@BeforeClass
	public static void beforeClass() {
		config = new ConfigFicha(false, false, false, false, false, false, false, false, false, false); 
		duenoMascota = new Dueno("seba", "pose", "seba@gmail.com", "1234", 22155620);	
		mascota = new Mascota("america", "perro", "callejero", "macho", "negro", "ninguna", null , null, duenoMascota, config);
		eventoD1 = new Vacunacion(new Date(), mascota, "sextuple");
		duenoJPA.save(duenoMascota);
		configFichaJPA.save(config);
		mascotaJPA.save(mascota);
		eventoJPA.save(eventoD1);	
	}
	@Test
	public void test() {
		ArrayList<Mascota> mascotas; 
		
		mascotas = (ArrayList<Mascota>) mascotaJPA.getAll();
		assertEquals(1,mascotas.size());
		Mascota m1 = mascotas.get(0);
		assertEquals(1, m1.getHistorial().size());
		Vacunacion e1 = (Vacunacion) m1.getHistorial().get(0);
		assertTrue(e1.equals(eventoD1));
		
		Vacunacion eventoD2 = new Vacunacion(new Date(), m1, "quintuple");
		eventoJPA.save(eventoD2);
		m1.agregarEvento(eventoD2);
		assertEquals(2, m1.getHistorial().size());
		m1.borrarEvento(eventoD2);	
		eventoJPA.delete(eventoD2);
		assertEquals(1, m1.getHistorial().size());
		
		e1.setDescripcion("hepatitis");
		eventoJPA.update(e1);
		mascotas = (ArrayList<Mascota>) mascotaJPA.getAll();
		Vacunacion e3 = (Vacunacion) mascotas.get(0).getHistorial().get(0);
		assertTrue(e3.getDescripcion().equals("hepatitis"));
		
		Vacunacion e4 = (Vacunacion) eventoJPA.getById(1);
		assertTrue(e4.equals(eventoD1));
	}
	@AfterClass
	public static void AfterClass() {
		mascota.borrarEvento(eventoD1);
		eventoJPA.delete(eventoD1);	
		mascotaJPA.delete(mascota);	
		configFichaJPA.delete(config);
		duenoJPA.delete(duenoMascota);	    
	}

}