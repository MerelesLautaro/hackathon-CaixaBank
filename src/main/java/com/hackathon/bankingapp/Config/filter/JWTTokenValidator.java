package com.hackathon.bankingapp.Config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hackathon.bankingapp.Services.TokenBlacklistService;
import com.hackathon.bankingapp.Utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JWTTokenValidator extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final TokenBlacklistService tokenBlacklistService;

    public JWTTokenValidator(JWTUtils jwtUtils, TokenBlacklistService tokenBlacklistService) {
        this.jwtUtils = jwtUtils;
        this.tokenBlacklistService = tokenBlacklistService;
    }


    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(jwtToken!=null){
            //Bearer tipo de tokenizacion -> substring nos elimina la palabra 'Bearer' para comparar el token correctamente
            jwtToken = jwtToken.substring(7);

            if (tokenBlacklistService.isTokenBlacklisted(jwtToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has been invalidated.");
                return; // Detiene el procesamiento del filtro si el token está bloqueado
            }

            //decodificamos
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

            //traemos el usuario
            String username = jwtUtils.extractUsername(decodedJWT);

            //traemos los permisos seperados por comas
            String authorities = jwtUtils.getSpecificClaim(decodedJWT,"authorities").asString();

            //Transformamos los permisos en GrantedAuthority necesario para el context holder
            Collection<? extends GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            //Variable del contexto de seguridad
            SecurityContext securityContext = SecurityContextHolder.getContext();

            //Nueva instancia de autenticacion, le pasamos las credenciales y permisos
            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,authoritiesList);

            //Seteamos la autenticacion al contexto de  seguridad
            securityContext.setAuthentication(authentication);

            //Seteo el contexto en el context holder
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request, response);

    }
}
