document.addEventListener("DOMContentLoaded", function() {
        // Lấy danh sách các item trong carousel
        var items = document.querySelectorAll('.carousel-inner .carousel-item');
        
        // Xác định chỉ số của item hiện tại đang active
        var activeIndex = Array.from(items).findIndex(item => item.classList.contains('active'));

        function showSlide(index) {
            // Loại bỏ lớp 'active' từ tất cả các item
            items.forEach(item => item.classList.remove('active'));

            // Thêm lớp 'active' cho item có chỉ số mới
            items[index].classList.add('active');
        }

        // Xử lý khi nhấn nút trước
        document.querySelector('.prev-button').addEventListener('click', function() {
            activeIndex = (activeIndex === 0) ? (items.length - 1) : (activeIndex - 1);
            showSlide(activeIndex);
        });

        // Xử lý khi nhấn nút sau
        document.querySelector('.next-button').addEventListener('click', function() {
            activeIndex = (activeIndex === (items.length - 1)) ? 0 : (activeIndex + 1);
            showSlide(activeIndex);
        });
    });


