<template id="board">
  <div>
    <div class="board" v-if="board != null">
      <div v-for="rank in board.grid" class="rank">
        <div v-for="piece in rank" class="piece">
          <div v-if="piece != null">{{piece | piece}}</div>
          <div v-if="piece == null">&nbsp;</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  Vue.component("board", {
    template: "#board",
    filters: {
      piece: function (piece) {
        if (piece.color === 'WHITE') {
          switch (piece.name.toLowerCase()) {
            case 'pawn': return '♙';
            case 'rook': return '♖';
            case 'queen': return '♕';
            case 'knight': return '♘';
            case 'bishop': return '♗';
            case 'king': return '♔';
            default: return `${piece.color[0]}${piece.name}`;
          }
        } else if (piece.color === 'BLACK') {
          switch (piece.name.toLowerCase()) {
            case 'pawn': return '♟';
            case 'rook': return '♜';
            case 'queen': return '♛';
            case 'knight': return '♞';
            case 'bishop': return '♝';
            case 'king': return '♚';
            default: return `${piece.color[0]}${piece.name}`;
          }
        }
      }
    },
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
    margin: 5rem auto;
    width: 36rem;
    box-shadow: 0 0 5rem #828282;
    border-radius: .5rem;
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
    font-size: 2.5rem;
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
