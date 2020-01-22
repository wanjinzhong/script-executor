<template>
  <div id="app">
    <Header style="margin-bottom: 10px;"></Header>
    <div style="background-color: rgba(137,138,116,0.29); width: 100%; height: 1px"/>
    <div style="margin-top: 10px;">
      <el-card v-for="(k, i) in data" :key="i" class="card" shadow="hover">
        <div slot="header">
          {{k.name}}
          <div style="float: right; font-size: 15px">
            <el-button icon="el-icon-document" size="small"
                       v-on:click="openLogs(k)">Logs
            </el-button>
            <el-button type="success" icon="el-icon-s-promotion" size="small"
                       v-on:click="run(k.id)">Run
            </el-button>
          </div>
        </div>
        <div class="desc">{{k.desc}}</div>
      </el-card>
    </div>
    <el-dialog
      :title="logTitle"
      :visible.sync="logVisible"
      width="80%"
      destroy-on-close
      :before-close="handleLogClose">
      <!--      <el-input class="log_content"-->
      <!--        type="textarea" v-model="logs" readonly style="height: 500px;width: 100%">-->
      <!--      </el-input>-->
      <div
        style="height: 500px;width: 99%; overflow: auto; border: rgba(105,105,105,0.31) solid 1px; padding: 5px; border-radius: 3px"
        id="content" v-on:scroll="test();">
        <div v-for="(log, i) in logs" style="margin-top: 2px; margin-bottom: 2px; " :key="i"
             :class="i === (logs.length - 1)? 'log_end':'log'" v-html="log">
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
      <el-button @click="handleLogClose">Close</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>

    import Header from "./components/Header"

    export default {
        name: 'App',
        components: {Header},
        data() {
            return {
                data: [],
                logTitle: "",
                logVisible: false,
                logs: [],
                interval: "",
                enableScroll: true
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
            },
            handleLogClose() {
                this.logVisible = false;
                clearInterval(this.interval)
            },
            openLogs(category) {
                this.getLogs(category)
                const that = this
                this.interval = setInterval(function () {
                    that.getLogs(category)
                }, 1000)
                this.logTitle = category.name + " - Logs"
                this.logVisible = true
            },
            getLogs(category) {
                this.axios.get('logs?id=' + category.id).then(res => {
                    for (const d in res.data) {
                        res.data[d] = res.data[d].replace(/\u001B\[0m/g,"</span>")
                        res.data[d] = res.data[d].replace(/\u001B\[30m/g,"<span style='color: black'>")
                        res.data[d] = res.data[d].replace(/\u001B\[31m/g,"<span style='color: red'>")
                        res.data[d] = res.data[d].replace(/\u001B\[32m/g,"<span style='color: green'>")
                        res.data[d] = res.data[d].replace(/\u001B\[33m/g,"<span style='color: yellow'>")
                        res.data[d] = res.data[d].replace(/\u001B\[34m/g,"<span style='color: blue'>")
                        res.data[d] = res.data[d].replace(/\u001B\[35m/g,"<span style='color: purple'>")
                        res.data[d] = res.data[d].replace(/\u001B\[36m/g,"<span style='color: darkgreen'>")
                        res.data[d] = res.data[d].replace(/\u001B\[37m/g,"<span style='color: white'>")
                    }
                    this.logs = res.data
                    const ele = document.querySelector(".log_end");
                    if (ele != null && this.enableScroll) {
                        ele.scrollIntoView({
                            block: 'start',
                            behavior: 'smooth'
                        })
                    }
                })
            },
            test() {
                var panel = document.getElementById("content");
                var scrollTop, maxScroll, minScroll = 0;
                scrollTop = panel.scrollTop;
                maxScroll = panel.scrollHeight - panel.offsetHeight;
                this.enableScroll = scrollTop >= maxScroll
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

  .desc {
    font-style: italic;
    color: dimgrey;
  }

  .log_content textarea {
    height: 100%;
  }
</style>
