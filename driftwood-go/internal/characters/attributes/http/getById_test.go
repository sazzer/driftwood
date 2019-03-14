package http_test

import (
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/golang/mock/gomock"
	"github.com/labstack/echo"
	"github.com/stretchr/testify/suite"
	"github.com/tidwall/gjson"

	"github.com/sazzer/driftwood/internal/characters/attributes"
	attributesHttp "github.com/sazzer/driftwood/internal/characters/attributes/http"
	"github.com/sazzer/driftwood/internal/characters/attributes/mocks"
)

type GetByIDSuite struct {
	suite.Suite
	mockCtrl  *gomock.Controller
	retriever *mocks.MockRetriever
}

func (suite *GetByIDSuite) SetupTest() {
	suite.mockCtrl = gomock.NewController(suite.T())
	suite.retriever = mocks.NewMockRetriever(suite.mockCtrl)
}

func (suite *GetByIDSuite) TearDownSuite() {
	suite.mockCtrl.Finish()
}

func TestGetByID(t *testing.T) {
	suite.Run(t, new(GetByIDSuite))
}

func (suite *GetByIDSuite) TestGetUnknownByID() {
	suite.retriever.
		EXPECT().
		GetByID(attributes.AttributeID("unknown")).
		Return(nil).
		Times(1)

	rec := suite.testGetById("unknown")

	suite.Assert().Equal(404, rec.Code)
	suite.Assert().Equal("application/problem+json", rec.Header().Get("Content-Type"))

	suite.Assert().Equal("tag:2018,grahamcox_co_uk:attributes/problems/not-found", gjson.Get(rec.Body.String(), "type").String())
	suite.Assert().Equal("The requested Attribute could not be found", gjson.Get(rec.Body.String(), "title").String())
	suite.Assert().Equal(int64(404), gjson.Get(rec.Body.String(), "status").Int())
}

func (suite *GetByIDSuite) TestGetKnownByID() {
	now, _ := time.Parse(time.RFC3339, "2019-03-14T07:59:25Z")

	suite.retriever.
		EXPECT().
		GetByID(attributes.AttributeID("unknown")).
		Return(&attributes.Attribute{
			ID:          attributes.AttributeID("known"),
			Version:     "version",
			Created:     now,
			Updated:     now,
			Name:        "Strength",
			Description: "How strong you are",
		}).
		Times(1)

	rec := suite.testGetById("known")

	suite.Assert().Equal(200, rec.Code)
	suite.Assert().Equal("application/json; charset=UTF-8", rec.Header().Get("Content-Type"))

	suite.Assert().Equal("known", gjson.Get(rec.Body.String(), "id").String())
	suite.Assert().Equal("Strength", gjson.Get(rec.Body.String(), "name").String())
	suite.Assert().Equal("How strong you are", gjson.Get(rec.Body.String(), "description").String())
}

func (suite *GetByIDSuite) testGetById(id string) *httptest.ResponseRecorder {
	e := echo.New()
	req := httptest.NewRequest(http.MethodGet, "/api/attributes/unknown", nil)
	rec := httptest.NewRecorder()

	attributesHttp.RegisterGetByIDHandler(e, suite.retriever)

	e.ServeHTTP(rec, req)

	return rec
}
