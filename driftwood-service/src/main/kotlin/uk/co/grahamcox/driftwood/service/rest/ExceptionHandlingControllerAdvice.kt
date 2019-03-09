package uk.co.grahamcox.driftwood.service.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import uk.co.grahamcox.driftwood.service.rest.problem.Problem
import java.net.URI

/**
 * Controller advice for common exception handling
 */
@RestControllerAdvice
class ExceptionHandlingControllerAdvice {
    /**
     * Handle when a method argument was invalid
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException) = Problem(
            type = URI("tag:2018,grahamcox.co.uk:problems/imvalid-argument"),
            title = "Request argument was not valid",
            statusCode = HttpStatus.BAD_REQUEST,
            extraData = mapOf(
                    "name" to e.name,
                    "value" to e.value
            )
    )
}
