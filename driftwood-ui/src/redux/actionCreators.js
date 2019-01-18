// @flow

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
