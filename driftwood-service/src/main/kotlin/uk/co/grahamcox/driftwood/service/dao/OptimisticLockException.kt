package uk.co.grahamcox.driftwood.service.dao

import uk.co.grahamcox.driftwood.service.model.Id
import java.lang.RuntimeException
import java.util.*

/**
 * Exception to indicate that an Optimistic Lock failure occurred
 */
class OptimisticLockException(val id: Id<*>, val currentVersion: UUID) : RuntimeException("Optimistic Lock Exception on resource: $id")
