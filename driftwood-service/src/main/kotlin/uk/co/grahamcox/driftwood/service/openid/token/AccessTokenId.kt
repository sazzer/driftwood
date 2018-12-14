package uk.co.grahamcox.driftwood.service.openid.token

import uk.co.grahamcox.driftwood.service.model.Id
import java.util.*

/**
 * The ID of an Access Token
 */
data class AccessTokenId(override val id: UUID) : Id<UUID>
