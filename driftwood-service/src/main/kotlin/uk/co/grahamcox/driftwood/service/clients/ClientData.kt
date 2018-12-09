package uk.co.grahamcox.driftwood.service.clients

import uk.co.grahamcox.driftwood.service.users.UserId
import java.net.URI

/**
 * Representation of an OpenID Connect Client
 * @property name the name of the clients
 * @property owner The owner of the clients
 * @property redirectUris The supported Redirect URIs for the clients
 * @property responseTypes The supported Response Types for the clients
 * @property grantTypes The supported Grant Types for the clients
 */
data class ClientData(
        val name: String,
        val owner: UserId,
        val redirectUris: Set<URI>,
        val responseTypes: Set<ResponseTypes>,
        val grantTypes: Set<GrantTypes>
)
