package health_test

import (
	"net/http"
	"net/http/httptest"

	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var _ = Describe("Health", func() {
	It("should be successful", func() {
		req := httptest.NewRequest(http.MethodGet, "/health", nil)
		rec := httptest.NewRecorder()

		serverWrapper.HandleRequest(req, rec)

		Expect(rec.Code).To(Equal(200))
	})

})
