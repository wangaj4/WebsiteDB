

const cartToggle = document.querySelector('.cart-button');
const cartContent = document.querySelector('.cart-list');


cartToggle.addEventListener('click', () => {
    cartContent.classList.toggle("active");
    getCart();

});


function send(value, movieName){


    jQuery.ajax({
        "method": "POST",
        "url": "api/shoppingCart",
        data: {value: value, movie:movieName},
        "success": function(data) {
            console.log("sent information to cart backend");
            getCart();
        },
        "error": function(errorData) {
            console.log("update cart error");
            console.log(errorData);
        }
    })
}


function getCart(){

    jQuery.ajax({
        "method": "GET",
        "url": "api/shoppingCart",
        "success": function(data) {
            var dict = JSON.parse(data);
            console.log(dict);

            if(dict["status"] === "empty"){
                cartContent.innerHTML = '';
                let emptyNotif = document.createElement("div");
                emptyNotif.className = "cart-empty";
                emptyNotif.textContent = "Cart is empty";
                cartContent.appendChild(emptyNotif);

            }
            else if(dict["status"]==="existing"){
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
                        let itemParagraph = document.createElement("label");
                        itemParagraph.textContent = key;
                        itemParagraph.style.fontWeight = "bold";



                        let options = document.createElement("div");
                        options.className = "cart-options";
                        let subtract = document.createElement("a");
                        subtract.textContent = "- ";
                        subtract.onclick = function () {

                            //send post request to backend
                            send("sub", key);
                        }

                        let add = document.createElement("a");
                        add.textContent = " +";
                        add.onclick = function () {

                            send("add", key);
                        }

                        options.appendChild(subtract);

                        let count = document.createElement("span");
                        count.textContent = value;
                        options.append(count);
                        options.appendChild(add);




                        let costLabel = document.createElement("label");
                        costLabel.textContent = "Cost: $" + value * 10;

                        test.appendChild(itemParagraph);
                        test.appendChild(options);
                        //test.appendChild(document.createElement("br"));
                        test.appendChild(costLabel);
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