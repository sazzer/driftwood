package uk.co.grahamcox.driftwood.service.authorization

import uk.co.grahamcox.driftwood.service.openid.token.AccessToken

/**
 * Interface describing how to get hold of the Access Token for the current request
 */
interface AccessTokenHolder {
    /** The access token */
    val accessToken: AccessToken?
}
