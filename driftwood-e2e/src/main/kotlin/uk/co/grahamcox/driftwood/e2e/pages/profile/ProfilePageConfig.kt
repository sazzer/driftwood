package uk.co.grahamcox.driftwood.e2e.pages.profile

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.e2e.matcher.BeanMatcher
import uk.co.grahamcox.driftwood.e2e.matcher.ListMatcher

/**
 * Configuration for working with the Profile Page
 */
@Configuration
class ProfilePageConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("accountDetailsPageModel") {
                BeanMatcher(
                        mapOf(
                                "Name" to BeanMatcher.FieldDefinition(
                                        extractor = AccountDetailsPageModel::screenName
                                ),
                                "Email" to BeanMatcher.FieldDefinition(
                                        extractor = AccountDetailsPageModel::email
                                )
                        )
                )
            }
            bean("loginProvidersMatcher") {
                ListMatcher(
                        BeanMatcher(
                                mapOf(
                                        "Provider" to BeanMatcher.FieldDefinition(
                                                extractor = LoginProvider::providerName
                                        ),
                                        "Display Name" to BeanMatcher.FieldDefinition(
                                                extractor = LoginProvider::displayName
                                        )
                                )
                        )
                )
            }
        }.initialize(context)
    }
}
