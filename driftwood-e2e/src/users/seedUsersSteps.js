const uuidv4 = require('uuid/v4');
const {Given} = require('cucumber');
const seeder = require('../seeding/seed');

/** The means to seed users */
const userSeeder = seeder('users', {
    'User ID': {
        field: 'user_id',
        defaultValue: uuidv4,
    },
    Version: {
        field: 'version',
        defaultValue: uuidv4,
    },
    Created: {
        field: 'created',
        defaultValue: () => new Date(),
    },
    Updated: {
        field: 'updated',
        defaultValue: () => new Date(),
    },
    Name: {
        field: 'name',
        defaultValue: () => 'Seeded User',
    },
    Email: {
        field: 'email',
        defaultValue: () => 'test@example.com',
    },
    Providers: {
        field: 'authentication',
        defaultValue: () => '[]',
        parser: (input) => {
            const values = input.split(';')
                .map(value => {
                    const [provider, providerId, displayName] = value.split(':');
                    return {provider, providerId, displayName};
                });
            return JSON.stringify(values);
        }
    },
});

Given('a user exists with details:', async function (dataTable) {
    await userSeeder(dataTable.rowsHash());
});
