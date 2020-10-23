<template id="board">
  <div>
    <pre v-if="debug">{{allowedNextPositions}}</pre>
    <div class="nextTurn" v-bind:class="board.nextTurn.toLowerCase()"></div>
    <div class="board" v-if="board != null">
      <div v-for="(rank, rankIdx) in board.grid" class="rank">
        <div v-for="(piece, fileIdx) in rank" class="piece"
             v-bind:class="{ allowedNextPosition: isAllowedNextPosition(fileIdx, rankIdx), selected: selected.id != null && selected.id === piece?.id}">
          <button v-if="piece != null"
                  v-bind:disabled="piece.color !== board.nextTurn && !isAllowedNextPosition(fileIdx, rankIdx)"
                  v-bind:class="piece.color.toLowerCase()"
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
        return ({
          'pawn': '♟',
          'rook': '♜',
          'queen': '♛',
          'knight': '♞',
          'bishop': '♝',
          'king': '♚',
        })[piece.name.toLowerCase()] || piece.name
      }
    },
    data: () => ({
      board: null,
      allowedNextPositions: [],
      debug: new URLSearchParams(location.search).get('debug') != null,
      selected: {},
    }),
    created() {
      const searchParams = new URLSearchParams(location.search);
      if (searchParams.get('board') != null) {
        fetch(`api/board/${searchParams.get('board')}`)
          .then(res => res.json())
          .then(res => this.board = res);
      } else {
        fetch("api/board", {method: 'POST'})
          .then(res => res.json())
          .then(res => {
            searchParams.set('board', res.id);
            history.replaceState(null, '', '?' + searchParams.toString());
            this.board = res;
          });
      }
    },
    methods: {
      select(piece) {
        if (piece.id === this.selected?.id) {
          this.selected = {};
          this.allowedNextPositions = [];
          return;
        }
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
          .then(res => {
            this.board = res;
            this.selected = {};
            this.allowedNextPositions = [];
          });
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
    padding: 0 .5rem;
    margin: 0;
    background: transparent;
    font-size: 3rem;
    cursor: pointer;
  }

  .piece button.white {
    color: white;
    text-shadow: 0 0 5px #000;
  }

  .piece button.black {
    color: black;
    text-shadow: 0 0 5px #fff;
  }

  .piece button[disabled] {
    cursor: inherit;
  }

  .piece button.white[disabled] {
    color: white
  }

  .piece button.black[disabled] {
    color: black;
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

  .nextTurn {
    position: absolute;
    height: 2rem;
    width: 2rem;
    border-radius: 50%;
    box-shadow: 0 0 1rem black;
    top: 1.5rem;
    right: 3rem;
  }

  .nextTurn.white {
    background-color: white;
  }

  .nextTurn.black {
    background-color: black;
  }

  .rank:nth-child(odd) .piece:nth-child(even) {
    background-color: #d18b47;
  }

  .rank:nth-child(odd) .piece:nth-child(odd) {
    background-color: #ffce9e;
  }

  .rank:nth-child(even) .piece:nth-child(odd) {
    background-color: #d18b47;
  }

  .rank:nth-child(even) .piece:nth-child(even) {
    background-color: #ffce9e;
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
