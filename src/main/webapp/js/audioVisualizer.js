$(function () {
    const audios = document.querySelectorAll('audio'); // 获取所有的 audio 元素
    const canvas = document.getElementById('visualizer');
    const canvasCtx = canvas.getContext('2d');
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();

    // 创建一个全局的 AnalyserNode
    const analyser = audioContext.createAnalyser();
    analyser.fftSize = 256;

    // 将 AnalyserNode 连接到 AudioContext.destination，只需连接一次
    analyser.connect(audioContext.destination);

    const bufferLength = analyser.frequencyBinCount;
    const dataArray = new Uint8Array(bufferLength);

    // 存储每个 audio 元素对应的 MediaElementSourceNode
    const audioSrcMap = new Map();
    let currentAudioSrc = null; // 当前正在连接的 MediaElementSourceNode

    audios.forEach(audio => {
        audio.addEventListener('play', function () {
            const currentAudio = this;

            // 如果有之前的音频源，断开其与 analyser 的连接
            if (currentAudioSrc && currentAudioSrc !== audioSrcMap.get(currentAudio)) {
                currentAudioSrc.disconnect();
            }

            // 检查是否已经为当前音频创建了 MediaElementSourceNode
            let audioSrc = audioSrcMap.get(currentAudio);
            if (!audioSrc) {
                audioSrc = audioContext.createMediaElementSource(currentAudio);
                audioSrcMap.set(currentAudio, audioSrc);
            }

            currentAudioSrc = audioSrc;

            // 将当前音频源连接到 analyser
            currentAudioSrc.connect(analyser);

            // 开始绘制可视化效果
            audioContext.resume().then(() => {
                drawVisualizer();
            });
        });
    });

    function drawVisualizer() {
        requestAnimationFrame(drawVisualizer);
        analyser.getByteFrequencyData(dataArray);

        // 清除之前绘制的内容
        canvasCtx.clearRect(0, 0, canvas.width, canvas.height);

        // 将画布的原点移到左下角并旋转90度（顺时针）
        canvasCtx.save();  // 保存画布的当前状态
        canvasCtx.translate(0, canvas.height); // 移动原点到左下角
        canvasCtx.rotate(-Math.PI / 2); // 顺时针旋转 90 度

        const barWidth = (canvas.height / bufferLength) * 1.5;
        let barHeight;
        let x = 0;

        for (let i = 0; i < bufferLength; i++) {
            // 增加 barHeight 高度
            barHeight = (dataArray[i] / 2) * 1.2;  // 20% 增加

            // 使用黑白灰的颜色基调
            const grayScale = barHeight + 10; // 灰度值
            const color = `rgb(${grayScale}, ${grayScale}, ${grayScale})`; // 颜色为黑、白、灰的基调

            canvasCtx.fillStyle = color;
            canvasCtx.fillRect(x, canvas.width - barHeight, barWidth, barHeight); // 画出条形

            x += barWidth + 1;
        }

        canvasCtx.restore(); // 恢复画布的状态
    }
});

