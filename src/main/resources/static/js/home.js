document.addEventListener("DOMContentLoaded", function () {
    var btnEdits = document.querySelectorAll('.btn-edit');

    if (btnEdits) {
        btnEdits.forEach(function (btnEdit) {
            btnEdit.addEventListener("click", function () {
                let productId = btnEdit.getAttribute('data-id'); 
                console.log(productId)
                window.location.href = "/admin/about-product?id=" + productId;
            });
        });
    }
});

document.addEventListener("DOMContentLoaded", function () {
    var items = document.querySelectorAll('.carousel-inner .carousel-item');
    
    var activeIndex = Array.from(items).findIndex(item => item.classList.contains('active'));

    function showSlide(index) {
        items.forEach(item => item.classList.remove('active'));

        items[index].classList.add('active');
    }

    document.querySelector('.carousel-control-prev').addEventListener('click', function () {
        activeIndex = (activeIndex === 0) ? (items.length - 1) : (activeIndex - 1);
        showSlide(activeIndex);
    });

    document.querySelector('.carousel-control-next').addEventListener('click', function () {
        activeIndex = (activeIndex === (items.length - 1)) ? 0 : (activeIndex + 1);
        showSlide(activeIndex);
    });

    setInterval(function () {
        activeIndex = (activeIndex === (items.length - 1)) ? 0 : (activeIndex + 1);
        showSlide(activeIndex);
    }, 1500);
});


