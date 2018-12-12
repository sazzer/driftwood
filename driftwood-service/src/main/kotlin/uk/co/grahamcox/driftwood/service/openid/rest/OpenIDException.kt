package uk.co.grahamcox.driftwood.service.openid.rest

import java.lang.RuntimeException

/**
 * Exception to indicate something went wrong in the OpenID Controllers
 */
open class OpenIDException(val error: ErrorResponse) : RuntimeException()
