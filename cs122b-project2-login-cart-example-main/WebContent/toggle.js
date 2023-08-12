

$(document).ready(function() {

    $('.toggle').click(function() {

        const cover = document.getElementById('back');
        const cartButton = document.getElementById('cart');
        const toggle = document.getElementById('toggle');
        const toggleButton = document.getElementById('toggleButton');
        const navbar = document.getElementById('navbar');

        if (cover.classList.contains("cover-dark")){
            cover.classList.remove("cover-dark");
            cartButton.classList.remove("dark-buttons");
            toggle.classList.remove("dark-buttons");
            navbar.classList.remove("dark-navbar");
            toggleButton.classList.remove("toggleButtonSwap");
        }else{
            cover.classList.add("cover-dark");
            cartButton.classList.add("dark-buttons");
            toggle.classList.add("dark-buttons");
            navbar.classList.add("dark-navbar");
            toggleButton.classList.add("toggleButtonSwap");
        }
    });
});