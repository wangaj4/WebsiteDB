
var cached = {};
function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")

    if(cached.hasOwnProperty(query)){
        console.log("Getting cached query results")
        var data = cached[query];
        handleLookupAjaxSuccess(data, query, doneCallback)
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
        "url": "auto-suggestion?query=" + escape(query),
        "success": function(data) {
            // pass the data, query, and doneCallback function into the success handler
            handleLookupAjaxSuccess(data, query, doneCallback)
        },
        "error": function(errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}

function handleLookupAjaxSuccess(data, query, doneCallback) {
    //cache the result into a global variable
    cached[query] = data;

    // parse the string into JSON
    var jsonData = JSON.parse(data);
    console.log(jsonData)



    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function.
 * When a suggestion is selected, this function is called by the library.
 *
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
    let address = "Movie?id=" + suggestion["data"]["id"];
    window.location.href = address;
}

// $('#autocomplete') is to find element by the ID "autocomplete"
$('#autocomplete').autocomplete({
    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    // set delay time and minimum characters
    minChars: 2,
});


/*
 * do normal full text search if no suggestion is selected
 */
function handleNormalSearch(query) {

    let address = "MovieList?Full=" + query;
    window.location.href = address;

}

$('#autocomplete').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        handleNormalSearch($('#autocomplete').val())
    }
})






window.addEventListener("popstate", function() {
    location.reload(true);
});


