package org.acme.todo;
import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.quarkus.oidc.IdToken;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Objects;

@PersistenceUnitExtension
@RequestScoped
public class TodoTenantResolver implements TenantResolver {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    @IdToken
    JsonWebToken idToken;

//    @Inject
//    JsonWebToken accessToken; // for JWT bearer token authentication

    @Override
    public String getDefaultTenantId() {
        return "UNKNOWN_TENANT";
    }

    @Override
    public String resolveTenantId() {
        if(Objects.nonNull(idToken)) {
            return idToken.getClaim("sub").toString();
        }
        if (Objects.nonNull(securityIdentity) && !securityIdentity.isAnonymous()) {
            return securityIdentity.getPrincipal().getName();
        }
        return getDefaultTenantId();
    }

//    @Override
//    public boolean isRoot(String tenantId) {
//        return "root".equals(tenantId);
//    }
}