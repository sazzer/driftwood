package uk.co.grahamcox.driftwood.service.authorization.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import uk.co.grahamcox.driftwood.service.openid.token.InvalidAccessTokenException
import uk.co.grahamcox.driftwood.service.rest.problem.Problem
import java.net.URI

/**
 * Controller Advice for handling Authorization issues
 */
@RestControllerAdvice
class AuthorizationControllerAdvice {
    /**
     * Handle when the access token was required but missing
     */
    @ExceptionHandler(MissingAccessTokenException::class)
    fun handleMissingAccessToken() = Problem(
            type = URI("tag:grahamcox.co.uk,2018:driftwood/problems/authorization/missing-access-token"),
            title = "No access token was present",
            statusCode = HttpStatus.UNAUTHORIZED
    )

    /**
     * Handle when the access token was present but invalid
     */
    @ExceptionHandler(InvalidAccessTokenException::class)
    fun handleInvalidAccessToken() = Problem(
            type = URI("tag:grahamcox.co.uk,2018:driftwood/problems/authorization/invalid-access-token"),
            title = "An access token was present but invalid",
            statusCode = HttpStatus.UNAUTHORIZED
    )
}
