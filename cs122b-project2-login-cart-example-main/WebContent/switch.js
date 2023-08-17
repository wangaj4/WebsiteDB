const tabButtons = document.querySelectorAll('.tab-button');
const searchBars = document.querySelectorAll('.carousel-block');

tabButtons.forEach(tabButton => {
    tabButton.addEventListener('click', () => {
        const targetId = tabButton.getAttribute('data-target');

        tabButtons.forEach(btn => {
            btn.classList.toggle('active', btn === tabButton);
        });

        searchBars.forEach(searchBar => {
            searchBar.classList.toggle('active', searchBar.id === targetId);
        });
    });
});

// Initialize by showing the first search bar
tabButtons[0].click();