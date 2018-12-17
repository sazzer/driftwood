package uk.co.grahamcox.driftwood.service.authorization.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.driftwood.service.openid.token.AccessToken
import uk.co.grahamcox.driftwood.service.users.UserId

/**
 * Controller for debugging access tokens
 */
@RestController
@RequestMapping("/api/authorization/debug")
class DebugController {
    /**
     * Get the access token, if present
     */
    @RequestMapping(value = ["/accessToken/optional"], method = [RequestMethod.GET])
    fun getAccessTokenOptional(accessToken: AccessToken?) = accessToken

    /**
     * Get the access token, failing if absent
     */
    @RequestMapping(value = ["/accessToken/required"], method = [RequestMethod.GET])
    fun getAccessTokenRequired(accessToken: AccessToken) = accessToken

    /**
     * Get the user id, if present
     */
    @RequestMapping(value = ["/userId/optional"], method = [RequestMethod.GET])
    fun getUserIdOptional(userId: UserId?) = userId

    /**
     * Get the user id, failing if absent
     */
    @RequestMapping(value = ["/userId/required"], method = [RequestMethod.GET])
    fun getUserIdRequired(userId: UserId) = userId
}
