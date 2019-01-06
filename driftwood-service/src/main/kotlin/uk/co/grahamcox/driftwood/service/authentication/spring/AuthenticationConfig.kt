package uk.co.grahamcox.driftwood.service.authentication.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.env.get
import uk.co.grahamcox.driftwood.service.authentication.*
import uk.co.grahamcox.driftwood.service.authentication.google.GoogleStartAuthenticationBuilder
import uk.co.grahamcox.driftwood.service.authentication.google.GoogleUserLoader
import uk.co.grahamcox.driftwood.service.authentication.rest.ExternalAuthenticationController
import java.net.URI

/**
 * Spring configuration for Authentication
 */
@Configuration
class AuthenticationConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean {
                val authenticators = listOf(
                        "google" to buildGoogleAuthenticator("google")
                )

                AuthenticatorRegistry(
                        authenticators
                                .filter { it.second != null }
                                .map { it.first to it.second!! }
                                .toMap()
                )
            }
            bean<ExternalAuthenticationController>()
        }.initialize(context)
    }

    /**
     * Build the Google Authenticator to use
     * @param name The name of the authenticator
     * @return the authenticator
     */
    private fun BeanDefinitionDsl.buildGoogleAuthenticator(name: String): Authenticator? {
        val googleAuthUrl = env["driftwood.authentication.google.authUrl"]!!
        //val googleTokenUrl = env["driftwood.authentication.google.tokenUrl"]!!
        val googleClientId = env["driftwood.authentication.google.clientId"]!!
        val googleClientSecret = env["driftwood.authentication.google.clientSecret"]!!

        return if (googleClientId.isNotBlank() && googleClientSecret.isNotBlank()) {
            StrategyAuthenticator(
                    startAuthenticationBuilder = GoogleStartAuthenticationBuilder(
                            authUrl = URI(googleAuthUrl),
                            clientId = googleClientId,
                            redirectUriBuilder = RedirectUriBuilder(name),
                            nonceBuilder = UUIDNonceGenerator()
                    ),
                    externalUserLoader = GoogleUserLoader()
            )
        } else {
            null
        }
    }
}
