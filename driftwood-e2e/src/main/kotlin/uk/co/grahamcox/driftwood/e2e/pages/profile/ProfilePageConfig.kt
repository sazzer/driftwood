package uk.co.grahamcox.driftwood.e2e.pages.profile

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.e2e.matcher.BeanMatcher
import uk.co.grahamcox.driftwood.e2e.matcher.ListMatcher
import uk.co.grahamcox.driftwood.e2e.populator.BeanPopulator

/**
 * Configuration for working with the Profile Page
 */
@Configuration
class ProfilePageConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("accountDetailsBeanMatcher") {
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
            bean("accountDetailsPopulator") {
                BeanPopulator(
                        mapOf(
                                "Name" to BeanPopulator.FieldDefinition(
                                        populator = AccountDetailsPageModel::screenName
                                ),
                                "Email" to BeanPopulator.FieldDefinition(
                                        populator = AccountDetailsPageModel::email
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
