package uk.co.grahamcox.driftwood.service.openid.spring

import io.fusionauth.jwt.hmac.HMACSigner
import io.fusionauth.jwt.hmac.HMACVerifier
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.env.get
import uk.co.grahamcox.driftwood.service.openid.rest.ClientCredentialsArgumentResolver
import uk.co.grahamcox.driftwood.service.openid.rest.TokenController
import uk.co.grahamcox.driftwood.service.openid.scopes.GlobalScopes
import uk.co.grahamcox.driftwood.service.openid.scopes.ScopeRegistry
import uk.co.grahamcox.driftwood.service.openid.token.AccessTokenCreator
import uk.co.grahamcox.driftwood.service.openid.token.JwtAccessTokenSerializerImpl
import java.time.Duration

/**
 * Spring configuration for the OAuth2 and OpenID Connect systems
 */
@Configuration
class OpenIDConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<ClientCredentialsArgumentResolver>()
            bean {
                ScopeRegistry(
                        setOf(
                                GlobalScopes::class.java
                        )
                )
            }
            bean {
                AccessTokenCreator(
                        clock = ref(),
                        duration = Duration.parse(env["driftwood.accessToken.duration"])
                )
            }
            bean {
                JwtAccessTokenSerializerImpl(
                        scopeRegistry = ref(),
                        signer = HMACSigner.newSHA512Signer(env["driftwood.accessToken.key"]),
                        verifier = HMACVerifier.newVerifier(env["driftwood.accessToken.key"])
                )
            }
            bean<TokenController>()
        }.initialize(context)
    }
}
