
var director_cached = {};
function director_handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")

    if(director_cached.hasOwnProperty(query)){
        console.log("Getting director_cached query results")
        var data = director_cached[query];
        director_handleLookupAjaxSuccess(data, query, doneCallback)
        return;
    }else{
        console.log("sending AJAX request to backend Java Servlet")
    }

    jQuery.ajax({
        "method": "GET",
        "url": "director-suggestion?query=" + escape(query),
        "success": function(data) {
            director_handleLookupAjaxSuccess(data, query, doneCallback)
        },
        "error": function(errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}

function director_handleLookupAjaxSuccess(data, query, doneCallback) {
    director_cached[query] = data;

    var jsonData = JSON.parse(data);
    console.log(jsonData)


    doneCallback( { suggestions: jsonData } );
}


function director_handleSelectSuggestion(suggestion) {
    let currentPath = window.location.pathname;
    if (currentPath.endsWith("/index.html")) {
        currentPath = currentPath.slice(0, -10); // Removes the last 10 characters ("/index.html") if it's there
    }
    let address = currentPath + "MovieList?Director=" + suggestion["value"];
    window.location.href = address;
}

$('#director_autocomplete').autocomplete({
    lookup: function (query, doneCallback) {
        director_handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        director_handleSelectSuggestion(suggestion)
    },
    // set delay time and minimum characters
    deferRequestBy: 300,
    minChars: 3,
});


function director_handleNormalSearch(query) {
    let currentPath = window.location.pathname;
    if (currentPath.endsWith("/index.html")) {
        currentPath = currentPath.slice(0, -10); // Removes the last 10 characters ("/index.html")
    }
    let address = currentPath + "MovieList?Director=" + query;
    window.location.href = address;

}

$('#director_autocomplete').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        director_handleNormalSearch($('#director_autocomplete').val())
    }
})




window.addEventListener("popstate", function() {
    location.reload(true);
});