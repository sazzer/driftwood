package uk.co.grahamcox.driftwood.service.clients

import uk.co.grahamcox.driftwood.service.model.Resource

/**
 * Interface representing a means to retrieve clients data
 */
interface ClientRetriever {
    /**
     * Get the Client with the given ID
     * @param id The ID of the client
     * @return The clients
     */
    fun getById(id: ClientId): Resource<ClientId, ClientData>

    /**
     * Get the Client with the given ID and Secret
     * @param id The ID of the client
     * @param secret The Secret of the client
     * @return The clients
     */
    fun getByCredentials(id: ClientId, secret: ClientSecret): Resource<ClientId, ClientData>
}
