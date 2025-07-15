/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


 function changeMainImage(thumbElement) {
        document.getElementById('mainProductImage').src = thumbElement.src;
        document.querySelectorAll('.thumbnail-container .thumb').forEach(thumb => {
            thumb.classList.remove('active');
        });
        thumbElement.classList.add('active');
    }

    function scrollGallery(direction) {
        const container = document.getElementById('thumbnailContainer');
        const scrollAmount = (120 + 12) * 3 * direction;
        container.scrollBy({left: scrollAmount, behavior: 'smooth'});
    }