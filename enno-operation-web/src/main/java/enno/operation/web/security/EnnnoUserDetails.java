package enno.operation.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Created by EriclLee on 15/12/2.
 */
public class EnnnoUserDetails implements UserDetails {

    private Collection<GrantedAuthority> grantedAuthorities;
    public EnnnoUserDetails(List<GrantedAuthority> authorityList)
    {
        grantedAuthorities = authorityList;
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return null;
    }

    public String getUsername() {
        return null;
    }

    public boolean isAccountNonExpired() {
        return false;
    }

    public boolean isAccountNonLocked() {
        return false;
    }

    public boolean isCredentialsNonExpired() {
        return false;
    }

    public boolean isEnabled() {
        return false;
    }
}
