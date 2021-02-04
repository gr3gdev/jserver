context('JServer tests : react integration', () => {

  beforeEach(() => {
    cy.visit('http://localhost:9000/react')
  })

  it('Access /react', () => {
    cy.url().should('include', '/react')
  })

  it('Actions with state', () => {
    cy.get('#field')
      .should('be.visible')
      .and('be.disabled')
      .and('be.empty')

    cy.get('#button').click()

    cy.get('#field')
      .should('be.visible')
      .and('not.be.disabled')
      .and('be.empty')

    cy.get('#button').click()

    cy.get('#field')
      .should('be.visible')
      .and('be.disabled')
      .and('be.empty')

    cy.get('#button').click()

    cy.get('#field').type('test')

    cy.get('#field')
      .should('be.visible')
      .and('not.be.disabled')
      .and('have.value', 'test')

    cy.get('#button').click()

    cy.get('#field')
      .should('be.visible')
      .and('be.disabled')
      .and('have.value', 'test')
  })

})
