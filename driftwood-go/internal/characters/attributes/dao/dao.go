package dao

//go:generate mockgen -destination=mocks/mock_dao.go -package=mocks github.com/sazzer/driftwood/internal/characters/attributes/dao AttributeDao

import (
	"github.com/sazzer/driftwood/internal/characters/attributes"
)

// AttributeDao represents the means to access Attributes in the Database
type AttributeDao interface {
	GetByID(id attributes.AttributeID) *attributes.Attribute
}
