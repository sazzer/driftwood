import HomePage from "../../pages/HomePage";

describe('The Authentication Menu', function() {
    let homepage;

    beforeEach(function() {
        homepage = new HomePage()
            .visit();
    });

    it('Appears when loading the home page', function() {
        const header = homepage.header;
        const loginMenu = header.loginMenu;

        loginMenu.assertIsClosed();
    });

    it('Can be opened', function() {
        const header = homepage.header;
        const loginMenu = header.loginMenu;

        loginMenu.open();

        loginMenu.assertIsOpen();
    });

    it('Has the expected providers', function() {
        const header = homepage.header;
        const loginMenu = header.loginMenu;

        loginMenu.open();

        loginMenu.assertProviderPresent('google');
    });
});
