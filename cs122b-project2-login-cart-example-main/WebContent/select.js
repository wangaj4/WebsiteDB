document.getElementById('genre').addEventListener('change', function() {
    const selectedValue = this.value;

    if (selectedValue) {
        const form = document.querySelector('.genres');
        const action = form.getAttribute('action');
        const url = `${action}?Genre=${selectedValue}`;

        window.location.href = url;
    }
});