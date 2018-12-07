package uk.co.grahamcox.driftwood.service.openid.rest

import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import uk.co.grahamcox.driftwood.service.client.ClientId
import uk.co.grahamcox.driftwood.service.client.ClientSecret
import java.lang.IllegalArgumentException
import java.util.*


/**
 * Argument Resolver for parsing a Basic Authorization header into Client Credentials
 */
class ClientCredentialsArgumentResolver : HandlerMethodArgumentResolver {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(ClientCredentialsArgumentResolver::class.java)

        /** The prefix for a Basic Authorization request */
        private const val BASIC_AUTH_PREFIX = "Basic "
    }
    /**
     * Whether we can provide a value for this parameter or not
     * @param param The parameter we are working with
     * @return True if this is a ClientCredentials parameter
     */
    override fun supportsParameter(param: MethodParameter) = param.parameterType == ClientCredentials::class.java

    /**
     * Attempt to parse a Client Credentials from the request
     * @param param The param we are parsing for. Unused.
     * @param mvc The Model and View Container. Unused.
     * @param request The request
     * @param webDataBinderFactory The web data binder factory. Unused.
     * @return the Client Credentials, or null if not found
     */
    override fun resolveArgument(param: MethodParameter,
                                 mvc: ModelAndViewContainer?,
                                 request: NativeWebRequest,
                                 webDataBinderFactory: WebDataBinderFactory?): Any? {

        val authHeader = request.getHeader("Authorization")
        return when {
            authHeader == null -> {
                LOG.debug("No Authorization header found")
                null
            }
            !authHeader.startsWith(BASIC_AUTH_PREFIX) -> {
                LOG.debug("Authorization header found, but not Basic Auth: {}", authHeader)
                null
            }
            else -> {
                LOG.debug("Basic Authorization header found: {}", authHeader)
                val encoded = authHeader.substring(BASIC_AUTH_PREFIX.length)
                val decoded = String(Base64.getDecoder().decode(encoded))

                return try {
                    val (id, secret) = decoded.split(":", limit = 2)
                    val credentials = ClientCredentials(
                            ClientId(UUID.fromString(id)),
                            ClientSecret(UUID.fromString(secret))
                    )
                    LOG.debug("Received Client Credentials: {}", credentials)
                    credentials
                } catch (e: IllegalArgumentException) {
                    LOG.warn("Received invalid Client Credentials: {}", decoded)
                    null
                } catch (e: IndexOutOfBoundsException) {
                    LOG.warn("Received invalid Client Credentials: {}", decoded)
                    null
                }
            }
        }
    }
}
