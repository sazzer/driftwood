// @flow

/**
 * Helper to build action names, with optional namespace and suffix
 * @param type the action type
 * @param namespace the namespace, if there is one
 * @param suffix the suffix, if there is one
 * @return {string} the action name
 */
export function buildActionName(type: string, namespace: ?string, suffix: ?string) : string {
    return [namespace, type, suffix]
        .filter(v => v !== undefined)
        .join('/');
}

/**
 * Create a simple action creator
 * @param type the type of the action
 * @return the action creator
 */
export function createAction(type: string) : function {
    return (payload) => (
        {
            type,
            payload,
        }
    );
}
