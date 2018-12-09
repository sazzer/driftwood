package uk.co.grahamcox.driftwood.service.clients

import uk.co.grahamcox.driftwood.service.model.Resource

/**
 * Interface representing a means to retrieve clients data
 */
interface ClientRetriever {
    /**
     * Get the Client with the given ID
     * @param id The ID of the clients
     * @return The clients
     */
    fun getById(id: ClientId): Resource<ClientId, ClientData>
}
