package service

import (
	log "github.com/sirupsen/logrus"

	"github.com/sazzer/driftwood/internal/characters/attributes"
)

// GetByID represents a way that we can retrieve an Attribute by it's unique ID
func (service AttributeService) GetByID(id attributes.AttributeID) (attributes.Attribute, error) {
	log.WithFields(log.Fields{"id": id}).Debug("Getting Attribute by ID")
	attribute, err := service.dao.GetByID(id)

	if err != nil {
		log.WithFields(log.Fields{"id": id}).WithError(err).Error("Failed to get Attribute by ID")
		return attributes.Attribute{}, attributes.UnknownAttributeError{}
	}

	if (attribute == attributes.Attribute{}) {
		log.WithFields(log.Fields{"id": id}).Debug("No Attribute found with given ID")
		return attributes.Attribute{}, attributes.UnknownAttributeError{}
	}

	log.WithFields(log.Fields{"id": id, "attribute": attribute}).Debug("Fetched Attribute by ID")

	return attribute, nil
}
