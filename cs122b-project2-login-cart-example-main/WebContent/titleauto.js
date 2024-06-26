
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

    let address = "Movie?id=" + suggestion["data"]["id"] + "&skip=true";;
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
    minChars: 2,
});


function title_handleNormalSearch(query) {

    let address = "MovieList?Title=" + query;
    window.location.href = address;

}

$('#title_autocomplete').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        title_handleNormalSearch($('#title_autocomplete').val())
    }
})





window.addEventListener("popstate", function() {
    location.reload(true);
});