

$(document).ready(function() {

    $('.toggle').click(function() {

        const cover = document.getElementById('back');
        const logo = document.getElementById('logo');
        const cartButton = document.getElementById('cart');
        const toggle = document.getElementById('toggle');
        const toggleButton = document.getElementById('toggleButton');
        const genre = document.getElementById('genre');
        const navbar = document.getElementById('navbar');
        const carouselBlocks = document.getElementsByClassName('carousel-block');
        const searches = document.getElementsByClassName('autocomplete-searchbox');

        const suggestions = document.getElementsByClassName('autocomplete-suggestions');


        if (cover.classList.contains("cover-dark")){
            cover.classList.remove("cover-dark");
            //cartButton.classList.remove("dark-buttons");
            logo.src = "img/logoblack.png";
            logo.classList.remove("logo-glow");
            toggle.classList.remove("dark-buttons");
            navbar.classList.remove("dark-navbar");
            toggleButton.classList.remove("toggleButtonSwap");
            genre.classList.remove("dark-buttons");
            for(let i = 0;i<carouselBlocks.length;i++){
                carouselBlocks[i].classList.remove('dark-navbar');
            }
            for(let i = 0;i<searches.length;i++){
                searches[i].classList.remove('autocomplete-dark');
            }
            for(let i = 0;i<suggestions.length;i++){
                suggestions[i].classList.remove('gray');
                suggestions[i].classList.remove('white');
            }

            revert_selected();



        }else{
            cover.classList.add("cover-dark");
            //cartButton.classList.add("dark-buttons");
            logo.src = "img/logoyellow.png";
            logo.classList.add("logo-glow");
            toggle.classList.add("dark-buttons");
            navbar.classList.add("dark-navbar");
            toggleButton.classList.add("toggleButtonSwap");
            genre.classList.add("dark-buttons");
            for(let i = 0;i<carouselBlocks.length;i++){
                carouselBlocks[i].classList.add('dark-navbar');
            }
            for(let i = 0;i<searches.length;i++){
                searches[i].classList.add('autocomplete-dark');
            }
            for(let i = 0;i<suggestions.length;i++){
                suggestions[i].classList.add('gray');
                suggestions[i].classList.add('white');
            }

            change_selected();

        }
    });
});


let newStyleRules = [];

function change_selected(on) {
    const updatedStyle1 = '.autocomplete-selected { background: #747474; }\n';
    const updatedStyle2 = '.autocomplete-suggestions strong { color: gold; }\n';

    const styleSheet = document.styleSheets[0];

    // Store the original rules if not already stored
    if (newStyleRules.length === 0) {
        newStyleRules.push(styleSheet.insertRule(updatedStyle1, styleSheet.cssRules.length));
        newStyleRules.push(styleSheet.insertRule(updatedStyle2, styleSheet.cssRules.length));
    }

}

function revert_selected() {
    const styleSheet = document.styleSheets[0];

    styleSheet.deleteRule(newStyleRules[0]);
    styleSheet.deleteRule(newStyleRules[1]-1);

    // Clear the array to indicate that the rules are removed
    newStyleRules = [];
}
