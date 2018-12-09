# OpenID Connect Support

## Supported Flows

### OIDC - Authorization Code Flow

**Todo - Future Work**

### OIDC - Implicit Flow

**Todo - Future Work**

### OAuth2 - Client Credentials Grant

Client makes a call to the Token Endpoint, providing:

* grant_type: client_credentials

Client receives a response containing:

* access_token
* token_type
* expires_in

## Supported APIs

### Authorization Endpoint

**Todo - Future Work**

### Token Endpoint

* URL: /api/oauth2/token
* Method: POST

#### Client Credentials Grant

| Parameter  | Location | Type   | Value                          |
|------------|----------|--------|--------------------------------|
| grant_type | Body     | String | client_credentials             |
| scope      | Body     | String | Comma separated list of scopes |

#### Authorization Code Grant

| Parameter    | Location | Type   | Value                                                  |
|--------------|----------|--------|--------------------------------------------------------|
| grant_type   | Body     | String | authorization_code                                     |
| code         | Body     | String | The Authorization Code to exchange for an Access Token |
| redirect_uri | Body     | String | The Redirect URI used to generate the code             |

#### Refresh Token Grant

| Parameter     | Location | Type   | Value                                                  |
|---------------|----------|--------|--------------------------------------------------------|
| grant_type    | Body     | String | refresh_token                                          |
| refresh_token | Body     | String | The refresh token to generate the new access token for |
| scope         | Body     | String | Comma separated list of scopes                         |
