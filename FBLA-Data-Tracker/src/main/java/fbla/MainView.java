package fbla;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import fbla.views.home.HomeView;
import fbla.views.viewandedit.ViewandEditView;
import fbla.views.reports.ReportsView;
import fbla.views.adddata.AddDataView;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@CssImport(value = "./styles/main-view-applayout.css", themeFor =  "vaadin-app-layout")
@PWA(name = "FBLA Data Tracker", shortName = "Data Tracker")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainView extends AppLayout {

    private final Tabs menu;

    public MainView() {
        menu = createMenuTabs();
        addToNavbar(menu);
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.add(getAvailableTabs());
        tabs.setFlexGrowForEnclosedTabs(1);
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
        final List<Tab> tabs = new ArrayList<>();
        tabs.add(createTab("Home", HomeView.class));
        tabs.add(createTab("View and Edit", ViewandEditView.class));
        tabs.add(createTab("Reports", ReportsView.class));
        tabs.add(createTab("Add Data", AddDataView.class));
        return tabs.toArray(new Tab[tabs.size()]);
    }

    private static Tab createTab(String title,
            Class<? extends Component> viewClass) {
        return createTab(populateLink(new RouterLink(null, viewClass), title));
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(content);
        return tab;
    }

    private static <T extends HasComponents> T populateLink(T a, String title) {
        a.add(title);
        return a;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        selectTab();
    }

    private void selectTab() {
        String target = RouteConfiguration.forSessionScope()
                .getUrl(getContent().getClass());
        Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
            Component child = tab.getChildren().findFirst().get();
            return child instanceof RouterLink
                    && ((RouterLink) child).getHref().equals(target);
        }).findFirst();
        tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
    }
}
