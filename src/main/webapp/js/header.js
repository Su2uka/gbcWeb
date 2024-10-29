$(function () {
    /*alert("hello");*/
    //查询用户信息
    $.get("user/findOne",{},function (data) {
        var msg;
        if (data != null) {
            msg = "欢迎您,"+data.email;
        } else {
            msg = "您尚未登录，部分功能无法使用！"
        }
        $("#me").attr("title",msg);
    });
});

$(function () {

    $("#search-form").submit(function (e) {
        // 阻止表单的默认提交行为
        e.preventDefault();

        var search = $("#search-input").val();

        // 获取当前页面路径
        var currentPath = window.location.pathname;

        if (currentPath.includes('like')) {
            location.href = "http://localhost/songs_list_like.html?search="+search;
            //location.href = "https://gent1.cn/songs_list_like.html?search="+search;

        } else {
            location.href="http://localhost/songs_list.html?search="+search;
            //location.href="https://gent1.cn/songs_list.html?search="+search;
        }
    });

});

$(function () {
    $(window).scroll(function () {
        var scrollY = $(window).scrollTop();  // 获取当前滚动距离
        // 获取当前页面路径
        var currentPath = window.location.pathname;

        if (currentPath.includes('index')||currentPath == '/') {
            if (scrollY > 50) {  // 当页面滚动超过50px时
                $('nav').addClass('navbar-inverse');  // 添加反色类
                $('nav').css('background-color', 'black');
                $('#logo').attr('src', 'img/logo_footer.webp');  // 替换为另一张 logo 图片
            } else {
                $('nav').removeClass('navbar-inverse');  // 移除反色类
                $('nav').css('background-color', '#e3e0e3')
                $('#logo').attr('src', 'img/logo.webp');  // 恢复原始 logo 图片
            }
        }

    });


    $('li').hover(
        function() {
            $(this).addClass('active'); // 鼠标移入时添加 active 类
        },
        function() {
            $(this).removeClass('active'); // 鼠标移出时移除 active 类
        }
    );

});

function dashangToggle() {
    const hideBox = document.querySelector('.hide_box');
    const shangBox = document.querySelector('.shang_box');
    const isVisible = hideBox.classList.contains('show');
    if (isVisible) {
        hideBox.classList.remove('show');
        shangBox.classList.remove('show');
        setTimeout(() => {
            hideBox.style.display = 'none';
        }, 300);
    } else {
        hideBox.style.display = 'block';
        setTimeout(() => {
            hideBox.classList.add('show');
            shangBox.classList.add('show');
        }, 10);
    }
}

function showQRCode(type) {
    const img = document.querySelector('.shang_box img');
    const text = document.querySelector('#shang_pay_txt');
    img.src = type === 'alipay' ? 'img/alipay_qr.jpg' : 'img/wechat_qr.jpg';
    text.textContent = type === 'alipay' ? '支付宝' : '微信';
    document.querySelectorAll('.pay_item').forEach(el => el.classList.remove('checked'));
    document.querySelector(`.pay_item[data-id="${type}"]`).classList.add('checked');
}