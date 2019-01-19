// @flow

/** Create an action type to indicate that the async action has started */
export const startedAction = (action: string) => action + '_STARTED';

/** Create an action type to indicate that the async action has finished */
export const finishedAction = (action: string) => action + '_FINISHED';

/** Create an action type to indicate that the async action has succeeded */
export const succeededAction = (action: string) => action + '_SUCCEEDED';

/** Create an action type to indicate that the async action has failed */
export const failedAction = (action: string) => action + '_FAILED';
