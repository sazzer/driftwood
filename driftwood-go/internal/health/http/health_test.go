package http_test

import (
	"errors"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/golang/mock/gomock"
	"github.com/labstack/echo"
	"github.com/stretchr/testify/suite"
	"github.com/tidwall/gjson"

	"github.com/sazzer/driftwood/internal/health"
	healthHttp "github.com/sazzer/driftwood/internal/health/http"
	"github.com/sazzer/driftwood/internal/health/mocks"
)

type HealthSuite struct {
	suite.Suite
	mockCtrl *gomock.Controller
	checks   map[string]health.Healthchecker
}

func (suite *HealthSuite) SetupTest() {
	suite.mockCtrl = gomock.NewController(suite.T())
	suite.checks = make(map[string]health.Healthchecker)
}

func (suite *HealthSuite) TearDownSuite() {
	suite.mockCtrl.Finish()
}

func TestHealth(t *testing.T) {
	suite.Run(t, new(HealthSuite))
}

func (suite *HealthSuite) TestNoHealthchecks() {
	rec := suite.testGetById()

	suite.Assert().Equal(200, rec.Code)
	suite.Assert().Equal("application/json; charset=UTF-8", rec.Header().Get("Content-Type"))

	suite.Assert().Equal("ok", gjson.Get(rec.Body.String(), "status").String())
}

func (suite *HealthSuite) TestOnePassingHealthchecks() {
	healthcheck := mocks.NewMockHealthchecker(suite.mockCtrl)
	healthcheck.
		EXPECT().
		CheckHealth().
		Return(nil).
		Times(1)

	suite.checks["healthcheck"] = healthcheck

	rec := suite.testGetById()

	suite.Assert().Equal(200, rec.Code)
	suite.Assert().Equal("application/json; charset=UTF-8", rec.Header().Get("Content-Type"))

	suite.Assert().Equal("ok", gjson.Get(rec.Body.String(), "status").String())
}

func (suite *HealthSuite) TestOneFailingHealthchecks() {
	healthcheck := mocks.NewMockHealthchecker(suite.mockCtrl)
	healthcheck.
		EXPECT().
		CheckHealth().
		Return(errors.New("It Failed")).
		Times(1)

	suite.checks["healthcheck"] = healthcheck

	rec := suite.testGetById()

	suite.Assert().Equal(503, rec.Code)
	suite.Assert().Equal("application/json; charset=UTF-8", rec.Header().Get("Content-Type"))

	suite.Assert().Equal("fail", gjson.Get(rec.Body.String(), "status").String())
	suite.Assert().Equal("It Failed", gjson.Get(rec.Body.String(), "errors.healthcheck").String())
}

func (suite *HealthSuite) TestPassingAndFailingHealthchecks() {
	passing := mocks.NewMockHealthchecker(suite.mockCtrl)
	passing.
		EXPECT().
		CheckHealth().
		Return(nil).
		Times(1)
	failing := mocks.NewMockHealthchecker(suite.mockCtrl)
	failing.
		EXPECT().
		CheckHealth().
		Return(errors.New("It Failed")).
		Times(1)

	suite.checks["passing"] = passing
	suite.checks["failing"] = failing

	rec := suite.testGetById()

	suite.Assert().Equal(503, rec.Code)
	suite.Assert().Equal("application/json; charset=UTF-8", rec.Header().Get("Content-Type"))

	suite.Assert().Equal("fail", gjson.Get(rec.Body.String(), "status").String())
	suite.Assert().Equal("It Failed", gjson.Get(rec.Body.String(), "errors.failing").String())
}

func (suite *HealthSuite) testGetById() *httptest.ResponseRecorder {
	e := echo.New()
	req := httptest.NewRequest(http.MethodGet, "/health", nil)
	rec := httptest.NewRecorder()

	healthHttp.RegisterHealthHandler(e, suite.checks)

	e.ServeHTTP(rec, req)

	return rec
}
