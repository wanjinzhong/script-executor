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
          <el-button type="success" icon="el-icon-s-promotion" v-on:click="run(k.id)">Run</el-button>
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
            run(id) {
                this.axios.get("exec?id=" + id).then(res => {
                    if (res.data === "OK") {
                        this.$message.success("OK, Let`s run...")
                    } else {
                        this.$message.error("Failed to run this script...")
                    }
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
