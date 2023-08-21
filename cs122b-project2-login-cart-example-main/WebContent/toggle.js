const cover = document.getElementById('back');
const logo = document.getElementById('logo');
const toggle = document.getElementById('toggle');
const toggleButton = document.getElementById('toggleButton');
const genre = document.getElementById('genre');
const darken = document.querySelectorAll('.darken');
const searches = document.getElementsByClassName('autocomplete-searchbox');

const suggestions = document.getElementsByClassName('autocomplete-suggestions');
const tabs = document.querySelectorAll('.tab-button');
const bars = document.querySelectorAll('.bar');

const actorGrid = document.querySelectorAll('.actor_grid');
const movieGrid = document.querySelectorAll('.movie_grid');


$(document).ready(function() {
    jQuery.ajax({
        "method": "GET",
        "url": "api/darkmode?get=true",
        "success": function(data) {
            if(data==="true"){
                console.log("darkening");

                cover.style.display = "none";
                dark()

                setTimeout(() => {
                    cover.style.display = "block";
                }, 60);


            }

        },
        "error": function(errorData) {
            console.log("get current error")
            console.log(errorData)
        }
    })

    $('.toggle').click(function() {


        let darkModeOn = cover.classList.contains("cover-dark");

        jQuery.ajax({
            "method": "GET",
            "url": "api/darkmode?dark=" + !darkModeOn,
            "success": function(data) {
                console.log("toggle success")
            },
            "error": function(errorData) {
                console.log("toggle error")
                console.log(errorData)
            }
        })

        if(darkModeOn){
            light();
        }else dark();

    });
});




function light(){
    cover.classList.remove("cover-dark");
    logo.src = "img/logoblack.png";
    logo.classList.remove("logo-glow");
    toggle.classList.remove("dark-buttons");
    toggleButton.classList.remove("toggleButtonSwap");
    genre.classList.remove("dark-buttons");

    for(let i = 0;i<searches.length;i++){
        searches[i].classList.remove('autocomplete-dark');
    }
    for(let i = 0;i<suggestions.length;i++){
        suggestions[i].classList.remove('gray');
        suggestions[i].classList.remove('white');
    }
    tabs.forEach(button =>{
        button.classList.remove('tab-button-dark');
    });
    bars.forEach(element =>{
        element.classList.remove('bar-dark');
    });
    darken.forEach(element =>{
        element.classList.remove('dark-navbar');
    });

    actorGrid.forEach(element =>{
        element.classList.remove('actor_grid_darken');
    });

    movieGrid.forEach(element =>{
        element.classList.remove('movie_grid_darken');
    });

    revert_selected();
}

function dark(){
    cover.classList.add("cover-dark");
    logo.src = "img/logoyellow.png";
    logo.classList.add("logo-glow");
    toggle.classList.add("dark-buttons");
    toggleButton.classList.add("toggleButtonSwap");
    genre.classList.add("dark-buttons");

    for(let i = 0;i<searches.length;i++){
        searches[i].classList.add('autocomplete-dark');
    }
    for(let i = 0;i<suggestions.length;i++){
        suggestions[i].classList.add('gray');
        suggestions[i].classList.add('white');
    }
    tabs.forEach(button =>{
        button.classList.add('tab-button-dark');
    });
    bars.forEach(element =>{
        element.classList.add('bar-dark');
    });
    darken.forEach(element =>{
        element.classList.add('dark-navbar');
    });
    actorGrid.forEach(element =>{
        element.classList.add('actor_grid_darken');
    });
    movieGrid.forEach(element =>{
        element.classList.add('movie_grid_darken');
    });

    change_selected();
}



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
