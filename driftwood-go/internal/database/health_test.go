package database_test

import (
	"database/sql"
	"errors"
	"testing"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/suite"

	"github.com/sazzer/driftwood/internal/database"
)

type HealthSuite struct {
	suite.Suite
	db   *sql.DB
	mock sqlmock.Sqlmock

	testSubject database.DB
}

func (suite *HealthSuite) SetupTest() {
	db, mock, err := sqlmock.New()
	if err != nil {
		suite.NoError(err)
	}

	suite.db = db
	suite.mock = mock

	suite.testSubject = database.New(db)
}

func (suite *HealthSuite) TearDownSuite() {
	suite.db.Close()
	suite.NoError(suite.mock.ExpectationsWereMet())
}

func TestHealth(t *testing.T) {
	suite.Run(t, new(HealthSuite))
}

func (suite *HealthSuite) TestSuccessfulHealthcheck() {
	suite.mock.ExpectExec("SELECT 1").WillReturnResult(sqlmock.NewResult(0, 0))

	err := suite.testSubject.CheckHealth()
	suite.NoError(err)
}

func (suite *HealthSuite) TestUnsuccessfulHealthcheck() {
	suite.mock.ExpectExec("SELECT 1").WillReturnError(errors.New("Oops"))

	err := suite.testSubject.CheckHealth()
	suite.Error(err)
	suite.EqualError(err, "Oops")
}
