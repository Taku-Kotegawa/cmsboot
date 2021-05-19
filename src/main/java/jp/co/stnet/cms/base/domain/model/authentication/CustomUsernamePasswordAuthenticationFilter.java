package jp.co.stnet.cms.base.domain.model.authentication;

import jp.co.stnet.cms.base.domain.model.authentication.CustomUsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: "
                    + request.getMethod());
        }

        // (1)
        // Obtain UserName, Password, CompanyId
        String username = super.obtainUsername(request);
        String password = super.obtainPassword(request);
        Boolean loginAsAdministrator = obtainLoginAsAdministrator(request);
        if (username == null) {
            username = "";
        } else {
            username = username.trim();
        }
        if (password == null) {
            password = "";
        }
        CustomUsernamePasswordAuthenticationToken authRequest =
                new CustomUsernamePasswordAuthenticationToken(username, password, loginAsAdministrator);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest); // (2)

    }

    protected Boolean obtainLoginAsAdministrator(HttpServletRequest request) {
        if ("true".equals(request.getParameter("loginAsAdministrator"))) {
            return true;
        }
        return false;
    }

}
