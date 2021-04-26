context('JServer tests : thymeleaf', () => {

  it('Thymeleaf page', () => {
    cy.visit('http://localhost:9000/page1')

    cy.get('h1').contains('Titre pour le test du plugin Thymeleaf')
    cy.get('p').contains('Bienvenue sur la page de test thymeleaf !')

    cy.get('body').should('have.css', 'background-color', 'rgb(17, 17, 17)')
    cy.get('h1').should('have.css', 'color', 'rgb(255, 255, 255)')
    cy.get('p').should('have.css', 'color', 'rgb(255, 255, 255)')
  })

})
