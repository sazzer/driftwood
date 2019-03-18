package dao_test

import (
	"database/sql"
	"os"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	log "github.com/sirupsen/logrus"
	"github.com/stretchr/testify/suite"

	"github.com/sazzer/driftwood/internal/characters/attributes"
	"github.com/sazzer/driftwood/internal/characters/attributes/dao"
	"github.com/sazzer/driftwood/internal/database"
)

type GetByIDSuite struct {
	suite.Suite
	db   *sql.DB
	mock sqlmock.Sqlmock

	testSubject dao.AttributeDao
}

func (suite *GetByIDSuite) SetupTest() {
	log.SetOutput(os.Stdout)
	log.SetLevel(log.DebugLevel)

	db, mock, err := sqlmock.New(sqlmock.QueryMatcherOption(sqlmock.QueryMatcherEqual))
	if err != nil {
		suite.NoError(err)
	}

	suite.db = db
	suite.mock = mock

	database := database.New(db)
	suite.testSubject = dao.New(&database)

}

func (suite *GetByIDSuite) TearDownSuite() {
	suite.db.Close()
	suite.NoError(suite.mock.ExpectationsWereMet())
}

func TestGetByID(t *testing.T) {
	suite.Run(t, new(GetByIDSuite))
}

func (suite *GetByIDSuite) TestGetUnknownByID() {
	rows := sqlmock.NewRows([]string{})
	suite.mock.ExpectQuery("SELECT * FROM attributes WHERE attribute_id = $1").
		WithArgs("unknown").
		RowsWillBeClosed().
		WillReturnRows(rows)

	attribute, err := suite.testSubject.GetByID(attributes.AttributeID("unknown"))

	suite.Equal(attributes.Attribute{}, attribute)
	suite.NoError(err)
}

func (suite *GetByIDSuite) TestGetKnownByID() {
	now, _ := time.Parse(time.RFC3339, "2019-03-14T07:59:25Z")

	rows := sqlmock.NewRows([]string{"attribute_id", "version", "created", "updated", "name", "description"})
	rows.AddRow("known", "version", now, now, "Strength", "How Strong I am")
	suite.mock.ExpectQuery("SELECT * FROM attributes WHERE attribute_id = $1").
		WithArgs("known").
		RowsWillBeClosed().
		WillReturnRows(rows)

	attribute, err := suite.testSubject.GetByID(attributes.AttributeID("known"))

	suite.Equal(attributes.Attribute{
		ID:          "known",
		Version:     "version",
		Created:     now,
		Updated:     now,
		Name:        "Strength",
		Description: "How Strong I am",
	}, attribute)
	suite.NoError(err)
}
