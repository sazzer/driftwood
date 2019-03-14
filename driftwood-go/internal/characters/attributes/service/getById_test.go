package service_test

import (
	"os"
	"testing"
	"time"

	"github.com/golang/mock/gomock"
	log "github.com/sirupsen/logrus"
	"github.com/stretchr/testify/suite"

	"github.com/sazzer/driftwood/internal/characters/attributes"
	"github.com/sazzer/driftwood/internal/characters/attributes/dao/mocks"
	"github.com/sazzer/driftwood/internal/characters/attributes/service"
)

type GetByIDSuite struct {
	suite.Suite
	mockCtrl *gomock.Controller
	dao      *mocks.MockAttributeDao
}

func (suite *GetByIDSuite) SetupTest() {
	log.SetOutput(os.Stdout)
	log.SetLevel(log.DebugLevel)

	suite.mockCtrl = gomock.NewController(suite.T())
	suite.dao = mocks.NewMockAttributeDao(suite.mockCtrl)
}

func (suite *GetByIDSuite) TearDownSuite() {
	suite.mockCtrl.Finish()
}

func TestGetByID(t *testing.T) {
	suite.Run(t, new(GetByIDSuite))
}

func (suite *GetByIDSuite) TestGetUnknownByID() {
	suite.dao.
		EXPECT().
		GetByID(attributes.AttributeID("unknown")).
		Return(attributes.Attribute{}, nil).
		Times(1)

	service := service.New(suite.dao)

	result, err := service.GetByID(attributes.AttributeID("unknown"))

	suite.Assert().Error(err, attributes.UnknownAttributeError{})

	suite.Assert().Equal(attributes.Attribute{}, result)
}

func (suite *GetByIDSuite) TestGetKnownByID() {
	now, _ := time.Parse(time.RFC3339, "2019-03-14T07:59:25Z")

	attribute := attributes.Attribute{
		ID:          attributes.AttributeID("known"),
		Version:     "version",
		Created:     now,
		Updated:     now,
		Name:        "Strength",
		Description: "How strong you are",
	}

	suite.dao.
		EXPECT().
		GetByID(attributes.AttributeID("known")).
		Return(attribute, nil).
		Times(1)

	service := service.New(suite.dao)

	result, err := service.GetByID(attributes.AttributeID("known"))

	suite.Assert().NoError(err)

	suite.Assert().Equal(attribute, result)
}
