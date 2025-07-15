/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 Legacy JS code
 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
$(document).ready(function () {
    "use strict"; // start of use strict

    /*==============================
     Bg
     ==============================*/
    $('.section__ad--bg').each(function () {
        if ($(this).attr("data-bg")) {
            $(this).css({
                'background': 'url(' + $(this).data('bg') + ')',
                'background-position': 'center top 80px',
                'background-repeat': 'no-repeat',
                'background-size': 'auto 1000px'
            });
        }
    });

    $('.section__details--bg').each(function () {
        if ($(this).attr("data-bg")) {
            $(this).css({
                'background': 'url(' + $(this).data('bg') + ')',
                'background-position': 'center top 60px',
                'background-repeat': 'no-repeat',
                'background-size': 'auto 1000px'
            });
        }
    });

    $('.section--head').each(function () {
        if ($(this).attr("data-bg")) {
            $(this).css({
                'background': 'url(' + $(this).data('bg') + ')',
                'background-position': 'center top 140px',
                'background-repeat': 'no-repeat',
                'background-size': 'cover'
            });
        }
    });

    $('.section--full-bg').each(function () {
        if ($(this).attr("data-bg")) {
            $(this).css({
                'background': 'url(' + $(this).data('bg') + ')',
                'background-position': 'center center',
                'background-repeat': 'no-repeat',
                'background-size': 'cover'
            });
        }
    });

    /*==============================
     Section carousel
     ==============================*/
    $('.section__carousel--ad').owlCarousel({
        mouseDrag: true,
        touchDrag: true,
        dots: false,
        loop: true,
        autoplay: true,
        smartSpeed: 1600,
        margin: 30,
        autoHeight: true,
        items: 1
    });

    $('.section__carousel--catalog').owlCarousel({
        mouseDrag: true,
        touchDrag: true,
        dots: false,
        loop: true,
        autoplay: true,
        smartSpeed: 700,
        margin: 20,
        autoHeight: true,
        autoWidth: true,
        responsive: {
            0: {
                items: 2,
            },
            576: {
                items: 3,
            },
            768: {
                items: 3,
                margin: 30,
                autoWidth: false,
            },
            992: {
                items: 4,
                margin: 30,
                autoWidth: false,
            },
            1200: {
                items: 5,
                margin: 30,
                autoWidth: false,
                mouseDrag: false,
                touchDrag: false,
            },
        }
    });

    $('.section__nav--prev, .details__nav--prev').on('click', function () {
        var carouselId = $(this).attr('data-nav');
        $(carouselId).trigger('prev.owl.carousel');
    });
    $('.section__nav--next, .details__nav--next').on('click', function () {
        var carouselId = $(this).attr('data-nav');
        $(carouselId).trigger('next.owl.carousel');
    });

    /*==============================
     Details
     ==============================*/
    $('.details__thumb').on('click', function () {
        const preview = $(this).data('preview');
        $('#mainPreview').attr('src', preview);

        $('.details__thumb').removeClass('active');
        $(this).addClass('active');
    });

    const descToggle = document.getElementById('descToggle');
    const descContent = document.getElementById('descContent');

    if (descToggle && descContent) {
        descToggle.addEventListener('click', () => {
            const expanded = descContent.classList.toggle('expanded');
            descToggle.textContent = expanded ? 'Read less' : 'Read more';
        });
    }

    /*==============================
     Admin
     ==============================*/
    $('.admin-dropdown').on('click', function (e) {
        $('#adminDropdown').toggleClass('show');
        e.stopPropagation();
    });

    $(document).on('click', function (e) {
        if (!$(e.target).closest('.admin-dropdown').length) {
            $('#adminDropdown').removeClass('show');
        }
    });
    /*==============================
     Modal
     ==============================*/

    $('.open-modal').magnificPopup({
        fixedContentPos: true,
        fixedBgPos: true,
        overflowY: 'auto',
        type: 'inline',
        preloader: false,
        focus: '#username',
        modal: false,
        removalDelay: 300,
        mainClass: 'my-mfp-zoom-in',
        callbacks: {
            open: function () {
                if ($(window).width() > 1200) {
                    $('.header').css('margin-left', "-" + (getScrollBarWidth() / 2) + "px");
                }
            },
            close: function () {
                if ($(window).width() > 1200) {
                    $('.header').css('margin-left', 0);
                }
            }
        }
    });

    $('.modal__close').on('click', function (e) {
        e.preventDefault();
        $.magnificPopup.close();
    });

    function getScrollBarWidth() {
        var $outer = $('<div>').css({visibility: 'hidden', width: 100, overflow: 'scroll'}).appendTo('body'),
                widthWithScroll = $('<div>').css({width: '100%'}).appendTo($outer).outerWidth();
        $outer.remove();
        return 100 - widthWithScroll;
    }
    ;

    /*==============================
     Admin
     ==============================*/
    $('input[name="images[]"]').on('change', function (e) {
        const previewContainer = $('#imagePreviewContainer');
        previewContainer.empty();

        Array.from(e.target.files).forEach(file => {
            const reader = new FileReader();
            reader.onload = function (e) {
                const img = $('<img>', {
                    src: e.target.result,
                    alt: file.name,
                    css: {
                        maxWidth: '120px',
                        borderRadius: '6px',
                        marginRight: '10px',
                        marginBottom: '10px',
                        boxShadow: '0 2px 6px rgba(0,0,0,0.2)'
                    }
                });
                previewContainer.append(img);
            };
            reader.readAsDataURL(file);
        });
    });
});