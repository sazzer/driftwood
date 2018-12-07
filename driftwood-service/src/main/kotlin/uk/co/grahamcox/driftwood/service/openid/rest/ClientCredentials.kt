package uk.co.grahamcox.driftwood.service.openid.rest

import uk.co.grahamcox.driftwood.service.client.ClientId
import uk.co.grahamcox.driftwood.service.client.ClientSecret


/**
 * The credentials of a client
 * @property clientId The Client ID
 * @property clientSecret The Client Secret
 */
data class ClientCredentials(val clientId: ClientId, val clientSecret: ClientSecret)
