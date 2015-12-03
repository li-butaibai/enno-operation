package enno.operation.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by EriclLee on 15/12/2.
 */
public class EnnoAuthentication implements Authentication {
    private Collection<GrantedAuthority> grantedAuthorities;
    private String password;
    private  boolean authenticated;
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public Object getCredentials() {
        return password;
    }

    public Object getDetails() {
        return null;
    }

    public Object getPrincipal() {
        return null;
    }

    public boolean isAuthenticated() {
        return false;
    }

    public void setAuthenticated(boolean b) throws IllegalArgumentException {

    }

    public String getName() {
        return null;
    }
}
