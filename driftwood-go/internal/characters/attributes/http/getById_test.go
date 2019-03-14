package http_test

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/golang/mock/gomock"
	"github.com/labstack/echo"
	"github.com/stretchr/testify/assert"
	"github.com/tidwall/gjson"

	attributesHttp "github.com/sazzer/driftwood/internal/characters/attributes/http"
	"github.com/sazzer/driftwood/internal/characters/attributes/mocks"
	"github.com/sazzer/driftwood/internal/characters/attributes"
)

func TestGetUnknownByID(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	defer mockCtrl.Finish()
	retriever := mocks.NewMockRetriever(mockCtrl)

	retriever.
		EXPECT().
		GetByID(attributes.AttributeID("unknown")).
		Return(nil).
		Times(1)

		rec := testGetById("unknown", retriever)

	assert.Equal(t, 404, rec.Code)
	assert.Equal(t, "application/problem+json", rec.Header().Get("Content-Type"))

	assert.Equal(t, "tag:2018,grahamcox_co_uk:attributes/problems/not-found", gjson.Get(rec.Body.String(), "type").String())
	assert.Equal(t, "The requested Attribute could not be found", gjson.Get(rec.Body.String(), "title").String())
	assert.Equal(t, int64(404), gjson.Get(rec.Body.String(), "status").Int())
}

func testGetById(id string, retriever attributes.Retriever) *httptest.ResponseRecorder {
	e := echo.New()
	req := httptest.NewRequest(http.MethodGet, "/api/attributes/unknown", nil)
	rec := httptest.NewRecorder()

	attributesHttp.RegisterGetByIDHandler(e, retriever)

	e.ServeHTTP(rec, req)

	return rec
}
