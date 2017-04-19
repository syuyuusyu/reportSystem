package ind.syu.dao;

import ind.syu.entity.ReportInput;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.springframework.transaction.annotation.Transactional;

public class ReportInputDaoImpl {
	
    @PersistenceContext  
    private EntityManager em; 
    
    @Transactional
    public List<ReportInput> somefind(String name){
        HibernateEntityManager hEntityManager = (HibernateEntityManager)em;
        Session session = hEntityManager.getSession();
        System.out.println("-----------------------------");
        ReportInput r3=(ReportInput) session.load(ReportInput.class, 1);
        r3.getName().toString();
        ReportInput r4=(ReportInput) session.load(ReportInput.class, 1);
        System.out.println("-----------------------------");
        ReportInput r1=(ReportInput) session.get(ReportInput.class, 1);
        ReportInput r2=(ReportInput) session.get(ReportInput.class, 1);

        

        
        System.out.println(r1.equals(r2));
        System.out.println(r3.equals(r4));
    	return null;
    }

}
