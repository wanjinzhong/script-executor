<template>
  <div id="app">
    <Header style="margin-bottom: 10px;"></Header>
    <div style="background-color: rgba(137,138,116,0.29); width: 100%; height: 1px"/>
    <div style="margin-top: 10px;">
      <el-card v-for="(k, i) in data" :key="i" class="card" shadow="hover">
        <div slot="header">
          {{k.name}}
          <el-button style="float: right; font-size: 15px" type="success" icon="el-icon-s-promotion" size="small"
                     v-on:click="run(k.id)">Run
          </el-button>
        </div>
        <div class="desc">{{k.desc}}</div>
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
                    this.$message({
                        message: res.data,
                        type: "success",
                        duration: 5000
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
    display: inline-block;
    width: 500px;
    margin: 20px;
  }

  .item-content, .run-div {
    display: inline-block;
  }

  .run-div {
    float: right;
  }

  .name {
    font-weight: bold;
  }

  .desc {
    font-style: italic;
    color: dimgrey;
  }
</style>
