package in.woowa.pilot.admin.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class OAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_NAME = "redirect_uri";

    private final HttpSession httpSession;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return (OAuth2AuthorizationRequest) httpSession.getAttribute(OAUTH2_AUTHORIZATION_REQUEST_NAME);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            return;
        }

        httpSession.setAttribute(OAUTH2_AUTHORIZATION_REQUEST_NAME, authorizationRequest);
        httpSession.setAttribute(REDIRECT_URI_PARAM_NAME, request.getParameter(REDIRECT_URI_PARAM_NAME));
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequest() {
        httpSession.removeAttribute(OAUTH2_AUTHORIZATION_REQUEST_NAME);
        httpSession.removeAttribute(REDIRECT_URI_PARAM_NAME);
    }
}
