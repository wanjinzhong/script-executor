<template>
  <div id="app">
    <Header></Header>
    <div>
      <el-card v-for="(k, i) in data" :key="i" class="card">
        <div class="item-content">
          <div class="name">{{k.name}}</div>
          <div class="desc">{{k.desc}}</div>
        </div>
        <div class="run-div">
          <el-button type="success" icon="el-icon-s-promotion" v-on:click="run(k.script)">Run</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>

    import Header from "./components/Header"

    export default {
        name: 'App',
        components: {Header},
        data() {
            return {
                data: []
            }
        },
        mounted() {
            const that = this
            this.axios.get("categories").then(res => {
                that.data = res.data.data;
            })
        },
        methods: {
            run(script) {
                this.axios.get("exec?script=" + script).then(res => {
                    this.$message({
                        type: "success",
                        message: "Running"
                    })
                })
            }
        }
    }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    margin: 20px;
    min-width: 800px;
  }

  .card {
    margin-top: 15px;
    margin-bottom: 15px;
  }

  .item-content, .run-div {
    display: inline-block;
  }

  .run-div {
    float: right;
  }

  .name {
    font-size: 18px;
    font-weight: bold;
  }

  .desc {
    font-style: italic;
    color: dimgrey;
  }
</style>
