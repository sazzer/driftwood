package uk.co.grahamcox.driftwood.e2e.selenium

import org.openqa.selenium.Keys
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

/**
 * Set the value of an input field to the given string
 * This focuses the input, backspaces the string and then types the new one
 * @param value The new value
 */
fun WebElement.setValue(value: String) {
    this.click()
    val currentValue = this.getAttribute("value")
    for (i in 0..currentValue.length) {
        this.sendKeys(Keys.BACK_SPACE)
    }

    value.forEach { this.sendKeys(it.toString()) }
    this.sendKeys(Keys.TAB)
}
