# External Authentication

## Google

### Start Authentication

Redirect User to:

* URL: https://accounts.google.com/o/oauth2/v2/auth?

Parameters: 

| Parameter     | Value                                                                    | Notes                      |
|---------------|--------------------------------------------------------------------------|----------------------------|
| client_id     | 484625354845-d8v3pkn5corneerhbh4c03dg42nvj360.apps.googleusercontent.com | Obtained from Config       |
| response_type | code                                                                     | Fixed Value                |
| scope         | openid email profile                                                     | Fixed Value                |
| redirect_uri  | http://localhost:8080/api/authentication/external/google/callback	       | Obtained from Config       |
| state         | 7C262CE9-C730-4BED-9F05-EA2CE0E51C27                                     | Generated for this request |

### Finish Authentication

User redirected back to:

* URL: http://localhost:8080/ump/api/authentication/external/google/redirect

Parameters:

| Parameter | Value                                         | Notes                      |
|-----------|-----------------------------------------------|----------------------------|
| state     | 7C262CE9-C730-4BED-9F05-EA2CE0E51C27          | Same as above              |
| code      | 4/FNMfQoF_k5vaNy9H5jM4KRRD8lUvcohz1HFLRBLNNLY | Generated for this request |

Server makes call to:

* URL: https://www.googleapis.com/oauth2/v4/token
* Method: POST

Parameters:

| Parameter     | Value                                                                    | Notes                |
|---------------|--------------------------------------------------------------------------|----------------------|
| code          | 4/FNMfQoF_k5vaNy9H5jM4KRRD8lUvcohz1HFLRBLNNLY                            | Same as above        |
| client_id     | 484625354845-d8v3pkn5corneerhbh4c03dg42nvj360.apps.googleusercontent.com | Obtained from Config |
| client_secret | smPtH3V7rAFUPQ3qZ48Txa8a                                                 | Obtained from Config |
| redirect_uri  | http://localhost:8080/api/authentication/external/google/callback	       | Obtained from Config |
| grant_type    | authorization_code                                                       | Fixed Value          |

Response is:

```json
{
 "access_token": "ya29.Ci-rA6iOA1pAk2ZrgkbTz8Hbznd6i-4-WK9W83QTr_6f0iFmkHj5dKwhoBdNXhp_vQ",
 "token_type": "Bearer",
 "expires_in": 3600,
 "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6ImQ5Y2ZlNDE5MWZkODUyNDEyOTQxNDY2ZGU2ODgwYTM5ODA1MWI3M2MifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJpYXQiOjE0ODA5NDUxNzksImV4cCI6MTQ4MDk0ODc3OSwiYXRfaGFzaCI6ImNMamgybE9vbmlSWVU1MTJrMTBUYXciLCJhdWQiOiI5MjQzMDE3MDU3NzMtbGI4Z2hqbzRyMnRjOWxkYnZzMDM5Zms2aTVjNGhzZzYuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTY0NDAwOTc3MTc2OTI0OTcyNjQiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXpwIjoiOTI0MzAxNzA1NzczLWxiOGdoam80cjJ0YzlsZGJ2czAzOWZrNmk1YzRoc2c2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiaGQiOiJncmFoYW1jb3guY28udWsiLCJlbWFpbCI6ImdyYWhhbUBncmFoYW1jb3guY28udWsifQ.EFm0t2yL3Ar7SeN1Tp72t8EHZKgBEu5cKbsSHL8yJ44o_uOsFSvCk0odfEFkVmShaXkehKo1YIZaKx4_FGCsaghLYFHKh-KE7GrnlVTyq6DDHT0-CVI4bMOrTrJaPQ_GQdOEwy6mRDfyquvs6LXqWwlV_E6SgIxmPb_0tGOVh2wzaXQky2SeUJaOcG4vJKMMmSqTT2RSYK9Om_LTTiLyzV6jRpWRU0RZHu0fqnB8j3aIRF0st20dRdhOq4C7QSF3zsPvDBKH1rBodBbbMWDdn2c9gUEyKrEX9qSWRdzbZcNH4ER2tOjMuykIC2MMtlplMR-tUxz6l_UwJ4xtf30evg"
}
```

