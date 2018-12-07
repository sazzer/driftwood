package uk.co.grahamcox.driftwood.service.client

import uk.co.grahamcox.driftwood.service.model.Id
import java.util.*

/** The Secret of a Client */
data class ClientSecret(override val id: UUID) : Id<UUID>
