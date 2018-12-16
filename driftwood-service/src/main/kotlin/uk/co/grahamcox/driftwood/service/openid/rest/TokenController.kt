package uk.co.grahamcox.driftwood.service.openid.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.driftwood.service.clients.*
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.openid.scopes.Scope
import uk.co.grahamcox.driftwood.service.openid.scopes.ScopeRegistry
import uk.co.grahamcox.driftwood.service.openid.scopes.UnknownScopesException
import uk.co.grahamcox.driftwood.service.openid.token.AccessTokenCreator
import uk.co.grahamcox.driftwood.service.openid.token.AccessTokenSerializer
import uk.co.grahamcox.driftwood.service.users.UserRetriever
import java.time.Clock
import java.time.Duration

/**
 * Controller for handling the calls to the OAuth2 Token endpoint
 */
@RestController
@RequestMapping("/api/oauth2/token")
class TokenController(
        private val clock: Clock,
        private val clientRetriever: ClientRetriever,
        private val scopeRegistry: ScopeRegistry,
        private val accessTokenCreator: AccessTokenCreator,
        private val accessTokenSerializer: AccessTokenSerializer
) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(TokenController::class.java)
    }

    /**
     * Handle the request for an Access Token
     * @param grantType The Grant Type to use
     * @param clientCredentials The clients credentials to use
     * @param params The other request parameters
     */
    @RequestMapping(method = [RequestMethod.POST])
    fun tokenRequest(@RequestParam(value = "grant_type", required = false) grantType: String?,
                     clientCredentials: ClientCredentials?,
                     @RequestParam params: Map<String, String>) : AccessTokenModel {
        LOG.info("Processing token request for grant type {}, clients credentials {} and params {}",
                grantType, clientCredentials, params)

        if (clientCredentials == null) {
            LOG.warn("No client details provided")
            throw InvalidClientException()
        }

        val client = try {
            clientCredentials.let { clientRetriever.getByCredentials(it.clientId, it.clientSecret) }
        } catch (e: ClientNotFoundException) {
            LOG.warn("Client ID not found", e)
            throw InvalidClientException()
        }

        val scopes = params["scope"]
                ?.let { scopeRegistry.parseScopes(it) }
                ?: emptySet()

        return when(grantType) {
            "client_credentials" -> handleClientCredentials(client, scopes)
            "authorization_code" -> TODO("Authorization Code Grant")
            "refresh_token" -> TODO("Refresh Token Grant")
            null -> throw MissingGrantTypeException()
            else -> throw UnknownGrantTypeException(grantType)
        }
    }

    /**
     * Handle the Client Credentials Grant request
     * @param client The client details
     * @param scopes The scope
     */
    fun handleClientCredentials(client: Resource<ClientId, ClientData>,
                                scopes: Set<Scope>): AccessTokenModel {
        if (!client.data.grantTypes.contains(GrantTypes.CLIENT_CREDENTIALS)) {
            throw UnsupportedGrantTypeException(GrantTypes.CLIENT_CREDENTIALS.id)
        }

        val token = accessTokenCreator.createToken(client, client.data.owner, scopes)
        val serialized = accessTokenSerializer.serializeAccessToken(token)
        val now = clock.instant()
        val duration = Duration.between(now, token.expires).seconds

        return AccessTokenModel(
                accessToken = serialized,
                expiry = duration
        )
    }

    /**
     * Handle when an OpenID Exception occurs
     * @param e The error to handle
     */
    @ExceptionHandler(OpenIDException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleOpenIdException(e: OpenIDException) = e.error

    /**
     * Handle when an OpenID Invalid Client Exception occurs
     * @param e The error to handle
     */
    @ExceptionHandler(InvalidClientException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleInvalidClientException(e: InvalidClientException) = e.error

    /**
     * Handle when an Unknown Scopes Exception occurs
     * @param e The error to handle
     */
    @ExceptionHandler(UnknownScopesException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnknownScopesException(e: UnknownScopesException) = ErrorResponse(
            error = ErrorCode.INVALID_SCOPE,
            description = "The requested scopes were not all valid",
            details = mapOf("scopes" to e.scopes)
    )
}
