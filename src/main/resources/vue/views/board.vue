<template id="board">
  <div v-if="board">
    <pre v-if="debug">{{board.castlingAllowed}}</pre>
    <div class="overlay" v-if="pendingPromotion != null">
      <div class="promotion">
        <button v-on:click="promote('rook')">♜</button>
        <button v-on:click="promote('queen')">♛</button>
        <button v-on:click="promote('knight')">♞</button>
        <button v-on:click="promote('bishop')">♝</button>
      </div>
    </div>
    <div class="captured">
      <div v-for="(piece,idx) in board.captured" v-bind:class="piece.color.toLowerCase()">
        {{piece | piece}}
        <div v-if="justCaptured && idx === board.captured.length - 1" class="justCaptured"></div>
      </div>
    </div>
    <div class="nextTurn" v-bind:class="board.nextTurn.toLowerCase()"></div>
    <button class="castling kingside"
            v-if="castlingAllowed('KINGSIDE')"
            v-on:click="castle('KINGSIDE')">
      <span>♚</span>
      <span>♜</span>
    </button>
    <button class="castling queenside"
            v-if="castlingAllowed('QUEENSIDE')"
            v-on:click="castle('QUEENSIDE')">
      <span>♜</span>
      <span>♚</span>
    </button>
    <div class="board" v-if="board != null">
      <div v-for="(rank, rankIdx) in board.grid" class="rank">
        <div v-for="(piece, fileIdx) in rank" class="piece"
             v-bind:class="pieceClasses(fileIdx, rankIdx, piece)">
          <button v-if="piece != null"
                  v-bind:disabled="piece.color !== board.nextTurn && !isAllowedNextPosition(fileIdx, rankIdx)"
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
  const uuid = localStorage.getItem('uuid') || (() => {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      const r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  })();
  localStorage.setItem('uuid', uuid);

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
      justCaptured: false,
      allowedNextPositions: [],
      debug: new URLSearchParams(location.search).get('debug') != null,
      selected: {},
      pendingPromotion: null,
    }),
    created() {
      let boardIdPromise;
      const searchParams = new URLSearchParams(location.search);
      if (searchParams.get('board') != null) {
        boardIdPromise = fetch(`api/board/${searchParams.get('board')}`)
          .then(res => res.json())
          .then(res => {
            this.board = res;
            return res.id;
          });
      } else {
        boardIdPromise = fetch("api/board", {method: 'POST', headers: {'auth': uuid}})
          .then(res => res.json())
          .then(res => {
            searchParams.set('board', res.id);
            history.replaceState(null, '', '?' + searchParams.toString());
            this.board = res;
            return res.id
          });
      }
      boardIdPromise.then(boardId => {
        const poll = () => {
          setTimeout(() => {
            fetch(`api/board/${boardId}`)
              .then(res => res.json())
              .then(res => {
                if (res.movements.length !== this.board.movements.length) {
                  this.justCaptured = res.captured.length !== this.board.captured.length;
                }
                this.board = res;
              })
              .then(poll);
          }, 3000);
        };
        poll();
      })
    },
    computed: {
      isMyTurn() {
        if (this.board?.nextTurn == null) return false;
        return ((this.board.nextTurn === 'WHITE' && (this.board.uuidWhite === uuid || this.board.uuidWhite == null))
          || (this.board.nextTurn === 'BLACK' && (this.board.uuidBlack === uuid || this.board.uuidBlack == null)))
      },
      lastMovement() {
        const last = this.board.movements[this.board.movements.length - 1];
        if (last == null) return {from: null, to: null};
        const to = {position: last.second, piece: last.first};
        const movementsOfPieces = this.board.movements
          .filter(m => m.first.id === last.first.id)
        let from;
        if (movementsOfPieces.length <= 1) {
          from = {position: last.first.position, piece: last.first};
        } else {
          const movement = movementsOfPieces[movementsOfPieces.length - 2];
          from = {position: movement.second, piece: movement.first};
        }
        return {from, to}
      }
    },
    methods: {
      pieceClasses(fileIdx, rankIdx, piece) {
        const {to, from} = this.lastMovement;
        return [
          {
            allowedNextPosition: this.isAllowedNextPosition(fileIdx, rankIdx),
            selected: this.selected.id != null && this.selected.id === piece?.id,
            ...to != null && {to: to.position.file === fileIdx && to.position.rank === rankIdx},
            ...from != null && {from: from.position.file === fileIdx && from.position.rank === rankIdx},
          },
          piece != null ? piece.color.toLowerCase() : (from != null && from.piece.color.toLowerCase())
        ]
      },
      castlingAllowed(side) {
        return this.isMyTurn && this.board.castlingAllowed[this.board.nextTurn].includes(side);
      },
      select(piece) {
        if (piece.id === this.selected?.id) {
          this.selected = {};
          this.allowedNextPositions = [];
          return;
        }
        if (!this.isMyTurn) {
          return;
        }
        this.selected = piece;
        fetch(`api/board/${this.board.id}/piece/${piece.id}/allowed-next-positions`)
          .then(res => res.json())
          .then(res => this.allowedNextPositions = res)
      },
      promote(promotion) {
        fetch(
          `api/board/${this.board.id}/turn`,
          {
            method: 'POST',
            body: JSON.stringify({piece: this.selected.id, target: this.pendingPromotion, promotion}),
            headers: {'Content-Type': 'application/json'}
          })
          .then(res => res.json())
          .then(res => {
            this.board = res;
            this.selected = {};
            this.allowedNextPositions = [];
            this.pendingPromotion = null;
          });

      },
      move(file, rank) {
        if (this.selected.name.toLowerCase() === 'pawn' && (rank === 7 || rank === 0)) {
          this.pendingPromotion = {file, rank};
          return;
        }
        const piece = this.selected;
        fetch(
          `api/board/${this.board.id}/turn`,
          {
            method: 'POST',
            body: JSON.stringify({piece: piece.id, target: {file, rank}}),
            headers: {'Content-Type': 'application/json', 'auth': uuid}
          })
          .then(res => res.json())
          .then(res => {
            this.justCaptured = res.captured.length !== this.board.captured.length;
            this.board = res;
            this.selected = {};
            this.allowedNextPositions = [];
          });
      },
      castle(side) {
        fetch(`api/board/${this.board.id}/castle/${side.toLowerCase()}`, {method: 'POST'})
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
  .overlay {
    display: flex;
    justify-content: center;
    align-items: center;
    align-content: center;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background-color: white;
    opacity: 0.8;
    backdrop-filter: blur(20px);
  }

  .overlay .promotion button {
    height: 8rem;
    width: 8rem;
    margin: 1rem;
    font-size: 4rem;
    cursor: pointer;
    border-radius: .2rem;
    transition: transform .1s;
  }

  .overlay .promotion button:hover {
    transform: scale(1.2);
    transition: transform .3s;
  }

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
    position: relative;
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

  .piece button:focus {
    outline: none;
  }

  .piece.white button {
    color: white;
    text-shadow: 0 0 5px #000;
  }

  .piece.black button {
    color: black;
    text-shadow: 0 0 5px #fff;
  }

  .piece button[disabled] {
    cursor: inherit;
  }

  .piece.white button[disabled] {
    color: white
  }

  .piece.black button[disabled] {
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

  .piece.white.to,
  .piece.white.from {
    box-shadow: inset 0 0 0 2px white;
  }

  .piece.black.to,
  .piece.black.from {
    box-shadow: inset 0 0 0 2px black;
  }

  .castling {
    position: absolute;
    left: 1rem;
  }

  .castling.queenside {
    top: 11rem;
  }

  button.castling {
    padding: 0 .5rem;
    margin: 0;
    font-size: 3.5rem;
    cursor: pointer;
    border-radius: 50%;
    height: 4rem;
    width: 4rem;
    overflow: hidden;
    outline: none;
    border: none;
    box-shadow: 0 0 5px #000;
  }

  button.castling:active {
    border: 1px outset black;
  }

  .castling span {
    display: inline-block;
    position: absolute;
    top: -.7rem;
    left: -.4rem;
  }

  .castling span:last-child {
    left: 1.3rem;
    top: .5rem;
  }

  .piece.white button.castling {
    background-color: #000;
  }

  .piece.black button.castling {
    background-color: #fff;
  }

  .piece.white .castling {
    color: #fff;
  }

  .piece.black .castling {
    color: #000;
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

  .captured {
    display: flex;
    flex-direction: column-reverse;
    position: absolute;
    font-size: 3rem;
    background-color: rgba(248, 248, 248, 0.6);
    backdrop-filter: blur(20px);
    padding: 1rem;
    border-radius: 1rem;
    right: 2rem;
  }

  .captured .white {
    color: white;
    text-shadow: 0 0 5px #000;
  }

  .captured .black {
    color: black;
    text-shadow: 0 0 5px #fff;
  }

  .captured .justCaptured {
    position: absolute;
    width: 2.2rem;
    background-color: transparent;
    box-shadow: .2rem -1.3rem 1.2rem .6rem red;
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
