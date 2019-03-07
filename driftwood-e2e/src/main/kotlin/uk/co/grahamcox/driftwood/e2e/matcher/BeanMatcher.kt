package uk.co.grahamcox.driftwood.e2e.matcher

import org.junit.Assert

/**
 * Mechanism to match a single bean to the provided data
 */
class BeanMatcher<T>(private val fields: Map<String, FieldDefinition<T, *>>) {
    /** Representation of a field that can be matched against */
    data class FieldDefinition<T, R>(
            val extractor: (T) -> R,
            val converter: ((String) -> R)? = null
    )

    /**
     * Match the expected and actual values to see if they differ
     * @param expected The expected values
     * @param actual The actual values
     */
    fun match(expected: Map<String, String>, actual: T) {
        val unexpected = expected.keys
                .filterNot(fields::containsKey)
        Assert.assertTrue("Unexpected fields: $unexpected", unexpected.isEmpty())

        fields.entries
                .filter { expected.containsKey(it.key) }
                .forEach { (field, definition) ->
                    val realValue = definition.extractor(actual)
                    val expectedValue = when(definition.converter) {
                        null -> expected[field]
                        else -> definition.converter.invoke(expected.getValue(field))
                    }

                    Assert.assertEquals("Field didn't match: $field", expectedValue, realValue)
                }
    }
}
