package helper;

import models.User;
import play.mvc.Controller;
import play.mvc.Http;

import javax.persistence.NoResultException;


/**
 * Created with IntelliJ IDEA.
 * User: gustav
 * Date: 2012-11-16
 */
public class LoginHelper {
    private static final String LOGGED_IN = "LOGGED_IN";

    /**
     * Sets the user as logged in for the session duration
     * @param session
     * @param user
     */
    public static void loginUser(Http.Session session,
                                 User user){
        session.put(LOGGED_IN, user.email);
    }

    public static User getLoggedInUser(Http.Session session){
        try{
            return User.findByEmail(session.get(LOGGED_IN));
        }catch(NoResultException e){
            clearLoggedInUser(session);
            return null;
        }
    }

    public static void clearLoggedInUser(Http.Session session) {
        session.remove(LOGGED_IN);
    }

    public static boolean attemptLogin(User user) {
        if (user.attemptLogin()) {
            loginUser(Controller.session(), user);

            return true;
        } else {
            return false;
        }
    }
}
