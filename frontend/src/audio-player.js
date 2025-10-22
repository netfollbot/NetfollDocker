window.audioPlayer = (function() {
  let audioEl;
  return {
    play: function(url, loop) {
      if (!audioEl) {
        audioEl = new Audio();
        audioEl.volume = 0.4;
      }
      audioEl.src = url;
      audioEl.loop = !!loop;
      audioEl.play().catch(()=>{});
    },
    stop: function() {
      if (audioEl) {
        audioEl.pause();
        audioEl.currentTime = 0;
      }
    }
  };
})();
