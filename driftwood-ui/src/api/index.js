// @flow

import fetchDefaults from 'fetch-defaults';

/** The Base URI for the API */
const API_URI = process.env.REACT_APP_API_URI || window.DRIFTWOOD_CONFIG.API_URI;

const requester = fetchDefaults(fetch, API_URI, {
    mode: 'cors',
    headers: {
        accept: 'application/json',
    },
});

/** Type representing the details of a request */
export type Request = {
    method?: string,
};

/** Default values for a request, if not otherwise specified */
const DEFAULT_REQUEST = {
    method: 'get',
};

/**
 * Actually make a request to the server
 * @param url the URL to request
 * @param params the request parameters
 * @return the response
 */
export function request(url: string, params: Request = DEFAULT_REQUEST) : Promise<any> {
    const fetchParams = {
        method: params.method || DEFAULT_REQUEST.method,
    };

    return requester(url, fetchParams)
        .catch(e => {
            console.log('Error making HTTP request: ', e);
            throw e;
        })
        .then(response => response.json())
}
