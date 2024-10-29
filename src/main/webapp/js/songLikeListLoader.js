var likeList = [];

var currentPageNum;

var search;

function like() {
    return new Promise(function(resolve, reject) {
        $.get("like/queryLike", {}, function(list) {
            likeList = list;
            resolve();  // 解决 Promise，表示 `like()` 完成
        }).fail(function() {
            reject();  // 处理错误情况
        });
    });
}


$(function () {
    //获取search的参数值
    search = getParameter("search");
    //search不为空或者""，解码,并显示search-feedback
    if (search) {
        search = window.decodeURIComponent(search);

        console.log(search);

        $("#search-content").text(search);
        $(".search-feedback").css('visibility', 'visible');
    } else {
        $(".search-feedback").css('visibility', 'hidden');
    }

    // 首先调用 like，然后在它完成后调用 load
    like().then(function() {
        load(null, search);  // like() 完成后再加载页面内容
    }).catch(function(error) {
        console.error("Error loading like list: ", error);
    });

});

function load(currentPage, search) {
    //发送ajax请求
    $.get("song/likePageQuery", {currentPage: currentPage, search: search}, function (pb) {
        currentPageNum = currentPage;

        //1.分页工具条展示
        var lis = "";

        var firstPage = '<li onclick="javascript:like(); load(null,\'' + search + '\'); "><a href="javascript:void(0)">首页</a></li>';

        //计算上一页的页码
        var preNum = pb.currentPage - 1;
        if (preNum <= 1) {
            preNum = 1;
        }

        var prepage = '<li onclick="javascript:like(); load(' + preNum + ',\'' + search + '\'); ">\n' +
            '          <a href="javascript:void(0);" aria-label="Previous">\n' +
            '            <span aria-hidden="true">&laquo;</span>\n' +
            '          </a>\n' +
            '        </li>';

        lis += firstPage;
        lis += prepage;

        var begin;  //开始位置
        var end;  //结束位置

        //显示5个页码
        if (pb.totalPage < 5) {
            begin = 1;
            end = pb.totalPage;
        } else {

            begin = pb.currentPage - 2;
            end = pb.currentPage + 2;

            //如果前面不够2个，后面补齐
            if (begin < 1) {
                begin = 1;
                end = begin + 4;
            }

            //如果后面不足2个,前面补齐
            if (end > pb.totalPage) {
                end = pb.totalPage;
                begin = end - 4;
            }
        }

        for (var i = begin; i <= end; i++) {
            var li;
            //判断当前页码是否等于i
            if (i == pb.currentPage) {
                li = '<li class="active" onclick="javascript:like(); load(' + i + ',\'' + search + '\');"><a href="javascript:void(0);">' + i + '</a></li>';
            } else {
                li = '<li onclick="javascript:like(); load(' + i + ',\'' + search + '\'); "><a href="javascript:void(0);">' + i + '</a></li>';
            }
            lis += li;
        }

        //计算下一页的页码
        var nextNum = pb.currentPage + 1;
        if (nextNum >= pb.totalPage) {
            nextNum = pb.totalPage;
        }

        var nextPage = '<li onClick="like(); javascript:load(' + nextNum + ',\'' + search + '\');">\n' +
            '          <a href="javascript:void(0);" aria-label="Next">\n' +
            '            <span aria-hidden="true">&raquo;</span>\n' +
            '          </a>\n' +
            '        </li>';
        var lastPage = '<li onclick="javascript:like(); load(' + pb.totalPage + ',\'' + search + '\');"><a href="javascript:void(0);">末页</a></li>';

        lis += nextPage;
        lis += lastPage;

        $("#pageNum").html(lis);




        //2. 列表数据展示
        var song_lis = "";

        // 播放器当前播放歌曲的 id
        var currentId = $(".audioPlayer").attr('id');
        currentId = parseInt(currentId);  // 确保 currentId 是数字
        var audio = $("#myAudio")[0];

        // 遍历歌曲列表
        for (var i = 0; i < pb.list.length; i++) {
            var song = pb.list[i];
            var li = '';  // 在循环的每次迭代中初始化 li，确保它不会保持上次循环的值

            // 处理播放状态和喜欢状态
            var isPlaying = (song.sid == currentId && isMusicPlaying(audio));
            var isLiked = false;

            // 查找该歌曲是否在 likeList 中
            for (var j = 0; j < likeList.length; j++) {
                var likeId = likeList[j].sid;
                /*console.log(likeId);*/
                if (song.sid == likeId) {
                    isLiked = true;
                    break;
                }
            }

            // 构建 li 标签
            li += '<li class="song-item" id="' + song.sid + '">\n' +
                '    <img src="' + song.cover + '" alt="Song Cover" class="song-cover">\n' +
                '    <div class="song-info">\n' +
                '        <h5 class="song-title" title="' + song.title + '">' + song.title + '</h5>\n' +
                '        <p class="song-artist">' + song.artist + '</p>\n' +
                '        <p class="song-album">' + song.album + '</p>\n' +
                '    </div>\n' +
                '    <div class="song-actions">\n' +
                '        <span class="song-duration">' + song.duration + '</span>\n' +
                '        <a href="#" class="icon-link download-button">\n' +
                '            <span class="glyphicon glyphicon-download-alt download-icon"></span>\n' +
                '        </a>\n';

            // 添加播放按钮
            li += '        <a href="#" class="icon-link play-button">\n' +
                '            <span class="glyphicon glyphicon-' + (isPlaying ? 'pause' : 'play') + ' play-icon"></span>\n' +
                '        </a>\n';

            // 添加喜欢按钮
            li += '        <a href="#" class="icon-link like-button">\n' +
                '            <span class="glyphicon glyphicon-heart like-icon ' + (isLiked ? 'like-active' : '') + '"></span>\n' +
                '        </a>\n';

            // 添加音频元素
            li += '        <audio src="' + song.filepath + '"></audio>\n' +
                '    </div>\n' +
                '</li>';

            // 将 li 添加到 song_lis 中
            song_lis += li;
        }

        // 更新歌曲列表
        $("#song-list").html(song_lis);
    })
}
