package controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebFilter("/*")
public class RoleFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {
        "/login", "/login.jsp", "/CSS/", "/IMG/"
    };

    private static final String[] ADMIN_ONLY_PATHS = {
        "/insert_parkinglot", "/edit_parkinglot", "/parkingLot",
        "/manage_slots", "/manage_slot_view",
        "/show_all_parkingslots"
    };

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        // 1. Dejar pasar rutas públicas
        for (String pub : PUBLIC_PATHS) {
            if (path.startsWith(pub)) {
                chain.doFilter(req, res);
                return;
            }
        }

        // 2. Verificar que exista sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");

        if ("clerk".equals(role)) {
            for (String adminPath : ADMIN_ONLY_PATHS) {
                if (path.startsWith(adminPath)) {
                    // Clerk intenta entrar a zona admin → denegar
                    session.setAttribute("accessError", "Acceso denegado: se requiere rol de administrador.");
                    response.sendRedirect(request.getContextPath() + "/menu_clerk.jsp");
                    return;
                }
            }
        }

        chain.doFilter(req, res);
    }

    @Override public void init(FilterConfig fc) throws ServletException {}
    @Override public void destroy() {}
}