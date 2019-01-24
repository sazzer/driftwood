import HomePage from "../pages/HomePage";

describe('The Homepage', function() {
    it('Loads', function() {
        const homepage = new HomePage()
            .visit();
        const header = homepage.header;
        header.title.should('equal', 'Driftwood');
    });
});
