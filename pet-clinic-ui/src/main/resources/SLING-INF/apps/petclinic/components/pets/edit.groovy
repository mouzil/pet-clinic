import org.apache.sling.api.resource.ValueMap

def suffix = request.getRequestPathInfo().getSuffix()
def resourceResolver = resource.getResourceResolver()
def petResource = resourceResolver.getResource(suffix);
def petProps = petResource.adaptTo(ValueMap.class);
def ownerResource = petResource.getParent().getParent()
def ownerProps = ownerResource.adaptTo(ValueMap.class)
def petTypesResource = resourceResolver.getResource('/sling/content/petTypes')

markupBuilder.form(class: 'ui form', role: 'form', action: "${petResource.getPath()}", method: 'POST') {
  div(class: 'field') {
    label(for: 'firstName', 'First Name:')
    input(id: 'firstName', name: 'firstName', type: 'text',
        placeholder: "${ownerProps.get('firstName')} ${ownerProps.get('lastName')}", readonly: 'true')
  }
  div(class: 'field') {
    label(for: 'name', 'Name:')
    input(id: 'name', name: 'name', type: 'text', placeholder: 'Name',
        value: "${petProps.get('name')}")
  }
  div(class: 'field') {
    label(for: 'birthDate', 'Birth Date:')
    input(id: 'birthDate', name: 'birthDate', type: 'text', placeholder: 'dd/MM/yy',
        value: "${petProps.get('birthDate')}")
  }
  div(class: 'grouped inline fields ui segment') {
    petTypesResource.listChildren().each { petTypeResource ->
      def petTypeProperties = petTypeResource.adaptTo(ValueMap.class)
      div(class: 'field') {
        div(class: 'ui radio checkbox') {
          if (petTypeResource.getPath() == petProps.get('typeId'))
            input(type: 'radio', name: 'typeId', value: "${petTypeResource.getPath()}", checked: 'checked')
          else
            input(type: 'radio', name: 'typeId', value: "${petTypeResource.getPath()}")
          label("${petTypeProperties.get('name')}")
        }
      }
    }
  }
  input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/pet')
  input(type: 'hidden', name: ':redirect',
      value: "/content/petclinic/en/owners.detail.html${ownerResource.getPath()}")
  input(type: 'hidden', name: '_charset_', value: 'UTF-8')
  button(type: 'submit', class: 'ui blue submit button', 'Save')
}
