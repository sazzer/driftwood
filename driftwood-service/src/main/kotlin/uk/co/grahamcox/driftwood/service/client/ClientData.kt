package uk.co.grahamcox.driftwood.service.client

import uk.co.grahamcox.driftwood.service.users.UserId
import java.net.URI

/**
 * Representation of an OpenID Connect Client
 * @property name the name of the client
 * @property owner The owner of the client
 * @property redirectUris The supported Redirect URIs for the client
 * @property responseTypes The supported Response Types for the client
 * @property grantTypes The supported Grant Types for the client
 */
data class ClientData(
        val name: String,
        val owner: UserId,
        val redirectUris: Set<URI>,
        val responseTypes: Set<ResponseTypes>,
        val grantTypes: Set<GrantTypes>
)
