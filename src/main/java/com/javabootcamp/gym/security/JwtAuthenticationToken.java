package com.javabootcamp.gym.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    @NotNull
    private final String token;
    private final Object principal;
    private final Object credentials;

    /*
     * Creates a token with the supplied array of authorities.
     * <p>
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     * represented by this authentication object.
     */
    public JwtAuthenticationToken(@NotNull String token) {
        super(null);
        this.token = token;
        credentials = null;
        principal = null;
    }

    public JwtAuthenticationToken(@NotNull String token, Object principal, Object credentials) {
        super(null);
        this.token = token;
        this.principal = principal;
        this.credentials = credentials;
    }

    public static JwtAuthenticationToken authenticated(@NotNull String token, Object principal, Object credentials) {
        var bearer = new JwtAuthenticationToken(token, principal, credentials);
        bearer.setAuthenticated(true);
        return bearer;
    }

    /**
     * Get the  <a href="https://tools.ietf.org/html/rfc6750#section-1.2">Bearer Token</a>
     *
     * @return the token that proves the caller's authority to perform the {@code HttpServletRequest}
     */
    public @NotNull String getToken() {
        return token;
    }

    /**
     * <p><b>Description copied from interface: {@link org.springframework.security.core.Authentication}</b></p>
     * <p>
     * The credentials that prove the principal is correct. This is usually a password,
     * but could be anything relevant to the <code>AuthenticationManager</code>. Callers
     * are expected to populate the credentials.
     * </p>
     *
     * @return the credentials that prove the identity of the <code>Principal</code>
     */
    @Override
    public Object getCredentials() {
        return credentials;
    }

    /**
     * <b>Description copied from interface: {@link org.springframework.security.core.Authentication}</b>
     * The identity of the principal being authenticated. In the case of an authentication
     * request with username and password, this would be the username. Callers are
     * expected to populate the principal for an authentication request.
     * <p>
     * The <tt>AuthenticationManager</tt> implementation will often return an
     * <tt>Authentication</tt> containing richer information as the principal for use by
     * the application. Many of the authentication providers will create a
     * {@code UserDetails} object as the principal.
     *
     * @return the <code>Principal</code> being authenticated or the authenticated
     * principal after authentication.
     */
    @Override
    public Object getPrincipal() {
        return principal;
    }
}
