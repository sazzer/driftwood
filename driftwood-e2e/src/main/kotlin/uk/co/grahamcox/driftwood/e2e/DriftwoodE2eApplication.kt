package uk.co.grahamcox.driftwood.e2e

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.internal.TextListener
import org.junit.runner.JUnitCore
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
        tags = ["not @ignore", "@wip"],
        strict = false,
        junit = ["--step-notifications"]
)
class Wip

@RunWith(Cucumber::class)
@CucumberOptions(
        tags = ["not @ignore", "not @wip"],
        strict = true,
        junit = ["--step-notifications"]
)
class All

/**
 * Entrypoint into the application
 * @param args The commandline arguments
 */
fun main(args: Array<String>) {
    val junit = JUnitCore()
    junit.addListener(TextListener(System.out))
    val result = junit.run(All::class.java)

    if (!result.wasSuccessful()) {
        System.exit(1)
    }
}
