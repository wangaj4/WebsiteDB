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

let r = document.querySelector(':root');

$(document).ready(function() {

    cover.style.display = "none";

    jQuery.ajax({
        "method": "GET",
        "url": "api/darkmode?get=true",
        "success": function(data) {
            if(data==="true"){
                console.log("lightening");
                dark()
            }

            setTimeout(() => {
                cover.style.display = "block";
            }, 20);

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

    r.style.setProperty('--whitesmoke-gray', 'whitesmoke');
    r.style.setProperty('--auto-strong', '#3399FF');
    r.style.setProperty('--auto-selected', '#B8B8B8');


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


}

function dark(){
    cover.classList.add("cover-dark");
    logo.src = "img/logoyellow.png";
    logo.classList.add("logo-glow");
    toggle.classList.add("dark-buttons");
    toggleButton.classList.add("toggleButtonSwap");
    genre.classList.add("dark-buttons");

    r.style.setProperty('--whitesmoke-gray', 'gray');
    r.style.setProperty('--auto-strong', 'gold');
    r.style.setProperty('--auto-selected', '#747474');


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


}

