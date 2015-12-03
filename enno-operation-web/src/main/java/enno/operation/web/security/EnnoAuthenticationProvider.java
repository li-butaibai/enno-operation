package enno.operation.web.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by EriclLee on 15/12/2.
 */
public class EnnoAuthenticationProvider implements AuthenticationProvider {

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //Authentication authentication1 = new A
        return null;
    }

    public boolean supports(Class<?> aClass) {
        return true;
    }
}
