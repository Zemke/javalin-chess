<template id="board">
  <div>
    <h1>Javalin Chess</h1>
    <div class="board">
      <div v-if="board != null" v-for="rank in board.grid" class="rank">
        <div v-for="piece in rank" class="piece">
          <div v-if="piece != null">{{piece.color[0]}}{{piece.name}}</div>
          <div v-if="piece == null">&nbsp;</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  Vue.component("board", {
    template: "#board",
    data: () => ({
      board: null,
    }),
    created() {
      fetch("api/board", {method: 'POST'})
        .then(res => res.json())
        .then(res => this.board = res);
    }
  });
</script>
<style>
  h1 {
    text-align: center;
  }

  .board {
    margin: auto;
    width: 36rem;
  }

  .rank {
    display: flex;
    justify-content: center;
    align-items: center;
    align-content: center;
  }

  .piece {
    width: 4.5rem;
    height: 4.5rem;
    text-shadow: 0 0 5px #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    align-content: center;
  }

  .rank:nth-child(odd) .piece:nth-child(even) {
    background-color: #ffce9e;
  }

  .rank:nth-child(odd) .piece:nth-child(odd) {
    background-color: #d18b47;
  }

  .rank:nth-child(even) .piece:nth-child(odd) {
    background-color: #ffce9e;
  }

  .rank:nth-child(even) .piece:nth-child(even) {
    background-color: #d18b47;
  }

  .rank:first-child .piece:first-child {
    border-top-left-radius: .5rem;
  }

  .rank:first-child .piece:last-child {
    border-top-right-radius: .5rem;
  }

  .rank:last-child .piece:first-child {
    border-bottom-left-radius: .5rem;
  }

  .rank:last-child .piece:last-child {
    border-bottom-right-radius: .5rem;
  }
</style>
