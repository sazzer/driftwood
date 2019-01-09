<html>
<head>
    <title>Authentication Success</title>
</head>
<body>
    <dl>
        <dt>User ID</dt>
        <dd>${user.identity.id.id}</dd>

        <dt>User Name</dt>
        <dd>${user.data.name}</dd>

        <dt>Access Token</dt>
        <dd>${serialized}</dd>

        <dt>Access Token Issued</dt>
        <dd>${accessToken.issued}</dd>

        <dt>Access Token Expiry</dt>
        <dd>${accessToken.expires}</dd>
    </dl>
</body>
</html>
