// @flow

/**
 * Helper to build a selector that only sees a subset of the store
 * @param path the path to the store subset
 * @param selector the selector that is to be wrapped
 * @return the selector
 */
export function buildSelector(path: Array<string>, selector: function): function {
    return (state) => {
        const statePart = path.reduce((accum, current) => {
            if (typeof accum === 'object') {
                return accum[current];
            } else {
                return accum;
            }
        }, state);

        return statePart && selector(statePart);
    }
}
