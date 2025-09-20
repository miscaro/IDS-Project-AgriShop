package it.ids.mad.ProgettoIDS.web;

import it.ids.mad.ProgettoIDS.factory.DashboardFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return DashboardFactory.viewForAuthorities(authentication.getAuthorities());
        }
        return "web/dashboard/index";
    }

    @GetMapping("/dashboard/acquirente")
    public String acquirenteDashboard() {
        return "web/dashboard/acquirente_dashboard";
    }

    @GetMapping("/dashboard/venditore")
    public String venditoreDashboard() {
        return "web/dashboard/venditore_dashboard";
    }

    @GetMapping("/dashboard/curatore")
    public String curatoreDashboard() {
        return "web/dashboard/curatore_dashboard";
    }

    @GetMapping("/dashboard/gestore")
    public String gestoreDashboard() {
        return "web/dashboard/gestore_dashboard";
    }

    @GetMapping("/dashboard/animatore")
    public String animatoreDashboard() {
        return "web/dashboard/animatore_dashboard";
    }
}
