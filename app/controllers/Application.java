package controllers;

import helper.LoginHelper;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import views.html.fragments.stateNotLoggedIn;

public class Application extends Controller {

    protected static Result HOME = redirect(
            routes.Application.index()
    );
    private static final String GENERIC_ERROR = Messages.get("error.generic");

    public static Result index() {

        return ok(index.render(
                stateNotLoggedIn.render()
        ));
    }

    @Transactional(readOnly = true)
    public static Result loginForm() {
        Form<User> loginForm = form(User.class);
        return ok(
                login.render(loginForm)
        );
    }

    @Transactional
    public static Result doLogin() {
        Form<User> loginForm = form(User.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        }
        if (!LoginHelper.attemptLogin(loginForm.get())) {
            loginForm.reject("password");

            return badRequest(login.render(loginForm));
        }
        return redirect(
                routes.Application.userProfile()
        );
    }

    @Transactional(readOnly = true)
    public static Result registrationForm() {
        Form<User> registrationForm = form(User.class);
        return ok(
                register.render(registrationForm)
        );
    }

    @Transactional
    public static Result doRegistration() {
        Form<User> registrationForm = form(User.class).bindFromRequest();

        // Check repeated password
        if(!registrationForm.field("password").valueOr("").isEmpty()) {
            if(!registrationForm.field("password").valueOr("").equals(registrationForm.field("repeatPassword").value())) {
                registrationForm.reject("repeatPassword", "Password don't match");
            }
        }

        if (registrationForm.hasErrors()) {
            return badRequest(register.render(registrationForm));
        }

        registrationForm.get().save();
        return redirect(
                routes.Application.registrationComplete()
        );
    }

    public static Result registrationComplete() {

        return ok(
                thankyou.render()
        );
    }

    @Transactional(readOnly = true)
    public static Result userProfile() {
        final User user = LoginHelper.getLoggedInUser(session());
        if (user == null) {
            flash("ERROR", Messages.get("profile.invalidaccount"));
            return redirect(routes.Application.error());
        }
        return ok(
                profile.render(user)
        );


    }

    @Transactional(readOnly = true)
    public static Result users() {


        return ok(
                userlist.render(User.findAll())
        );
    }

    public static Result error() {
        String errorMessage = flash("ERROR");
        if(errorMessage == null){
            errorMessage = GENERIC_ERROR;
        }
        return ok(
                errorpage.render(GENERIC_ERROR)
        );
    }


}