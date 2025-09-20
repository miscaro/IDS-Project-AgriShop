package it.ids.mad.ProgettoIDS.factory;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class DashboardFactory {
    public static String viewForAuthorities(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return defaultView();
        }
        for (GrantedAuthority a : authorities) {
            String role = a.getAuthority();
            if ("ROLE_GESTOREPIATTAFORMA".equals(role)) return "web/dashboard/gestore_dashboard";
            if ("ROLE_ACQUIRENTE".equals(role)) return "web/dashboard/acquirente_dashboard";
            if ("ROLE_ANIMATORE_FILIERA".equals(role)) return "web/dashboard/animatore_dashboard";
            if ("ROLE_CURATORE".equals(role)) return "web/dashboard/curatore_dashboard";
            if ("ROLE_PRODUTTORE".equals(role) || "ROLE_TRASFORMATORE".equals(role) || "ROLE_DISTRIBUTORE_TIPICITA".equals(role)) {
                return "web/dashboard/venditore_dashboard";
            }
        }
        return defaultView();
    }

    private static String defaultView() {
        return "web/dashboard/index";
    }
}
