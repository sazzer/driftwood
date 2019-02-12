// @flow

import axios from 'axios';

/** The Base URI for the API */
const API_URI = process.env.REACT_APP_API_URI || window.DRIFTWOOD_CONFIG.API_URI;

/** The access token to use */
let accessToken: string | null;

/** Type representing the details of a request */
export type Request = {
    method?: string,
    data?: any,
};

/** Type representing the response of a request */
export type Response = {
    body: any,
    status: number,
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
        url,
        baseURL: API_URI,
        method: params.method || DEFAULT_REQUEST.method,
        headers: {},
        data: undefined,
    };

    if (accessToken) {
        fetchParams.headers.authorization = `Bearer ${accessToken}`;
    }

    if (params.data) {
        fetchParams.data = params.data;
        fetchParams.headers['content-type'] = 'application/json';
    }

    return axios(fetchParams)
        .catch(e => {
            console.log('Error making HTTP request: ', e);
            throw e;
        })
        .then(response => (
            {
                status: response.status,
                headers: response.headers,
                body: response.data,
            }
        ));
}

/**
 * Set the access token to use for all future requests
 * @param token the token to use
 */
export function setAccessToken(token: string | null) {
    console.log('Setting access token to %s', token);
    accessToken = token;
}
