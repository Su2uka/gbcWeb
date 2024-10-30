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
    // 发送 AJAX 请求
    $.get("song/likePageQuery", {currentPage: currentPage, search: search}, function (pb) {
        currentPageNum = currentPage;

        // 1. 分页工具条展示
        const lis = [];

        // 首页和上一页
        const preNum = Math.max(pb.currentPage - 1, 1);
        lis.push(`
            <li onclick="like(); load(null, '${search}');"><a href="#">首页</a></li>
            <li onclick="like(); load(${preNum}, '${search}');">
                <a href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
        `);

        // 计算页码范围
        let begin = Math.max(pb.currentPage - 2, 1);
        let end = Math.min(pb.currentPage + 2, pb.totalPage);

        if (pb.totalPage > 5) {
            if (end - begin < 4) {
                if (begin === 1) {
                    end = begin + 4;
                } else if (end === pb.totalPage) {
                    begin = end - 4;
                }
            }
        } else {
            begin = 1;
            end = pb.totalPage;
        }

        // 中间页码
        for (let i = begin; i <= end; i++) {
            lis.push(`
                <li class="${i === pb.currentPage ? 'active' : ''}" onclick="like(); load(${i}, '${search}');">
                    <a href="#">${i}</a>
                </li>
            `);
        }

        // 下一页和末页
        const nextNum = Math.min(pb.currentPage + 1, pb.totalPage);
        lis.push(`
            <li onclick="like(); load(${nextNum}, '${search}');">
                <a href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
            <li onclick="like(); load(${pb.totalPage}, '${search}');"><a href="#">末页</a></li>
        `);

        $("#pageNum").html(lis.join(''));

        // 2. 列表数据展示
        const songItems = pb.list.map(song => {
            // 处理播放状态和喜欢状态
            const currentId = parseInt($(".audioPlayer").attr('id'));
            const audio = $("#myAudio")[0];
            const isPlaying = song.sid === currentId && isMusicPlaying(audio);
            const isLiked = likeList.some(like => like.sid === song.sid);

            return `
                <li class="song-item" id="${song.sid}">
                    <img src="${song.cover}" alt="Song Cover" class="song-cover">
                    <div class="song-info">
                        <h5 class="song-title" title="${song.title}">${song.title}</h5>
                        <p class="song-artist">${song.artist}</p>
                        <p class="song-album">${song.album}</p>
                    </div>
                    <div class="song-actions">
                        <span class="song-duration">${song.duration}</span>
                        <a href="${song.download || '#'}" class="icon-link download-button">
                            <span class="glyphicon glyphicon-download-alt download-icon"></span>
                        </a>
                        <a href="#" class="icon-link play-button">
                            <span class="glyphicon glyphicon-${isPlaying ? 'pause' : 'play'} play-icon"></span>
                        </a>
                        <a href="#" class="icon-link like-button">
                            <span class="glyphicon glyphicon-heart like-icon ${isLiked ? 'like-active' : ''}"></span>
                        </a>
                        <audio src="${song.filepath}"></audio>
                    </div>
                </li>
            `;
        });

        // 更新歌曲列表
        $("#song-list").html(songItems.join(''));
    });
}

