package org.acme.todo;

import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.RefreshToken;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 * Token resource for Token debugging<br/>
 * Based on <a href="https://quarkus.io/guides/security-oidc-code-flow-authentication-tutorial#write-the-application">Quarkus Guide: Protect a web application by using OpenID Connect (OIDC) authorization code flow</a>
 */
@Path("/tokens")
public class TokenResource {

    /**
     * Injection point for the ID token issued by the OpenID Connect provider
     */
    @Inject
    @IdToken
    JsonWebToken idToken;

    /**
     * Injection point for the access token issued by the OpenID Connect provider
     */
    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    JsonWebToken accessToken;

    /**
     * Injection point for the refresh token issued by the OpenID Connect provider
     */
    @Inject
    RefreshToken refreshToken;

    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Returns the tokens available to the application.
     * This endpoint exists only for demonstration purposes.
     * Do not expose these tokens in a real application.
     *
     * @return an HTML page containing the tokens available to the application.
     */
    @GET
    @Produces("text/html")
    public String getTokens() {
        StringBuilder response = new StringBuilder().append("<html>")
                .append("<body>")
                .append("<ul>");


        Object rawToken = this.idToken.getRawToken();

        if (rawToken != null) {
            response.append("<li>raw_token_idToken: ").append(rawToken).append("</li>");
        }

        Object rawAccessToken = this.accessToken.getRawToken();

        if (rawToken != null) {
            response.append("<li>raw_token_accessToken: ").append(rawAccessToken.toString()).append("</li>");
        }

        Object userName = this.idToken.getClaim(Claims.preferred_username);

        if (userName != null) {
            response.append("<li>username: ").append(userName).append("</li>");
        }

        Object scopes = this.accessToken.getClaim("scope");

        if (scopes != null) {
            response.append("<li>scopes: ").append(scopes).append("</li>");
        }

        response.append("<li>claim_names: ").append(String.join(", ", this.idToken.getClaimNames())).append("</li>");
        response.append("<li>groups: ").append(String.join(", ", this.idToken.getGroups())).append("</li>");
        response.append("<li>audience: ").append(String.join(", ", this.idToken.getAudience())).append("</li>");

        response.append("<li>refresh_token: ").append(refreshToken.getToken() != null).append("</li>");

        Object resourceAccess = this.idToken.getClaim("resource_access");

        response.append("<li>rescource_access: ").append(resourceAccess.toString()).append("</li>");

        response.append("<li>security_identity_roles: ").append(String.join(", ", securityIdentity.getRoles())).append("</li>");

        response.append("<li>security_identity_name: ").append(securityIdentity.getPrincipal().getName()).append("</li>");

        return response.append("</ul>").append("</body>").append("</html>").toString();
    }
}