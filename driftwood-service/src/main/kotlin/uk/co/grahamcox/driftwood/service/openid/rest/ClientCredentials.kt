package uk.co.grahamcox.driftwood.service.openid.rest

import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.clients.ClientSecret


/**
 * The credentials of a clients
 * @property clientId The Client ID
 * @property clientSecret The Client Secret
 */
data class ClientCredentials(val clientId: ClientId, val clientSecret: ClientSecret)
