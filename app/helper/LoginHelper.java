package helper;

import models.Login;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;

import javax.persistence.NoResultException;
import java.util.Date;


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
        createLogin(user);
    }

    private static void createLogin(User user) {
        //TODO Trim the logins table to discard old data
        Login login = new Login();
        login.user = user;
        //TODO Don't use date, hard to mock
        login.loginDate = new Date();
        login.save();
    }

    /**
     * Get the currently logged in user. If no user is logged in, null is returned
     * @param session
     * @return
     */
    public static User getLoggedInUser(Http.Session session){
        try{
            return User.findByEmail(session.get(LOGGED_IN));
        }catch(NoResultException e){
            clearLoggedInUser(session);
            return null;
        }
    }

    /**
     * Checks if any user is logged in
     * @return
     */
    public static boolean isUserLoggedIn(){
        return Controller.session().get(LOGGED_IN) != null;
    }

    /**
     * Clears the currently logged in uses. a.k.a. Log out
     * @param session
     */
    public static void clearLoggedInUser(Http.Session session) {
        session.remove(LOGGED_IN);
    }

    /**
     * Attempts to log in this user using the email/password in the instance
     * @param user
     * @return
     */
    public static boolean attemptLogin(User user) {
        if (user.attemptLogin()) {
            loginUser(Controller.session(), user);

            return true;
        } else {
            return false;
        }
    }
}
