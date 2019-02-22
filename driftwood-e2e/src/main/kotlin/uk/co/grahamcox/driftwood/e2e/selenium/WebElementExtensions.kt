package uk.co.grahamcox.driftwood.e2e.selenium

import org.openqa.selenium.WebElement

/**
 * Extension function to check if a Web Element has a particular CSS Class name
 * @param className The class name to look for
 * @return true if the class name is present. False if not
 */
fun WebElement.hasClassName(className: String) : Boolean {
    val classes = this.getAttribute("class").split(" ")
    return classes.contains(className)
}
