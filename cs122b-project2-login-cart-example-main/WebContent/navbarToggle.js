
const navbarToggle = document.querySelector('.navbar-toggle');
const navContent = document.querySelector('.navbar-content');

navbarToggle.addEventListener('click', () => {
    navbarToggle.classList.toggle("active");
    navContent.classList.toggle("active");
    document.getElementById("spacer").classList.toggle("active");

});

