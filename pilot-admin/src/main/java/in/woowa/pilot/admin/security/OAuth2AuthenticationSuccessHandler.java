package in.woowa.pilot.admin.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static in.woowa.pilot.admin.security.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_NAME;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2AuthorizationRequestRepository OAuth2AuthorizationRequestRepository;
    private final HttpSession httpSession;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        super.clearAuthenticationAttributes(request);
        OAuth2AuthorizationRequestRepository.removeAuthorizationRequest();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUri = (String) httpSession.getAttribute(REDIRECT_URI_PARAM_NAME);

        return UriComponentsBuilder.fromUriString(redirectUri).build().toUriString();
    }
}
