$(function () {
    $('.card').click(function () {
        // 获取当前点击的卡片的 id
        const cardId = $(this).attr('id');

        // 根据卡片的 id 执行不同的跳转操作
        switch(cardId) {
            case 'card1':
                /*alert(cardId);*/
                videoSrc = 'videos/rupa.mp4'; // 设置第一个视频的路径
                break;
            case 'card2':
                /*alert(cardId);*/
                videoSrc = 'videos/subaru.mp4';
                break;
            case 'card3':
                /*alert(cardId);*/
                videoSrc = 'videos/nina.mp4';
                break;
            case 'card4':
                /*alert(cardId);*/
                videoSrc = 'videos/tomo.mp4';
                break;
            case 'card5':
                /*alert(cardId);*/
                videoSrc = 'videos/momoka.mp4';
                break;
        }

        // 显示视频播放器并设置视频源
        $('#video-player').attr('src', videoSrc);
        $('.video-container').show();
        document.getElementById('video-player').play(); // 播放视频

        // 获取目标元素的位置
        const offsetTop = $('.video-container').offset().top;

        // 平滑滚动到指定位置
        $('html, body').animate({
            scrollTop: offsetTop
        }, 500); // 500 毫秒的动画时间
    });
});