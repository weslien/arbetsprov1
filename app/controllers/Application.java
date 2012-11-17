package controllers;

import helper.LoginHelper;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;

import views.html.*;
import views.html.fragments.stateNotLoggedIn;
import views.html.index;
import views.html.login;

public class Application extends Controller {

    protected static Result HOME = redirect(
            routes.Application.index()
    );


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
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        }
        if(!attemptLogin(loginForm.get())){
            return badRequest(login.render(loginForm));
        }
        return redirect(
            routes.Application.userProfile()
        );
    }

    private static boolean attemptLogin(User user) {
        if(user.attemptLogin()){
            LoginHelper.loginUser(session(), user);

            return true;
        }else{
            return false;
        }
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
        if(registrationForm.hasErrors()) {
            return badRequest(login.render(registrationForm));
        }
        registrationForm.get().save();
        return redirect(
                routes.Application.registrationComplete()
        );
    }

    @Transactional
    public static Result registrationComplete() {

        return ok(
                thankyou.render()
        );
    }


    @Transactional(readOnly = true)
    public static Result userProfile() {
        final User user = LoginHelper.getLoggedInUser(session());
        if(user == null){
            return index();
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



}