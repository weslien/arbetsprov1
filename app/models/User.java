package models;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gustav
 * Date: 2012-11-16
 */
@Entity(name = "users")
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    public Long id;


    public String name;

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

    public static User findById(Long id){
        return JPA.em().find(User.class, id);
    }

    public static User findByUsernamePassword(String email, String encryptedPassword){
        return (User)JPA.em().createQuery("from users u where email=? and password=?")
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
         findByUsernamePassword(this.email, this.password);
            return true;
        }catch(NoResultException e){
            return false;
        }
    }

    public static User findByEmail(String email) {
        return (User)JPA.em().createQuery("from users u where email=?")
                .setParameter(1, email)
                .getSingleResult();
    }

    @OneToMany(mappedBy = "user")
    private Collection<Login> logins;

    public Collection<Login> getLogins() {
        return logins;
    }

    public void setLogins(Collection<Login> logins) {
        this.logins = logins;
    }
}
