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

/** The access token to use */
let accessToken: string | null;

/** Type representing the details of a request */
export type Request = {
    method?: string,
};

/** Type representing the response of a request */
export type Response = {
    body: any,
}

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
export function request(url: string, params: Request = DEFAULT_REQUEST) : Promise<Response> {
    const fetchParams = {
        method: params.method || DEFAULT_REQUEST.method,
        headers: {}
    };

    if (accessToken) {
        fetchParams.headers.authorization = `Bearer ${accessToken}`;
    }

    return requester(url, fetchParams)
        .catch(e => {
            console.log('Error making HTTP request: ', e);
            throw e;
        })
        .then(response => response.json().then(body => {
            return {
                body,
            }
        }))
        .catch(e => {
            console.log(e);
            throw e;
        })
}

/**
 * Set the access token to use for all future requests
 * @param token the token to use
 */
export function setAccessToken(token: string | null) {
    console.log('Setting access token to %s', token);
    accessToken = token;
}
