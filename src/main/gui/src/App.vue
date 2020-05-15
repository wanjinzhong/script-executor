<template>
  <div id="app">
    <Header style="margin-bottom: 10px;"></Header>
    <div style="background-color: rgba(137,138,116,0.29); width: 100%; height: 1px"/>
    <div style="margin-top: 10px; vertical-align: top">
      <el-collapse v-model="activeGroup">
        <el-collapse-item v-for="group in data" :key="group.id" :title="group.group" :name="group.id">
          <el-card v-for="(k, i) in group.tasks" :key="i" class="card" shadow="hover">
            <div slot="header">
              {{k.name}}
              <div style="float: right; font-size: 15px">
                <div>
                <el-button icon="el-icon-document" size="small"
                           v-on:click="openLogs(k)">Logs
                </el-button>
                <el-button type="success" icon="el-icon-s-promotion" size="small"
                           v-on:click="runTask(k)">Run
                </el-button>
                </div>
              </div>
            </div>
            <div class="desc" v-html="k.desc"></div>
            <div class="lastRunTime desc" style="float: right" v-if="k.lastRunTime != null"><i class="el-icon-timer"/> {{k.lastRunTime}}</div>
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
      <pre
        id="content" v-on:scroll="scroll()">
        <div v-for="(log, i) in logs" style="margin-top: 2px; margin-bottom: 2px; " :key="i"
             :class="i === (logs.length - 1)? 'log_end':'log'" v-html="log">
        </div>
      </pre>
      <span slot="footer" class="dialog-footer">
        <el-button @click="allLogs()" size="small">Full Log</el-button>
        <el-button @click="handleLogClose" size="small">Close</el-button>
    </span>
    </el-dialog>
    <el-dialog
      :title="runTitle"
      :visible.sync="runVisible"
      width="50%"
      destroy-on-close
      :before-close="handleRunClose">
      <el-form ref="form" :model="runForm" label-width="80px">
        <el-form-item v-for="p in runForm.data" :key="p.id" :label="p.name">
          <el-input v-if="p.type === 'TEXT'" v-model="p.value" clearable></el-input>
          <el-input v-if="p.type === 'PASSWORD'" type="password" v-model="p.value" show-password></el-input>
          <el-input-number v-if="p.type === 'NUMBER'" v-model="p.value"></el-input-number>
          <el-switch v-if="p.type === 'BOOLEAN'" v-model="p.value"></el-switch>
          <el-select v-if="p.type === 'DROPDOWN'" v-model="p.value">
            <el-option v-for="v in p.availableValue" :key="v" :value="v" :label="v"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div style="text-align:right; font-size: 15px">
        <el-button @click="handleRunClose">Cancel</el-button>
        <el-button @click="runTaskWithParam(runForm)" type="success">Run</el-button>
      </div>
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
        groups: [],
        runTitle: "",
        runVisible: false,
        runForm: {},
        activeGroup: []
      }
    },
    mounted() {
      const that = this
      this.axios.get("tasks").then(res => {
        that.data = res.data.data;
        for (let i in that.data) {
          if  (that.data[i].expand === 'Y') {
            this.activeGroup.push(that.data[i].id)
          }
        }
        let reg = /(http:\/\/)?([A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*)/g;
        for (let i in that.data) {
          that.groups.push(that.data[i].group)
          for (let j in that.data[i].tasks) {
            that.data[i].tasks[j].desc = that.data[i].tasks[j].desc.replace(reg, function (a, b, c) {
              return '<a target="view_window" href="http://' + c + '"\>' + a + '</a>';
            });
          }
        }
      })
    },
    methods: {
      handleRunClose() {
        this.runVisible = false
        this.runTitle = ""
        this.runForm = {}
      },
      runTaskWithParam(form) {
        const params = [];
        for (let i in form.data) {
          params.push({"paramId": form.data[i].id, "value": form.data[i].value});
        }
        this.run({"taskId": form.id, "params": params})
        this.handleRunClose()
      },
      run(form) {
        this.axios.post("exec", form).then(res => {
          this.$message({
            message: res.data.message,
            type: "success",
            duration: 5000
          })
          for (let i in this.data) {
            for (let j in this.data[i].tasks) {
              if (this.data[i].tasks[j].id === form.taskId) {
                this.data[i].tasks[j].lastRunTime = res.data.runTime;
              }
            }
          }
        })
      },
      runTask(task) {
        if (task.params.length === 0) {
          this.run({"taskId": task.id})
        } else {
          this.runForm = {"data": task.params, "id": task.id}
          this.runTitle = "Run: " + task.name
          this.runVisible = true
        }
      },
      handleLogClose() {
        this.logVisible = false;
        this.logTitle = "";
        this.logId = 0;
        this.logs = [];
        this.enableScroll = true;
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
          // for (let i in res.data) {
            // res.data[i] = res.data[i].replace(/ /g, "&nbsp;");
            // res.data[i] = res.data[i].replace(/\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;");
          // }
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
