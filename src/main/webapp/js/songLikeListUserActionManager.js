var songList = [];

var playList = [];

var playList_copy = [];


//更新播放列表
function updateLikeList() {
    $.get("song/queryAllLike", {search: search}, function (list) {
        playList = list;
        // 创建 playList 的副本，避免引用同一个数组对象
        playList_copy = playList.slice();
    });
}

// 全局变量定义
let user = null; // 初始化为 null，表明尚未获取用户信息

$(function () {
    $.get("user/findOne",{},function (u) {
        user = u;
    });
});

$(function () {

    $.get("song/queryAll", {}, function (list) {
        songList = list;
    });


    updateLikeList();
    /*$.get("song/queryAllLike", {search: search}, function (list) {
      playList = list;
      // 创建 playList 的副本，避免引用同一个数组对象
      playList_copy = playList.slice();
    });*/
});

$(function () {

    var audio = $("#myAudio")[0];

    //播放器当前播放歌曲的id
    var currentId = $(".audioPlayer").attr('id');
    currentId = parseInt(currentId);  // 确保 currentId 是数字

    // 获取当前点击按钮所在的最近的 li 元素的 id
    var songId;
    /*var songId = $(this).closest('li').attr('id');
    songId = parseInt(songId);  // 确保 songId 是数字*/

    // 初始化进度条
    $("#progress-bar").slider({
        range: "min",
        min: 0,
        max: 100,
        value: 0,
        slide: function (event, ui) {
            if (audio) {
                var seekTo = audio.duration * (ui.value / 100); // 计算用户拖动到的播放时间
                audio.currentTime = seekTo; // 设置音频的当前时间
            }
        }
    });

    // 更新当前播放时间的函数
    function updateCurrentTime(audio) {
        var currentMinutes = Math.floor(audio.currentTime / 60);
        var currentSeconds = Math.floor(audio.currentTime - currentMinutes * 60);
        if (currentSeconds < 10) {
            currentSeconds = "0" + currentSeconds;
        }
        $("#current-time").text(currentMinutes + ":" + currentSeconds); // 更新当前播放时间
    }

    //播放按钮
    $(document).on('click', '.play-button', function (e) {
        e.preventDefault(); // 防止默认点击动作

        // 获取当前点击按钮所在的最近的 li 元素的 id
        songId = $(this).closest('li').attr('id');
        songId = parseInt(songId);  // 确保 songId 是数字
        //播放器当前播放歌曲的id
        currentId = $(".audioPlayer").attr('id');
        currentId = parseInt(currentId);  // 确保 currentId 是数字

        //暂停当前播放
        if (songId == currentId && isMusicPlaying(audio)) {
            audio.pause();
            return;
        }

        //恢复当前播放
        if (songId == currentId && !isMusicPlaying(audio)) {
            audio.play();

            return;
        }

        //首次播放
        if (songId != currentId) {

            // 显示唱片机
            $('.record-player').addClass('show');

            /*for (var i = 0; i < likeList.length; i++) {
                var likeId = likeList[i].sid;

                if (likeId == songId) {
                    $("#player-like-button .like-icon").addClass("like-active");
                    break;
                } else {
                    $("#player-like-button .like-icon").removeClass("like-active");
                }
            }*/
            //更新唱片机喜欢状态
            playerLike(songId);

            //更新唱片机
            updatePlayer(songId);

        }

        //切换播放
        if (currentId != "0" && songId != currentId) {
            //切换图标样式,切换为暂停图标(上一个播放的按钮)
            $("#" + $.escapeSelector(currentId) + " .play-button").html('<span class="glyphicon glyphicon-play play-icon"></span>');


            //更新唱片机喜欢状态
            playerLike(songId);

            // 停止唱片旋转
            $('.record').removeClass('playing');

            //更新唱片机
            updatePlayer(songId);
        }


        $(".audioPlayer").attr('id', songId);
        $("#audioSource").attr('src', songList[songId - 1].filepath);

        // 重新加载音频并播放
        audio.load();  // 重新加载新源
        audio.play();  // 播放音频


    });

    //当点击播放
    audio.addEventListener('play', function () {
        // 更新播放图标为加载中
        $("#" + $.escapeSelector(songId) + " .play-button").html('<img src="img/loading.webp">');
        $("#player-play-button").html('<img src="img/loading.webp">');

        /*// 开始唱片旋转
        $('.record').addClass('playing');*/
    });

    // 当音频开始播放时
    audio.addEventListener('playing', function () {
        // 更新播放图标为暂停图标
        $("#" + $.escapeSelector(songId) + " .play-button").html('<span class="glyphicon glyphicon-pause play-icon"></span>');
        $("#player-play-button").html('<span class="glyphicon glyphicon-pause play-icon"></span>');


        // 开始唱片旋转
        $('.record').addClass('playing');
    });

    // 当音频暂停时
    audio.addEventListener('pause', function () {
        // 进行暂停图标切换操作
        $("#" + $.escapeSelector(songId) + " .play-button").html('<span class="glyphicon glyphicon-play play-icon"></span>');
        $("#player-play-button").html('<span class="glyphicon glyphicon-play play-icon"></span>');

        // 停止唱片旋转
        $('.record').removeClass('playing');
    });

    // 每当音频播放进度更新时，更新进度条和当前时间
    audio.addEventListener('timeupdate', function () {
        var value = (audio.currentTime / audio.duration) * 100; // 计算当前播放进度百分比
        $("#progress-bar").slider("value", value); // 更新进度条的当前值
        updateCurrentTime(audio); // 更新当前时间
    });

    // 当音频播放结束时，停止旋转并恢复播放按钮
    audio.addEventListener('ended', function () {
        $('.play-button').html('<span class="glyphicon glyphicon-play play-icon"></span>'); // 恢复所有播放图标
        $('.record').removeClass('playing'); // 停止唱片旋转
        $("#current-time").text("0" + ":" + "00"); // 更新进度条当前时间
        $("#progress-bar").slider("value", 0); // 将进度条重置为 0


        //正在播放歌曲的id
        currentId = $(".audioPlayer").attr('id');
        currentId = parseInt(currentId);


        //顺序播放
        if (currentModeIndex==0) {
            //是否到了播放列表最末尾
            if (currentId == playList[playList.length-1].sid) {
                //到了
                return;
            }

            //没有到最末尾
            for (var i = 0; i < playList.length; i++) {
                if (currentId == playList[i].sid){
                    songId = playList[i+1].sid;
                    break;
                }
            }
        } else if (currentModeIndex == 1) {  //单曲循环
            songId = currentId;
        } else if (currentModeIndex == 2) {  //随机播放
            for (var i = 0; i < playList.length; i++) {
                if (currentId == playList[i].sid){
                    if (i < (playList.length-1)) {
                        songId = playList[i+1].sid;
                        break;
                    }
                    songId = playList[0].sid;
                }
            }
        } else {  //列表循环
            for (var i = 0; i < playList.length; i++) {
                if (currentId == playList[i].sid){
                    if (i < (playList.length-1)) {
                        songId = playList[i+1].sid;
                        break;
                    }
                    songId = playList[0].sid;
                }
            }
        }

        $(".audioPlayer").attr('id', songId);
        $("#audioSource").attr('src', songList[songId - 1].filepath);

        //更新唱片机喜欢状态
        playerLike(songId);

        updatePlayer(songId);


        // 重新加载音频并播放
        audio.load();  // 重新加载新源
        audio.play();  // 播放音频

    });



    //唱片机播放按钮
    $("#player-play-button").click(function (e) {
        e.preventDefault(); // 防止默认点击动作

        if (isMusicPlaying(audio)) {
            audio.pause();
            $("#player-play-button").html('<span class="glyphicon glyphicon-play play-icon"></span>');
        } else {
            audio.play();
            $("#player-play-button").html('<span class="glyphicon glyphicon-pause play-icon"></span>');
        }

    });

    //监听空格暂停
    document.addEventListener('keydown', function(event) {
        if (event.key === ' ' || event.keyCode === 32) {
            event.preventDefault(); // 防止页面滚动
            /*alert('空格键被按下了！');*/

            $("#player-play-button").click();
        }
    });

    //喜欢按钮
    $(document).on('click', '.like-button', async function (e) {
        e.preventDefault(); // 防止默认点击动作

        var button = $(this);

        // 禁用按钮，避免重复点击
        button.prop("disabled", true);

        // 获取当前点击按钮所在的最近的 li 元素的 id
        var songId = $(this).closest('li').attr('id');
        songId = parseInt(songId);

        currentId = $(".audioPlayer").attr('id');
        currentId = parseInt(currentId);  // 确保 currentId 是数字


        var search = getParameter("search");

        // 如果 search 存在，进行解码
        if (search) {
            search = decodeURIComponent(search); // 对 URL 编码进行解码
        }


        try {
            // 判断用户是否登录
            /*user = await $.get("user/findOne");*/
            if (!user) {
                // 用户未登录
                showDialog();
                return;
            }

            // 判断是否已经喜欢
            const info = await $.get("like/checkLike", { sid: songId, uid: user.uid });

            var playerID = $(".audioPlayer").attr('id')

            // 根据是否喜欢，执行相应的操作
            if (info.flag) {
                await toggleLike(false, songId, user.uid);

                if (songId == playerID) {
                    $("#player-like-button .like-icon").removeClass("like-active");
                }

            } else {
                await toggleLike(true, songId, user.uid);

                if (songId == playerID) {
                    $("#player-like-button .like-icon").addClass("like-active");
                }

            }

            // 喜欢/取消喜欢完成后刷新内容
            await like();
            await load(currentPageNum, search);

        } catch (error) {
            console.error("An error occurred:", error);
        } finally {
            // 请求完成后，重新启用按钮
            button.prop("disabled", false);
        }

        if (songId === currentId) {
            audio.pause();
            /*audio.delete()*/

            $(".audioPlayer").attr('id', 0);
            $("#audioSource").attr('src', null);

            // 重新加载新源
            audio.load();

            // 隐藏唱片机
            $('.record-player').removeClass('show');
        }

        //更新播放列表
        playList = removeBySid(songId);


    });


    // 播放器喜欢按钮
    $("#player-like-button").click(async function (e) {
        e.preventDefault(); // 防止默认点击动作

        if (!user) {
            // 用户未登录
            showDialog();
            return;
        }


        var songId = $(".audioPlayer").attr('id');
        songId = parseInt(songId);
        var userId = user.uid;  // 假设用户对象已存在，并且包含 uid
        var likeButton = $("#" + $.escapeSelector(songId) + " .like-button");
        var playerLikeButton = $(this);  // 当前按钮引用

        currentId = $(".audioPlayer").attr('id');
        currentId = parseInt(currentId);  // 确保 currentId 是数字

        // 禁用按钮，避免重复点击
        playerLikeButton.prop("disabled", true);

        try {
            if (likeButton.length) {
                // 如果歌曲在页面中，模拟点击 likeButton
                likeButton.click();

                // 等待 likeButton 的点击事件完成
                await new Promise(resolve => setTimeout(resolve, 100)); // 等待 100ms，确保状态更新

                // 检查喜欢状态并更新播放器喜欢按钮的图标
                if (likeButton.find(".like-icon").hasClass("like-active")) {
                    // 已喜欢，移除 like-active 类
                    $("#player-like-button .like-icon").removeClass("like-active");
                } else {
                    // 未喜欢， 添加like-active 类
                    $("#player-like-button .like-icon").addClass("like-active");
                }
            } else {
                // 歌曲不在页面中，检查是否已喜欢
                const info = await $.get("like/checkLike", { sid: songId, uid: userId });
                const isLiked = info.flag; // true 表示已喜欢

                // 调用通用的 toggleLike 函数，切换喜欢状态
                await toggleLike(!isLiked, songId, userId);

                // 更新播放器喜欢按钮的图标
                if (isLiked) {
                    // 已经喜欢，取消喜欢并移除 like-active 类
                    $("#player-like-button .like-icon").removeClass("like-active");
                } else {
                    // 未喜欢，添加喜欢并添加 like-active 类
                    $("#player-like-button .like-icon").addClass("like-active");
                }

                //暂停
                audio.pause();

                // 隐藏唱片机
                $('.record-player').removeClass('show');

                //更新播放列表
                playList = removeBySid(songId);

            }
        } catch (error) {
            console.error("操作失败:", error);
        } finally {
            // 操作完成后，重新启用按钮
            playerLikeButton.prop("disabled", false);
        }


    });


    //下载按钮
    $(document).on('click','.download-button',function (e) {
        e.preventDefault(); // 防止默认点击动作

        if (!user) {
            // 用户未登录
            showDialog();
            return;
        }

        var songId = $(this).closest('li').attr('id');
        songId = parseInt(songId);  // 确保 songId 是数字

        $.get("song/download",{sid:songId},function (link) {

            if (link != null && link.length>0)  {
                location.href = link;
            } else {
                showDLD();
            }

        });

    });

    //唱片机下载按钮
    $("#player-download-button").click(function (e) {
        e.preventDefault(); // 防止默认点击动作

        if (!user) {
            // 用户未登录
            showDialog();
            return;
        }

        var songId = $(".audioPlayer").attr('id');

        var downloadButton = $("#" + $.escapeSelector(songId) + " .download-button");

        if (downloadButton.length) {
            // 如果歌曲在页面中，模拟点击
            downloadButton.click();
        } else {
            $.get("song/download",{sid:songId},function (link) {
                if (link != null && link.length>0)  {
                    location.href = link;
                } else {
                    showDLD();
                }
            });
        }
    });

    //唱片机向后按钮
    $("#player-backward-button").click(function (e) {
        e.preventDefault(); // 防止默认点击动作

        // 停止唱片旋转
        $('.record').removeClass('playing');

        //正在播放歌曲的id
        currentId = $(".audioPlayer").attr('id');
        currentId = parseInt(currentId);
        //console.log("currentId:"+currentId);

        for (var i = 0; i < playList.length; i++) {
            if (currentId == playList[i].sid){
                if (i != 0) {
                    songId = playList[i-1].sid;
                    break;
                }
                songId = playList[playList.length-1].sid;
            }
        }

        songId = parseInt(songId);
        //console.log("songId:"+songId);


        $("#" + $.escapeSelector(currentId) + " .play-button").html('<span class="glyphicon glyphicon-play play-icon"></span>');

        $(".audioPlayer").attr('id', songId);
        $("#audioSource").attr('src', songList[songId - 1].filepath);

        //更新唱片机喜欢状态
        playerLike(songId);

        updatePlayer(songId);


        // 重新加载音频并播放
        audio.load();  // 重新加载新源
        audio.play();  // 播放音频


    });

    //唱片机向前按钮
    $("#player-forward-button").click(function (e) {
        e.preventDefault(); // 防止默认点击动作

        // 停止唱片旋转
        $('.record').removeClass('playing');

        //正在播放歌曲的id
        currentId = $(".audioPlayer").attr('id');
        currentId = parseInt(currentId);
        //console.log("currentId:"+currentId);

        for (var i = 0; i < playList.length; i++) {
            if (currentId == playList[i].sid){
                if (i < (playList.length-1)) {
                    songId = playList[i+1].sid;
                    break;
                }
                songId = playList[0].sid;
            }
        }

        songId = parseInt(songId);
        //console.log("songId:"+songId);


        $("#" + $.escapeSelector(currentId) + " .play-button").html('<span class="glyphicon glyphicon-play play-icon"></span>');

        $(".audioPlayer").attr('id', songId);
        $("#audioSource").attr('src', songList[songId - 1].filepath);

        //更新唱片机喜欢状态
        playerLike(songId);

        updatePlayer(songId);


        // 重新加载音频并播放
        audio.load();  // 重新加载新源
        audio.play();  // 播放音频

    });



    //唱片机播放模式切换

    // 定义四种模式对应的图片路径
    var modes = [
        "img/playMode/shuiXuPlay.png",    // 顺序播放
        "img/playMode/danQuXunHuan.png",  // 单曲循环
        "img/playMode/suiJiPlay.png",     // 随机播放
        "img/playMode/lieBiaoXunHuan.png"  // 列表循环
    ];


    // 定义模式的名称，用于更新 title 属性
    var modeTitles = [
        "顺序播放",
        "单曲循环",
        "随机播放",
        "列表循环"
    ];

    // 当前模式的索引
    var currentModeIndex = 0;


    //唱片机播放模式切换
    $("#player-playMode-button").click(function (e) {
        // 防止默认事件（如果有需要）
        e.preventDefault();

        //console.log(playList);

        // 更新当前模式索引，循环从 0 开始
        currentModeIndex = (currentModeIndex + 1) % modes.length;

        if (currentModeIndex === 2) { //对应随机播放
            playList = shuffleArray(playList.slice()); // 重新洗牌副本，避免修改原数组
        } else {
            playList = playList_copy.slice(); // 恢复原始顺序，创建副本
        }

        // 更新图片的 src 属性
        $(".playMode-icon-img").attr("src", modes[currentModeIndex]);

        // 更新按钮的 title 属性
        $(this).attr("title", modeTitles[currentModeIndex]);
    });


});

