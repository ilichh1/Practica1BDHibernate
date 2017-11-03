/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author RigoBono
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final ThreadLocal localSession;
    
    
    //Se ejecuta una sola vez por hilo
    static {
        try {
            //Intentamos crear una nueva configuracion
           Configuration config = new Configuration();
           //Creamos una nueva configuracion con nuestro xml seleccionado
            config.configure("hibernate.cfg.xml");
            // Es un protocolo que registra las sesiones de los clientes
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
            applySettings(config.getProperties());
            // ¿Construimos la sesion?
            sessionFactory = config.buildSessionFactory(builder.build());
        } catch (Throwable ex) { //Si hay un error, imprimimos el error
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        
        localSession = new ThreadLocal();
    }
    
    // Devolvemos la sesión cuando la pidan
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    
    
     public static Session getLocalSession() {
         //Obtenemos la sesion guardada en localSession y le hacemos un casta Session
        Session session = (Session) localSession.get();
        if (session == null) {
            //Si la sesion es nula creamos una nueva sesion
            session = sessionFactory.openSession();
            //Guardamos la session creada en nuestra sesion local
            localSession.set(session);
            //Imprimimos que creamos una sesion
            System.out.println("\nsesion iniciada");
        }
        // Regresamos la session
        return session;
    }
     
     public static void closeLocalSession() {
         //Obtenemos la sesion guardada en localSession y le hacemos un casta Session
        Session session = (Session) localSession.get();
        // Si la session NO es nula la cerramos
        if (session != null) session.close();
        //A la sesion local le damos un valor de null
        localSession.set(null);
        //Imprimimos session
        System.out.println("sesion cerrada\n");
    }
}