### Worked Example

Redirect to: https://accounts.google.com/o/oauth2/v2/auth?client_id=484625354845-d8v3pkn5corneerhbh4c03dg42nvj360.apps.googleusercontent.com&response_type=code&scope=openid+email+profile&redirect_uri=http://localhost:8080/api/authentication/external/google/callback&state=7C262CE9-C730-4BED-9F05-EA2CE0E51C27

User logs in

Redirected back to: http://localhost:8080/api/authentication/external/google/callback?state=7C262CE9-C730-4BED-9F05-EA2CE0E51C27&code=4/xQAON3DSDJA7OqyQrdcsutjEAv4bNUwBAucVujZSpVOmEipgIRKNaO9o29N8yb4tIBjCFZpqasR9EIXzJXUo4J8&scope=openid%20email%20profile%20https://www.googleapis.com/auth/plus.me%20https://www.googleapis.com/auth/userinfo.profile%20https://www.googleapis.com/auth/userinfo.email&authuser=1&hd=grahamcox.co.uk&session_state=0e4fec8189d0a0478b8b62e673eebd77b05875bc..8f1a&prompt=none

```
> $ curl -v -X POST https://www.googleapis.com/oauth2/v4/token --data "code=4/xQAON3DSDJA7OqyQrdcsutjEAv4bNUwBAucVujZSpVOmEipgIRKNaO9o29N8yb4tIBjCFZpqasR9EIXzJXUo4J8&client_id=484625354845-d8v3pkn5corneerhbh4c03dg42nvj360.apps.googleusercontent.com&client_secret=smPtH3V7rAFUPQ3qZ48Txa8a&redirect_uri=http://localhost:8080/api/authentication/external/google/callback&grant_type=authorization_code"
*   Trying 216.58.208.138...
* TCP_NODELAY set
* Connected to www.googleapis.com (216.58.208.138) port 443 (#0)
* ALPN, offering h2
* ALPN, offering http/1.1
* Cipher selection: ALL:!EXPORT:!EXPORT40:!EXPORT56:!aNULL:!LOW:!RC4:@STRENGTH
* successfully set certificate verify locations:
*   CAfile: /etc/ssl/cert.pem
  CApath: none
* TLSv1.2 (OUT), TLS handshake, Client hello (1):
* TLSv1.2 (IN), TLS handshake, Server hello (2):
* TLSv1.2 (IN), TLS handshake, Certificate (11):
* TLSv1.2 (IN), TLS handshake, Server key exchange (12):
* TLSv1.2 (IN), TLS handshake, Server finished (14):
* TLSv1.2 (OUT), TLS handshake, Client key exchange (16):
* TLSv1.2 (OUT), TLS change cipher, Client hello (1):
* TLSv1.2 (OUT), TLS handshake, Finished (20):
* TLSv1.2 (IN), TLS change cipher, Client hello (1):
* TLSv1.2 (IN), TLS handshake, Finished (20):
* SSL connection using TLSv1.2 / ECDHE-ECDSA-AES128-GCM-SHA256
* ALPN, server accepted to use h2
* Server certificate:
*  subject: C=US; ST=California; L=Mountain View; O=Google LLC; CN=*.googleapis.com
*  start date: Dec  4 09:33:00 2018 GMT
*  expire date: Feb 26 09:33:00 2019 GMT
*  subjectAltName: host "www.googleapis.com" matched cert's "*.googleapis.com"
*  issuer: C=US; O=Google Trust Services; CN=Google Internet Authority G3
*  SSL certificate verify ok.
* Using HTTP2, server supports multi-use
* Connection state changed (HTTP/2 confirmed)
* Copying HTTP/2 data in stream buffer to connection buffer after upgrade: len=0
* Using Stream ID: 1 (easy handle 0x7f8cf2800600)
> POST /oauth2/v4/token HTTP/2
> Host: www.googleapis.com
> User-Agent: curl/7.54.0
> Accept: */*
> Content-Length: 325
> Content-Type: application/x-www-form-urlencoded
>
* Connection state changed (MAX_CONCURRENT_STREAMS updated)!
* We are completely uploaded and fine
< HTTP/2 200
< content-type: application/json; charset=utf-8
< vary: X-Origin
< vary: Referer
< vary: Origin,Accept-Encoding
< date: Tue, 01 Jan 2019 15:29:02 GMT
< server: ESF
< cache-control: private
< x-xss-protection: 1; mode=block
< x-frame-options: SAMEORIGIN
< x-content-type-options: nosniff
< alt-svc: quic=":443"; ma=2592000; v="44,43,39,35"
< accept-ranges: none
<
{
  "access_token": "ya29.Gl2EBqYQ0F-32zy1fg8EQmBDeBUa_q8vOXqRxzyekNDZ_99qVw3i0w2uMjTVAwxcuZyoucBqMS40IbAq-wMTKF9VYrgKSJ0zxUphkNvJ4pMSE1xNiSJjksf2k2FOuSU",
  "expires_in": 3503,
  "scope": "https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
  "token_type": "Bearer",
  "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijc5NzhhOTEzNDcyNjFhMjkxYmQ3MWRjYWI0YTQ2NGJlN2QyNzk2NjYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0ODQ2MjUzNTQ4NDUtZDh2M3BrbjVjb3JuZWVyaGJoNGMwM2RnNDJudmozNjAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0ODQ2MjUzNTQ4NDUtZDh2M3BrbjVjb3JuZWVyaGJoNGMwM2RnNDJudmozNjAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTY0NDAwOTc3MTc2OTI0OTcyNjQiLCJoZCI6ImdyYWhhbWNveC5jby51ayIsImVtYWlsIjoiZ3JhaGFtQGdyYWhhbWNveC5jby51ayIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiQ3pJUjdwVlJFZGR2czJKZF9RMUM0dyIsIm5hbWUiOiJHcmFoYW0gQ294IiwicGljdHVyZSI6Imh0dHBzOi8vbGg0Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tcHV3aXctbG14VGsvQUFBQUFBQUFBQUkvQUFBQUFBQUFBQ1UvNkxSVk56UjliMm8vczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6IkdyYWhhbSIsImZhbWlseV9uYW1lIjoiQ294IiwibG9jYWxlIjoiZW4tR0IiLCJpYXQiOjE1NDYzNTY1NDIsImV4cCI6MTU0NjM2MDE0Mn0.XbqHQute2DO_XuI8qnAfaFsu8FFoIVUgDcKe5d7A6LgnekJ_bkFKiaYWjid6zbGrvaFvOnn-DoK0EFBFaQvQVxY_fE5EV9loButRNuKQVI-o2-6yeCzMhHaVtesDwkAjQ93RZpCFzGaFJlR6tW63JjIiTSedfWUaJ0_ebJsFRJGyNJo4KBn66fvyeCCaMjwJt58oGcKYZI6uiJMNMWuO4ej36sB-210GOTDFgo0pontC1pjQ8dIoRKPrSQtZBfhk1G0eWoly0j8TKjuaskOGkbsKj9GYJ3qk8j79o4qhCI2lFOjN7pCoWym-IXJLxOz4JafazpgMBlRjdhSuCSHiIw"
}
```

Decoded id_token payload:
```json
{
  "iss": "https://accounts.google.com",
  "azp": "484625354845-d8v3pkn5corneerhbh4c03dg42nvj360.apps.googleusercontent.com",
  "aud": "484625354845-d8v3pkn5corneerhbh4c03dg42nvj360.apps.googleusercontent.com",
  "sub": "xxxxxx",
  "hd": "grahamcox.co.uk",
  "email": "graham@grahamcox.co.uk",
  "email_verified": true,
  "at_hash": "CzIR7pVREddvs2Jd_Q1C4w",
  "name": "Graham Cox",
  "picture": "https://lh4.googleusercontent.com/-puwiw-lmxTk/AAAAAAAAAAI/AAAAAAAAACU/6LRVNzR9b2o/s96-c/photo.jpg",
  "given_name": "Graham",
  "family_name": "Cox",
  "locale": "en-GB",
  "iat": 1546356542,
  "exp": 1546360142
}
```

## Twitter

TODO: Details here

## Facebook

TODO: Details here

## Instagram

TODO: Details here
