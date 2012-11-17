package models;

import play.data.format.Formats;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gustav
 * Date: 2012-11-16
 * Time: 21:25
 * To change this template use File | Settings | File Templates.
 */
@Entity(name = "userlogins")
@Table(name = "userlogins")
@SequenceGenerator(name = "userlogin_seq", sequenceName = "userlogin_seq")
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userlogin_seq")
    public Long id;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date loginDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    public User user;

    public void save(){
        if(this.user.id == null){
            this.user = null;
        }else{
            this.user = User.findById(user.id);
        }
        JPA.em().persist(this);
    }
}
