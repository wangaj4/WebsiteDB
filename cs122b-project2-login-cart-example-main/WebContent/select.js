document.getElementById('genre').addEventListener('change', function() {
    const selectedValue = this.value;

    if (selectedValue) {
        const form = document.querySelector('.genres');
        const action = form.getAttribute('action');
        const url = `${action}?Genre=${selectedValue}`;

        window.location.href = url;
    }
});

document.getElementById('Per').addEventListener('change', function() {
    const selectedValue = this.value;

    if (selectedValue) {
        const url = `MovieList?Per=${selectedValue}`;

        window.location.href = url;
    }
});


const orders = document.querySelectorAll('.order')
orders.forEach(element =>{
    element.addEventListener('change', function() {
        const selectedValue = this.value;

        if (selectedValue) {
            const url = `MovieList?order=${selectedValue}`;

            window.location.href = url;
        }
    });
})

