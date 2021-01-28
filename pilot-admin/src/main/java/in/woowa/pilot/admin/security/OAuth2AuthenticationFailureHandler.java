package in.woowa.pilot.admin.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static in.woowa.pilot.admin.security.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_NAME;


@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final OAuth2AuthorizationRequestRepository OAuth2AuthorizationRequestRepository;
    private final HttpSession httpSession;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String targetUrl = (String) httpSession.getAttribute(REDIRECT_URI_PARAM_NAME);

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("ERROR", exception.getLocalizedMessage())
                .build().toUriString();
        OAuth2AuthorizationRequestRepository.removeAuthorizationRequest();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
