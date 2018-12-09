package uk.co.grahamcox.driftwood.service.clients

import uk.co.grahamcox.driftwood.service.model.Id
import java.util.*

/** The ID of a Client */
data class ClientId(override val id: UUID) : Id<UUID>
