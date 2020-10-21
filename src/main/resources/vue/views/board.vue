<template id="board">
  <div>
    <pre v-if="debug">{{allowedNextPositions}}</pre>
    <div class="board" v-if="board != null">
      <div v-for="(rank, rankIdx) in board.grid" class="rank">
        <div v-for="(piece, fileIdx) in rank" class="piece"
             v-bind:class="{ allowedNextPosition: isAllowedNextPosition(fileIdx, rankIdx), selected: selected.id != null && selected.id === piece?.id}">
          <button v-if="piece != null"
                  v-on:click="isAllowedNextPosition(fileIdx, rankIdx) ? move(fileIdx, rankIdx) : select(piece)">
            {{piece | piece}}
          </button>
          <button v-if="piece == null && isAllowedNextPosition(fileIdx, rankIdx)"
                  v-on:click="move(fileIdx, rankIdx)">
            &nbsp;
          </button>
          <span v-if="debug">
            {{fileIdx}},{{rankIdx}}
            :{{isAllowedNextPosition(fileIdx, rankIdx) ? 'npY' : 'npN'}}
            :{{selected.id === piece?.id ? 'sY' : 'sN'}}
          </span>
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
            // @formatter:off
            case 'pawn': return '♙';
            case 'rook': return '♖';
            case 'queen': return '♕';
            case 'knight': return '♘';
            case 'bishop': return '♗';
            case 'king': return '♔';
            default: return `${piece.color[0]}${piece.name}`;
            // @formatter:on
          }
        } else if (piece.color === 'BLACK') {
          switch (piece.name.toLowerCase()) {
            // @formatter:off
            case 'pawn': return '♟';
            case 'rook': return '♜';
            case 'queen': return '♛';
            case 'knight': return '♞';
            case 'bishop': return '♝';
            case 'king': return '♚';
            default: return `${piece.color[0]}${piece.name}`;
            // @formatter:on
          }
        }
      }
    },
    data: () => ({
      board: null,
      allowedNextPositions: [],
      debug: new URLSearchParams(location.search).get('debug') != null,
      selected: {},
    }),
    created() {
      fetch("api/board", {method: 'POST'})
        .then(res => res.json())
        .then(res => this.board = res);
    },
    methods: {
      select(piece) {
        this.selected = piece;
        fetch(`api/board/${this.board.id}/piece/${piece.id}/allowed-next-positions`)
          .then(res => res.json())
          .then(res => this.allowedNextPositions = res)
      },
      move(file, rank) {
        const piece = this.selected;
        fetch(
          `api/board/${this.board.id}/turn`,
          {
            method: 'POST',
            body: JSON.stringify({piece: piece.id, target: {file, rank}}),
            headers: {'Content-Type': 'application/json'}
          })
          .then(res => res.json())
          .then(res => this.board = res);
      },
      isAllowedNextPosition(file, rank) {
        return !!this.allowedNextPositions
          .find(pos => pos.file === file && pos.rank === rank)
      }
    },
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
    display: flex;
    justify-content: center;
    align-items: center;
    align-content: center;
  }

  .piece button {
    border: none;
    padding: 0;
    margin: 0;
    background: transparent;
    font-size: 2.5rem;
    text-shadow: 0 0 5px #fff;
    cursor: pointer;
  }

  .piece.selected {
    box-shadow: inset 0 0 10px black;
  }

  .piece.allowedNextPosition {
    z-index: 1;
    transition: transform .5s ease-out;
    box-shadow: 0 0 5px black;
  }

  .piece.allowedNextPosition button {
    padding: 0 1.3rem;
  }

  .piece.allowedNextPosition:hover {
    transform: scale(1.2);
    box-shadow: 0 0 5px black;
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
