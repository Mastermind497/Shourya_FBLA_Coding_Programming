import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * The Login View used to allow logging in and to secure all accounts
 *
 * @author Shourya Bansal
 */
@Tag("sa-login-view")
@Route(value = LoginView.ROUTE)
@PageTitle("Login")
public class LoginView extends AppLayout {
    public static final String ROUTE = "login";

    /**
     * The Login Overlay Component with which one logs in
     */
    private LoginOverlay login = new LoginOverlay();

    public LoginView() {
        login.setAction("login");
        login.setOpened(true);
        setContent(login);
    }
}
