package dao

// AttributeDao represents the means to access Attributes in the Database
type AttributeDao struct {
}

// New creates a new instance of the Attribute DAO
func New() AttributeDao {
	return AttributeDao{}
}
