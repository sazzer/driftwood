package uk.co.grahamcox.driftwood.service.clients

import java.lang.RuntimeException

/**
 * Exception to indicate that no clients could be found with the given ID
 * @property id The ID of the clients
 */
class ClientNotFoundException(val id: ClientId) : RuntimeException("Client not found: ${id.id}")
