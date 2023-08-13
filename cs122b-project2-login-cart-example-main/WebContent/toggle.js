

$(document).ready(function() {

    $('.toggle').click(function() {

        const cover = document.getElementById('back');
        const cartButton = document.getElementById('cart');
        const toggle = document.getElementById('toggle');
        const toggleButton = document.getElementById('toggleButton');
        const navbar = document.getElementById('navbar');
        const carouselBlocks = document.getElementsByClassName('carousel-block');

        if (cover.classList.contains("cover-dark")){
            cover.classList.remove("cover-dark");
            cartButton.classList.remove("dark-buttons");
            toggle.classList.remove("dark-buttons");
            navbar.classList.remove("dark-navbar");
            toggleButton.classList.remove("toggleButtonSwap");
            for(let i = 0;i<carouselBlocks.length;i++){
                carouselBlocks[i].classList.remove('dark-navbar');
            }

        }else{
            cover.classList.add("cover-dark");
            cartButton.classList.add("dark-buttons");
            toggle.classList.add("dark-buttons");
            navbar.classList.add("dark-navbar");
            toggleButton.classList.add("toggleButtonSwap");
            for(let i = 0;i<carouselBlocks.length;i++){
                carouselBlocks[i].classList.add('dark-navbar');
            }
        }
    });
});