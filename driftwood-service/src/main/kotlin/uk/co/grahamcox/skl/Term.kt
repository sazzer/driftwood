package uk.co.grahamcox.skl

/**
 * Format up a term to use in a clause
 * @param term The term to format
 * @return the formatted term
 */
fun formatTerm(term: Any): String = when (term) {
    is String -> "'$term'"
    is BindTerm -> ":${term.name}"
    is FieldTerm -> {
        if (term.tableName == null) {
            term.fieldName
        } else {
            "${term.tableName}.${term.fieldName}"
        }
    }
    is CastTerm -> "${formatTerm(term.term)}::${term.castTo}"
    else -> term.toString()
}
