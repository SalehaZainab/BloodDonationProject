package com.example.BloodDonationProject.security;

import com.example.BloodDonationProject.entity.User;
import com.example.BloodDonationProject.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final com.example.BloodDonationProject.service.AuthService authService;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Skip if not a controller method
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // Check method-level @RequireAuth annotation
        RequireAuth requireAuth = handlerMethod.getMethodAnnotation(RequireAuth.class);

        // Check class-level @RequireAuth annotation if method-level not found
        if (requireAuth == null) {
            requireAuth = handlerMethod.getBeanType().getAnnotation(RequireAuth.class);
        }

        if (requireAuth == null) {
            return true; // No authentication required
        }

        // Get Authorization header or cookie
        String authHeader = request.getHeader("Authorization");
        String token = null;

        // Try to get token from Authorization header first
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            // If not in header, check cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"status\":false,\"statusNumber\":1010,\"message\":\"Access token required\",\"data\":{}}");
            return false;
        }

        // Extract token (already extracted above, no need to substring again)
        // String token = authHeader.substring(7);

        try {
            // Validate token
            if (!jwtTokenProvider.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter()
                        .write("{\"status\":false,\"statusNumber\":1010,\"message\":\"Token expired\",\"data\":{}}");
                return false;
            }

            // Get user ID from token and set it in request attribute
            String userId = jwtTokenProvider.getUserIdFromToken(token);

            // Check if user account is verified/active
            Optional<User> userOptional = userRepository.findByIdAndDeletedAtIsNull(userId);
            if (userOptional.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter()
                        .write("{\"status\":false,\"statusNumber\":1010,\"message\":\"User not found\",\"data\":{}}");
                return false;
            }

            User user = userOptional.get();
            if (!user.IsActive()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter()
                        .write("{\"status\":false,\"statusNumber\":1010,\"message\":\"Please verify your account first\",\"data\":{}}");
                return false;
            }

            request.setAttribute("userId", userId);

            // Update lastActive for the authenticated user
            try {
                authService.updateLastActive(userId);
            } catch (Exception err) {
                System.err.println("Failed to update lastActive: " + err.getMessage());
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter()
                    .write("{\"status\":false,\"statusNumber\":1010,\"message\":\"Invalid access token\",\"data\":{}}");
            return false;
        }

        return true;
    }
}
