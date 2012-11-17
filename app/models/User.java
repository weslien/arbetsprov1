package models;

import play.data.validation.Constraints;
import play.db.jpa.JPA;
import scala.Equals;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gustav
 * Date: 2012-11-16
 */
@Entity(name = "users")
@Table(name = "users")
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    public Long id;


    public String name;

    @Constraints.Required
    @Constraints.Email
    @Column(unique = true)
    public String email;

    @Constraints.Required

    public String password;

    public static User findById(Long id){
        return JPA.em().find(User.class, id);
    }

    public static User findByUsernamePassword(String email, String encryptedPassword){
        return (User)JPA.em().createQuery("from users where email=? and password=?")
                .setParameter(1, email)
                .setParameter(2, encryptedPassword)
                .getSingleResult();
    }

    /**
     * Update this user.
     */
    public void update(Long id) {
        this.id = id;
        JPA.em().merge(this);
    }

    /**
     * Insert this new user.
     */
    public void save() {
        JPA.em().persist(this);
    }

    /**
     * Delete this user.
     */
    public void delete() {
        JPA.em().remove(this);
    }

    public static List<User> findAll() {
        return JPA.em().createQuery("from users u").getResultList();

    }

    public Boolean attemptLogin() {
        try{
         User u = findByUsernamePassword(this.email, this.password);
            this.id = u.id;
            this.name = u.name;
            return true;
        }catch(NoResultException e){
            return false;
        }
    }

    public static User findByEmail(String email) {
        List<User> list = JPA.em().createQuery("from users u where email=?")
                .setParameter(1, email)
                .getResultList();
        return list.size() > 0 ? list.get(0) : null;
    }



}
