$(function () {
    $.get("header.html",function (data) {
        $("#header").html(data);

        // 获取当前页面路径
        var currentPath = window.location.pathname;

        console.log(currentPath);

        // 根据不同页面路径，动态修改 header 的样式或内容
        if (currentPath.includes('register')) {
            $('.navbar').css('background-color', 'white');
            $('.navbar-collapse').css('background-color', 'white');  // 改变背景色为白色
        } else if (currentPath.includes('login')) {
            $('.navbar').css('background-color', 'black');  // 改变背景色为黑色
            $('.navbar-collapse').css('background-color', 'black');
            $('#navbar').addClass('navbar-inverse');  // 添加反色类
            $('#logo').attr('src', 'img/logo_footer.webp');  // 替换为另一张 logo 图片
        } else if (currentPath.includes('songs')) {
            $('.navbar').css('background-color', 'white');
            $('.navbar-collapse').css('background-color', 'white');
        }
        else {
            $('.navbar').css('background-color', '#e3e0e3');  // 默认背景色
        }

    });
    /*$.get("footer.html",function (data) {
        $("#footer").html(data);
    });*/
});