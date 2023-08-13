$(document).ready(function() {
    let currentIndex = 0;
    let pages = $('.carousel-container');
    let totalImages = pages.length;

    let prev = $('.prev-arrow');
    prev.addClass('hidden');

    let next = $('.next-arrow');


    // Set the initial active image
    pages.eq(currentIndex).addClass('active');

    next.click(function() {
        currentIndex = (currentIndex + 1) % totalImages;
        updateCarousel();
        prev.removeClass('hidden');
        if(currentIndex === totalImages-1){
            next.addClass('hidden');
        }
    });

    prev.click(function() {
        currentIndex = (currentIndex - 1 + totalImages) % totalImages;
        updateCarousel();
        next.removeClass('hidden');
        if(currentIndex === 0){
            prev.addClass('hidden');
        }
    });


    function updateCarousel() {
        let offset = currentIndex * -100; // Adjust based on image width

        pages.removeClass('active');
        pages.eq(currentIndex).addClass('active');

        $('.carousel-elements').css('transform', 'translateX(' + offset + '%)');
    }


});


