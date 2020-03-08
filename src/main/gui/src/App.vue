<template>
  <div id="app">
    <Header style="margin-bottom: 10px;"></Header>
    <div style="background-color: rgba(137,138,116,0.29); width: 100%; height: 1px"/>
    <div style="margin-top: 10px; vertical-align: top">
      <el-collapse v-model="groups">
        <el-collapse-item v-for="(group, i) in data" :key="i" :title="group.group" :name="group.group">
          <el-card v-for="(k, i) in group.categories" :key="i" class="card" shadow="hover">
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
        <div class="desc" v-html="k.desc"></div>
      </el-card>
        </el-collapse-item>
      </el-collapse>
    </div>
    <el-dialog
      :title="logTitle"
      :visible.sync="logVisible"
      width="80%"
      destroy-on-close
      :before-close="handleLogClose">
      <div
        id="content" v-on:scroll="scroll()">
        <div v-for="(log, i) in logs" style="margin-top: 2px; margin-bottom: 2px; " :key="i"
             :class="i === (logs.length - 1)? 'log_end':'log'" v-html="log">
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="allLogs()" size="small">Full Log</el-button>
        <el-button @click="handleLogClose" size="small">Close</el-button>
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
                logId: 0,
                logVisible: false,
                logs: [],
                interval: "",
                enableScroll: true,
                groups: []
            }
        },
        mounted() {
            const that = this
            this.axios.get("categories").then(res => {
                that.data = res.data.data;
                let reg = /(http:\/\/)?([A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*)/g;
                for (let i in that.data) {
                  that.groups.push(that.data[i].group)
                  for (let j in that.data[i].categories) {
                    that.data[i].categories[j].desc = that.data[i].categories[j].desc.replace(reg, function (a, b, c) {
                      return '<a href="http://' + c + '"\>' + a + '</a>';
                    });
                  }
                }
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
                this.logTitle= "";
                this.logId= 0;
                this.logs= [];
                this.enableScroll= true;
                clearInterval(this.interval)
            },
            openLogs(category) {
                this.getLogs(category)
                const that = this
                this.interval = setInterval(function () {
                    that.getLogs(category)
                }, 1000)
                this.logTitle = category.name + " - Logs"
                this.logId = category.id
                this.logVisible = true
            },
            getLogs(category) {
                this.axios.get('logs?id=' + category.id).then(res => {
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
            scroll() {
                var panel = document.getElementById("content");
                var scrollTop, maxScroll, minScroll = 0;
                scrollTop = panel.scrollTop;
                maxScroll = panel.scrollHeight - panel.offsetHeight;
                this.enableScroll = scrollTop >= maxScroll
            },
            allLogs() {
                window.open('allLogs?id=' + this.logId)
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
    vertical-align: top;
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

  #content {
    height: 500px;
    width: 98%;
    overflow: auto;
    border: rgba(105, 105, 105, 0.31) solid 1px;
    border-radius: 3px;
    padding: 10px;
  }
</style>
