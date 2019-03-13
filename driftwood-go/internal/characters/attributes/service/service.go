package service

import (
	"github.com/sazzer/driftwood/internal/characters/attributes/dao"
)

// AttributeService represents the way that we can interact with Attributes
type AttributeService struct {
	dao dao.AttributeDao
}

// New creates a new instance of the Attribute Service
func New(dao dao.AttributeDao) AttributeService {
	return AttributeService{
		dao: dao,
	}
}
