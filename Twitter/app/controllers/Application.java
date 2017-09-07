package controllers;

import Module.TwitterAuthenticator;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;


/**
 * The Controller for login actions.
 */

public class Application extends Controller {



    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    /**
     * Twitter login button action.
     *
     * @return the result
     */
    public static Result twitterLogin() {
        final String url = new TwitterAuthenticator().authenticate();
        return redirect(url);
    }

}
