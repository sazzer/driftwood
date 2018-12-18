package uk.co.grahamcox.driftwood.service.users.rest

import com.fasterxml.jackson.annotation.JsonUnwrapped

/**
 * Identified version of a User Model
 * @property id The ID of the user
 * @property user The user data
 */
data class IdentifiedUserModel(
        val id: String,
        @JsonUnwrapped val user: UserModel
)
