package uk.co.grahamcox.driftwood.service.openid.token

import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.openid.scopes.Scope
import uk.co.grahamcox.driftwood.service.users.UserId
import java.time.Instant

/**
 * Representation of an Access Token
 * @property id The ID of the token
 * @property client The client that issued the token
 * @property user The user that the token represents
 * @property issued When the token was issued
 * @property expires When the token expires
 * @property scopes The scopes for the token
 */
data class AccessToken(
        val id: AccessTokenId,
        val client: ClientId,
        val user: UserId,
        val issued: Instant,
        val expires: Instant,
        val scopes: Set<Scope>
)
