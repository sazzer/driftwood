package uk.co.grahamcox.driftwood.service.users

import uk.co.grahamcox.driftwood.service.model.Resource

/**
 * Interface representing a means to retrieve user data
 */
interface UserRetriever {
    /**
     * Get the User with the given ID
     * @param id The ID of the user
     * @return The user
     */
    fun getById(id: UserId): Resource<UserId, UserData>

    /**
     * Get the User with the given ID in the given External Provider
     * @param id The ID of the user
     * @param provider The Provider that the ID belongs to
     * @return The user, or null if one isn't found
     */
    fun getByProviderId(id: String, provider: String): Resource<UserId, UserData>?
}
