context('JServer tests : security', () => {

  it('Redirect to /login', () => {
    cy.visit('http://localhost:9000/secure')

    cy.url().should('include', '/login')
  })

  it('User connection failed', () => {
    cy.visit('http://localhost:9000/login')

    cy.get('input[name="username"]')
      .type('inconnu')
    cy.get('input[name="password"]')
      .type('inconnu')
    cy.get('input[type="submit"]').click()

    cy.url().should('include', '/login')
  })

  it('User connection', () => {
    cy.visit('http://localhost:9000/login')

    cy.get('input[name="username"]')
      .type('user')
    cy.get('input[name="password"]')
      .type('password')
    cy.get('input[type="submit"]').click()

    cy.url().should('include', '/secure')

    cy.get('h1').contains('Access secure works !')
  })

})