//唱片机喜欢图标状态
function playerLike(songId) {
    for (var i = 0; i < likeList.length; i++) {
        var likeId = likeList[i].sid;

        if (likeId == songId) {
            $("#player-like-button .like-icon").addClass("like-active");
            break;
        } else {
            $("#player-like-button .like-icon").removeClass("like-active");
        }
    }
}

// 定义洗牌函数
function shuffleArray(array) {
    for (var i = array.length - 1; i > 0; i--) {
        // 生成 0 到 i 之间的随机整数
        var j = Math.floor(Math.random() * (i + 1));
        // 交换元素 array[i] 和 array[j]
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    return array;
}

// 通用的喜欢/取消喜欢函数
async function toggleLike(isLike, songId, userId) {
    if (isLike) {
        // 添加喜欢
        await $.get("like/addLike", { sid: songId, uid: userId });
    } else {
        // 取消喜欢
        await $.get("like/cancelLike", { sid: songId, uid: userId });
    }
}

function isMusicPlaying(audioElement) {
    // 判断音乐是否在播放，返回布尔值
    return !audioElement.paused; // 如果 paused 为 false，说明正在播放，返回 true
}

//更新唱片机样式
function updatePlayer(songId) {
    //唱片机显示总时长
    $("#total-duration").text(songList[songId - 1].duration);
    //显示标题
    $("#current-song-title").text(songList[songId - 1].title); // 更新到界面
    $("#current-song-title").attr('title', songList[songId - 1].title); // 设置 title 属性
    //显示封面
    $("#recordCover").attr('src', songList[songId - 1].cover);
}

function removeBySid(sidToRemove) {
    return playList.filter(item => item.sid !== sidToRemove);
}

// 绑定双击事件
$(document).on('dblclick', '.song-item', function(e) {
    // 获取双击的目标元素
    var target = $(e.target);

    if (!target.closest('.song-actions').length) {
        // 如果双击的不是指定要排除的元素，则触发逻辑
        $(this).find('.play-button').click();
    }
});