context('JServer tests', () => {

  before(() => {
    cy.exec('../jserver-samples/build/install/jserver-samples/bin/jserver-samples > /dev/null 2>&1 &')
    cy.wait(1000)
  })

  it('Redirect /login', () => {
    cy.visit('http://localhost:9000/secure')

    cy.url().should('include', '/login')

    cy.get('input[name="username"]')
        .should('be.visible')
        .and('be.empty')

    cy.get('input[name="password"]')
        .should('be.visible')
        .and('be.empty')

    cy.get('input[type="submit"]')
        .should('contain', 'Login')
  })

  it('Login', () => {
    cy.visit('http://localhost:9000/secure')

    cy.get('input[name="username"]')
        .type('user')

    cy.get('input[name="password"]')
        .type('password')

    cy.get('input[type="submit"]')
        .click()

    cy.url().should('include', '/secure')

    cy.get('h1').should('contain', 'Access secure works !')
  })

  it('REST API - get users (empty)', () => {
    cy.request('GET', 'http://localhost:9000/api/users')
        .then(response => {
            expect(response.status).to.eq(200)
            expect(response.body).to.have.length(0)
        })
  })

  it('REST API - put users', () => {
    cy.request('PUT', 'http://localhost:9000/api/users', {
        username: "userTest",
        password: "passwordTest"
    })
        .then(response => {
            expect(response.status).to.eq(200)
            expect(response.body).to.have.property('username', 'userTest')
        })
  })

  it('REST API - get users (1 result)', () => {
    cy.request('GET', 'http://localhost:9000/api/users')
        .then(response => {
            expect(response.status).to.eq(200)
            expect(response.body).to.have.length(1)
            expect(response.body[0]).to.have.property('username', 'userTest')
        })
  })

  it('Stop', () => {
    cy.request('GET', 'stop')
  })

})
