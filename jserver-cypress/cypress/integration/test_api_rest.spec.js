context('JServer tests : react integration', () => {

  it('Get persons (empty)', () => {
    cy.request('GET', 'http://localhost:9000/api/persons')
      .then(response => {
                  expect(response.status).to.eq(200)
                  expect(response.body).to.have.length(0)
              })
  })

  it('Add a person', () => {
    cy.request('PUT', 'http://localhost:9000/api/persons', {
      name: 'Person1'
    })
      .then(response => {
                  expect(response.status).to.eq(201)
                  expect(response.body).to.have.property('id', 1)
                  expect(response.body).to.have.property('name', 'Person1')
              })
  })

  it('Update a person', () => {
    cy.request('PUT', 'http://localhost:9000/api/persons', {
      id: 1,
      name: 'Person1 renamed'
    })
      .then(response => {
                  expect(response.status).to.eq(200)
                  expect(response.body).to.have.property('id', 1)
                  expect(response.body).to.have.property('name', 'Person1 renamed')
              })
  })

  it('Get persons', () => {
    cy.request('GET', 'http://localhost:9000/api/persons')
      .then(response => {
                  expect(response.status).to.eq(200)
                  expect(response.body).to.have.length(1)
                  expect(response.body[0]).to.have.property('id', 1)
                  expect(response.body[0]).to.have.property('name', 'Person1 renamed')
              })
  })

  it('Get person 1', () => {
    cy.request('GET', 'http://localhost:9000/api/persons/1')
      .then(response => {
                  expect(response.status).to.eq(200)
                  expect(response.body).to.have.property('id', 1)
                  expect(response.body).to.have.property('name', 'Person1 renamed')
              })
  })

})
