import HomePage from "../../pages/HomePage";

describe('Authentication using Google', function() {
    let homepage;

    beforeEach(function() {
        homepage = new HomePage()
            .visit();
    });

    it('is a valid option', function() {
        const header = homepage.header;
        const loginMenu = header.loginMenu;

        loginMenu.open();

        loginMenu.assertProviderPresent('google');
    });

    it('Successfully logs in', function() {
        const header = homepage.header;
        const loginMenu = header.loginMenu;

        loginMenu.authenticateUsing('google');

        header.profileMenu.assertUsersName('Test User');
    });
});
