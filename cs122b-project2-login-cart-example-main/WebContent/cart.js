

const cartToggle = document.querySelector('.cart-button');
const cartContent = document.querySelector('.cart-list');


cartToggle.addEventListener('click', () => {

    getCart();

});




function getCart(){
    //cartToggle.classList.toggle("active");
    cartContent.classList.toggle("active");
    jQuery.ajax({
        "method": "GET",
        "url": "api/shoppingCart",
        "success": function(data) {
            var dict = JSON.parse(data);
            console.log(dict);

            if(dict["status"]==="existing"){
                //wipe slate
                cartContent.innerHTML = '';
                for (const [key, value] of Object.entries(dict)) {
                    let test = document.createElement("div");
                    console.log(key, value);
                    if(key==="status"){
                        continue;
                    }
                    if(key==="total"){
                        test.className = "cart-ending";
                        test.textContent= "Total Price: " + value;
                    }else{
                        test.className = "cart-item";
                        test.textContent= key + ": " + value;
                    }

                    cartContent.appendChild(test);

                }

            }



        },
        "error": function(errorData) {
            console.log("get cart error")
            console.log(errorData)
        }
    })
}