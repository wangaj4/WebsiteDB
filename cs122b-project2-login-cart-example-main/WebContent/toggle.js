

$(document).ready(function() {

    $('.toggle').click(function() {

        const cover = document.getElementById('back');
        const cartButton = document.getElementById('cart');
        const navbar = document.getElementById('navbar');

        if (cover.classList.contains("cover-dark")){
            cover.classList.remove("cover-dark");
            cartButton.classList.remove("dark-buttons");
            navbar.classList.remove("dark-navbar");
        }else{
            cover.classList.add("cover-dark");
            cartButton.classList.add("dark-buttons");
            navbar.classList.add("dark-navbar");
        }
    });
});