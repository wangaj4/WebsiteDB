
var title_cached = {};
function title_handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")

    if(title_cached.hasOwnProperty(query)){
        console.log("Getting title_cached query results")
        var data = title_cached[query];
        title_handleLookupAjaxSuccess(data, query, doneCallback)
        return;
    }else{
        console.log("sending AJAX request to backend Java Servlet")
    }
    // sending the HTTP GET request to the Java Servlet endpoint auto-suggestion
    // with the query data
    jQuery.ajax({
        "method": "GET",
        // generate the request url from the query.
        // escape the query string to avoid errors caused by special characters
        "url": "title-suggestion?query=" + escape(query),
        "success": function(data) {
            // pass the data, query, and doneCallback function into the success handler
            title_handleLookupAjaxSuccess(data, query, doneCallback)
        },
        "error": function(errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}

function title_handleLookupAjaxSuccess(data, query, doneCallback) {
    //cache the result into a global variable
    title_cached[query] = data;

    // parse the string into JSON
    var jsonData = JSON.parse(data);
    console.log(jsonData)


    doneCallback( { suggestions: jsonData } );
}


function title_handleSelectSuggestion(suggestion) {
    //jump to the specific result page based on the selected suggestion
    let currentPath = window.location.pathname;
    if (currentPath.endsWith("/index.html")) {
        currentPath = currentPath.slice(0, -10); // Removes the last 10 characters ("/index.html")
    }
    let address = currentPath + "Movie?id=" + suggestion["data"]["id"];
    window.location.href = address;
}

$('#title_autocomplete').autocomplete({
    lookup: function (query, doneCallback) {
        title_handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        title_handleSelectSuggestion(suggestion)
    },
    // set delay time and minimum characters
    deferRequestBy: 300,
    minChars: 3,
});


/*
 * do normal full text search if no suggestion is selected
 */
function title_handleNormalSearch(query) {
    //you should do normal search here
    let currentPath = window.location.pathname;
    if (currentPath.endsWith("/index.html")) {
        currentPath = currentPath.slice(0, -10); // Removes the last 10 characters ("/index.html")
    }
    let address = currentPath + "MovieList?Title=" + query;
    window.location.href = address;

}

$('#title_autocomplete').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        title_handleNormalSearch($('#title_autocomplete').val())
    }
})



