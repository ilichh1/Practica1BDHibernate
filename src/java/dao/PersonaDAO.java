/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import hbm.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import pojo.Persona;
import pojo.TipoPersona;

/**
 *
 * @author RigoBono
 */
public class PersonaDAO {
    // Session
    Session session;
    
    public PersonaDAO() {
        //DAO = Data Acces Object
        // Creamos una nueva sesion por cada objeto
        session=HibernateUtil.getLocalSession();
    }
    
    public  Persona getPersonaById(int id) {
        // Obtenemos el objeto persona de la sesion y le hacemos un cast al tipo Persona
        return (Persona)session.load(Persona.class, id);
    }
    
    public List<Persona> getPersonaByName(String nombre) {
        // Regresamos una lista de oersonas con una busqueda por
        // nombre en el campo de persona de su entidad
        List<Persona> listaDePersonas=(List<Persona>)
                session.createCriteria(Persona.class)
                .add(Restrictions.eq("Nombre", nombre));
        return listaDePersonas;
    }
    
    public boolean updateById(int id,Persona persona) {
        // Este metodo actualiza a una persona con su identificador unico
        Persona personaAModificar=getPersonaById(id);
        try{
            // Hacemos un try en caso de error
            Transaction transaccion=session.beginTransaction(); // Empezamos la transaccion
            // Cambiamos el atributo de la persona a modificar
            personaAModificar.setNombre(persona.getNombre());
            // De la sesion actual, actualizamos la persona modificada
            session.update(personaAModificar);
            // Avisamos a nuestra transaccion de los cambios realizados
            transaccion.commit();
            return true;
        }catch(Exception e){
            // En caso de error regresamos false
            return false;
        }
    }
    
    public boolean savePersona(String nombre,String materno,String paterno,String telefono,int idTipoPersona) {
        //Creamos una persona
        Persona personaDeTanque=new Persona();
        // De la session actual cargamos el tipo de persona
        TipoPersona tipoDeTanque=(TipoPersona)session.load(TipoPersona.class, idTipoPersona);
        // Asignamos todos los atributos de la persona recien creada
        personaDeTanque.setNombre(nombre);
        personaDeTanque.setMaterno(materno);
        personaDeTanque.setPaterno(paterno);
        personaDeTanque.setTelefono(telefono);
        personaDeTanque.setTipoPersona(tipoDeTanque);
        try{
            // Intentamos obtener la Transaccion de la sesion actual
            Transaction transaccion=session.beginTransaction();
            //Guardamos la persona que creamos arriba
            session.save(personaDeTanque);
            // Notificamos a las demas sesiones de los cambios
            transaccion.commit();
            return true;
        }catch(Exception e){
            // No hacemos nada en el catch
        }finally{
            // Tampoco cen el finally
        }
        return true;
    }
    
}
