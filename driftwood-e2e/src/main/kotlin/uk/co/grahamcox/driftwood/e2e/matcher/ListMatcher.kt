package uk.co.grahamcox.driftwood.e2e.matcher

import org.junit.Assert

/**
 * Match a list of actual objects to a list of expected values
 */
class ListMatcher<T>(private val beanMatcher: BeanMatcher<T>) {
    /**
     * Match the expected and actual values to see if they differ
     * @param expectedList The expected values
     * @param actualList The actual values
     */
    fun matchExact(expectedList: List<Map<String, String>>, actualList: List<T>) {
        Assert.assertEquals(expectedList.size, actualList.size)

        expectedList.zip(actualList)
                .forEach { (expected, actual) -> beanMatcher.match(expected, actual) }
    }
}
