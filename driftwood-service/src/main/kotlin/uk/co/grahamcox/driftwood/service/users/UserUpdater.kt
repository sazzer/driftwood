package uk.co.grahamcox.driftwood.service.users

import uk.co.grahamcox.driftwood.service.model.Resource

/**
 * Interface representing a means to update user data
 */
interface UserUpdater {
    /**
     * Save changes to an existing user
     * @param user The new user details
     * @return the updated user details
     */
    fun save(user: Resource<UserId, UserData>) : Resource<UserId, UserData>

    /**
     * Save a new user
     * @param user The new user details
     * @return the new user details
     */
    fun save(user: UserData) : Resource<UserId, UserData>
}
