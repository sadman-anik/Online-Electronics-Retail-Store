package ebusiness.ejb;

import ebusiness.entity.Wuser;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Date;

@Stateless
public class AuthenticationEJB {

    @PersistenceContext(unitName = "RetailStorePU")
    private EntityManager em;

    public Wuser findByUsername(String username) {
        try {
            return em.createNamedQuery("Wuser.findByUsername", Wuser.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Wuser findByEmail(String email) {
        try {
            return em.createNamedQuery("Wuser.findByEmail", Wuser.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Wuser createUser(Wuser user) {
        user.setSince(new Date());
        em.persist(user);
        return user;
    }

    public Wuser updateUser(Wuser user) {
        return em.merge(user);
    }
}
