package org.iesalixar.daw2.danielquintero.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

/**
 * Servlet encargado de gestionar el cambio de idioma.
 */
@WebServlet("/language")
public class LanguageServlet extends HttpServlet {

    private static final Logger logger =
            LoggerFactory.getLogger(LanguageServlet.class);

    /**
     * Cambia el idioma seleccionado por el usuario.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        logger.info("Entrando en doGet de LanguageServlet");

        String lang =
                request.getParameter("lang");

        Locale locale;

        if ("en".equals(lang)) {

            locale =
                    Locale.ENGLISH;

        } else {

            locale =
                    Locale.forLanguageTag("es");
        }

        request.getSession()
                .setAttribute(
                        "locale",
                        locale
                );

        logger.info(
                "Idioma cambiado a: {}",
                locale.getLanguage()
        );

        String referer =
                request.getHeader("Referer");

        if (referer != null &&
                !referer.isBlank()) {

            response.sendRedirect(
                    referer
            );

        } else {

            response.sendRedirect(
                    request.getContextPath() + "/users"
            );
        }

        logger.info("Saliendo de doGet de LanguageServlet");
    }
}