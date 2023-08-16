
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

    jQuery.ajax({
        "method": "GET",
        "url": "title-suggestion?query=" + escape(query),
        "success": function(data) {
            title_handleLookupAjaxSuccess(data, query, doneCallback)
        },
        "error": function(errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}

function title_handleLookupAjaxSuccess(data, query, doneCallback) {
    title_cached[query] = data;

    var jsonData = JSON.parse(data);
    console.log(jsonData)


    doneCallback( { suggestions: jsonData } );
}


function title_handleSelectSuggestion(suggestion) {
    let currentPath = window.location.pathname;
    if (currentPath.endsWith("/index.html")) {
        currentPath = currentPath.slice(0, -10); // Removes the last 10 characters ("/index.html") if it's there
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


function title_handleNormalSearch(query) {
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



