
var actor_cached = {};
function actor_handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")

    if(actor_cached.hasOwnProperty(query)){
        console.log("Getting actor_cached query results")
        var data = actor_cached[query];
        actor_handleLookupAjaxSuccess(data, query, doneCallback)
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
        "url": "actor-suggestion?query=" + escape(query),
        "success": function(data) {
            // pass the data, query, and doneCallback function into the success handler
            actor_handleLookupAjaxSuccess(data, query, doneCallback)
        },
        "error": function(errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}

function actor_handleLookupAjaxSuccess(data, query, doneCallback) {
    //cache the result into a global variable
    actor_cached[query] = data;

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
function actor_handleSelectSuggestion(suggestion) {
    //jump to the specific result page based on the selected suggestion
    let currentPath = window.location.pathname;
    if (currentPath.endsWith("/index.html")) {
        currentPath = currentPath.slice(0, -10); // Removes the last 10 characters ("/index.html")
    }
    let address = currentPath + "stars?id=" + suggestion["data"]["id"];
    window.location.href = address;
}

$('#actor_autocomplete').autocomplete({
    lookup: function (query, doneCallback) {
        actor_handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        actor_handleSelectSuggestion(suggestion)
    },
    // set delay time and minimum characters
    minChars: 2,
});




window.addEventListener("popstate", function() {
    location.reload(true);
});


