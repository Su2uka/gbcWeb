$(document).ready(function() {
    function updateRecordPlayerPosition() {
        // 获取 .col-md-2 元素
        var $colMd2 = $('.col-md-2');
        // 获取 .record-player 元素
        var $recordPlayer = $('.record-player');

        if ($colMd2.length && $recordPlayer.length) {
            // 获取 .col-md-2 相对于视口的左边距和宽度
            var rect = $colMd2[0].getBoundingClientRect();
            var colWidth = rect.width;

            // 获取 .record-player 的宽度
            var playerWidth = $recordPlayer.outerWidth();

            // 计算 .record-player 应该定位的 left 值
            var leftPosition = rect.left + (colWidth / 2) - (playerWidth / 2);

            leftPosition += 25;

            // 设置 .record-player 的 left 值
            $recordPlayer.css('left', leftPosition + 'px');
        }
    }

    // 页面加载时调用函数
    updateRecordPlayerPosition();

    // 浏览器窗口大小改变时调用函数
    $(window).on('resize', updateRecordPlayerPosition);
});