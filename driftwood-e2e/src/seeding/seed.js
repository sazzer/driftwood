const debug = require('debug')('driftwood');
const {Maybe} = require('monet');
const {update} = require('./db');

/**
 * Create the seeder to use
 * @param table the table to seed
 * @param fields the fields to seed
 * @return {Function} the seeder function
 */
function seeder(table, fields) {
    return async function(params) {
        const fieldNames = [];
        const binds = [];
        const values = [];

        Object.keys(fields).forEach(fieldKey => {
            const field = fields[fieldKey];

            const defaultProvider = Maybe.fromUndefined(field.defaultValue)
                .getOrElse(() => undefined);

            const value = Maybe.fromUndefined(params[fieldKey])
                .map(input => {
                    if (field.parser) {
                        const parsed = field.parser(input);
                        debug('Parsed value %s for field %s into %s', input, fieldKey, parsed);
                        return parsed;
                    } else {
                        return input;
                    }
                })
                .orLazy(defaultProvider);

            fieldNames.push(field.field);
            binds.push('$' + (binds.length + 1));
            values.push(value);

            debug('Field %s has value %s', field.field, value);
        });

        debug('Field names: %s, Binds: %s, Values: %s', fieldNames, binds, values);

        const sql = 'INSERT INTO ' + table
            + '(' + fieldNames.join(', ') + ')'
            + ' VALUES (' + binds.join(', ') + ')';

        await update(sql, values);
    }
}

module.exports = seeder;
