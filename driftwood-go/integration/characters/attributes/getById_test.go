package attributes_test

import (
	"net/http"
	"net/http/httptest"

	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var _ = Describe("getById", func() {
	Describe("Categorizing book length", func() {
		Context("With more than 300 pages", func() {
			It("should be a novel", func() {
				req := httptest.NewRequest(http.MethodGet, "/health", nil)
				rec := httptest.NewRecorder()

				serverWrapper.HandleRequest(req, rec)

				Expect(rec.Code).To(Equal(200))
			})
		})
	})

})
