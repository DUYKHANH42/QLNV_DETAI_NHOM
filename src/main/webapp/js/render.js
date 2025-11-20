$(document).ready(function() {
  
    $(document).on("click", "a.ajax-link", function() {
       
        const url = $(this).attr("href");

        $.get(url, function(data) {
            const dom = $('<div>').html(data);
            const mainContent = dom.find('main.main-content');
            $(".main-content").html(mainContent.html());
        });

        $("a.ajax-link").removeClass("active");
        $(this).addClass("active");
    });

    // 👇 Gắn event phân trang bằng delegate
    $(document).on("click", ".page-link", function(e) {
        e.preventDefault();
        const page = $(this).data("page");

        $.get("emloyment?action=list&page=" + page, function(data) {
            const dom = $('<div>').html(data);
            const mainContent = dom.find('main.main-content');
            $(".main-content").html(mainContent.html());
        });
    });
});
